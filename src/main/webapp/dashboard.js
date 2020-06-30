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

// ELEMENTS_FOR_ABOUT_ME_SECTION
/**
 * Elements from dashboard.html.
 * @type {HTMLElement}
 */
const ABOUT_ME_CARD_DIV = document.getElementById('about-me-card-body');
const USER_BIO = document.getElementById('user-bio');
const USER_IMAGE = document.getElementById('user-image');
const USER_NAME_AND_LOCATION = document.getElementById('user-name-location');
const USER_TAG_ROW = document.getElementById('user-tag-row');
const USER_GITHUB_BUTTON = document.getElementById('user-github');

/**
 * Fake information for the hardcoded stage.
 */
const dummyName = 'Dior';
const dummyLocation = 'Sunnyvale CA.';
const dummyBio = 'The cutest pug in The tech industry. Lorem ipsum dolor sit ' +
  'amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut' +
  ' labore et dolore magna aliqua.';
const dummyImg = 'images/dior.jpg';
const dummyTags = ['Weekly', '30 mins', 'Kubernetes', 'Open Sesame',
  'Documentation',
];
const dummyGitSrc = '#';

/**@const {User} */
const dummyUser = {
  name: dummyName,
  location: dummyLocation,
  image: dummyImg,
  bio: dummyBio,
  tags: dummyTags
};

/**
 * Populate the card element 'ABOUT_ME_CARD_DIV' with information about a
 * given user.
 * @param {User} user
 */
function createAboutMe(user) {
  let editButton = createEditButton('#');
  ABOUT_ME_CARD_DIV.prepend(editButton);

  USER_IMAGE.src = user.image;

  USER_NAME_AND_LOCATION.innerHTML = user.name + '<br>';
  let userLocation = createLocation(user.location);
  USER_NAME_AND_LOCATION.append(userLocation);

  USER_BIO.innerText = user.bio;

  USER_GITHUB_BUTTON.href = user.gitSrc;

  for (tag of user.tags) {
    addTag(tag, USER_TAG_ROW);
  }
}

/**
 * Creates an edit button element that links to the action @param link.
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
 * Adds a tag with @param tagText to @param tagDiv
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
 * Call functions to populate page sections with data.
 */
function setUpPage() {
  createAboutMe(dummyUser);
}

window.onload = setUpPage;
