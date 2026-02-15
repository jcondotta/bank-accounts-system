resource "aws_eks_node_group" "bank_accounts_nodes" {
  cluster_name    = var.cluster_name
  node_group_name = local.bank_accounts_eks_cluster_node_name
  node_role_arn   = aws_iam_role.eks_node_role.arn
  subnet_ids      = var.private_subnet_ids

  scaling_config {
    desired_size = 1
    max_size     = 1
    min_size     = 1
  }

  instance_types = ["t3.small"]

  capacity_type = "SPOT"

  update_config {
    max_unavailable = 1
  }

  tags = {
    Name = local.bank_accounts_eks_cluster_node_name
    Tier = local.tier
  }
}