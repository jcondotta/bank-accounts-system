module "networking" {
  source = "./modules/networking"

  aws_region  = var.aws_region
  environment = var.environment
}

module "dynamodb" {
  source = "./modules/data/dynamodb"

  aws_region  = var.aws_region
  environment = var.environment

  vpc_id = module.networking.vpc_id

  private_route_table_ids = [
    module.networking.private_route_table_id
  ]

  depends_on = [
    module.networking
  ]
}

module "kafka_secret_parameter_store" {
  source = "./modules/security/parameter-store/kafka"

  aws_region  = var.aws_region
  environment = var.environment

  kafka_api_key = var.kafka_api_key
  kafka_api_secret = var.kafka_api_secret
}

module "eks_cluster" {
  source = "./modules/computing/eks/cluster"

  aws_region  = var.aws_region
  environment = var.environment

  vpc_id = module.networking.vpc_id
  private_subnet_ids = module.networking.private_subnet_ids

  depends_on = [
    module.networking
  ]
}

module "eks_worker_nodes" {
  source = "./modules/computing/eks/worker-nodes"

  aws_region  = var.aws_region
  environment = var.environment

  cluster_name = module.eks_cluster.cluster_name
  private_subnet_ids = module.networking.private_subnet_ids

  dynamodb_policy_arn = module.dynamodb.dynamodb_policy_arn

  depends_on = [
    module.eks_cluster
  ]
}
