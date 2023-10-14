import * as cdk from 'aws-cdk-lib';
import { Construct } from 'constructs';

export interface DatabaseStackProps extends cdk.StackProps {
}

export class DatabaseStack extends cdk.Stack {
  constructor(scope: Construct, id: string, props?: DatabaseStackProps) {
    super(scope, id, props);

    //// Create DynamoDB Tables
    // ProductTrackingTable has attributes productLink, productName, originalPrice, currentPrice, historicLow, lastUpdated
    const productTrackingTable = new cdk.aws_dynamodb.TableV2(this, 'productTrackingTable', {
      partitionKey: {name: 'productLink', type: cdk.aws_dynamodb.AttributeType.STRING},
      tableClass: cdk.aws_dynamodb.TableClass.STANDARD,
      pointInTimeRecovery: true,
      tableName: 'productTrackingTable',
      removalPolicy: cdk.RemovalPolicy.DESTROY,
    });
    // UserTrackingTable has attributes userHash, productLinks, notificationEmails
    const userTrackingTable = new cdk.aws_dynamodb.TableV2(this, 'userTrackingTable', {
      partitionKey: {name: 'userHash', type: cdk.aws_dynamodb.AttributeType.STRING},
      tableClass: cdk.aws_dynamodb.TableClass.STANDARD,
      pointInTimeRecovery: true,
      tableName: 'userTrackingTable',
      removalPolicy: cdk.RemovalPolicy.DESTROY,
    });
  }
}
