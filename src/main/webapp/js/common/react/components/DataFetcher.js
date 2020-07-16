import checkTesting from '../../../checkTesting.js';
import { basicErrorHandling } from '../../../fetch_handler.js';
checkTesting();

/**
 * A generic data fetching component that can be used for fetching data for any
 * component.
 */
export default class DataFetcher extends React.Component {
  /**
   * Create a generic data fetching component.
   * @param {Object} props 
   */
  constructor(props) {
    super(props);

    this.state = {
      isFetching: true,
      data: null,
    }

    /**
     * Controller used to abort fetch requests if the React component is
     * unmounted during a request:
     * https://stackoverflow.com/questions/31061838/how-do-i-cancel-an-http-fetch-request
     */
    this.abortController = new AbortController();
  }

  /**
   * Called before the component is destroyed.
   * @override
   */
  componentWillUnmount() {
    // Abort ongoing fetch requests if there are any.
    this.abortController.abort();
  }

  /**
   * Called after the component is first rendered by React.
   * @override
   */
  componentDidMount() {
    // TODO : Add automatic retry functionality here.
    // TOOD : Add better user notification system here.
    this.props.getFetchRequest(this.abortController.signal).then((data) => {
      console.log('Data received:');
      console.log(data);
      
      this.setState({
        isFetching: false,
        data,
      });
    }).catch((error) => basicErrorHandling(error));
  }
  
  render() {
    // https://reactjs.org/docs/render-props.html
    return this.props.render(this.state);
  }
}

DataFetcher.propTypes = {
  getFetchRequest: PropTypes.func.isRequired,
  render: PropTypes.func.isRequired,
};