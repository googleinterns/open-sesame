export const authDataType = PropTypes.shape({
  authorized: PropTypes.bool.isRequired,
  hasProfile: PropTypes.bool.isRequired,
  user: PropTypes.shape({
    email: PropTypes.string.isRequired,
    id: PropTypes.string.isRequired,
  }),
});
