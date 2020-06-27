export const projectTagType = PropTypes.shape({
    text: PropTypes.string.isRequired,
});

export const projectPreviewType = PropTypes.shape({
    title: PropTypes.string.isRequired,
    shortDescription: PropTypes.string.isRequired,
    tags: PropTypes.arrayOf(projectTagType).isRequired,
    numMentors: PropTypes.number.isRequired,
});
