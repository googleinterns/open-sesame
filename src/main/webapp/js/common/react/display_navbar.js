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
  ];

  const navbarContainer = document.getElementById('navbar-container');
  ReactDOM.render(<Navbar urls={urls} />, navbarContainer);
}

initNavbar();
