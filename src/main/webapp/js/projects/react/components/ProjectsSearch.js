/**
 * @fileoverview The highest-order component of the mentored project search
 * page. This handles the loading of project preview data.
 */
import ProjectList from './ProjectList.js';
import {
  standardizeFetchErrors,
  makeRelativeUrlAbsolute,
  basicErrorHandling,
} from '../../../fetch_handler.js';
import DataFetcher from '../../../common/react/components/DataFetcher.js';
import ProjectPreview from './ProjectPreview.js';

const Router = ReactRouterDOM.BrowserRouter;
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
   * Renders the list of project previews or a loading notification if the data
   * is still being loaded.
   * @return {React.Component}
   * @override
   */
  render() {
    return (
      <Router basename="/projects.html">
        <Switch>
          <Route path="/">
            <DataFetcher 
                getFetchRequest={this.getProjectPreviewsFetch}
                render={this.renderProjectPreviews} />
          </Route>
          <Route path="/:id">
            
          </Route>
        </Switch>
      </Router>
    );
  }

  renderProjectPreviews(dataFetcher) {
    return (
      <ProjectList 
          loading={dataFetcher.isFetching}
          projectPreviews={dataFetcher.data} />
    );
  }

  getProjectPreviewsFetch(signal) {
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
}
