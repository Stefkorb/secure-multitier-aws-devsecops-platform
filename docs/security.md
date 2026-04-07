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
