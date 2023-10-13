#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { ApiStack, DatabaseStack, MonitoringStack } from '../lib/stacks/index';
import { environments} from '../lib/config/constants';

const app = new cdk.App();

// For each account labeled in lib/config/constants.ts
for (var account of environments) {

  new ApiStack(app, 'ApiStack-'+account.region, {
    env: account,
  }); 

  new DatabaseStack(app, 'DatabaseStack-'+account.region, {
    env: account,
  }); 

  new MonitoringStack(app, 'MonitoringStack-'+account.region, {
    env: account,
  }); 
}