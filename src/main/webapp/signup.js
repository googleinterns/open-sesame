/**
 * @typedef SignUpData
 * @property {string} gitHubAuthToken
 * @property {string[]} interestTags
 */
import {gitHubAuthorizer} from './authorization.js';
import {
  standardizeFetchErrors,
  makeRelativeUrlAbsolute,
} from './js/fetch_handler.js';

const AFTER_SIGNUP_REDIRECT = '/dashboard.html';

/**
 * Initializes the sign up form.
 */
function initSignupForm() {
  const signupForm = document.getElementById('signup-form');
  signupForm.addEventListener('submit', submitSignup);

  signupForm.elements['github-link-button'].addEventListener(
      'click', handleGitHubLink);
  gitHubAuthorizer
      .getFirebase().auth().onAuthStateChanged(handleAuthStateChanged);
  // Handle auth state changed on page load in case of existing authentication.
  handleAuthStateChanged(gitHubAuthorizer.getUser());
}

/**
 * Handles the click event for the GitHub linking button.
 * @param {Event} e The button click event.
 */
function handleGitHubLink(e) {
  e.preventDefault();

  gitHubAuthorizer.toggleSignIn().catch((error) => {
    console.error(error);
    alert('Encountered an error with GitHub authentication.'
        + ' Please try again later.');
  });
}

/**
 * Updates the signup UI when the state of user GitHub authentication changes.
 * @param {Firebase.User} user
 */
function handleAuthStateChanged(user) {
  const signupForm = document.getElementById('signup-form');
  const submitButton = signupForm.elements['submit-button'];
  const gitHubLinkButton = signupForm.elements['github-link-button'];
  if (user) {
    submitButton.disabled = false;
    gitHubLinkButton.innerText = 'Unlink Your GitHub Account';
    gitHubLinkButton.classList.add('btn-success');
    gitHubLinkButton.classList.remove('btn-purple');
  } else {
    submitButton.disabled = true;
    gitHubLinkButton.innerText = 'Link Your GitHub Account';
    gitHubLinkButton.classList.add('btn-purple');
    gitHubLinkButton.classList.remove('btn-success');
  }
}

/**
 * Validates and submits a user signup request.
 * @param {Event} e The signup form submit event.
 */
function submitSignup(e) {
  e.preventDefault();

  const signupForm = document.getElementById('signup-form');
  const gitHubLinkButton = signupForm.elements['github-link-button'];
  const submitButton = signupForm.elements['submit-button'];
  gitHubLinkButton.disabled = true;
  submitButton.disabled = true;

  const signupBody = createSignupBody();
  console.log('Body of signup request:');
  console.log(signupBody);
  if (signupBody) {
    const signupRequest = createSignupRequest(signupBody);

    signupRequest.then((response) => {
      // If signup is successful, redirect.
      gitHubLinkButton.disabled = false;
      submitButton.disabled = false;
      window.location.href = AFTER_SIGNUP_REDIRECT;
    }).catch((errorResponse) => {
      console.error(
        `Error ${errorResponse.statusCode}: ${errorResponse.error}`);
      alert(errorResponse.userMessage);
      gitHubLinkButton.disabled = false;
      submitButton.disabled = false;
    });
  } else {
    gitHubLinkButton.disabled = false;
    submitButton.disabled = false;
  }
}

/**
 * Creates a signup post request.
 * This requires url-encoding the signup body and formatting the errors.
 * @param {SignUpData} signupBody
 * @return {Promise} Returns a prepared signup fetch request.
 */
function createSignupRequest(signupBody) {
  const encodedBody = new URLSearchParams();
  encodedBody.append('gitHubAuthToken', signupBody.gitHubAuthToken);
  // Appending multiple values to the same parameter is the standard protocol
  // for encoding an array of values:
  // https://stackoverflow.com/questions/38797509/passing-array-into-urlsearchparams-while-consuming-http-call-for-get-request
  signupBody.interestTags.forEach((interestTag) => {
    encodedBody.append('interestTags', interestTag);
  });

  const fetchRequest = fetch(makeRelativeUrlAbsolute('/user'), {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: encodedBody,
  });

  const errorFormattedFetchRequest = standardizeFetchErrors(
      fetchRequest,
      'Failed to communicate with the server. Please try again later.',
      'An error occcured while creating your account.'
          + ' Please try again later.');

  return errorFormattedFetchRequest;
}

/**
 * Creates the body of a signup request to be sent to the Open Sesame API.
 * @return {SignUpData} Returns the body of the signup request or null if the
 *    requirements have not been met to sign up.
 */
function createSignupBody() {
  if (!gitHubAuthorizer.getUser()) {
    // TODO : Add to a more elegant notification system
    alert('Please link your GitHub account before creating your profile.');
    return null;
  }

  let interestCheckBox = document.getElementById('check1');
  const interestTags = [];
  for (let i = 2; interestCheckBox; i++) {
    if (interestCheckBox.checked) {
      interestTags.push(interestCheckBox.value);
    }
    interestCheckBox = document.getElementById(`check${i}`);
  }

  /** @type {SignUpData} */
  return {
    gitHubAuthToken: gitHubAuthorizer.getToken(),
    interestTags,
  };
}

initSignupForm();
