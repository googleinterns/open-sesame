import {gitHubAuthorizer} from './authorization.js';

function initSignupForm() {
  const signupForm = document.getElementById('signup-form');
  signupForm.addEventListener('submit', submitSignup);

  signupForm.elements['github-link-button'].addEventListener(
      'click', (e) => {
    e.preventDefault();
    gitHubAuthorizer.toggleAuthorization()
  });
  gitHubAuthorizer
      .getFirebase().auth().onAuthStateChanged(handleAuthStateChanged);
  // Handle auth state changed on page load.
  handleAuthStateChanged(gitHubAuthorizer.getUser());
}

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

function submitSignup(e) {
  e.preventDefault();
}

initSignupForm();