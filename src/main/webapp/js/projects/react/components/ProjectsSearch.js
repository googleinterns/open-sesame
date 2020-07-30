/**
 * @fileoverview The highest-order component of the mentored project search
 * page. This handles the loading of project preview data.
 */
import {ProjectList} from './ProjectList.js';
import {DataFetcher} from '../../../common/react/components/DataFetcher.js';
import {ExpandedProject} from './ExpandedProject.js';
import {
  standardizeFetchErrors,
  makeRelativeUrlAbsolute,
} from '../../../fetch_handler.js';
import {checkTesting} from '../../../checkTesting.js';
checkTesting();

// Using HashRouter instead of standard BrowserRouter because it supports
// client-side routing that doesn't interfere with server-side
// (servlet) routing.
// https://stackoverflow.com/questions/27928372/react-router-urls-dont-work-when-refreshing-or-writing-manually
const Router = ReactRouterDOM.HashRouter;
const Switch = ReactRouterDOM.Switch;
const Route = ReactRouterDOM.Route;

/**
 * The main project search page react component.
 * @extends React.Component
 */
export class ProjectsSearch extends React.Component {
  /**
   * Create the projects search page react component.
   * @param {{}} props
   */
  constructor(props) {
    super(props);

    this.state = {
      isFetching: true,
    };
  }

  /**
   * Renders either a list of project previews or the expanded view of a single
   * project.
   * @return {React.Component}
   * @override
   */
  render() {
    return (
      <Router>
        <Switch>
          <Route path="/:projectId">
            <ExpandedProjectFetcher />
          </Route>
          <Route path="/">
            <ProjectPreviewFetcher />
          </Route>
        </Switch>
      </Router>
    );
  }
}

/**
 * A data fetcher for project preview data that renders the list of project
 * previews after the data is loaded.
 * @return {React.Component} Returns the React component.
 */
function ProjectPreviewFetcher() {
  const onRender = (dataFetcherState) => {
    return (
      <ProjectList
        loading={dataFetcherState.isFetching}
        projectPreviews={dataFetcherState.data}
        inRouter={true} />
    );
  };

  return (
    <DataFetcher
      createFetchRequest={createProjectPreviewFetch}
      render={onRender} />
  );
}

/**
 * A data fetcher for project expanded project data that renders the expanded
 * project view after the data is loaded.
 * @return {React.Component} Returns the React component.
 */
function ExpandedProjectFetcher() {
  const {projectId} = ReactRouterDOM.useParams();
  const onRender = (dataFetcherState) => {
    return (
      <ExpandedProject
        loading={dataFetcherState.isFetching}
        project={dataFetcherState.data} />
    );
  };

  return (
    <DataFetcher
      createFetchRequest={createProjectFetchGetter(projectId)}
      render={onRender} />
  );
}

/**
 * Creates a function that creates a fetch request with an inputted signal.
 * This is used in the DataFetcher component to create a fetch request to get
 * project data.
 * @param {string} projectId
 * @return {function(AbortSignal): Promise} Returns the function to create
 *    fetch requests.
 */
function createProjectFetchGetter(projectId) {
  return (signal) => {
    const projectUrl = makeRelativeUrlAbsolute('/projects/full');
    projectUrl.searchParams.append('projectId', projectId);
    const fetchRequest = fetch(projectUrl, {
      method: 'get',
      signal: signal,
    });

    const errorFormattedFetchRequest = standardizeFetchErrors(
        fetchRequest,
        'Failed to communicate with the server, please try again later.',
        'Encountered a server error, please try again later.');

    return errorFormattedFetchRequest.then((response) => {
      return response.json();
    }).then((projects) => {
      const projectData = projects[0];
      if (projectData.readmeUrl) {
        return getProjectReadme(projectData, signal);
      } else {
        return projectData;
      }
    });
  };
}

/**
 * Gets the project README markdown, converts it to HTML, sanitizes the HTML,
 * and then adds it to the project data as readmeHtml.
 * @param {Object} projectData 
 * @param {AbortSignal} signal
 * @return {Promise} Returns a promise that resolves to the project data with
 *     the added readme HTML.
 */
function getProjectReadme(projectData, signal) {
  return fetch(projectData.readmeUrl, {
    method: 'get',
    signal: signal,
  }).then((response) => response.text()).then((readmeMarkdown) => {
    const readmeHtml = DOMPurify.sanitize(marked(readmeMarkdown));
    projectData.readmeHtml = readmeHtml;
    return projectData;
  });
}

/**
 * Creates a fetch request for project previews.
 * @param {AbortSignal} signal
 * @return {Promise} Returns the fetch request.
 */
function createProjectPreviewFetch(signal) {
  const projectUrl = makeRelativeUrlAbsolute('/projects/preview');
  const fetchRequest = fetch(projectUrl, {
    method: 'get',
    signal: signal,
  });

  return standardizeFetchErrors(
      fetchRequest,
      'Failed to communicate with the server, please try again later.',
      'Encountered a server error, please try again later.')
      .then((response) => response.json());
}
