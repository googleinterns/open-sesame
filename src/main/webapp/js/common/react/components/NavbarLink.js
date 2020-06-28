import checkTesting from '../tests/checkTesting.js';
checkTesting();

/**
 * Returns a React navbar link component.
 * @param {{href: string, name: string}} props
 * @return {React.Component} Returns the navbar link.
 */
export default function NavbarLink(props) {
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

NavbarLink.propTypes = {
  href: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
};
