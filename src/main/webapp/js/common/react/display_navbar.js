/**
 * @fileoverview Adds a React-based Navbar to an HTML page.
 * Before importing this script, be sure to add the React scripts
 * as seen here: https://reactjs.org/docs/add-react-to-a-website.html#step-2-add-the-script-tags.
 * Also, be sure to add a div with the ID "navbar-container". This
 * is where the Navbar will be rendered to.
 */
import Navbar from './components/Navbar.js';

/**
 * Renders the React navbar in the navbar container.
 */
function initNavbar() {
  const urls = [
    {
      href: '/',
      name: 'Home',
    },
    {
      href: '/projects.html',
      name: 'Projects',
    },
    {
      href: '/mentors.html',
      name: 'Mentors',
    },
    {
      href: '/dashboard.html',
      name: 'Dashboard',
    },
  ];

  const navbarContainer = document.getElementById('navbar-container');
  ReactDOM.render(<Navbar urls={urls} />, navbarContainer);
}

initNavbar();
