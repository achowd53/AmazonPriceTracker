import json

def blank(event, context):
    return {
        "statusCode": "200",
        "headers": {"Content-Type": "application/json"},
        "body": json.dumps({
            "message": "Hello From Lambdas!"
        })
    }