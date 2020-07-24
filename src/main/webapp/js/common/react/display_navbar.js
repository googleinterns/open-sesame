/**
 * @fileoverview Adds a React-based Navbar to an HTML page.
 * Before importing this script, be sure to add the React scripts
 * as seen here: https://reactjs.org/docs/add-react-to-a-website.html#step-2-add-the-script-tags.
 * Also, be sure to add a div with the ID "navbar-container". This
 * is where the Navbar will be rendered to.
 */
import Navbar from './components/Navbar.js';
import {DataFetcher} from './components/DataFetcher.js';
import {authDataFetch} from '../../auth_check.js';

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
        createFetchRequest={() => authDataFetch}
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

initNavbar();
