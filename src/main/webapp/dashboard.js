import { getUser } from './user.js';
import {
  standardizeFetchErrors,
  makeRelativeUrlAbsolute,
} from './js/fetch_handler.js';

/**
 * A User.
 * This datatype is a JS interpretation of the UserData Java Object returned
 * from the java servlet. For more information, please look at the
 * {@link ../java/com/google/opensesame/user/UserData UserData file}
 * @typedef {Object} User
 * @property {string[]} interestTags - The user's tags
 * @property {string} bio - The bio of the user
 * @property {string} email - the email address of the user
 * @property {string} gitHubURL: - The user's github page
 * @property {string} image - The User's profile picture
 * @property {string} location - the location of the user
 * @property {string} name - the name of the user
 * @property {boolean} isMentor - true if the user is a mentor for a project
 *                                false otherwise.
 * @property {string[]} projectIds - A List of Projects a user is working on.
 */

/**
 * A Project from datastore.
 * This datatype is a JS interpretation of the UserData Java Object returned
 * from the java servlet. For more information, please look at the
 * [ProjectData file]{@link ../java/com/google/opensesame/projects/ProjectData}
 * @typedef {Object} Project
 * @property {ProjectPreview} projectPreview - The data used to showcase a
 *                                             project on the projects page
 */

/**
 * Data contained in a project from Datastore.
 * This datatype is a JS interpretation of the UserData Java Object returned
 * from the java servlet. For more information, please look at the
 * [ProjectPreviewData file]
 * {@link ../java/com/google/opensesame/projects/ProjectPreviewData}
 * @typedef {Object} ProjectPreview
 * @property {string} name - The name of the project.
 */

// ELEMENTS_FOR_ABOUT_ME_SECTION
/**
 * Elements from dashboard.html.
 * @type {HTMLElement}
 */
const aboutMeCardDiv = document.getElementById('about-me-card-body');
const userBioElement = document.getElementById('user-bio');
const userImageElement = document.getElementById('user-image');
const userGithubButton = document.getElementById('user-github');
const userEmailButton = document.getElementById('user-email');

/**
 * Populate the card element 'aboutMeCardDiv' with information about a
 * given user.
 * @param {User} user
 */
function createAboutMe(user) {
  const editButton = createEditButton('#');
  aboutMeCardDiv.prepend(editButton);

  userImageElement.src = user.image;

  const userNameAndLocationElement =
    document.getElementById('user-name-location');
  userNameAndLocationElement.innerHTML = user.name + '<br>';
  if (user.location) {
    const userLocation = createLocation(user.location);
    userNameAndLocationElement.append(userLocation);
  }

  userGithubButton.href = user.gitHubURL;
  if (!user.email) {
    userEmailButton.style.display = 'none';
  }
  userEmailButton.href = 'mailto:' + user.email;
  mailtouiApp.run()

  if (user.bio) {
    userBioElement.innerText = user.bio;
  }

  if (user.interestTags) {
    aboutMeCardDiv.append(createCardTitle('Interest Tags'));
    const interestTagRow = createRowElement();
    for (const tag of user.interestTags) {
      addTag(tag, interestTagRow);
    }
    aboutMeCardDiv.append(interestTagRow);
  }

  if (user.projectIds) {
    aboutMeCardDiv.append(createCardTitle('Projects'));
    const projectTagRow = createRowElement();
    for (const projectID of user.projectIds) {
      addProject(projectID, projectTagRow);
    }
    aboutMeCardDiv.append(projectTagRow);
  }
}

/**
 * Return an edit button element that links to the action @param link.
 * @param {string} link the href link to the action the edit button will perform
 * @return {HTMLElement} edit button
 */
function createEditButton(link) {
  const editButton = document.createElement('a');
  editButton.className = 'float-right bold';
  editButton.innerText = 'Edit';
  editButton.href = link;
  return editButton;
}

/**
 * Append a tag with @param tagText to @param tagDiv
 * @param {string} tagText the text in the tag element.
 * @param {HTMLElement} tagDiv the div the tag is to be added to.
 */
function addTag(tagText, tagDiv) {
  const tagElement = document.createElement('div');
  tagElement.className = 'border border-muted text-muted mr-1 mb-1 badge';
  tagElement.innerText = tagText;
  tagDiv.append(tagElement);
}

/**
 * Append a tag representing a project with the Id @param projectID and the
 * name @param projectName to the div @param projectDiv
 * @param {string} projectName the name of the project in question.
 * @param {string} projectID the id used to distinguish projects in the
 * database.
 * @param {HTMLElement} projectDiv the div the project tag is to be added to.
 */
function addProjectTag(projectName, projectID, projectDiv) {
  const projectTagElement = document.createElement('a');
  projectTagElement.className = 'border border-muted text-muted mr-1 mb-1' +
    ' project-tag badge';
  projectTagElement.innerText = projectName;
  projectTagElement.href =
    new URL('/projects.html#/' + projectID, window.location.origin);
  projectDiv.append(projectTagElement);
}

/**
 * Creates a small element with a given @param location
 * @param {string} location text for a given location.
 * @return {HTMLElement} location small element
 */
function createLocation(location) {
  const userLocation = document.createElement('small');
  userLocation.innerText = location;
  return userLocation;
}

/**
 * Get the project stored in Datastore with the Id @param projectID and append a
 * tag representing the project to the div @param projectTagRow. The appended
 * tag is linked to the project's entry on the OpenSesame project page.
 * @param {string} projectID - Datastore Id of a given project.
 * @param {HTMLElement} projectTagDiv - div element to append the tag to.
 */
async function addProject(projectID, projectTagDiv) {
  const projectData = await getProject(projectID);
  addProjectTag(projectData.previewData.name, projectID, projectTagDiv);
}

/**
 * Return a bootstrap card-title div element with @param titleText
 * @param {string} titleText
 * @return {HTMLElement} div representing a card title with @param titleText
 */
function createCardTitle(titleText) {
  const cardTitleElement = document.createElement('h5');
  cardTitleElement.className = 'dark-emph row mt-1 mb-0';
  cardTitleElement.innerText = titleText;
  return cardTitleElement;
}

/**
 * Return a bootstrap row element
 * @return {HTMLElement} bootstrap row div
 */
function createRowElement() {
  const rowElement = document.createElement('div');
  rowElement.className = 'row p-2';
  return rowElement;
}

/**
 * Get the project with the Id @param projectID from Datastore using the
 * /project servlet.
 * @param {string} projectID the id used to store a project in datastore.
 * @return {Project} Data about the project with th id @param projectID
 */
function getProject(projectID) {
  const fetchURL = '/project?projectId=' + projectID;
  const fetchRequest = fetch(makeRelativeUrlAbsolute(fetchURL));

  return standardizeFetchErrors(
      fetchRequest,
      'Failed to communicate with the server. Please try again later.',
      'An error occcured while retrieving this project.' +
      ' Please try again later.')
    .then((response) => response.json());
}

/**
 * Call functions to populate page sections with data.
 */
async function setUpPage() {
  const user = await getUser();
  // TODO: redirect to sign up/in page if the user is not found.
  createAboutMe(user);
}

window.onload = setUpPage;
