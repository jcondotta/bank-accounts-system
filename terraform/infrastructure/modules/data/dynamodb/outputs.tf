output "banking_entities_table_name" {
  description = "The name of the DynamoDB banking entities table "
  value       = aws_dynamodb_table.banking_entities.name
}

output "banking_entities_table_arn" {
  description = "The ARN of the DynamoDB banking entities table"
  value       = aws_dynamodb_table.banking_entities.arn
}

output "dynamodb_policy_arn" {
  value = aws_iam_policy.banking_entities_dynamodb_policy.arn
}