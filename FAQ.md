# FAQ

## Development
### Why did you not implement the extension with Kotlin?
If implemented with Kotlin we'd need to bundle the Kotlin runtime libraries which would increase the size. Therefore we stay with Java. However, this does not prevent you from implementing your application with Kotlin and still use this extension.

### Why are you not using Lombok?
We're not depending on the Lombok to simplify the setup of the development environment - otherwise this would require a plugin for the IDE.

### How do I create and publish a new release?

1. Trigger the release
   1. Open https://github.com/camunda-community-hub/micronaut-camunda-platform-7/releases
   2. Click "Draft a new release"
   3. Enter a tag version of the master branch, e.g. v0.1.0 considering the last tag and https://semver.org/
   4. Click on "Publish release"
2. Check Maven Central (Portal)
   1. The new release should appear on https://central.sonatype.com/publishing/deployments after a few minutes in state "Published" and also on https://central.sonatype.com/search?q=micronaut-camunda-bpm-feature
   2. Update README.md
      1. Dependencies for Gradle and Maven
      2. Compatibility Matrix
   3. That's all :-) There is no need to update the version in the project. It will stay at 0.0.1-SNAPSHOT.
3. Update Example Applications
   1. https://github.com/tobiasschaefer/micronaut-camunda-example-onboarding
   2. https://github.com/tobiasschaefer/micronaut-camunda-example-java-maven
   3. https://github.com/tobiasschaefer/micronaut-camunda-example-kotlin-gradle
4. Update Micronaut Launch in [starter-core project](https://github.com/micronaut-projects/micronaut-starter/blob/2.4.x/starter-core/src/main/resources/pom.xml) (check branch!)
   1. Update version number of micronaut-camunda-bpm-feature
   2. Update version number of camunda-bpm-assert (if changed)
