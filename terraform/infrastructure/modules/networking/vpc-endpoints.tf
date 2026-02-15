resource "aws_vpc_endpoint" "cloudwatch_logs_vpc_endpoint" {
  vpc_id              = aws_vpc.this.id
  service_name        = "com.amazonaws.${var.aws_region}.logs"
  vpc_endpoint_type   = "Interface"
  subnet_ids          = values(aws_subnet.private_subnets)[*].id
  private_dns_enabled = true

  security_group_ids = [
    aws_security_group.cloudwatch_logs_vpc_endpoint_sg.id
  ]

  tags = {
    Name = "bank-accounts-cloudwatch-logs-vpc-endpoint-${var.environment}"
    Tier = local.tiers.observability
  }
}

resource "aws_vpc_endpoint" "ecr_api" {
  vpc_id              = aws_vpc.this.id
  service_name        = "com.amazonaws.${var.aws_region}.ecr.api"
  vpc_endpoint_type   = "Interface"
  subnet_ids          = values(aws_subnet.private_subnets)[*].id
  security_group_ids  = [
    aws_security_group.ecr_vpc_endpoint_sg.id
  ]
  private_dns_enabled = true

  tags = {
    Name = "bank-accounts-ecr-api-vpc-endpoint-${var.environment}"
    Tier = local.tiers.network
  }
}

resource "aws_vpc_endpoint" "ecr_dkr" {
  vpc_id              = aws_vpc.this.id
  service_name        = "com.amazonaws.${var.aws_region}.ecr.dkr"
  vpc_endpoint_type   = "Interface"
  subnet_ids          = values(aws_subnet.private_subnets)[*].id
  security_group_ids  = [
    aws_security_group.ecr_vpc_endpoint_sg.id
  ]
  private_dns_enabled = true

  tags = {
    Name = "bank-accounts-ecr-dkr-vpc-endpoint-${var.environment}"
    Tier = local.tiers.network
  }
}

resource "aws_vpc_endpoint" "s3_gateway" {
  vpc_id            = aws_vpc.this.id
  service_name      = "com.amazonaws.${var.aws_region}.s3"
  vpc_endpoint_type = "Gateway"

  route_table_ids = [
    aws_route_table.private_route_table.id
  ]

  tags = {
    Name        = "bank-accounts-s3-gateway-${var.environment}"
    Tier = local.tiers.network
  }
}
