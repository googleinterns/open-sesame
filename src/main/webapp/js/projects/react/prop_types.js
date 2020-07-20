export const projectPreviewType = PropTypes.shape({
  name: PropTypes.string.isRequired,
  description: PropTypes.string.isRequired,
  topicTags: PropTypes.arrayOf(PropTypes.string).isRequired,
  primaryLanguage: PropTypes.string.isRequired,
  numMentors: PropTypes.number.isRequired,
  repositoryId: PropTypes.string.isRequired,
});

export const mentorType = PropTypes.shape({
  bio: PropTypes.string,
  email: PropTypes.string.isRequired,
  interestTags: PropTypes.arrayOf(PropTypes.string).isRequired,
  gitHubURL: PropTypes.string.isRequired,
  image: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
});

export const expandedProjectType = PropTypes.shape({
  previewData: projectPreviewType.isRequired,
  mentors: PropTypes.arrayOf(mentorType).isRequired,
});
