/**
 * Get mentors from the mentor servlet.
 * @param {String} response
 */
function getMentors() {
  console.log('entering get mentors function/n');
  let url = new URL('/mentors', location.protocol + '//' + location.hostname);

  fetch(url).then(errorHandling).then((response) => response.json())
      .then((mentors) => {
    const mentorsContainer = document.getElementById('mentors-container');
    mentorsContainer.innerHTML = '';
    for (const mentor of mentors) {
      console.log(mentor);
      mentorsContainer.appendChild(createMentorElement(mentor));
    }
  })
  .catch((error) => {
    console.log(error);
  });
}

/**
 * Basic error handling checks if fetch results are 'ok.'
 */
function errorHandling(response) {
  if (!response.ok) {
    throw Error(response.statusText);
  }
  return response;
}

//mentor.name,  mentor.description, mentor.gitHubID, mentor.interestTags, mentor.projectIDs
/**
 * Creates a mentor container element from input text.
 * @param {Object} mentor
 * @return {HTMLElement} mentorContainer
 */
function createMentorElement(mentor) {
  const mentorContainer = document.createElement('div');
  mentorContainer.className = 'p-1 col-lg-4';

  const mentorCard = document.createElement('div');
  mentorCard.className = 'card-holder card h-100 border-primary mb-3';

  const mentorCardBody = document.createElement('div');
  mentorCardBody.className = 'card-body pb-0';

  const mentorName = document.createElement('h5');
  mentorName.className = 'card-title text-primary';
  mentorName.innerHTML = mentor.name;
  mentorCardBody.appendChild(mentorName);

  mentorCardBody.appendChild(createGitHubLink(mentor.gitHubID));

  const mentorDescription = document.createElement('p');
  mentorDescription.innerHTML = mentor.description;
  mentorCardBody.appendChild(mentorDescription);

  mentorCardBody.appendChild(createTagRow(mentor.interestTags));

  mentorCardBody.appendChild( createProjectsRow(mentor.projectIDs));

  mentorCard.appendChild(mentorCardBody);
  mentorContainer.appendChild(mentorCard);

  return mentorContainer;
}

/**
 * Creates a github profile button element from username.
 * @param {String} gitHubID
 * @return {HTMLElement} userGithubButton
 */
function createGitHubLink(gitHubID) {
  let gitHubBaseUrl = 'https://github.com/';
  const gitLink = gitHubBaseUrl.concat(gitHubID);
  let userGithubButton = document.createElement('a');
  userGithubButton.innerText = 'GitHub Profile';
  userGithubButton.className = 'btn btn-primary';
  userGithubButton.role = 'button';
  userGithubButton.href = gitLink;
  return userGithubButton;
}

/**
 * Creates a block of interest tags.
 * @param {String[]} interestTags
 * @return {HTMLElement} tagDiv
 */
function createTagRow(interestTags) {
  let tagDiv = document.createElement('div');
  tagDiv.className = 'row p-3';
  for (const tagText of interestTags) {
    let tagElement = document.createElement('div');
    tagElement.className = 'border border-muted text-muted mr-1 mb-1 badge';
    tagElement.innerText = tagText;
    tagDiv.append(tagElement);
  }
  return tagDiv;
}

/**
 * Creates a block of project cards.
 * @param {String[]} projectIDs
 * @return {HTMLElement} projectsDiv
 */
function createProjectsRow(projectIDs) {
  let projectsDiv = document.createElement('div');
  projectsDiv.className = 'row p-3';
  for (const projectID of projectIDs) {
    let projectElement = document.createElement('div');
    projectElement.className = 'card container card-holder col-12' +
        ' text-center project-card p-3 m-3';
    let cardTitleElement = document.createElement('h4');
    cardTitleElement.className = 'card-title dark-emph';
    cardTitleElement.innerText = projectID;
    projectElement.appendChild(cardTitleElement);
    projectsDiv.append(projectElement);
  }
  return projectsDiv;
}
