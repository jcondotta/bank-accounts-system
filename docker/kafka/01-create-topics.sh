#!/bin/bash

set -e

echo "🚀 Running Kafka init script..."

BOOTSTRAP_SERVER="kafka-1:29092"

create_topic() {
  local topic_name=$1

  kafka-topics --bootstrap-server "$BOOTSTRAP_SERVER" \
    --create \
    --if-not-exists \
    --topic "$topic_name" \
    --partitions 3 \
    --replication-factor 1
}

create_topic "bank-account-opened"
create_topic "bank-account-activated"
create_topic "joint-account-holder-added"

create_topic "bank-account-blocked"
create_topic "bank-account-unblocked"
create_topic "bank-account-closed"

echo "✅ Kafka topics created successfully"