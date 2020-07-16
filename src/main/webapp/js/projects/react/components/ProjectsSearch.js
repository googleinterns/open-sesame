/**
 * @fileoverview The highest-order component of the mentored project search
 * page. This handles the loading of project preview data.
 */
import ProjectList from './ProjectList.js';
import {
  standardizeFetchErrors,
  makeRelativeUrlAbsolute,
} from '../../../fetch_handler.js';
import DataFetcher from '../../../common/react/components/DataFetcher.js';
import ExpandedProject from './ExpandedProject.js';

// Using HashRouter to avoid having to configure servlet routing:
// https://stackoverflow.com/questions/27928372/react-router-urls-dont-work-when-refreshing-or-writing-manually
const Router = ReactRouterDOM.HashRouter;
const Switch = ReactRouterDOM.Switch;
const Route = ReactRouterDOM.Route;

/**
 * The main project search page react component.
 * @extends React.Component
 */
export default class ProjectsSearch extends React.Component {
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
  const onRender = (dataFetcher) => {
    return (
      <ProjectList
        loading={dataFetcher.isFetching}
        projectPreviews={dataFetcher.data} />
    );
  }
  
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
  const { projectId } = ReactRouterDOM.useParams();
  const onRender = (dataFetcher) => {
    return (
      <ExpandedProject 
          loading={dataFetcher.isFetching} 
          project={dataFetcher.data} />
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
 * @return {function(AbortSignal): Promise}
 */
function createProjectFetchGetter(projectId) {
  return (signal) => {
    const projectUrl = makeRelativeUrlAbsolute('/project');
    projectUrl.searchParams.append("projectId", projectId);
    const fetchRequest = fetch(projectUrl, {
      method: 'get',
      signal: signal,
    });

    return standardizeFetchErrors(
      fetchRequest,
      'Failed to communicate with the server, please try again later.',
      'Encountered a server error, please try again later.')
          .then((response) => response.json());
  };
}

/**
 * Creates a fetch request for project previews.
 * @param {AbortSignal} signal 
 */
function createProjectPreviewFetch(signal) {
  const fetchRequest = fetch(makeRelativeUrlAbsolute('/project-previews'), {
    method: 'get',
    signal: signal,
  });
  
  return standardizeFetchErrors(
    fetchRequest,
    'Failed to communicate with the server, please try again later.',
    'Encountered a server error, please try again later.')
        .then((response) => response.json());
}
