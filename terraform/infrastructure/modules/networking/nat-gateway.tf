resource "aws_nat_gateway" "this" {
  allocation_id = aws_eip.this.id
  subnet_id     = values(aws_subnet.public_subnets)[0].id

  tags = {
    Name = "bank-accounts-nat-${var.environment}",
    Tier = local.tiers.network
  }

  depends_on = [
    aws_internet_gateway.this
  ]
}

resource "aws_route" "private_nat_route" {
  route_table_id         = aws_route_table.private_route_table.id
  destination_cidr_block = "0.0.0.0/0"
  nat_gateway_id         = aws_nat_gateway.this.id
}
