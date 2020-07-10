/**
 * A project
 * @typedef {Object} Project
 * @property {String} name - the name of a given project.
 * @property {Mentee List} mentees - the list of mentees under a given project.
 */

/**
 * A mentee
 * @typedef {Object} Mentee
 * @property {String} image -the link to a mentee's image
 * @property {String} name - the name of the mentee
 * @property {String} starLink - the link to the action that rewards a given
 *                               mentee with a star
 */

/**
 * A User
 * @typedef {Object} User
 * @property {String} bio - The bio of the user
 * @property {String} gitSrc - The user's github page
 * @property {String} image - The User's profile picture
 * @property {String} location - the location of the user
 * @property {String} name - the name of the user
 * @property {String List} tags - The user's tags
 */

/**
 * Fake information for the hardcoded stage.
 */
/** @const {String} */
const dummyName = 'Dior';
const dummyLocation = 'Sunnyvale CA.';
const dummyBio = 'The cutest pug in The tech industry. Lorem ipsum dolor sit ' +
  'amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut' +
  ' labore et dolore magna aliqua.';
const dummyImg = 'images/dior.jpg';
const dummyGitSrc = '#';
const dummyProjectName = 'Kubernetes';

/** @const {String List} */
const dummyTags = ['Weekly', '30 mins', 'Kubernetes', 'Open Sesame',
  'Documentation',
];

/** @const {User} */
const dummyUser = {
  name: dummyName,
  location: dummyLocation,
  image: dummyImg,
  bio: dummyBio,
  tags: dummyTags,
  gitSrc: dummyGitSrc,
};

/** @const {Mentee List} */
const dummyMentees = [{
    name: 'Richi',
    starLink: '#givestar',
    image: dummyImg,
  },
  {
    name: 'Obi',
    starLink: '#givestar',
    image: dummyImg,
  },
  {
    name: 'Sami',
    starLink: '#givestar',
    image: dummyImg,
  },
];

/** @const {Project} */
const dummyProject = {
  name: dummyProjectName,
  mentees: dummyMentees,
};

// ELEMENTS_FOR_ABOUT_ME_SECTION
/**
 * Elements from dashboard.html.
 * @type {HTMLElement}
 */
const aboutMeCardDiv = document.getElementById('about-me-card-body');
const userBioElement = document.getElementById('user-bio');
const userImageElement = document.getElementById('user-image');
const userNameAndLocationElement =
  document.getElementById('user-name-location');
const userTagRow = document.getElementById('user-tag-row');
const userGithubButton = document.getElementById('user-github');
const affiliationsDiv = document.getElementById('affiliations');

/**
 * Populate the card element 'aboutMeCardDiv' with information about a
 * given user.
 * @param {User} user
 */
function createAboutMe(user) {
  let editButton = createEditButton('#');
  aboutMeCardDiv.prepend(editButton);

  userImageElement.src = user.image;

  userNameAndLocationElement.innerHTML = user.name + '<br>';
  let userLocation = createLocation(user.location);
  userNameAndLocationElement.append(userLocation);

  userBioElement.innerText = user.bio;

  userGithubButton.href = user.gitSrc;

  for (let tag of user.tags) {
    addTag(tag, userTagRow);
  }
}

/**
 * Return an edit button element that links to the action @param link.
 * @param {String} link the href link to the action the edit button will perform
 * @return {HTMLElement} edit button
 */
function createEditButton(link) {
  let editButton = document.createElement('a');
  editButton.className = 'float-right bold';
  editButton.innerText = 'Edit';
  editButton.href = link;
  return editButton;
}

/**
 * Append a tag with @param tagText to @param tagDiv
 * @param {String} tagText the text in the tag element.
 * @param {HTMLElement} tagDiv the div the tag is to be added to.
 */
function addTag(tagText, tagDiv) {
  let tagElement = document.createElement('div');
  tagElement.className = 'border border-muted text-muted mr-1 mb-1 badge';
  tagElement.innerText = tagText;
  tagDiv.append(tagElement);
}

/**
 * Creates a small element with a given @param location
 * @param {String} location text for a given location.
 * @return {HTMLElement} location small element
 */
function createLocation(location) {
  let userLocation = document.createElement('small');
  userLocation.innerText = location;
  return userLocation;
}

/**
 * Populate the AFFILIATION_DIV with information about the commitments a mentor
 * has made to @param projectObject.
 * @param {Project} projectObject
 */
function addProject(projectObject) {
  let projectCardElement = createProjectCard(projectObject.name);
  affiliationsDiv.append(projectCardElement);
  let menteeRow = createRowElement();
  for (let mentee of projectObject.mentees) {
    menteeRow.append(createMenteeCard(mentee));
  }
  projectCardElement.append(menteeRow);
}

/**
 * Return a project card div element with the title @param projectName
 * @param {String} projectName
 * @return {HTMLElement} div representing a project card with the
 * title @param projectName
 */
function createProjectCard(projectName) {
  let projectCardElement = document.createElement('div');
  projectCardElement.className = 'card container card-holder col-12' +
    ' text-center project-card p-3 m-3';
  let projectTitleElement = createCardTitle(projectName);
  projectCardElement.append(projectTitleElement);
  return projectCardElement;
}

/**
 * Return a bootstrap card-title div element with @param titleText
 * @param {String} titleText
 * @return {HTMLElement} div representing a card title with @param titleText
 */
function createCardTitle(titleText) {
  let cardTitleElement = document.createElement('h4');
  cardTitleElement.className = 'card-title dark-emph';
  cardTitleElement.innerText = titleText;
  return cardTitleElement;
}

/**
 * Return a card with information about a given @param mentee object and
 * a link to send @param mentee a star          
 * 
 * 
 * 
 * @param {Mentee} mentee
 * @return {HTMLElement} card with information about a given mentee
 */
function createMenteeCard(mentee) {
  let menteeCardElement = document.createElement('div');
  menteeCardElement.className = 'card card-holder col-' +
    ' text-center m-1 p-2';
  let menteeCardBody = document.createElement('div');
  menteeCardBody.className = 'card-body';
  menteeCardElement.append(menteeCardBody);
  menteeCardBody.append(createMenteeCardImage(mentee.image));
  menteeCardBody.append(createCardTitle(mentee.name));
  menteeCardBody.append(createStarButton(mentee.starLink));
  return menteeCardElement;
}

/**
 * Return a bootstrap row element
 * @return {HTMLElement} bootstrap row div
 */
function createRowElement() {
  let rowElement = document.createElement('div');
  rowElement.className = 'row p-3';
  return rowElement;
}

/**
 * Return an anchor of the class star that perform the acton @param link
 * @param {String} link
 * @return {HTMLElement} anchor that performs the given action
 */
function createStarButton(link) {
  let starButton = document.createElement('a');
  starButton.className = 'star';
  starButton.innerText = 'Send star';
  starButton.href = link;
  return starButton;
}

/**
 * Return a small image with the source @param imgSrc
 * @param {String} imgSrc
 * @return {HTMLElement} image object
 */
function createMenteeCardImage(imgSrc) {
  let menteeCardImage = document.createElement('img');
  menteeCardImage.className = 'card-img-top mentee-card-img';
  menteeCardImage.src = imgSrc;
  return menteeCardImage;
}

/**
 * Call functions to populate page sections with data.
 */
function setUpPage() {
  createAboutMe(dummyUser);
  addProject(dummyProject);
  addProject(dummyProject);
}

window.onload = setUpPage;
