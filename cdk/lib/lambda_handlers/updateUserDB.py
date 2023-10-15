import boto3
import json

def updateUserDB(event, context):
    event = json.loads(event["body"])
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
    user_hash = str(hash(event["username"]+"_@%|&"+event["password"]))
    track_list = set(event["add_track"]) if "add_track" in event else set()
    remove_track = event["remove_track"] if "remove_track" in event else set()
    email = event["email"] if "email" in event else None
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
            if "trackList" in data["Item"]:
                track_list = (set(data["Item"]["trackList"]['S'].split(','))-set(remove_track))|track_list
            if "email" in data["Item"]:
                email = data["Item"]["email"]['S'] if not email else email
        # Create/Update Table Entry For Userhash
        client.put_item(
            TableName = "userTrackingTable",
            Item = {
                "userHash": { 'S':user_hash },
                "trackList": { 'S':",".join([*track_list]) },
                "email": { 'S':email } if email else { 'S':"" }
            }
        )
        # Update Product Table With New Entries If Any
        data = client.scan(
            TableName = "productTrackingTable",
            ProjectionExpression = "productLink"
        )
        if "Items" in data:
            new_links = track_list - set(item["productLink"]['S'] for item in data["Items"])
            if len(new_links) > 0:
                client.batch_write_item(
                    RequestItems = {
                        "productTrackingTable" : [
                            { "PutRequest": { "Item": { "productLink": { 'S':track_link } } } }
                            for track_link in new_links
                        ]
                    }
                )
        # Return Response
        print("Successfully updated userTrackingTable for " + str(user_hash))
        return {
            "statusCode": "200",
            "headers": {"Content-Type": "application/json"},
            "body": json.dumps({
                "message": "Updated userTrackingTable for " + event["username"]
            })
        }
    except:
        print("Failed to update userTrackingTable DynamoDB")
        return {
            "statusCode": "503",
            "headers": {"Content-Type": "application/json"},
            "body": json.dumps({
                "message": "Failed to update userTrackingTable DynamoDB For An Unknown Reason"
            })
        }
