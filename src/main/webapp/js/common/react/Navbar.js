function Navbar(props) {
  const urls = [
    {
      href: "/",
      name: "Home"
    },
    {
      href: "/projects.html",
      name: "Projects"
    }
  ];

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <a className="navbar-brand" href="#">Open Sesame</a>
      <button 
          className="navbar-toggler" 
          type="button" 
          data-toggle="collapse" 
          data-target="#navbarSupportedContent" 
          aria-controls="navbarSupportedContent" 
          aria-expanded="false" 
          aria-label="Toggle navigation">
        <span className="navbar-toggler-icon"></span>
      </button>
      <div className="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav ml-auto">
          {urls.map((url, i) => 
              <NavbarLink key={i} href={url.href} name={url.name} />)}
        </ul>
      </div>
    </nav>
  );
}

function NavbarLink(props) {
  let classes = "nav-item";
  if (props.href === window.location.pathname) {
    classes += " active";
  }
  
  return (
    <div className={classes}>
      <a className="nav-link" href={props.href}>{props.name}</a>
    </div>
  );
}

const navbarContainer = document.getElementById("navbar-container");
ReactDOM.render(<Navbar />, navbarContainer);