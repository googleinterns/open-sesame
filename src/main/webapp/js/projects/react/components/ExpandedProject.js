import ProjectTagList from './ProjectTagList.js';
import {expandedProjectType} from '../prop_types.js';

/**
 * An expanded view of a project.
 * @param {Object} props
 * @return {React.Component} Returns the React component.
 */
export default function ExpandedProject(props) {
  if (props.loading) {
    return (
      <h2 className="text-center mt-1">Loading project...</h2>
    );
  }

  const project = props.project;
  const projectTags = project.previewData.topicTags;
  // Adds the primary language to the list of project tags. Should add
  // different types of tags in the future to make it easier to differentiate
  // topics from languages.
  projectTags.unshift(project.previewData.primaryLanguage);

  return (
    <div className="container">
      <div className="row mt-2">
        <div className="col-md-8">
          <div className="card mb-2">
            <div className="card-body px-3 py-2">
              <h3 className="card-title emphasis">
                {project.previewData.name}
              </h3>
              <ProjectTagList tags={projectTags} />
            </div>
          </div>
          <div className="card">
            {/* TODO : Add README renderer instead of description. */}
            <div className="card-body px-3 py-2">
              {project.previewData.description}
            </div>
          </div>
        </div>
        <div className="col-md-4">
        
        </div>
      </div>  
    </div>
  );
}

ExpandedProject.propTypes = {
  loading: PropTypes.bool.isRequired,
  project: expandedProjectType,
};
