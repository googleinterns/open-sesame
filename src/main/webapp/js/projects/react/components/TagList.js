/**
 * A horizontal list of badge-based tags.
 * @param {{tags: string[]}} props
 * @return {React.Component} Returns a React component list of tags.
 */
export function TagList(props) {
  return (
    <div className="d-flex flex-wrap">
      {props.tags.map((tag, i) => {
        return (
          <div key={i}
            className="border border-muted text-muted mr-1 mb-1 badge">
            {tag}
          </div>
        );
      })}
    </div>
  );
}

TagList.propTypes = {
  tags: PropTypes.arrayOf(PropTypes.string).isRequired,
};
