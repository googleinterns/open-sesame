# Open Sesame: a Mentorship Platform for Open Source
Open Sesame is a platform intended to make contributing to open source less intimidating by matching first-time contributors to experienced mentors.

## React Integration
This platform makes use of the [React framework](https://reactjs.org/). In order to provide React functionality while allowing for non-React HTML and JS
in other areas of the web app, [React is loaded and run on a page-by-page basis](https://reactjs.org/docs/add-react-to-a-website.html) (see `projects.html` for an example of this).
* **Be sure to replace all script tags with the React development scripts to the minified production versions** when the platform is being deployed to production.
These script tags are all clearly commented in each HTML page that makes use of React.
### JSX Preprocessing
This platform makes use of React's [JSX](https://reactjs.org/docs/introducing-jsx.html). In order to use JSX, a [preprocessor](https://reactjs.org/docs/add-react-to-a-website.html#add-jsx-to-a-project) must run before the application is run or deployed.

*Note: Must be in the directory containing `package.json` while working with npm*

**The initial setup** (only needs to be run once):
```
npm install
```

**Before running a development server or deploying to production:**
```
npm run jsx-preprocessor
```

* **There is a nifty shortcut** included that runs the JSX preprocessor and then immediately starts the development server from the cloud shell. Use this by running `npm run dev-server`.
* For every directory containing React components, there exists a subdirectory called `jsx-processed`, which contains the JS files that have had their JSX parsed by the preprocessor. These are the JS files that should be imported with script tags.

## GitHub API
The [GitHub API](https://developer.github.com/v3/) is used on the backend (within Java Servlets) to get public information about repositories and users. The servlets make use of [the Java GitHub API](https://github-api.kohsuke.org/), which makes working with the GitHub API much easier.\
**Please use the helper class `GitHubGetter` while using the GitHub API.** Do not create your own instances of the `GitHub` class.
```java
import org.kohsuke.github.GitHub;
import com.google.opensesame.github.GitHubGetter;

GitHub gitHub = GitHubGetter.getGitHub();
```
### Authorization
To [increase the hourly GitHub API rate limit](https://developer.github.com/v3/#increasing-the-unauthenticated-rate-limit-for-oauth-applications), please update the environment variables in `appengine-web.xml` with the credentials of an [OAuth application's](https://developer.github.com/apps/about-apps/#about-oauth-apps) client ID and client secret. **This must be done for production.** The `GitHubGetter` will still work without credentials, but there will be significantly reduced hourly API rate limits, and a warning will be printed in the console. 
```xml
<env-variables>
  <env-var name="GITHUB_CLIENT_ID" value="CLIENT_ID_HERE" />
  <env-var name="GITHUB_CLIENT_SECRET" value="CLIENT_SECRET_HERE" />
</env-variables>
```

## Continuous Integration (CI)
The configurations for linters and Continuous intigeration GitHub Actions run on
this project can be found in the [LintingAndTestingCI.yaml](.github/workflows/LintingAndTestingCI.yaml) file.

By Default, GitHub makes it so that any commit made by an Action will not trigger a new workflow
(essentially, Linters are not run on linted code). To bypass this, any action that
makes a commit must use a Personal Access Token when run. Further infomation can be found 
[here](https://docs.github.com/en/actions/reference/events-that-trigger-workflows#triggering-new-workflows-using-a-personal-access-token).

Action dependencies are Cached to speed up CI according to the GitHub Actions Cacheing tutorial
found [here](https://docs.github.com/en/actions/configuring-and-managing-workflows/caching-dependencies-to-speed-up-workflows).

__NOTE:__ A proposed PR must pass all CI checks or it will be prevented from merging with Master.


### Building and Testing
The ```job``` labeled ```BuildAndTest``` in the [LintingAndTestingCI.yaml](.github/workflows/LintingAndTestingCI.yaml) file handles building and testing during Continuous Integration. This job runs the ```npm run verify``` command in [package.json](package.json). ```npm run verify``` is a command that runs all the available tests in this repo and builds the OpenSesame Platform with the available code. 

### Linting
#### Java Linter
The ```job``` labeled ```JavaFormat``` in the [LintingAndTestingCI.yaml](.github/workflows/LintingAndTestingCI.yaml) file handles Java Linting. This job formats Java code to meet Google Style Guide standards. Any changes made by this linter are committed with the message _"Google Java Format"_. This Action was built using the [Google Java Format](https://github.com/marketplace/actions/google-java-format#google-java-format-action) GitHub Action.

#### JavaScript Linter
We use ```ESLint``` to lint our JavaScript code. OpenSesame's version of ```ESLint```
runs using the rules defined in our [.eslitrc](./.eslintrc) file (google and react style guide standards).

The ```job``` labeled ```ESLint``` in the [LintingAndTestingCI.yaml](.github/workflows/LintingAndTestingCI.yaml) file handles JavaScript Linting. This ```job``` performs two main steps:

1. Run the locally installed ESLint Linter (ESLint is a part of OpenSesame's
dependencies). The local linter is set to format code with the ```--fix``` option. 
Any ESLint errors and warnings that can't be fixed with the ```--fix``` option are annotated in the file changes PR tab.

2. Commit changes made during the previous step(1) with the [git-auto-commit-action](https://github.com/marketplace/actions/git-auto-commit#git-auto-commit-action). Commits are made with the message _"Apply ESLint Changes"_.

##### Locally Running ESLint 
Run the local ```ESLint``` JavaScript linter with:
```Bash
$ npm install eslint  # if not installed already.
$ ./node_modules/eslint/bin/eslint.js <file or folder>
```
or
```Bash
$ npm run js-lint # runs on all OpenSesame js files
```

## Testing
JavaScript unit testing is done with [Jest](https://jestjs.io/). Along with Jest, additional functionality is added with:
* [jest-dom](https://github.com/testing-library/jest-dom) for testing the DOM.
* [React Testing Library](https://testing-library.com/docs/react-testing-library/intro) for testing React.

The tests are also automatically run when using the `npm run dev-server` command to start a development server.

### JavaScript Testing
JavaScript unit testing is done with [Jest](https://jestjs.io/). Along with Jest, additional functionality is added with:
* [jest-dom](https://github.com/testing-library/jest-dom) for testing the DOM.
* [React Testing Library](https://testing-library.com/docs/react-testing-library/intro) for testing React.

### Java Testing
Java unit testing is done with [JUnit 4](https://junit.org/junit4/).

### Adding tests
**To add another JS test**, simply add a JS file that ends with `.test.js` (ex: `script.test.js`) and it will automatically be run by Jest.
**To add another Java test**, add a Java file to the testing directory `src/test/java/`. Be sure to follow the Java conventions for package directory structure, for example if you're testing a package in `com.google.opensesame.github`, the test should be placed in the directory `src/test/java/com/google/opensesame/github`.

## User Authentication and Sign Up
User signup consists of three parts:
1. Website authentication (a user creates an account on our website)
  * This is done using the [Users API](https://cloud.google.com/appengine/docs/standard/java/users)
  * In the future this could be migrated to the [Firebase Admin SDK](https://firebase.google.com/docs/reference/admin) or similar OAuth-based authentication API such that GitHub authentication and website authentication could become a single step.
2. GitHub authentication (a user verifies ownership of a GitHub account, which is then linked with the account on our website)
  * This is done using [Firebase Authentication](https://firebase.google.com/docs/auth) with [GitHub OAuth](https://docs.github.com/en/developers/apps/about-apps#about-oauth-apps)
  * An access token is granted to the user, which is then sent to the server during the profile creation step and verified on the backend. 
  * Once the access token is verified, the user has proven ownership of that GitHub account, and it is then stored by ID in the website profile.
3. Profile creation (a user with an authorized account can add more information about themselves, such as interest tags)
  * After a user completes website authentication, they are redirected to a form for profile creation.
  * GitHub authentication is completed during profile creation.

A user is only considered to have an account on Open Sesame if they have completed all three steps outlined above.

## Common Troubleshooting Tips
In the past, our developers have run into random issues building the codebase. 
If all else fails while troubleshooting, tyr the following commands;

- ```Bash
$ npm install
```
- ```Bash
$ mvn clean
```

## File Structure
Please refer to [this](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html) resource to learn more about what each project directory should be used for, and take a look at [this section](https://maven.apache.org/guides/getting-started/#how-do-i-make-my-first-maven-project) of the Maven getting started guide to see how the project naming scheme affects directory structure.

### Data Structures and Naming
The only data OpenSesame intends to store in Datastore is data that can’t be sourced from GitHub. Getting Data directly from GitHub allows us avoid issues where OpenSesame data is out of sync with GitHub.

When Data is sent to the front-end, we plan to conditionally populate Information sourced from GitHub. Conditional population of GitHub data will help us conserve our limited API calls.

As a naming convention, Java Files that end in ```Entity``` refer to ```Objectify``` enabled classes that represent data as it is stored in datastore. Java files that end in ```Data``` refer to classes that represent data that is sent to the frontend. ```Data``` classes are directly serialized to the ```JSON``` that is retreived when ```GET``` requests are sent to the various OpenSesame Java servlets.

__for example,__ If ```X``` is an aspect of our website with unique data that we plan to store in Datastore. We will handle it with an Objectify enabled ```XEntity``` Java class. If X also requires data from GitHub, we will have an additional ```XData``` Java class that builds on ```XEntity``` by populating its fields using the GitHub API. ```XData``` can then be converted to a ```JSON``` with the ```Gson``` Library and sent to the frontend using the ```XServlet```.

![Diagram of Data Structuring convention](https://i.postimg.cc/8PzT3yqn/sql1-N6j51-FA2-FYi-Ena1-S4-Q.png)

This convention allows us to manipulate data on the backend without having to waste API calls if there is no need to.
