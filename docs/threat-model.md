# Threat Model

This document describes the main security risks considered during the design and implementation of the MyCityGov cloud environment.

This model does not aim to provide a full application security analysis. Instead, it focuses on the cloud infrastructure, the deployment environment, and how different components interact and trust each other.

## Purpose

The purpose of this threat model is to support the design of a secure cloud architecture for the MyCityGov platform.Its also meant to provide a deeper understanding of the system through a security perspective.

The goal is to identify what needs to be protected, who or what could pose a risk, and where the system may be exposed, so that security decisions are made early and intentionally.

## A. Threat Model v0 - Early Design stage

### A.1 Scope

This threat model focuses on the initial cloud architecture used to host the MyCityGov application on AWS.

The scope includes:

- the public entry point of the system,
- the application layer running in the cloud,
- the data storage layer,
- and the supporting AWS services used for identity, logging, and security monitoring.

The application itself is considered at a high level, mainly in terms of how users interact with it and how data flows through the system.

### A.2 Assets

The following assets are considered important for the security of the system:

#### Α.2.1 Application and Logic

- The MyCityGov application and its business logic
- API authentication tokens and session-related values
- Application build artifacts (e.g. container images)

#### A.2.2 Data

- Citizen data and personal information
- Request and case-related data
- Appointment data
- Data stored in the database (RDS)
- Files and documents stored in object storage (S3)

#### A.2.3 Secrets and Credentials

- Database credentials
- Application secrets
- IAM credentials (access keys or temporary role credentials)

#### A.2.4 Infrastructure and Access Control

- IAM roles and access policies
- The cloud infrastructure resources hosting the application

#### A.2.5 Logging and Monitoring

- Application and infrastructure logs
- Audit logs (e.g. CloudTrail)
- Alerting and detection configuration

### A.4 Threat Actors

The following actors are considered in this model:

- External users interacting with the system through the internet
- Malicious or abusive authenticated users
- Employees or administrators with excessive or misused access
- Attackers targeting the public application interface
- Attackers using leaked credentials or secrets
- Misconfigured automation or CI/CD processes
- Human mistakes leading to insecure configuration

### A.4 Trust Boundaries

- Between internet and and the WAF:
Untrusted traffic from external users reaches the system through AWS WAF (attached to the ALB).

- Between ALB(public layer) and MyCityGov(private layer(ECS)):
Traffic that has passed through the WAF and ALB is forwarded to the application.

- Between MyCityGov(ECS) and Database(Data layer(RDS)):
The application communicates with the database to read and write data. This boundary is critical, as it protects sensitive data from direct exposure and limits access to authorized application components.

- Between MyCityGov(ECS) AND S3 bucket:
The application stores and retrieves user-uploaded files. This boundary involves handling untrusted input and requires controlled access to storage resources.

### A.5 Early Threats

This section represents early threat considerations that are going to help us proceed with fundamental security by design and implemetation

- Misconfiguration of AWS WAF or weak rule sets may allow malicious traffic to reach the application.
- Exposure of internal components (ECS or RDS) due to incorrect network configuration could increase the attack surface.
- Leaked or improperly managed database credentials may lead to unauthorized access, data exfiltration, or tampering.
- Misconfigured S3 storage could expose sensitive files or allow unauthorized access to uploaded content.
- Compromised user accounts, especially privileged roles (e.g. admin or employee), may lead to unauthorized actions or data access.
- Malicious insiders or misuse of legitimate access should be considered as a potential threat.
- Excessive IAM permissions assigned to users, services, or automation may increase the impact of compromise.
- Insufficient logging or monitoring may make it difficult to detect or investigate security incidents.
- Malicious input or abuse of application endpoints may lead to unintended behavior or data exposure, even if the application itself is not fully analyzed at this stage.

## B. Threat Model v1 - Refined Design Stage

### B.1 Updated Scope

### B.2 Refied Trust boundaries

### B.3 Updated Threats and mitigations

## C. Final Threat Model

### C.1 Final Threat Summary

### C.2 Control Mapping

### C.3 residual risks
