# Medeea BYOG - Adobe Cloud Manager Project

This project is configured to run on Adobe Cloud Manager build pipeline.

## Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher
- Adobe Cloud Manager access

## Build

To build the project locally:

```bash
mvn clean install
```

The build produces `ui.apps/target/medeea-byog.ui.apps-1.0.0-SNAPSHOT.zip`.
This is an AEM application content package containing a sample node at
`/apps/medeea-byog`. Cloud Manager can discover it as a non-skipped content
package; it is not just compiled Java classes.

The root project also compiles `SampleService.java` so syntax errors fail the
build. That class is a compilation fixture, not an OSGi service, and is not
included in the content package. Deploying Java code would require a separate
OSGi bundle module embedded in the package.

## Cloud Manager Configuration

The project includes:
- `pom.xml` - Maven build configuration
- `.cloudmanager/java-version` - Selects JDK 11 for Cloud Manager builds
- `.cloudmanager/pipeline.yml` - Cloud Manager pipeline configuration
- Quality gates for code quality, security, and performance

Cloud Manager uses `.cloudmanager/java-version` to select the build JDK;
the `jdkVersion` field in `pipeline.yml` is ignored. Keep this file set to `11`
to match the Maven compiler configuration and satisfy the Java 11+ enforcer rule.

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
