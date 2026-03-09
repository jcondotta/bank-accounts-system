#!/bin/bash

set -e

echo "🚀 Creating DynamoDB table: bank-accounts..."

awslocal dynamodb create-table \
  --table-name bank-accounts \
  --attribute-definitions \
    AttributeName=partitionKey,AttributeType=S \
    AttributeName=sortKey,AttributeType=S \
  --key-schema \
    AttributeName=partitionKey,KeyType=HASH \
    AttributeName=sortKey,KeyType=RANGE \
  --billing-mode PAY_PER_REQUEST

echo "✅ DynamoDB table created: bank-accounts"