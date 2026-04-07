# Identity and Access Management Model

The IAM design of the project separates human access, workload access, and automation access in order to reduce unnecessary privilege and improve control.

Human administrative access is kept separate from application and deployment identities. The AWS root account is not used for daily administration. Infrastructure management is performed through a dedicated administrative identity.

The application running on ECS Fargate uses a dedicated task role for its AWS API access. This role is intended only for the permissions required by the application itself, such as controlled access to S3 and read access to required secrets.

A separate ECS task execution role is used for runtime and platform-level operations such as pulling container images and sending logs to CloudWatch.

CI/CD access is separated from both human and application identities. GitHub Actions is intended to use role assumption through OpenID Connect rather than long-lived AWS credentials stored in the repository.

This model supports least privilege, clearer separation of duties, and a more production-like access design.

## IAM Role Breakdown

The system uses separate IAM roles for administration, application runtime, and automation.

The project administrator is used for initial setup and infrastructure management. This identity is not used by the application or the CI/CD pipeline.

The ECS task execution role is used by the ECS runtime for operational purposes such as pulling container images and sending logs. It does not have access to business data or application resources.

The ECS task role represents the application itself. It is granted only the permissions required for its operation, including access to specific S3 resources and read access to required secrets. It does not have access to unrelated AWS services or administrative capabilities.

The CI/CD pipeline uses a dedicated deployment role that is assumed through GitHub OIDC. This avoids storing long-lived AWS credentials and limits access to deployment-related actions only.

An optional read-only role can be used for auditing and inspection of logs, configuration, and security findings without allowing modifications.

This separation ensures that each component operates with only the permissions it requires and reduces the impact of potential compromise.

## Secrets Management and Secure Configuration

Sensitive runtime values used by the MyCityGov application are handled separately from general application configuration.

Secrets such as database credentials,app secret key and JWT are stored in AWS Secrets Manager. They are not hardcoded in the source code, committed to the repository, or stored in plaintext configuration files.

The application running on ECS Fargate is the only workload that needs access to these secrets at runtime. Access is granted through the ECS task role and is limited to the specific secrets required by the application.

Non-sensitive configuration values, such as application port, environment name, database host, database name, AWS region, and S3 bucket name, are treated as standard runtime configuration rather than secrets.

This approach reduces secret exposure, supports least privilege, and keeps configuration management cleaner and easier to control.

## Edge Protection and Perimeter Security

The public exposure of the MyCityGov platform is limited to a single internet-facing Application Load Balancer deployed across two public subnets.

Client traffic is intended to use HTTPS, with TLS termination at the load balancer. Certificates are managed through AWS Certificate Manager. If HTTP is enabled, it is used only to redirect clients to HTTPS.

AWS WAF is associated with the load balancer in order to filter malicious or unwanted web traffic before it reaches the application layer. The initial protection approach is based on managed WAF rules and rate limiting.

The load balancer security group allows only the required inbound web ports, while the application service remains private and accepts traffic only from the load balancer.

This design minimizes the public attack surface, enforces a controlled ingress path, and provides a realistic baseline for perimeter security.
