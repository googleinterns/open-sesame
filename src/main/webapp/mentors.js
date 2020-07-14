import standardizeFetchErrors from '/js/fetch_handler.js';

if (window.location.href.includes('mentors.html')) {
  getMentors();
}
if (window.location.href.includes('mentor_form.html')) {
  console.log('mentor form page!');
  document.getElementById('mentor-form').addEventListener('submit', submitForm);
}


/**
 * Get mentors from the mentor servlet.
 * @param {String} response
 */
function getMentors() { // eslint-disable-line no-unused-vars
  console.log('entering get mentors function/n');
  const url = new URL('/mentors', window.location.origin);
  const fetchRequest = standardizeFetchErrors(
      fetch(url),
      'Failed to communicate with the server, please try again later.',
      'Encountered a server error, please try again later.');

  fetchRequest.then((mentors) => {
    console.log('got mentor data');
    const mentorsContainer = document.getElementById('mentors-container');
    mentorsContainer.innerHTML = '';
    for (const mentor of mentors) {
      console.log(mentor);
      mentorsContainer.appendChild(createMentorElement(mentor));
    }
  })
      .catch((errorResponse) => {
        alert(errorResponse.userMessage);
        console.error(
            `Error ${errorResponse.statusCode}: ${errorResponse.error}`);
      });
}

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

  mentorCardBody.appendChild(createTagDiv(mentor.interestTags));

  mentorCardBody.appendChild( createProjectsDiv(mentor.projectIDs));

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
  const gitHubBaseUrl = 'https://github.com/';
  const gitLink = gitHubBaseUrl.concat(gitHubID);
  const userGithubButton = document.createElement('a');
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
function createTagDiv(interestTags) {
  const tagDiv = document.createElement('div');
  tagDiv.className = 'row p-3';
  for (const tagText of interestTags) {
    const tagElement = document.createElement('div');
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
function createProjectsDiv(projectIDs) {
  const projectsDiv = document.createElement('div');
  projectsDiv.className = 'row p-3';
  for (const projectID of projectIDs) {
    const projectElement = document.createElement('div');
    projectElement.className = 'card container card-holder col-12' +
        ' text-center project-card p-3 m-3';
    const cardTitleElement = document.createElement('h4');
    cardTitleElement.className = 'card-title dark-emph';
    cardTitleElement.innerText = projectID;
    projectElement.appendChild(cardTitleElement);
    projectsDiv.append(projectElement);
  }
  return projectsDiv;
}

function submitForm(e) {
  e.preventDefault();
  const inputUrl = document.getElementById('inputRepo').value;
  const encodedBody = new URLSearchParams();
  encodedBody.append('inputRepo', inputUrl);
  const url = new URL('/mentors', window.location.origin);
  const fetchRequest = fetch((url), {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: encodedBody,
  });

  standardizeFetchErrors(fetchRequest, 'Fetch failed message here', 'Server error message here').then((response) => {
    console.log('valid url');
    window.location.href = '/dashboard.html';
  }).catch((errorResponse) => {
    if (errorResponse.statusCode == 400) {
      const errorContainer = document.getElementById('error-message-container');
      errorContainer.innerText = errorResponse.message;
    } else {
      console.error(
          `Error ${errorResponse.statusCode}: ${errorResponse.message}`);
      alert(errorResponse.userMessage);
    }
  });
}
