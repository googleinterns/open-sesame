import checkTesting from '../../../checkTesting.js';
import {authDataType} from '../navbar_prop_types.js';
checkTesting();

const USER_SIGNUP_URL = '/signup.html';

/**
 * Login/Logout button that changes its appearance based on the inputted
 * authentication data.
 * @param {Object} props
 * @return {React.Component} Returns the React component.
 */
export default function AuthButton(props) {
  const authData = props.authData;

  let classList = 'btn btn-emphasis';
  let linkText;
  let authLink;
  if (props.loading) {
    // The button is disabled while the authorization data is being loaded.
    classList += ' disabled';
    linkText = 'Log In';
    authLink = '/';
  } else {
    if (authData.authorized) {
      if (authData.hasProfile) {
        authLink = authData.logoutUrl;
        linkText = 'Log Out';
      } else {
        authLink = USER_SIGNUP_URL;
        linkText = 'Create Profile';
      }
    } else {
      linkText = 'Log In';
      authLink = authData.loginUrl;
    }
  }

  return (
    <a href={authLink} className={classList}>
      {linkText}
    </a>
  );
}

AuthButton.propTypes = {
  loading: PropTypes.bool.isRequired,
  authData: authDataType,
};
