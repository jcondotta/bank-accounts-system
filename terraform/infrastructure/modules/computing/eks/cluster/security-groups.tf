resource "aws_security_group" "eks_cluster_sg" {
  name        = "bank-accounts-eks-cluster-sg-${var.environment}"
  description = "Security group for EKS control plane"
  vpc_id      = var.vpc_id

  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "bank-accounts-eks-cluster-sg-${var.environment}"
    Tier = local.tier
  }
}