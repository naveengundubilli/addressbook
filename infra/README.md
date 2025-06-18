# AWS Infrastructure for Address Book (Terraform)

This directory contains Terraform code to provision all AWS resources needed to run the Address Book Spring Boot application on ECS Fargate, fronted by an Application Load Balancer (ALB).

---

## What This Deploys
- VPC with public and private subnets
- Security groups for ALB and ECS tasks
- ECR repository for Docker images
- ECS cluster (Fargate)
- IAM roles for ECS task execution
- Application Load Balancer (ALB)
- ECS task definition and service (Fargate)
- CloudWatch log group

**Default: Public-facing service via ALB.**

---

## Prerequisites
- [Terraform](https://learn.hashicorp.com/tutorials/terraform/install-cli) v1.3+
- AWS CLI configured (`aws configure`)
- Sufficient AWS permissions to create VPC, ECS, ECR, IAM, ALB, etc.

---

## Quick Start

1. **Initialize Terraform**
   ```sh
   cd infra
   terraform init
   ```

2. **Review/Customize Variables**
   - Edit `variables.tf` or create a `terraform.tfvars` file to override defaults (region, names, CIDRs, etc).

3. **Plan the Deployment**
   ```sh
   terraform plan
   ```

4. **Apply the Deployment**
   ```sh
   terraform apply
   ```
   _Type `yes` to confirm._

5. **Get Outputs**
   - After apply, Terraform will print the ALB DNS name, ECR repo URL, and more.

---

## Outputs
- **ALB DNS Name**: Public URL for your app (e.g., `http://<alb-dns-name>`)
- **ECR Repository URL**: For pushing Docker images
- **ECS Cluster/Service/Task ARNs**
- **VPC ID**

---

## How to Deploy Your App
1. **Build and push your Docker image to ECR** (see main project README for details)
2. **Update ECS service** (manually or via CI/CD) to use the new image
3. **Access your app** at the ALB DNS output

---

## Switching to a Private Cluster
To make your service internal-only (not public):
- In `main.tf`:
  - Set `internal = true` for the `aws_lb.app` resource
  - Use `private_subnets` instead of `public_subnets` for ALB and ECS service
  - Set `assign_public_ip = false` in the ECS service
  - Restrict ALB security group ingress to your VPC CIDR (not `0.0.0.0/0`)

---

## Clean Up
To destroy all resources:
```sh
terraform destroy
```

---

## Notes
- This setup is suitable for dev, test, and production (with tweaks).
- You can extend it for HTTPS, autoscaling, custom domains, etc.
- All resources are tagged with the `name_prefix` variable for easy identification.

---

## License
Apache 2.0 