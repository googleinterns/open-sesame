import {checkTesting} from '../../checkTesting.js';
checkTesting();

const projectPreviewData = {
  name: PropTypes.string.isRequired,
  description: PropTypes.string.isRequired,
  topicTags: PropTypes.arrayOf(PropTypes.string).isRequired,
  primaryLanguage: PropTypes.string.isRequired,
  numMentors: PropTypes.number.isRequired,
  repositoryId: PropTypes.string.isRequired,
};

export const projectPreviewType = PropTypes.shape(projectPreviewData);

export const mentorType = PropTypes.shape({
  userID: PropTypes.string.isRequired,
  bio: PropTypes.string,
  email: PropTypes.string,
  interestTags: PropTypes.arrayOf(PropTypes.string).isRequired,
  gitHubURL: PropTypes.string.isRequired,
  image: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
  location: PropTypes.string,
});

export const expandedProjectType = PropTypes.shape({
  ...projectPreviewData,
  mentors: PropTypes.arrayOf(mentorType).isRequired,
});
