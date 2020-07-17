import {standardizeFetchErrors} from '/js/fetch_handler.js';

getMockMentors();
initForm();

function initForm() {
  const mentorForm = document.getElementById('mentor-form');
  if (mentorForm) {
    mentorForm.addEventListener('submit', submitForm);
  }
  return;
}

/**
 * A Mentor
 * @typedef {Object} Mentor
 * @property {string List} interestTags - The user's tags
 * @property {string List} projectIDs - The user's projects
 * @property {string} description - The bio of the user
 * @property {string} email - the email address of the user
 * @property {string} gitHubID: - The user's github page
 * @property {string} image - The User's profile picture
 * @property {string} location - the location of the user
 * @property {string} name - the name of the user
 */

function getMockMentors() {
  // let mentors = new Array();
  const mentors = [
    {
      interestTags: ['code', 'trapeze', 'baking'],
      projectIDs: ['opensesame'],
      description: 'Sami works on opensesame',
      email: 'samialves@google.com',
      image: 'images/dior.jpg',
      gitHubID: 'Sami-2000',
      name: 'Sami Alves',
      location: 'Mansfield, MA',
    },
    {
      interestTags: null,
      projectIDs: null,
      description: null,
      email: 'obiabbi@google.com',
      image: 'images/dior.jpg',
      gitHubID: 'Obinnabii',
      name: 'Obi Abii',
      location: 'Idk maybe near Cornell',
    },
  ];
  // mentors.push(Sami);
  const mentorsContainer = document.getElementById('mentors-container');
  if (!mentorsContainer) {
    return;
  }
  mentorsContainer.innerHTML = '';
  console.log(mentors);
  for (const mentor of mentors) {
    console.log(mentor);
    mentorsContainer.appendChild(createMentorElement(mentor));
  }
}

/**
 * Get mentors from the mentor servlet.
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
      'Encountered a server error, please try again later.');

  fetchRequest.then((mentors) => {
    console.log('got mentor data');
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
  mentorCardBody.className = 'card-body pb-0 text-center d-flex flex-column';

  addImg(mentor.image, mentorCardBody);
  addName(mentor.name, mentorCardBody);
  addLocation(mentor.location, mentorCardBody);
  addEmail(mentor.email, mentorCardBody);
  addBio(mentor.description, mentorCardBody);
  createGitHubLink(mentor.gitHubID, mentorCardBody);
  createTagDiv(mentor.interestTags, mentorCardBody);
  createProjectsDiv(mentor.projectIDs, mentorCardBody);

  mentorCard.appendChild(mentorCardBody);
  mentorContainer.appendChild(mentorCard);

  return mentorContainer;
}

/**
 * Creates an email for mentor card.
 * @param {String} email
 * @param {HTMLElement} mentorCardBody
 * @return {HTMLElement} mentorCardBody
 */
function addEmail(email, mentorCardBody) {
  if (email == null) {
    return mentorCardBody;
  }
  const mentorEmail = document.createElement('a');
  mentorEmail.href = 'mailto: ' + email;
  mentorEmail.innerText = 'Send Email Introduction';
  mentorCardBody.appendChild(mentorEmail);
  return mentorCardBody;
}

/**
 * Creates an image for mentor card.
 * @param {String} location
 * @param {HTMLElement} mentorCardBody
 * @return {HTMLElement} mentorCardBody
 */
function addLocation(location, mentorCardBody) {
  if (location == null) {
    return mentorCardBody;
  }
  const mentorLocation = document.createElement('h6');
  mentorLocation.className = 'card-subtitle text-muted text-center mb-1';
  mentorLocation.innerText = location;
  mentorCardBody.appendChild(mentorLocation);
  return mentorCardBody;
}

/**
 * Creates an image for mentor card.
 * @param {String} img
 * @param {HTMLElement} mentorCardBody
 * @return {HTMLElement} mentorCardBody
 */
function addImg(img, mentorCardBody) {
  if (img == null) {
    return mentorCardBody;
  }
  const mentorImg = document.createElement('img');
  mentorImg.className = 'mentor-picture';
  mentorImg.src = img;
  mentorCardBody.appendChild(mentorImg);
  return mentorCardBody;
}

/**
 * Creates a description for mentor card.
 * @param {String} bio
 * @param {HTMLElement} mentorCardBody
 * @return {HTMLElement} mentorCardBody
 */
function addBio(bio, mentorCardBody) {
  if (bio == null) {
    return mentorCardBody;
  }
  const mentorDescription = document.createElement('p');
  mentorDescription.innerHTML = bio;
  mentorCardBody.appendChild(mentorDescription);
  return mentorCardBody;
}

/**
 * Creates a name tag for mentor card.
 * @param {String} name
 * @param {HTMLElement} mentorCardBody
 * @return {HTMLElement} mentorCardBody
 */
function addName(name, mentorCardBody) {
  if (name == null) {
    return mentorCardBody;
  }
  const mentorName = document.createElement('h5');
  mentorName.className = 'card-title text-primary';
  mentorName.innerHTML = name;
  mentorCardBody.appendChild(mentorName);
  return mentorCardBody;
}

/**
 * Creates a github profile button element from username.
 * @param {String} gitHubID
 * @param {HTMLElement} mentorCardBody
 * @return {HTMLElement} mentorCardBody
 */
function createGitHubLink(gitHubID, mentorCardBody) {
  if (gitHubID == null) {
    return mentorCardBody;
  }
  const gitHubBaseUrl = 'https://github.com/';
  const gitLink = gitHubBaseUrl.concat(gitHubID);
  const userGithubButton = document.createElement('a');
  userGithubButton.innerText = 'GitHub Profile';
  userGithubButton.className = 'btn btn-primary';
  userGithubButton.role = 'button';
  userGithubButton.href = gitLink;
  mentorCardBody.appendChild(userGithubButton);
  return mentorCardBody;
}

/**
 * Creates a block of interest tags.
 * @param {String[]} interestTags
 * @param {HTMLElement} mentorCardBody
 * @return {HTMLElement} mentorCardBody
 */
function createTagDiv(interestTags, mentorCardBody) {
  if (interestTags == null) {
    return mentorCardBody;
  }
  const tagDiv = document.createElement('div');
  tagDiv.className = 'd-flex justify-content-center p-3';
  for (const tagText of interestTags) {
    const tagElement = document.createElement('div');
    tagElement.className = 
        'border border-muted text-muted mr-1 mb-1 badge text-center';
    tagElement.innerText = tagText;
    tagDiv.append(tagElement);
  }
  mentorCardBody.appendChild(tagDiv);
  return mentorCardBody;
}

/**
 * Creates a block of project cards.
 * @param {String[]} projectIDs
 * @param {HTMLElement} mentorCardBody
 * @return {HTMLElement} mentorCardBody
 */
function createProjectsDiv(projectIDs, mentorCardBody) {
  if (projectIDs == null) {
    return mentorCardBody;
  }
  const projectsDiv = document.createElement('div');
  projectsDiv.className = 'd-flex justify-content-center p-3';
  for (const projectID of projectIDs) {
    const projectElement = document.createElement('div');
    projectElement.className = 
        'card-holder border border-muted text-muted mr-1 mb-1 badge text-center';
    const cardTitleElement = document.createElement('h4');
    cardTitleElement.className = 'card-title dark-emph';
    cardTitleElement.innerText = projectID;
    projectElement.appendChild(cardTitleElement);
    projectsDiv.append(projectElement);
  }
  mentorCardBody.appendChild(projectsDiv);
  return mentorCardBody;
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
