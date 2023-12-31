# AmazonPriceTracker
Price Tracker for Amazon built on AWS architecture generated with AWS CDK for TypeScript with Python for the backend AWS Lambda functions, and a UI available in Java

## CDK Setup

1) `cd cdk/`
2) Setup TypeScript and AWS-CDK-Lib
3) Get AWS Accounts Information And Store In lib/config/constants.ts
4) Set AWS SES Verified Email in lib/lambda_handlers/sendScheduledMessages.py
5) Setup AWS Credentials For Accounts To Use AWS Cli
6) Run `cdk bootstrap`

This is a blank project for CDK development with TypeScript.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

### Useful commands

* `npm run build`   compile typescript to js
* `npm run watch`   watch for changes and compile
* `npm run test`    perform the jest unit tests
* `cdk deploy`      deploy this stack to your default AWS account/region
* `cdk diff`        compare deployed stack with current state
* `cdk synth`       emits the synthesized CloudFormation template

## Java UI
1) `cd app/`
2) Setup Maven and Make
3) Add API_ID and API_REGION for AmazonPriceTracker from API Gateway Console to src/main/java/com/apt/data/APTDataSingleton.java
4) Run `make` after putting user, pwd in file or `mvn compile && mvn exec:java -Dexec.mainClass=com.apt.App -Dexec.args="$(username) $(password)"`