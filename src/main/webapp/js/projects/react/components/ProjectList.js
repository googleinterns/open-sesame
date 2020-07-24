import {ProjectPreview} from './ProjectPreview.js';
import {projectPreviewType} from '../prop_types.js';
import {checkTesting} from '../../../checkTesting.js';
checkTesting();

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
export function ProjectList(props) {
  if (props.loading) {
    return (
      <h2 className="text-center mt-1">Loading projects...</h2>
    );
  }

  return (
    <div className="p-4 row">
      {props.projectPreviews.map((projectPreview) => {
        return (
          <div className="p-1 col-lg-4" key={projectPreview.repositoryId}>
            <ProjectPreview 
                projectPreview={projectPreview}
                inRouter={props.inRouter} />
          </div>
        );
      })}
    </div>
  );
}

ProjectList.propTypes = {
  loading: PropTypes.bool.isRequired,
  projectPreviews: PropTypes.arrayOf(projectPreviewType),
  inRouter: PropTypes.bool.isRequired,
};
