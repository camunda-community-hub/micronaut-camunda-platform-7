# FAQ

## Development
### Why did you not implement the extension with Kotlin?
If implemented with Kotlin we'd need to bundle the Kotlin runtime libraries which would increase the size. Therefore we stay with Java. However, this does not prevent you from implementing your application with Kotlin and still use this extension.

### Why are you not using Lombok?
We're not depending on the Lombok to simplify the setup of the development environment - otherwise this would require a plugin for the IDE.

### How do I create and publish a new release?

1. Trigger the release
   1. Open https://github.com/NovatecConsulting/micronaut-camunda-bpm/releases
   2. Click "Draft a new release"
   3. Enter a tag version of the master branch, e.g. v0.1.0 considering the last tag and https://semver.org/
   4. Click on "Publish release"
2. Verify that release is built and uploaded to OSSRH successfully
   1. Open https://github.com/NovatecConsulting/micronaut-camunda-bpm/actions?query=workflow%3A%22Publish+to+OSSRH+when+released%22
   2. Wait for build to succeed  
   The build sporadically fails due to timeouts during the upload of artifacts. In this case delete the release via the GitHub UI, delete the tag with `git push origin :<tag>` and then retry.
3. Publish to Maven Central (see also [Detailed instructions](https://central.sonatype.org/pages/releasing-the-deployment.html))
   1. Open https://oss.sonatype.org/#stagingRepositories
   2. Start verification by selecting the repository and clicking on "Close"
   3. Wait a few minutes and click on "Refresh" for the status to update
   4. Release version to Maven Central by selecting the repository and clicking on "Release"
4. Check Maven Central
   1. The new release should appear on https://search.maven.org/search?q=micronaut-camunda-bpm-feature after a few minutes
   2. Update README.md
      1. Dependencies for Gradle and Maven
      2. Compatibility Matrix
   3. That's all :-) There is no need to update the version in the project. It will stay at 0.0.1-SNAPSHOT.
