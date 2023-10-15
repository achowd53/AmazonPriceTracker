#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { ApiStack, DatabaseStack } from '../lib/stacks/index';
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
}

// Create templates for stacks
app.synth();