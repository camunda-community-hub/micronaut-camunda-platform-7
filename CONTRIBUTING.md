# Getting Started

To get started see our example application at [/micronaut-camunda-bpm-example](/micronaut-camunda-bpm-example).

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
