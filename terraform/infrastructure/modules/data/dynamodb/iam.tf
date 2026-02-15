resource "aws_iam_policy" "banking_entities_dynamodb_policy" {
  name = "bank-accounts-dynamodb-${local.banking_entities_table_name}"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Action = [
        "dynamodb:PutItem",
        "dynamodb:GetItem",
        "dynamodb:UpdateItem",
        "dynamodb:Query",
      ]
      Resource = "arn:aws:dynamodb:${var.aws_region}:${data.aws_caller_identity.current.account_id}:table/${local.banking_entities_table_name}"
    }]
  })
}