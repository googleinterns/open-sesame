/**
 * @fileoverview The highest-order component of the mentored project search
 * page. This handles the loading of project preview data.
 */
import {ProjectList} from './ProjectList.js';
import {
  standardizeFetchErrors,
  makeRelativeUrlAbsolute,
} from '../../../fetch_handler.js';
import {DataFetcher} from '../../../common/react/components/DataFetcher.js';
import {ExpandedProject} from './ExpandedProject.js';

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
        projectPreviews={dataFetcherState.data} />
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
    const projectUrl = makeRelativeUrlAbsolute('/projects');
    projectUrl.searchParams.append('projectId', projectId);
    projectUrl.searchParams.append('fullData', true);
    const fetchRequest = fetch(projectUrl, {
      method: 'get',
      signal: signal,
    });

    return standardizeFetchErrors(
        fetchRequest,
        'Failed to communicate with the server, please try again later.',
        'Encountered a server error, please try again later.')
        .then((response) => response.json()).then((projects) => projects[0]);
  };
}

/**
 * Creates a fetch request for project previews.
 * @param {AbortSignal} signal
 * @return {Promise} Returns the fetch request.
 */
function createProjectPreviewFetch(signal) {
  const projectUrl = makeRelativeUrlAbsolute('/projects');
  projectUrl.searchParams.append('previewData', true);
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
