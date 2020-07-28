/**
 * @fileoverview This component is used to fetch data for another component.
 * Using this component means that you do not need to create your own fetch
 * boilerplate. This component fetches a requested resource and then
 * renders the supplied react component with that resource.
 *
 * To use this component, you need two things:
 * 1) A function that returns a fetch request for the data needed. This will be
 * provided to the DataFetcher through its props. See {@link DataFetcherProps}.
 * One important thing to note is that the DataFetcher supports abort signalling
 * which is used to cancel the fetch request in the case that the component is
 * removed before the fetch can be completed.
 * 2) A function that renders the React component using the data being fetched.
 * See {@link DataFetcherState} for what properties will be sent to the render
 * function.
 *
 * Below is a simple example. For a working example, see ProjectSearch.js.
 * @example
 * // This example loads data from the '/test' endpoint and renders it to a
 * // header element. While the data is loading it displays 'Loading...' in a
 * // header element.
 * <DataFetcher
 *    createFetchRequest={(signal) => fetch('/test', {signal: signal})}
 *    render={(fetchState) => {
 *      if (fetchState.isFetching) {
 *        return <h1>Loading...</h1>;
 *      }
 *      return <h1>fetchState.data.text</h1>;
 *    }} />
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
import {setGlobalsIfTesting} from '../../../setTestingGlobals.js';
import {basicErrorHandling} from '../../../fetch_handler.js';
setGlobalsIfTesting();

/**
 * A generic data fetching component that can be used for fetching data for any
 * component.
 */
export class DataFetcher extends React.Component {
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
    // TODO : Add better user notification system here.
    this.props.createFetchRequest(this.abortController.signal).then((data) => {
      // TODO : Remove for production
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
