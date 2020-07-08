importScripts("https://www.gstatic.com/firebasejs/7.15.5/firebase-auth.js")

// GCP Generated Github OAuth configuration
const config = {
  apiKey: 'AIzaSyAR88Giah8cCEAvT_zDSIREWvgIIAeS8yY',
  authDomain: 'step2020-279820.firebaseapp.com',
};

/**
 * Initialize firebase if firebase is not already initialized. This method can
 * be called as many times as we need without any side effects.
 */
function initializeFirebase() {
  firebase = getFirebase();
  if (firebase.apps.length === 0) {
    firebase.initializeApp(config);
  }
}

/**
 * Get firebase
 * @return {Firebase}
 */
function getFirebase() {
  return firebase; //eslint-disable-line
}


/**
 * Function called when clicking the Login/Logout button.
 */
function toggleSignIn() {
  firebase = getFirebase();
  if (!firebase.auth().currentUser) {
    let provider = new firebase.auth.GithubAuthProvider();
    firebase.auth().signInWithPopup(provider).then(function(result) {
      // This gives you a GitHub Access Token.
      // You can use it to access the GitHub API.
      const token = result.credential.accessToken;
      // The signed-in user info.
      const user = result.user;
    }).catch(function(error) {
      // Handle Errors here.
      console.error(error);
      // Change this in future iterations
      alert(error.message);
    });
  } else {
    // if logged in, sign out
    firebase.auth().signOut();
  }
}

export {
  initializeFirebase,
  toggleSignIn,
  getFirebase,
};
