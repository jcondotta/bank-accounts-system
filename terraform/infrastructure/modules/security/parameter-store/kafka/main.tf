resource "aws_ssm_parameter" "kafka_api_key" {
  name  = "${local.base_path}/api-key"
  type  = "SecureString"
  value = var.kafka_api_key

  tags = {
    Name = "${local.base_path}/api-key"
    Tier = local.tier
  }
}

resource "aws_ssm_parameter" "kafka_api_secret" {
  name  = "${local.base_path}/api-secret"
  type  = "SecureString"
  value = var.kafka_api_secret

  tags = {
    Name = "${local.base_path}/api-secret"
    Tier = local.tier
  }
}