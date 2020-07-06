/**
 * @fileoverview The highest-order component of the mentored project search
 * page. This handles the loading of project preview data.
 */
import ProjectList from './ProjectList.js';
import standardizeFetchErrors from '../../../fetch_handler.js';

export default class ProjectsSearch extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      isFetching: true,
    };
  }

  componentDidMount() {
    const fetchRequest = standardizeFetchErrors(
        fetch('/project-previews'),
        "Failed to communicate with the server, please try again later.",
        "Encountered a server error, please try again later.");
    fetchRequest.then((projectPreviews) => {
      console.log("Project Previews Received:");
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

  render() {
    if (this.state.isFetching) {
      return <h1 className="text-primary text-center mt-2">Loading projects...</h1>;
    }

    return <ProjectList projectPreviews={this.state.projectPreviews} />;
  }
}
