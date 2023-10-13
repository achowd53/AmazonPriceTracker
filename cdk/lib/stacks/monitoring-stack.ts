import * as cdk from 'aws-cdk-lib';
import { Construct } from 'constructs';
// import * as sqs from 'aws-cdk-lib/aws-sqs';

export interface MonitoringStackProps extends cdk.StackProps {
}

export class MonitoringStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: MonitoringStackProps) {
    super(scope, id, props);

    // The code that defines your stack goes here

    // example resource
    // const queue = new sqs.Queue(this, 'AwsArchitectureQueue', {
    //   visibilityTimeout: cdk.Duration.seconds(300)
    // });
  }
}