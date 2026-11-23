# Medeea BYOG - Adobe Cloud Manager Project

This project is configured to run on Adobe Cloud Manager build pipeline.

## Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher
- Adobe Cloud Manager access

## Build

### Large-archive build-error fixture

This branch includes 2,048 synthetic JSON files under `archive-fixture/`.
Each contains 16 KiB of random bytes encoded as Base64, so both ZIP and
gzip-compressed Git source archives exceed 25 MiB. These files are outside
the Maven source and resource directories and are not deployed.

The branch has one intentional Java compilation error: the missing semicolon
in the `SampleService` constructor at
`src/main/java/com/adobe/aem/medeea/SampleService.java:11`.
`mvn clean install` fails with `';' expected`. Restore that semicolon to
allow compilation; the existing code-quality issues described below remain.

To build the project locally:

```bash
mvn clean install
```

The root project builds an AEM FileVault content package without submodules at
`target/medeea-byog-1.0.0-SNAPSHOT.zip`. Cloud Manager requires this deployable
content package; a plain JAR does not satisfy its artifact check. The package
creates only the `/apps/medeea-byog` fixture folder.

This branch intentionally contains code-quality issues in `QualityIssuesService.java`
for pipeline troubleshooting: string identity comparisons, empty broad exception
handlers, unused code, mutable public state, duplicated logic, excessive nesting,
discarded string transformations, deprecated wrapper constructors, console logging,
and an `equals` override without `hashCode`. The Java fixture is compiled for
analysis, but is not deployed as an OSGi bundle and is not called by `SampleService`.
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
