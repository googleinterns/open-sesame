import {setGlobalsIfTesting} from '../../../setTestingGlobals.js';
import {authDataType} from '../navbar_prop_types.js';
setGlobalsIfTesting();

export const USER_SIGNUP_URL = '/signup.html';
export const HOME_URL = '/';

/**
 * Login/Logout button that changes its appearance based on the inputted
 * authentication data.
 * @param {Object} props
 * @return {React.Component} Returns the React component.
 */
export function AuthButton(props) {
  const authData = props.authData;

  let classList = 'btn btn-emphasis';
  let linkText;
  let authLink;
  if (props.loading) {
    // The button is disabled while the authorization data is being loaded.
    classList += ' disabled';
    linkText = 'Log In';
    authLink = HOME_URL;
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
