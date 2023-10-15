'''
void sendScheduledMessage()
- Scheduled to run weekly
- Checks DynamoDB#2 and sends out emails to all users attached to SQS
- Calls updateProductPrices then getTrackedPrices (internal version)
DynamoDB #2
- User Product Tracking Database
- Key = Hash(Hash(User) | Hash(Password))
- Links User Is Tracking
- Emails To Send Tracked Info To (Also keep encrypted in
table)
'''