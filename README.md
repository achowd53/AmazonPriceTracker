# AmazonPriceTracker
Price Tracker for Amazon built on AWS architecture, with UIs available through Java and JavaScript

## CDK Setup

1) Get AWS Accounts Information And Store In lib/config/constants.ts
2) Setup AWS Credentials For Accounts To Use AWS Cli
3) Run `cdk bootstrap`

This is a blank project for CDK development with TypeScript.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

### Useful commands

* `npm run build`   compile typescript to js
* `npm run watch`   watch for changes and compile
* `npm run test`    perform the jest unit tests
* `cdk deploy`      deploy this stack to your default AWS account/region
* `cdk diff`        compare deployed stack with current state
* `cdk synth`       emits the synthesized CloudFormation template
