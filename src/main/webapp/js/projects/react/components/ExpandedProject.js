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

  // TODO : Add expanded project frontend view.
  // For now this is just the name of the project to ensure that everything
  // works.
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
