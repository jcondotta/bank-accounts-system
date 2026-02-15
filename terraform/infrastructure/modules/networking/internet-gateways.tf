resource "aws_internet_gateway" "this" {
  vpc_id = aws_vpc.this.id

  tags = {
    Name = "bank-accounts-internet-gateway-${var.environment}",
    Tier = local.tiers.network
  }
}