/**
 * @fileoverview Adds a React-based Navbar to an HTML page.
 * Before importing this script, be sure to add the React scripts
 * as seen here: https://reactjs.org/docs/add-react-to-a-website.html#step-2-add-the-script-tags.
 * Also, be sure to add a div with the ID "navbar-container". This
 * is where the Navbar will be rendered to.
 */
import Navbar from './components/Navbar.js';
import {DataFetcher} from './components/DataFetcher.js';
import {
  standardizeFetchErrors,
  makeRelativeUrlAbsolute,
} from '../../fetch_handler.js';

const urls = [
  {
    href: '/',
    name: 'Home',
    requiresAuth: false,
  },
  {
    href: '/projects.html',
    name: 'Projects',
    requiresAuth: false,
  },
  {
    href: '/mentors.html',
    name: 'Mentors',
    requiresAuth: false,
  },
  {
    href: '/dashboard.html',
    name: 'Dashboard',
    requiresAuth: true,
  },
];

/**
 * Renders the React navbar in the navbar container.
 */
function initNavbar() {
  const navbarContainer = document.getElementById('navbar-container');
  ReactDOM.render(
      <DataFetcher
        createFetchRequest={createAuthFetchRequest}
        render={renderNavbar} />,
      navbarContainer);
}

/**
 * Renders the Navbar using the state of the data fetcher.
 * @param {DataFetcherState} dataFetcher
 * @return {React.Component} Returns the React component.
 */
function renderNavbar(dataFetcher) {
  return (
    <Navbar
      urls={urls}
      loading={dataFetcher.isFetching}
      authData={dataFetcher.data} />
  );
}

/**
 * Creates a fetch request to get the authentication status of the user.
 * @param {AbortSignal} signal
 * @return {Promise} Returns the authentication fetch request.
 */
function createAuthFetchRequest(signal) {
  const fetchRequest = fetch(makeRelativeUrlAbsolute('/auth'), {
    method: 'GET',
    signal: signal,
  });

  return standardizeFetchErrors(
      fetchRequest,
      'Failed to communicate with the server, please try again later.',
      'Encountered a server error, please try again later.')
      .then((response) => response.json());
}

initNavbar();
