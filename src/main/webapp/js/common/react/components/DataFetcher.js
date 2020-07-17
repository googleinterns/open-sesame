/**
 * @fileoverview This component is used to fetch data for another component.
 * You must supply this component with a function to create a fetch request, as
 * well as a function to render from.
 */
/**
 * @typedef DataFetcherState
 * @property {boolean} isFetching Whether or not the data is currently being
 *    fetched.
 * @property {?Object} data The data received from the fetch request. This is
 *    null if the data is still being fetched.
 */
/**
 * @typedef DataFetcherProps
 * @property {function(AbortSignal): Promise} createFetchRequest
 * @property {function(DataFetcherState): React.Component} render
 */
import checkTesting from '../../../checkTesting.js';
import {basicErrorHandling} from '../../../fetch_handler.js';
checkTesting();

/**
 * A generic data fetching component that can be used for fetching data for any
 * component.
 */
export default class DataFetcher extends React.Component {
  /**
   * Create a generic data fetching component.
   * @param {DataFetcherProps} props
   */
  constructor(props) {
    super(props);

    /**
     * The state of the DataFetcher React component.
     * @type {DataFetcherState}
     */
    this.state = {
      isFetching: true,
      data: null,
    };

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
    this.props.createFetchRequest(this.abortController.signal).then((data) => {
      console.log('Data received:');
      console.log(data);

      this.setState({
        isFetching: false,
        data,
      });
    }).catch((error) => basicErrorHandling(error));
  }

  /**
   * Renders the component passed through the render props:
   * https://reactjs.org/docs/render-props.html
   * @return {React.Component} Returns the React component.
   * @override
   */
  render() {
    return this.props.render(this.state);
  }
}

DataFetcher.propTypes = {
  createFetchRequest: PropTypes.func.isRequired,
  render: PropTypes.func.isRequired,
};
