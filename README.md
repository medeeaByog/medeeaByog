# Medeea BYOG - Adobe Cloud Manager Project

This project is configured to run on Adobe Cloud Manager build pipeline.

## Intentional config pipeline failure

Branch `config-pipeline-validation-error` contains malformed YAML in
`config/cdn.yaml`: the `rules` array is opened with `[` but never closed.
This fixture is intended to fail config pipeline YAML validation before deployment.
The exact error wording depends on Cloud Manager's YAML parser.

In Cloud Manager, select this repository and branch, use a Targeted Deployment
configuration pipeline (not Full Stack Code), and set the configuration location
to `config`. Run it against a non-production test environment.
The Maven build is not involved in this failure.

To remove the intentional syntax error, replace `rules: [` with `rules: []`.
Do not deploy this troubleshooting branch to production.

## Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher
- Adobe Cloud Manager access

## Build

To build the project locally:

```bash
mvn clean install
```

## Cloud Manager Configuration

The project includes:
- `pom.xml` - Maven build configuration
- `.cloudmanager/pipeline.yml` - Cloud Manager pipeline configuration
- Quality gates for code quality, security, and performance

## Pipeline

The Cloud Manager pipeline will:
1. Check out the code
2. Build using Maven (`mvn clean install`)
3. Run quality checks (code quality, security, performance)
4. Deploy to configured environments (dev → stage → prod)

## Environments

- **Dev**: Auto-deploy on build success
- **Stage**: Auto-deploy with rolling strategy
- **Prod**: Manual approval required, blue-green deployment

## Quality Gates

All gates have a 70% threshold:
- Code Quality
- Security
- Performance
