/**
 * A horizontal list of badge-based tags for a project.
 * @param {{tags: ProjectTag[]}} props
 * @return {React.Component} Returns a React component list of tags for a 
 * project.
 */
export default function ProjectTagList(props) {
  return (
    <div className="d-flex flex-wrap">
      {props.tags.map((tag, i) => {
        return (
          <div key={i} 
              className="border border-muted text-muted mr-1 mb-1 badge">
            {tag.text}
          </div>
        );
      })}
    </div>
  )
}
