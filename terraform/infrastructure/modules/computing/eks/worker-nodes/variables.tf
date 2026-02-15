variable "aws_region" {
  description = "The AWS region where resources will be deployed."
  type        = string
}

variable "environment" {
  description = "Deployment environment"
  type        = string
}

variable "private_subnet_ids" {
  description = "Private subnets for EKS worker nodes"
  type        = list(string)
}

variable "cluster_name" {
  description = "Name of the EKS cluster"
  type        = string
}

variable "dynamodb_policy_arn" {
  type        = string
  description = "ARN of the IAM policy that grants the EKS node role access to the bank-accounts DynamoDB table."
}
