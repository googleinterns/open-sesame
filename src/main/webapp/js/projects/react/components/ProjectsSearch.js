/**
 * @fileoverview The highest-order component of the mentored project search
 * page. This handles the loading of project preview data.
 */
import ProjectList from './ProjectList.js';
import standardizeFetchErrors from '../../../fetch_handler.js';

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
   * Loads the project preview data after React has initialized the component.
   */
  componentDidMount() {
    const fetchRequest = standardizeFetchErrors(
        fetch('/project-previews'),
        'Failed to communicate with the server, please try again later.',
        'Encountered a server error, please try again later.');
    fetchRequest.then((projectPreviews) => {
      console.log('Project Previews Received:');
      console.log(projectPreviews);
      this.setState({
        isFetching: false,
        projectPreviews,
      });
    }).catch((errorResponse) => {
      alert(errorResponse.userMessage);
      console.error(
          `Error ${errorResponse.statusCode}: ${errorResponse.error}`);
    });
  }

  /**
   * Renders the list of project previews or a loading notification if the data
   * is still being loaded.
   */
  render() {
    if (this.state.isFetching) {
      return (
        <h1 className="text-primary text-center mt-2">Loading projects...</h1>
      );
    }

    return <ProjectList projectPreviews={this.state.projectPreviews} />;
  }
}
