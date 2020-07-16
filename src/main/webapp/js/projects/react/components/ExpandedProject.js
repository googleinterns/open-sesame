import {expandedProjectType} from '../prop_types.js';

export default function ExpandedProject(props) {
  if (props.loading) {
    return (
      <h2 className="text-center mt-1">Loading project...</h2>
    );
  }

  return (
    <div>
      {props.project.previewData.name}
    </div>
  )
}

ExpandedProject.propTypes = {
  loading: PropTypes.bool.isRequired,
  project: expandedProjectType,
};
