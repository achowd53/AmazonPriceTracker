'''
table getTrackedPrices(username=, password=, userhash=)
- Pull info from DynamoDB#2 for what is being tracked
- Pull info for prices from DynamoDB#1
- If anything tracked not refreshed recently enough within hour, calls
updateProductPrices()
- Make a version that accepts just hash value as well as an optional hidden
value
- GET Request
DynamoDB #2
- User Product Tracking Database
- Key = Hash(Hash(User) | Hash(Password))
- Links User Is Tracking
- Emails To Send Tracked Info To (Also keep encrypted in
table)
'''