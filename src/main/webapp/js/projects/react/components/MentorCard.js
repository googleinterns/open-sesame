import { mentorType } from "../prop_types.js";
import TagList from "./TagList.js";

export default function MentorCard(props) {
  const mentor = props.mentor;

  return (
    <div className="card project-card h-100">
      <div className="d-flex flex-column card-body">
        <div className="d-flex justify-content-center mb-2">
          <img className="mentor-picture" src={mentor.image} />
        </div>
        <h5 className="card-title text-center text-dark">
          <a href={mentor.gitHubURL}>{mentor.name}</a>
        </h5>
        {mentor.location
            ? <h6 className="card-subtitle text-muted text-center mb-1">
                {mentor.location}
              </h6>
            : null}
        <TagList tags={mentor.interestTags} />
        {mentor.bio
            ? <p>{mentor.bio}</p>
            : null}
        <button className="mt-auto btn btn-light">Connect</button>
      </div>
    </div>
  )
}

MentorCard.propTypes = {
  mentor: mentorType.isRequired,
};
