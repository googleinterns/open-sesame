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
 * Populate mentor search page with mock mentors.
 */
function getMockMentors() { // eslint-disable-line no-unused-vars
  const mentors = [
    {
      interestTags: ['code', 'trapeze', 'baking'],
      projectIDs: ['opensesame'],
      bio: 'Sami works on opensesame',
      email: 'samialves@google.com',
      image: 'images/dior.jpg',
      gitHubID: 'Sami-2000',
      name: 'Sami Alves',
      location: 'Mansfield, MA',
    },
    {
      interestTags: null,
      projectIDs: null,
      bio: null,
      email: 'obiabbi@google.com',
      image: 'images/dior.jpg',
      gitHubID: 'Obinnabii',
      name: 'Obi Abii',
      location: 'Idk maybe near Cornell',
    },
  ];
  const mentorsContainer = document.getElementById('mentors-container');
  if (!mentorsContainer) {
    return;
  }
  mentorsContainer.innerHTML = '';
  for (const mentor of mentors) {
    console.log(mentor);
    mentorsContainer.appendChild(createMentorElement(mentor));
  }
}

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
    mentorCardBody.appendChild(createName(mentor.name));
  }
  if (mentor.location != null) {
    mentorCardBody.appendChild(createLocation(mentor.location));
  }
  if (mentor.email != null) {
    mentorCardBody.appendChild(createEmail(mentor.email));
  }
  if (mentor.bio != null) {
    mentorCardBody.appendChild(createBio(mentor.bio));
  }
  mentorCardBody.appendChild(createGitHubLink(mentor.gitHubID));
  if (mentor.interestTags != null) {
    mentorCardBody.appendChild(createTagDiv(mentor.interestTags));
  }
  if (mentor.projectIds != null) {
    mentorCardBody.appendChild(createProjectsDiv(mentor.projectIds));
  }

  mentorCard.appendChild(mentorCardBody);
  mentorContainer.appendChild(mentorCard);

  return mentorContainer;
}

/**
 * Creates an email for mentor card.
 * @param {String} email
 * @return {HTMLElement} mentorEmail
 */
function createEmail(email) {
  const mentorEmail = document.createElement('a');
  mentorEmail.href = 'mailto: ' + email;
  mentorEmail.innerText = 'Send Email Introduction';
  return mentorEmail;
}

/**
 * Creates a location for mentor card.
 * @param {String} location
 * @return {HTMLElement} mentorLocation
 */
function createLocation(location) {
  const mentorLocation = document.createElement('h6');
  mentorLocation.className = 'card-subtitle text-muted text-center mb-1';
  mentorLocation.innerText = location;
  return mentorLocation;
}

/**
 * Creates an image for mentor card.
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
 * Creates a bio for mentor card.
 * @param {String} bio
 * @return {HTMLElement} mentorBio
 */
function createBio(bio) {
  const mentorBio = document.createElement('p');
  mentorBio.innerHTML = bio;
  return mentorBio;
}

/**
 * Creates a name tag for mentor card.
 * @param {String} name
 * @return {HTMLElement} mentorName
 */
function createName(name) {
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
function createTagDiv(interestTags) {
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
 * @param {String[]} projectIDs
 * @return {HTMLElement} projectsDiv
 */
function createProjectsDiv(projectIDs) {
  const projectsDiv = document.createElement('div');
  projectsDiv.className = 'd-flex justify-content-center p-3';
  for (const projectID of projectIDs) {
    const projectUrl = new URL('/project', window.location.origin);
    projectUrl.searchParams.append('projectId', Number(projectID));
    const fetchRequest = fetch(projectUrl, {
      method: 'get',
    });
    standardizeFetchErrors(
        fetchRequest,
        'Failed to communicate with the server, please try again later.',
        'Encountered a server error, please try again later.')
        .then((response) => response.json()).then((data) => {
          console.log('got project data');
          console.log(data);
          const projectElement = document.createElement('a');
          projectElement.href =
              new URL('/projects.html#/' + projectID, window.location.origin);
          projectElement.className = 'card-holder border border-muted' +
              ' text-muted mr-1 mb-1 badge text-center';
          const cardTitleElement = document.createElement('h4');
          cardTitleElement.className = 'card-title dark-emph';
          cardTitleElement.innerText = data.previewData.name;
          projectElement.appendChild(cardTitleElement);
          projectsDiv.append(projectElement);
        })
        .catch((errorResponse) => {
          console.error(
              `Error ${errorResponse.statusCode}: ${errorResponse.message}`);
          alert(errorResponse.userMessage);
        });
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
