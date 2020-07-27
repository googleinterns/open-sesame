import {standardizeFetchErrors} from '/js/fetch_handler.js';

getMentors();
initForm();

/**
 * Initializes the mentor form and adds a listener for it.
 */
function initForm() {
  const mentorForm = document.getElementById('mentor-form');
  if (mentorForm) {
    mentorForm.addEventListener('submit', submitForm);
  }
  return;
}

/**
 * User Object
 * @typedef {Object} User
 * @property {string[]} interestTags - The user's tags
 * @property {string[]} projectIDs - The user's projects
 * @property {string} bio - The bio of the user
 * @property {string} email - the email address of the user
 * @property {string} gitHubID: - The user's github page
 * @property {string} image - The User's profile picture
 * @property {string} location - the location of the user
 * @property {string} name - the name of the user
 */

/**
 * Populate mentor search page with mentors from the mentor servlet.
 * @param {String} response
 */
function getMentors() {
  const mentorsContainer = document.getElementById('mentors-container');
  if (!mentorsContainer) {
    return;
  }
  console.log('entering get mentors function/n');
  const url = new URL('/mentors', window.location.origin);
  const fetchRequest = standardizeFetchErrors(
      fetch(url),
      'Failed to communicate with the server, please try again later.',
      'Encountered a server error, please try again later.',
  );

  fetchRequest.then((response) => response.json()).then((mentors) => {
    mentorsContainer.innerHTML = '';
    for (const mentor of mentors) {
      console.log(mentor);
      mentorsContainer.appendChild(createMentorElement(mentor));
    }
    mailtouiApp.run();
  })
      .catch((errorResponse) => {
        console.error(
            `Error ${errorResponse.statusCode}: ${errorResponse.message}`);
        alert(errorResponse.userMessage);
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
  mentorCardBody.className = 'card-body pb-0 text-center d-flex flex-column';

  if (mentor.image != null) {
    mentorCardBody.appendChild(createImg(mentor.image));
  }
  if (mentor.name != null) {
    mentorCardBody.appendChild(createNameHeader(mentor.name));
  }
  if (mentor.location != null) {
    mentorCardBody.appendChild(createLocationHeader(mentor.location));
  }
  if (mentor.email != null) {
    mentorCardBody.appendChild(createEmailLink(mentor.email));
  }
  if (mentor.bio != null) {
    mentorCardBody.appendChild(createBioParagraph(mentor.bio));
  }
  mentorCardBody.appendChild(createGitHubLink(mentor.gitHubID));
  if (mentor.interestTags != null) {
    mentorCardBody.appendChild(createInterestTagsDiv(mentor.interestTags));
  }
  if (mentor.projectIds != null) {
    mentorCardBody.appendChild(createProjectLinks(mentor.projects));
  }

  mentorCard.appendChild(mentorCardBody);
  mentorContainer.appendChild(mentorCard);

  return mentorContainer;
}

/**
 * Creates an email for a mentor card.
 * @param {String} email
 * @return {HTMLElement} mentorEmail
 */
function createEmailLink(email) {
  const mentorEmail = document.createElement('a');
  mentorEmail.href = 'mailto: ' + email;
  mentorEmail.className = 'mailtoui';
  mentorEmail.innerText = 'Send Email Introduction';
  return mentorEmail;
}

/**
 * Creates a location for a mentor card.
 * @param {String} location
 * @return {HTMLElement} mentorLocation
 */
function createLocationHeader(location) {
  const mentorLocation = document.createElement('h6');
  mentorLocation.className = 'card-subtitle text-muted text-center mb-1';
  mentorLocation.innerText = location;
  return mentorLocation;
}

/**
 * Creates an image for a mentor card.
 * @param {String} img
 * @return {HTMLElement} mentorImg
 */
function createImg(img) {
  const mentorImg = document.createElement('img');
  mentorImg.className = 'mentor-picture';
  mentorImg.src = img;
  return mentorImg;
}

/**
 * Creates a bio for a mentor card.
 * @param {String} bio
 * @return {HTMLElement} mentorBio
 */
function createBioParagraph(bio) {
  const mentorBio = document.createElement('p');
  mentorBio.innerHTML = bio;
  return mentorBio;
}

/**
 * Creates a name tag for a mentor card.
 * @param {String} name
 * @return {HTMLElement} mentorName
 */
function createNameHeader(name) {
  const mentorName = document.createElement('h5');
  mentorName.className = 'card-title text-primary';
  mentorName.innerHTML = name;
  return mentorName;
}

/**
 * Creates a github profile button element from gitHub ID.
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
function createInterestTagsDiv(interestTags) {
  const tagDiv = document.createElement('div');
  tagDiv.className = 'd-flex justify-content-center p-3';
  for (const tagText of interestTags) {
    const tagElement = document.createElement('div');
    tagElement.className =
        'border border-muted text-muted mr-1 mb-1 badge text-center';
    tagElement.innerText = tagText;
    tagDiv.append(tagElement);
  }
  return tagDiv;
}

/**
 * Creates a block of project tags.
 * @param {ProjectData[]} projects
 * @return {HTMLElement} projectsDiv
 */
function createProjectLinks(projects) {
  const projectsDiv = document.createElement('div');
  projectsDiv.className = 'd-flex justify-content-center p-3';
  for (const project of projects) {
    const projectElement = document.createElement('a');
    projectElement.href =
          new URL('/projects.html#/' +
               project.repositoryId, window.location.origin);
    projectElement.className = 'card-holder border border-muted' +
          ' text-muted mr-1 mb-1 badge text-center';
    const cardTitleElement = document.createElement('h4');
    cardTitleElement.className = 'card-title dark-emph';
    cardTitleElement.innerText = project.name;
    projectElement.appendChild(cardTitleElement);
    projectsDiv.append(projectElement);
  }
  return projectsDiv;
}

/**
 * Submit the mentor form with Post request.
 * @param {InputEvent} e
 */
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

  standardizeFetchErrors(fetchRequest, 'Fetch failed message here',
      'Server error message here').then((response) => {
    console.log('valid url');
    window.location.href = '/dashboard.html';
  }).catch((errorResponse) => {
    if (errorResponse.statusCode === 400) {
      const errorContainer = document.getElementById('error-message-container');
      errorContainer.innerText = errorResponse.message;
    } else {
      console.error(
          `Error ${errorResponse.statusCode}: ${errorResponse.message}`);
      alert(errorResponse.userMessage);
    }
  });
}
