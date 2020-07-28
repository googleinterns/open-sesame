import {setGlobalsIfTesting} from '../../setTestingGlobals.js';
setGlobalsIfTesting();

export const authDataType = PropTypes.shape({
  authorized: PropTypes.bool.isRequired,
  hasProfile: PropTypes.bool.isRequired,
  user: PropTypes.shape({
    email: PropTypes.string.isRequired,
    id: PropTypes.string.isRequired,
  }),
});

export const navbarLinkType = PropTypes.shape({
  href: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
  requiresAuth: PropTypes.bool.isRequired,
});
