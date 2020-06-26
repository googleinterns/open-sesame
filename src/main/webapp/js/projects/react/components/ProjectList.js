import ProjectPreview from './ProjectPreview.js';

// ProjectTag is an object and not a string because of the upcoming
// addition of different types of tags (to be indicated with a tagType property)
/**
 * @typedef {Object} ProjectTag
 * @property {string} text
 */
/**
 * @typedef {Object} ProjectPreviewData
 * @property {string} title
 * @property {string} shortDescription
 * @property {ProjectTag[]} tags
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
  )
}
