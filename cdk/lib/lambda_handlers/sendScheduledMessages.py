import json
import boto3

VERIFIED_EMAIL = "FILLER EMAIL" # You must verify a sender email on AWS SES Console first

def sendScheduledMessages(event, context):
    try:
        # Update Product Prices Every Week
        boto3.client("lambda").invoke(
            FunctionName = "updateProductPricesFn",
            InvocationType = "RequestResponse",
            Payload = '{}'
        )
        # Create DynamoDB Client
        client = boto3.client("dynamodb")
        # Get Details of All Users
        data = client.scan(
            TableName = "userTrackingTable",
            ProjectionExpression = "userHash,email"
        )
        email_list = [] # (email, items)
        if "Items" in data:
            for item in data["Items"]:
                if "email" in item and item["email"]['S'] != "":
                    email_list.append((item["email"]['S'], item["userHash"]['S']))
        else:
            print("No one to email :(")
            return {
                "statusCode": "502",
                "headers": {"Content-Type": "application/json"},
                "body": json.dumps({
                    "message": "No entries found in table"
                })
            }
        # Send out emails with getTrackedPrices and SES
        ses_client = boto3.client('ses')
        for email, userHash in email_list:
            verified = ses_client.get_identity_verification_attributes(Identities=[email])['VerificationAttributes'][email]['VerificationStatus']
            if verified != 'Success':
                ses_client.verify_email_identity(EmailAddress = email)
                print("Sent verification email to", userHash,"at",email)
            else:
                # GetTrackedPrices for user 
                user, pwd = userHash.split('&')
                data = boto3.client("lambda").invoke(
                    FunctionName = "getTrackedPricesFn",
                    InvocationType = "RequestResponse",
                    Payload = '{"queryStringParameters": { "username": "'+user+'", "password": "'+pwd+'" } }'
                )
                if "Payload" in data:
                    data = json.loads(data['Payload'].read().decode())
                    if data["statusCode"] == "200" and "Items" in data["body"]:
                        data = json.loads(data["body"])["Items"]
                        data_text = "Link To Amazon Product\tProduct Name\tCurrent Price\tOriginal Price\tHistoric Low\n"
                        for item in data:
                            for link in item:
                                data_text += link + '\t' + '\t'.join(item[link]) + '\n'
                        data_html = '<table class="table table-bordered table-hover table-condensed"><thead><tr><th title="Field #1">Link To Amazon Product</th><th title="Field #2">Product Name</th><th title="Field #3">Current Price</th><th title="Field #4">Original Price</th><th title="Field #5">Historic Low</th></tr></thead><tbody>'
                        for item in data:
                            for link in item:
                                data_html += '<tr><td>' + link + '</td><td>' + '</td><td>'.join(item[link]) + '</td></tr>'
                        data_html += '</tbody></table>'
                        ses_client.send_email(
                            Source = VERIFIED_EMAIL, 
                            Destination = {
                                "ToAddresses": [
                                    email
                                ]
                            },
                            Message = {
                                "Subject": {
                                    "Data": "Weekly AmazonPriceTracker Email",
                                },
                                "Body": {
                                    "Text": {
                                        "Data": data_text,
                                    },
                                    "Html": {
                                        "Data": data_html,
                                    }
                                }
                            }
                        )
                        print("Sent tracking email to", userHash,"at",email)
        # Return Response
        print("Successfully sent out emails")
        return {
            "statusCode": "200",
            "headers": {"Content-Type": "application/json"},
            "body": json.dumps({
                "message": "Successfully sent out emails"
            })
        }
    except:
        print("Failed to update userTrackingTable DynamoDB")
        return {
            "statusCode": "503",
            "headers": {"Content-Type": "application/json"},
            "body": json.dumps({
                "message": "Failed to read DynamoDB Tables For An Unknown Reason"
            })
    }