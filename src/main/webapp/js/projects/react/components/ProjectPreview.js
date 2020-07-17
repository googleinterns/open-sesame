import TagList from './TagList.js';
import {projectPreviewType} from '../prop_types.js';

const Link = ReactRouterDOM.Link;

/**
 * A card-based preview of a project.
 * @param {{projectPreview: ProjectPreviewData}} props
 * @return {React.Component} Returns a React component project preview card.
 */
export default function ProjectPreview(props) {
  const projectPreview = props.projectPreview;
  const projectTags = projectPreview.topicTags;
  // Adds the primary language to the list of project tags. Should add
  // different types of tags in the future to make it easier to differentiate
  // topics from languages.
  projectTags.unshift(projectPreview.primaryLanguage);

  return (
    <div className="project-card card h-100">
      <div className="card-body pb-0">
        <Link
          to={'/' + projectPreview.repositoryId}
          className="stretched-link">
          <h5 className="card-title emphasis">{projectPreview.name}</h5>
        </Link>
        <TagList tags={projectTags} />
        <p>{projectPreview.description}</p>
      </div>
      <div className=
        "px-3 pb-3 mt-auto d-flex flex-wrap justify-content-center">
        <span>
          <strong className="h5 text-success mr-2">
            {projectPreview.numMentors}
          </strong>
            Mentors
        </span>
      </div>
    </div>
  );
}

ProjectPreview.propTypes = {
  projectPreview: projectPreviewType.isRequired,
};
