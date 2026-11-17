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

The root project builds a JAR without submodules at
`target/medeea-byog-1.0.0-SNAPSHOT.jar`.

This branch intentionally contains code-quality issues in `QualityIssuesService.java`
for pipeline troubleshooting: string identity comparisons, empty broad exception
handlers, unused code, mutable public state, duplicated logic, excessive nesting,
discarded string transformations, deprecated wrapper constructors, console logging,
and an `equals` override without `hashCode`. The Maven build
passes; the fixture is not production code and is not called by `SampleService`.
The exact findings and gate outcome depend on the pipeline's analyzer and profile.

To generate a local PMD report without making quality findings fail the Maven build,
run Maven with JDK 17 (PMD 7.7.0 in this plugin does not support newer JDK class
formats):

```bash
mvn org.apache.maven.plugins:maven-pmd-plugin:3.26.0:pmd
```

Findings are written to `target/pmd.xml`.

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
