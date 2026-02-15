resource "aws_eks_cluster" "this" {
  name     = local.bank_accounts_eks_cluster_name
  role_arn = aws_iam_role.eks_cluster_role.arn
  version  = "1.29"

  vpc_config {
    subnet_ids              = var.private_subnet_ids
    security_group_ids      = [
      aws_security_group.eks_cluster_sg.id
    ]
    endpoint_private_access = true
    endpoint_public_access  = true
  }

  tags = {
    Name = local.bank_accounts_eks_cluster_name
    Tier = local.tier
  }

  depends_on = [
    aws_iam_role_policy_attachment.eks_cluster_policy
  ]
}