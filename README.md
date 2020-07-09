# Open Sesame: an Open Source Mentorship Platform
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

## Testing
**Run the tests** with:
```
npm test
```
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

## File Structure
Please refer to [this](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html) resource to learn more about what each project directory should be used for, and take a look at [this section](https://maven.apache.org/guides/getting-started/#how-do-i-make-my-first-maven-project) of the Maven getting started guide to see how the project naming scheme affects directory structure.
