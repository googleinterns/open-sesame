import {checkTesting} from '../../../checkTesting.js';
import {NavbarLink} from './NavbarLink.js';
import {AuthButton} from './AuthButton.js';
import {authDataType, navbarLinkType} from '../navbar_prop_types.js';
checkTesting();

/**
 * @typedef NavbarUrl
 * @property {string} href
 * @property {string} name
 * @property {boolean} requiresAuth
 */
/**
 * @typedef NavbarProps
 * @property {NavbarUrl[]} urls
 * @property {boolean} loading
 * @property {?Object} authData
 */
/**
 * Returns a React navbar component.
 * @param {NavbarProps} props
 * @return {React.Component} Returns the navbar.
 */
export function Navbar(props) {
  const authenticated =
      !props.loading && props.authData.authorized && props.authData.hasProfile;

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <a className="navbar-brand" href="/">Open Sesame</a>
      <button
        className="navbar-toggler"
        data-toggle="collapse"
        data-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent"
        aria-expanded="false"
        aria-label="Toggle navigation">
        <span className="navbar-toggler-icon"></span>
      </button>
      <div className="collapse navbar-collapse" id="navbarSupportedContent">
        <ul className="navbar-nav ml-auto">
          {props.urls.map((url, i) => renderNavbarLink(url, authenticated))}
        </ul>
        <div className="form-inline ml-lg-1 my-2 my-lg-0">
          <AuthButton loading={props.loading} authData={props.authData} />
        </div>
      </div>
    </nav>
  );
}

/**
 * Determines whether or not to render a NavbarLink depending on the
 * authentication status of the user.
 * @param {NavbarUrl} url
 * @param {boolean} authenticated
 * @return {?React.Component} Returns the NavbarLink or null.
 */
function renderNavbarLink(url, authenticated) {
  if (authenticated || !url.requiresAuth) {
    return <NavbarLink key={url.href} url={url} />;
  } else {
    return null;
  }
}

Navbar.propTypes = {
  urls: PropTypes.arrayOf(navbarLinkType).isRequired,
  loading: PropTypes.bool.isRequired,
  authData: authDataType,
};
