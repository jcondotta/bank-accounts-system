resource "aws_eip" "this" {
  domain = "vpc"

  tags = {
    Name = "bank-accounts-nat-eip-${var.environment}",
    Tier = local.tiers.network

  }
}