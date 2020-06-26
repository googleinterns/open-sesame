import ProjectTagList from './ProjectTagList.js';

/**
 * A card-based preview of a project.
 * @param {{projectPreview: ProjectPreviewData}} props
 * @return {React.Component} Returns a React component project preview card.
 */
export default function ProjectPreview(props) {
  const projectPreview = props.projectPreview;

  return (
    <div className="p-1 col-lg-4">
      <div className="project-preview card h-100">
        <div className="card-body pb-0">
          <h5 className="card-title text-primary">{projectPreview.title}</h5>
          <ProjectTagList tags={projectPreview.tags} />
          <p>{projectPreview.shortDescription}</p>
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
    </div>
  );
}
