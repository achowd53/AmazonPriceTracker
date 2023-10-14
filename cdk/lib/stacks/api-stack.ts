import * as cdk from 'aws-cdk-lib';
import * as lambda from 'aws-cdk-lib/aws-lambda';
import * as events from 'aws-cdk-lib/aws-events'
import * as targets from 'aws-cdk-lib/aws-events-targets'
import * as apigw from 'aws-cdk-lib/aws-apigateway'
import * as iam from 'aws-cdk-lib/aws-iam';
import { Construct } from 'constructs';
import * as path from 'path';

export interface ApiStackProps extends cdk.StackProps {
}

export class ApiStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: ApiStackProps) {
    super(scope, id, props);

    //// AWS IAM Role for Full Access
    // Create IAM Role for Lambda to access all internal services
    const lambdaRole = new iam.Role(this, 'lambdaRole', {
      assumedBy: new iam.ServicePrincipal('lambda.amazonaws.com'),
      description: 'This role is for full access to services for AWS Lambda',
      roleName: `lambdaRole-${props?.env?.region}`,
    });
    lambdaRole.addToPolicy(new iam.PolicyStatement({
      effect: iam.Effect.ALLOW,
      resources: ['*'],
      actions: [
        'lambda:*', 
        'ses:SendEmail', 'ses:SendRawEmail',
        'dynamodb:*',
        's3:ListAllMyBuckets',
        'logs:*',
      ],
    }));
    //// AWS Lambda Functions
    // Update Product Prices Lambda
    const updateProductPricesFn = new lambda.Function(this, 'updateProductPricesFn', {
      description: 'Update productTrackingTable DynamoDB',
      runtime: lambda.Runtime.PYTHON_3_11,
      handler: 'blank.blank',
      code: lambda.Code.fromAsset(path.join(__dirname, '/../lambda_handlers/')),
      role: lambdaRole,
      functionName: 'updateProductPricesFn',
    });
    // Send Scheduled Messages
    const sendScheduledMessagesFn = new lambda.Function(this, 'sendScheduledMessagesFn', {
      description: 'Send email of tracked product prices to users',
      runtime: lambda.Runtime.PYTHON_3_11,
      handler: 'blank.blank',
      code: lambda.Code.fromAsset(path.join(__dirname, '/../lambda_handlers/')),
      role: lambdaRole,
      functionName: 'sendScheduledMessagesFn',
    });
    const emailWeeklyRule = new events.Rule(this, 'emailWeeklyRule', {
      schedule: events.Schedule.expression('rate(3 days)'),
      ruleName: 'emailWeeklyRule',
    });
    emailWeeklyRule.addTarget(new targets.LambdaFunction(sendScheduledMessagesFn));
    // Update User DB
    const updateUserDBFn = new lambda.Function(this, 'updateUserDBFn', {
      description: 'Updates userTrackingTable DynamoDB',
      runtime: lambda.Runtime.PYTHON_3_11,
      handler: 'blank.blank',
      code: lambda.Code.fromAsset(path.join(__dirname, '/../lambda_handlers/')),
      role: lambdaRole,
      functionName: 'updateUserDBFn',
    });
    // Get Tracked Prices
    const getTrackedPricesFn = new lambda.Function(this, 'getTrackedPricesFn', {
      description: 'Get tracked link prices for user',
      runtime: lambda.Runtime.PYTHON_3_11,
      handler: 'blank.blank',
      code: lambda.Code.fromAsset(path.join(__dirname, '/../lambda_handlers/')),
      role: lambdaRole,
      functionName: 'getTrackedPricesFn',
    });
    //// AWS API Gateway Functions
    // Amazon Price Tracker REST Api
    const amazonPriceTrackerApi = new apigw.RestApi(this, 'amazonPriceTrackerApi', {
      restApiName: 'amazonPriceTrackerApi',
      description: 'API Gateway for AmazonPriceTracker',
      deploy: true,
    });
    // Update User DB Call
    const updateUserDBApi = amazonPriceTrackerApi.root.addResource('updateUserDB');
    updateUserDBApi.addMethod('PUT', new apigw.LambdaIntegration(updateUserDBFn, { proxy: true }));
    new cdk.CfnOutput(this, 'updateUserDBApiUrl', { value: amazonPriceTrackerApi.url+'updateUserDB/' });
    // Get Tracked Prices Call
    const getTrackedPricesApi = amazonPriceTrackerApi.root.addResource('getTrackedPrices');
    getTrackedPricesApi.addMethod('GET', new apigw.LambdaIntegration(getTrackedPricesFn, { proxy: true }));
    new cdk.CfnOutput(this, 'getTrackedPricesApiUrl', { value: amazonPriceTrackerApi.url+'getTrackedPrices/' });
  }
}
