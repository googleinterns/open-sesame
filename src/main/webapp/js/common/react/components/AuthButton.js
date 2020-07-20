import checkTesting from '../../../checkTesting.js';
import {
  standardizeFetchErrors,
  basicErrorHandling,
  makeRelativeUrlAbsolute,
} from '../../../fetch_handler.js';
checkTesting();

const USER_SIGNUP_URL = '/signup.html';

/**
 * Login/Logout button that fetches user authorization data to automatically
 * update its appearance.
 * @extends React.Component
 */
export default class AuthButton extends React.Component {
  /**
   * Create the AuthButton react component.
   * @param {{}} props
   */
  constructor(props) {
    super(props);

    this.state = {
      isFetching: true,
    };
  }

  /**
   * Loads user authorization data after React has initialized the component.
   * @override
   */
  componentDidMount() {
    const fetchRequest = standardizeFetchErrors(
        fetch(makeRelativeUrlAbsolute('/auth')),
        'Failed to communicate with the server, please try again later.',
        'Encountered a server error, please try again later.');
    fetchRequest.then((response) => response.json()).then((authData) => {
      console.log('Auth data received:');
      console.log(authData);
      this.setState({
        isFetching: false,
        authData,
      });
    }).catch((error) => basicErrorHandling(error));
  }

  /**
   * Renders the Login/Logout button. If the authorization data is still
   * loading, the button is disabled.
   * @return {React.Component}
   * @override
   */
  render() {
    let classList = 'btn btn-emphasis';
    let linkText;
    let authLink;
    if (this.state.isFetching) {
      // The button is disabled while the authorization data is being loaded.
      classList += ' disabled';
      linkText = 'Log In';
      authLink = '/';
    } else {
      if (this.state.authData.authorized) {
        if (this.state.authData.hasProfile) {
          authLink = this.state.authData.logoutUrl;
          linkText = 'Log Out';
        } else {
          authLink = USER_SIGNUP_URL;
          linkText = 'Create Profile';
        }
      } else {
        linkText = 'Log In';
        authLink = this.state.authData.loginUrl;
      }
    }

    return (
      <a href={authLink} className={classList}>
        {linkText}
      </a>
    );
  }
}
