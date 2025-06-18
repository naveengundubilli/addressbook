variable "aws_region" {
  description = "AWS region to deploy resources in"
  type        = string
  default     = "us-east-1"
}

variable "name_prefix" {
  description = "Prefix for resource names"
  type        = string
  default     = "addressbook"
}

variable "vpc_name" {
  description = "Name for the VPC"
  type        = string
  default     = "addressbook-vpc"
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "public_subnets" {
  description = "List of public subnet CIDRs"
  type        = list(string)
  default     = ["10.0.1.0/24", "10.0.2.0/24"]
}

variable "private_subnets" {
  description = "List of private subnet CIDRs"
  type        = list(string)
  default     = ["10.0.101.0/24", "10.0.102.0/24"]
}

variable "ecr_repo_name" {
  description = "ECR repository name"
  type        = string
  default     = "addressbook"
}

variable "ecs_cluster_name" {
  description = "ECS cluster name"
  type        = string
  default     = "addressbook-cluster"
}

variable "ecs_task_family" {
  description = "ECS task definition family name"
  type        = string
  default     = "addressbook-task"
}

variable "ecs_service_name" {
  description = "ECS service name"
  type        = string
  default     = "addressbook-service"
}

variable "container_name" {
  description = "Container name for ECS task definition"
  type        = string
  default     = "addressbook"
} 