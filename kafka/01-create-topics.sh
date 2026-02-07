#!/bin/bash

set -e

echo "🚀 Running Kafka init script..."

BOOTSTRAP_SERVER="kafka-1:29092"

kafka-topics --bootstrap-server kafka-1:29092 \
  --create \
  --if-not-exists \
  --topic bank-account-opened \
  --partitions 3 \
  --replication-factor 2

kafka-topics --bootstrap-server kafka-1:29092 \
  --create \
  --if-not-exists \
  --topic bank-account-activated \
  --partitions 3 \
  --replication-factor 2

kafka-topics --bootstrap-server kafka-1:29092 \
  --create \
  --if-not-exists \
  --topic joint-account-holder-added \
  --partitions 3 \
  --replication-factor 2

echo "✅ Kafka topics created successfully"