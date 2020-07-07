/**
 * The ID of the login button
 * @type {String}
 */
const LOGIN_BTN = 'login-btn';

// GCP Generated Github 'OAuth' configuration
let config = {
  apiKey: 'AIzaSyAR88Giah8cCEAvT_zDSIREWvgIIAeS8yY',
  authDomain: 'step2020-279820.firebaseapp.com',
};
firebase.initializeApp(config);

/**
 * Function called when clicking the Login/Logout button.
 */
function toggleSignIn() {
  // if not logged in
  if (!firebase.auth().currentUser) {
    let provider = new firebase.auth.GithubAuthProvider();
    // sign in
    firebase.auth().signInWithPopup(provider).then(function(result) {
      // This gives you a GitHub Access Token. 
      // You can use it to access the GitHub API.
      let token = result.credential.accessToken;
      // The signed-in user info.
      let user = result.user;
    }).catch(function(error) {
      // Handle Errors here.
      console.error(error);
    });
  }
  // if logged in 
  else {
    // sign out
    firebase.auth().signOut();
  }
}

document.getElementById('login-btn')
  .addEventListener('click', toggleSignIn, false);
