import json
import boto3

def getTrackedPrices(event, context):
    event = event["queryStringParameters"]
    # If username or password not provided
    if "username" not in event or "password" not in event:
        print("Missing User Credentials")
        return {
            "statusCode": "502",
            "headers": {"Content-Type": "application/json"},
            "body": json.dumps({
                "message": "User Credentials Missing From Input"
            })
        }
    # Get details from event
    user_hash = event["username"]+"&"+event["password"]
    try:
        # Create DynamoDB Client
        client = boto3.client("dynamodb")
        # Get Details of User If Already In Table
        data = client.get_item(
            TableName = "userTrackingTable",
            Key = {
                "userHash": { 'S': user_hash }
            }
        )
        if "Item" in data:
            track_list = data["Item"]["trackList"]['S'].split(',')
        else:
            print("No items to track from userHash " + user_hash)
            return {
                "statusCode": "502",
                "headers": {"Content-Type": "application/json"},
                "body": json.dumps({
                    "message": "No items to track found from user"
                })
            }
        # Get Information Of All Tracking Items
        results = []
        for i in range(0,len(track_list),25):
            track_list_seg = track_list[i:min(i+25,len(track_list))]
            data = client.batch_get_item(
                RequestItems = {
                    "productTrackingTable" : {
                        'Keys': [
                            { "productLink": { 'S':track_link } }
                            for track_link in track_list_seg
                        ]
                    }
                }
            )
            results.extend(data["Responses"]["productTrackingTable"])
        for i in range(len(results)):
            results[i] = {
                results[i]["productLink"]['S']: [
                    results[i]["productName"]['S'],
                    results[i]["currentPrice"]['S'],
                    results[i]["originalPrice"]['S'],
                    results[i]["historicLow"]['S']
                ]
            }   
        # Return Response
        print("Successfully updated userTrackingTable for " + str(user_hash))
        return {
            "statusCode": "200",
            "headers": {"Content-Type": "application/json"},
            "body": json.dumps({
                "Items": results
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