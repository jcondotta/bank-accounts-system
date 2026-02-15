locals {
  banking_entities_table_name = "banking-entities-${var.environment}"

  tier = "data"
}

data "aws_caller_identity" "current" {}
