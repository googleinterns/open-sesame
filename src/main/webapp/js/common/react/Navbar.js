/**
 * Returns a React navbar component.
 * @param {{}} props The properties of the navbar.
 * @return {React.Component} Returns the navbar.
 */
function Navbar(props) {
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

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <a className="navbar-brand" href="#">Open Sesame</a>
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
          {urls.map((url, i) =>
              <NavbarLink key={i} href={url.href} name={url.name} />)}
        </ul>
      </div>
    </nav>
  );
}

/**
 * Returns a React navbar link component.
 * @param {{href: string, name: string}} props
 * @return {React.Component} Returns the navbar link.
 */
function NavbarLink(props) {
  let classes = 'nav-item';
  if (props.href === window.location.pathname) {
    classes += ' active';
  }

  return (
    <div className={classes}>
      <a className="nav-link" href={props.href}>{props.name}</a>
    </div>
  );
}

const navbarContainer = document.getElementById('navbar-container');
ReactDOM.render(<Navbar />, navbarContainer);
