import checkTesting from '../../../checkTesting.js';
import NavbarLink from './NavbarLink.js';
import AuthButton from './AuthButton.js';
import {authDataType} from '../navbar_prop_types.js';
checkTesting();

/**
 * @typedef NavbarUrl
 * @property {string} href
 * @property {string} name
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
export default function Navbar(props) {
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
          {props.urls.map((url, i) =>
            <NavbarLink key={i} href={url.href} name={url.name} />)}
        </ul>
      </div>
      <div className="ml-1">
        <AuthButton loading={props.loading} authData={props.authData} />
      </div>
    </nav>
  );
}

Navbar.propTypes = {
  urls: PropTypes.arrayOf(PropTypes.shape({
    href: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
  })).isRequired,
  loading: PropTypes.bool.isRequired,
  authData: authDataType,
};
