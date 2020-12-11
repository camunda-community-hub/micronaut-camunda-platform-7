# Developer Guide
## Get the code

Create a local Git clone:

`git clone https://github.com/NovatecConsulting/micronaut-camunda-bpm.git`

## Open and run in IntelliJ IDEA

To import the project into IntelliJ IDEA simply open the build.gradle file and follow the instructions to import the project.

For IntelliJ IDEA if you plan to use the IntelliJ compiler then you should enable annotation processing under the "Build, Execution, Deployment → Compiler → Annotation Processors" by ticking the "Enable annotation processing" checkbox.

Once you have enabled annotation processing in IntelliJ you can run the application and tests directly within the IDE without the need of an external build tool such as Gradle.

## Build integration project and run the example application

To build the integration project (subfolder [`micronaut-camunda-bpm-feature`](/micronaut-camunda-bpm-feature)) and start the
example application (subfolder [`micronaut-camunda-bpm-example`](/micronaut-camunda-bpm-example)) simply execute:

Unix/Mac:
```
./gradlew clean run -p micronaut-camunda-bpm-example
```

Windows:

```
gradlew.bat clean run -p micronaut-camunda-bpm-example
```

## Call the example app

Open in your browser:
* http://localhost:8080/camunda/name will return "default" as the name of the default process engine.
* http://localhost:8080/camunda/definitions will return "HelloWorld" as the currently deployed process model.

## Persistent Database

By default, the example app will use an H2 in-memory database which is created on application start-up. If you need a
persistent database then the easiest approach is to configure the H2 database to be backed up by a file by configuring
the data source's URL:

`datasources.default.url: jdbc:h2:file:~/micronautdb;DB_CLOSE_ON_EXIT=FALSE`

To reset the database simply delete the `micronautdb*` files in your home directory.

# Contribution Guidelines

Do you have something you’d like to contribute? We welcome pull requests, but ask that you read this document first to understand how best to submit them; what kind of changes are likely to be accepted; and what to expect from the core developers when evaluating your submission.

Please refer back to this document as a checklist before issuing any pull request; this will save time for everyone. Thank you!

## Create your pull request

### Understanding the basics

Not sure what a pull request is, or how to submit one? Take a look at GitHub's excellent documentation at https://help.github.com/articles/about-pull-requests first.

### Discuss non-trivial contribution ideas with committers

If you're considering anything more than correcting a typo or fixing a minor bug, please discuss it with the core developers before submitting a pull request. We're happy to provide guidance, but please spend an hour or two researching the subject on your own prior discussions.

### Use real name in git commits

Please configure Git to use your real first and last name for any commits you intend to submit as pull requests.

You can configure this globally with:
```
git config --global user.name "John Doe"
git config --global user.email john.doe@example.com
```

These settings will be written to ~/.gitconfig on Unix and %APPDATA%\.gitconfig on Windows, see https://help.github.com/articles/set-up-git/

### Format commit messages

Most importantly, please format your commit messages in the following way:

* Prefix Git commit messages with the ticket number, e.g. "Close #42: xyz"
* Describe WHY you are making the change, e.g. "Close #42: Added logback to suppress the debug messages during maven build" (not only "changed logging").

### Clean up commit history

Use `git rebase --interactive` to "squash" multiple commits sensibly into atomic changes. In addition to the man pages for git, there are many resources online to help you understand how these tools work.

### Rebase

Please always base your (updated) pull request on the current master, i.e. rebase against the master branch before creating or updating your pull request. Please don't create pull requests which depend on other pull requests - instead put all commits into a single pull request or wait for the dependent pull request to be integrated.

### Expect discussion and rework

The core team will take a close look before accepting contributions. This is to keep code quality and stability as high as possible, and to keep complexity at a minimum. You may be asked to rework the submission for style (as explained above) and/or substance.

The continuous integration environment will build and test your pull request.

Note that you can always force push (`git push -f`) reworked / rebased commits against the branch used to submit your pull request. i.e. you do not need to issue a new pull request when asked to make changes.

### Now looking for work?

Have a look at the open issues at https://github.com/NovatecConsulting/micronaut-camunda-bpm/issues, especially those tagged with `good first issue`.

We're looking forward to your contribution :-)
