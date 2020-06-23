# Open Sesame: an Open Source Mentorship Platform
Open Sesame is a platform intended to make contributing to open source less intimidating by matching first-time contributors to experienced mentors.

## React Integration
This platform makes use of the [React framework](https://reactjs.org/). In order to provide React functionality to the projects section while allowing for non-React HTML and JS
in other areas of the web app, [React is loaded and run on a page-by-page basis](https://reactjs.org/docs/add-react-to-a-website.html) (see `projects.html` for an example of this).
### Project Search
The project search page uses React. All of the React components for the project search page can be found in `webapp/js/projects/react`. Within this folder, there also exists a directory called `jsx-processed`, which
contains JS files that have had their [JSX parsed](https://reactjs.org/docs/add-react-to-a-website.html#add-jsx-to-a-project).\
* **The parsing of JSX does not happen automatically**, and must instead be started with `npm run projects-jsx` from within the outermost directory of the repository. This must be done before a development server is run or the application is deployed.\
* **Be sure to replace all script tags with the React development scripts to the minified production versions** when the platform is being deployed to production.
These script tags are all clearly commented in `projects.html`.

## File Structure
Please refer to [this](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html) resource to learn more about what each project directory should be used for, and take a look at [this section](https://maven.apache.org/guides/getting-started/#how-do-i-make-my-first-maven-project) of the Maven getting started guide to see how the project naming scheme affects directory structure.
