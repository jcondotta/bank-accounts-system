output "kafka_api_key_parameter_name" {
  value = aws_ssm_parameter.kafka_api_key.name
}

output "kafka_api_secret_parameter_name" {
  value = aws_ssm_parameter.kafka_api_secret.name
}