resource "aws_dynamodb_table" "banking_entities" {
  name         = local.banking_entities_table_name
  billing_mode = "PAY_PER_REQUEST"

  hash_key  = "partitionKey"
  range_key = "sortKey"

  attribute {
    name = "partitionKey"
    type = "S"
  }

  attribute {
    name = "sortKey"
    type = "S"
  }

  tags = {
    Name = local.banking_entities_table_name
    Tier = local.tier
  }
}