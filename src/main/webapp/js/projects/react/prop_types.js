export const projectPreviewType = PropTypes.shape({
  name: PropTypes.string.isRequired,
  description: PropTypes.string.isRequired,
  topicTags: PropTypes.arrayOf(PropTypes.string).isRequired,
  primaryLanguage: PropTypes.string.isRequired,
  numMentors: PropTypes.number.isRequired,
});
