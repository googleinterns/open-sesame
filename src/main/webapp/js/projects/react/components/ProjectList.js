import ProjectPreview from './ProjectPreview.js';
import {projectPreviewType} from '../prop_types.js';

/**
 * @typedef {Object} ProjectPreviewData
 * @property {string} name
 * @property {string} description
 * @property {string[]} topicTags
 * @property {string} primaryLanguage
 * @property {number} numMentors
 */
/**
 * A list of card-based project previews.
 * @param {{projectPreviews: ProjectPreviewData}} props
 * @return {React.Component} Returns a React component which is a list of
 * project previews.
 */
export default function ProjectList(props) {
  return (
    <div className="p-4 row">
      {props.projectPreviews.map((projectPreview, i) =>
        <ProjectPreview key={i} projectPreview={projectPreview} />)}
    </div>
  );
}

ProjectList.propTypes = {
  projectPreviews: PropTypes.arrayOf(projectPreviewType).isRequired,
};
