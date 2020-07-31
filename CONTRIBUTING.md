# Contributing guidelines

## Pull Request Checklist

Before sending your pull requests, make sure you followed this list.

- Read [contributing guidelines](CONTRIBUTING.md).
- Ensure you have signed the [Contributor License Agreement (CLA)](https://cla.developers.google.com/).
- Changes are consistent with the [Coding Style](#coding-style).

## How to become a contributor and submit your own code

### Contributor License Agreements

We'd love to accept your patches! Before we can take them, we have to jump a couple of legal hurdles.

Please fill out either the individual or corporate Contributor License Agreement (CLA).

  * If you are an individual writing original source code and you're sure you own the intellectual property, then you'll need to sign an [individual CLA](https://code.google.com/legal/individual-cla-v1.0.html).
  * If you work for a company that wants to allow you to contribute your work, then you'll need to sign a [corporate CLA](https://code.google.com/legal/corporate-cla-v1.0.html).

Follow either of the two links above to access the appropriate CLA and instructions for how to sign and return it. Once we receive it, we'll be able to accept your pull requests.

***NOTE***: Only original source code from you and other people that have signed the CLA can be accepted into the main repository.

### Contributing code

If you have improvements to OpenSesame, send us your pull requests! For those
just getting started, Github has a
[how to](https://help.github.com/articles/using-pull-requests/).

OpenSesame team members will be assigned to review your pull requests. Once the
pull requests are approved by two team members and pass continuous integration
checks, an OpenSesame member will squash and merge your pul request with the 
master branch. After approval and merging, the site will be redeployed on a 
quaterly basis. This is done to ensure that we get a good amount of progress 
onto the website before new versions are rolled out.

If you want to contribute, start working through the OpenSesame codebase,
navigate to the
[Github "issues" tab](https://github.com/googleinterns/open-sesame/issues) and start
looking through interesting issues. If you are not sure of where to start, then
start by trying one of the smaller/easier issues here i.e.
[issues with the "good first issue" label](https://github.com/googleinterns/open-sesame/labels/good%20first%20issue)
and then take a look at the
[issues with the "contributions welcome" label](https://github.com/googleinterns/open-sesame/labels/stat%3Acontributions%20welcome).
. If you want to get acquainted with the code first, It might be useful to
start by looking into our documentation and proposing/fixing some 
[issues with the "documentation" label](https://github.com/googleinterns/open-sesame/issues?q=is%3Aissue+label%3Adocumentation).
These are issues that we believe are particularly well suited for outside
contributions, often because we probably won't get to them right now and it is 
easier to find holes in documentation if you are new to the codebase. 

***NOTE***: If you decide to start on an issue, leave a comment so that other people know that
you're working on it. If you want to help out, but not alone, use the issue
comment thread to coordinate.

### Contribution guidelines and standards

Before sending your pull request for
[review](https://github.com/googleinterns/open-sesame/pulls),
make sure your changes are consistent with the guidelines and follow the
OpenSesame coding style.

#### Coding style

* [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
* [Google JavaScript Style Guide](https://google.github.io/styleguide/jsguide.html)
* [Google HTML/CSS Style Guide](https://google.github.io/styleguide/htmlcssguide.html)

#### Running sanity check and tests

##### Running sanity check

If you have Node installed on your system, you can perform a sanity check on
your changes by running the command:

```bash
npm run sanity-check
```

This will catch most Javascript coding style, testing and BUILD file issues that
may exist in your changes.

##### Running unit tests

If you only want to run tests;

```bash
npm run test # Runs all tests
npm run js-test # Runs only JavaScript tests
npm run java-test # Runs only Java tests 
npm run verify # builds and tests code
```