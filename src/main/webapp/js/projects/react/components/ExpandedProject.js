import {TagList} from './TagList.js';
import {MentorCard} from './MentorCard.js';
import {expandedProjectType} from '../prop_types.js';

/**
 * An expanded view of a project.
 * @param {Object} props
 * @return {React.Component} Returns the React component.
 */
export function ExpandedProject(props) {
  if (props.loading) {
    return (
      <h2 className="text-center mt-1">Loading project...</h2>
    );
  }

  const project = props.project;
  const projectTags = project.previewData.topicTags;
  // Adds the primary language to the list of project tags. TODO add
  // different types of tags in the future to make it easier to differentiate
  // topics from languages.
  projectTags.unshift(project.previewData.primaryLanguage);

  return (
    <div className="container-fluid">
      <div></div>
      <div className="row mt-4">
        <div className="col-md-9">
          <div className="card mx-1 mb-2">
            <div className="card-body px-3 py-2">
              <h1 className="card-title emphasis">
                {project.previewData.name}
              </h1>
              <TagList tags={projectTags} />
            </div>
          </div>
          <div className="ml-1 emphasis">Mentors:</div>
          <div className="d-flex flex-wrap mb-2">
            {project.mentors.map((mentor) => {
              return (
                <div className="p-1 col-lg-3" key={mentor.userID}>
                  <MentorCard mentor={mentor} />
                </div>
              );
            })}
          </div>
          <div className="ml-1 emphasis">Description:</div>
          <div className="card mx-1">
            {/* TODO : Add README renderer instead of description. */}
            <div className="card-body px-3 py-2">
              {project.previewData.description}
            </div>
          </div>
        </div>
        <div className="col-md-3">
          {/* GitHub repository information will go here. */}
        </div>
      </div>
    </div>
  );
}

ExpandedProject.propTypes = {
  loading: PropTypes.bool.isRequired,
  project: expandedProjectType,
};
