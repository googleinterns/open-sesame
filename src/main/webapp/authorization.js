// GCP Generated Github OAuth configuration
const config = {
  apiKey: 'AIzaSyAR88Giah8cCEAvT_zDSIREWvgIIAeS8yY',
  authDomain: 'step2020-279820.firebaseapp.com',
};

/**
 * Object used for Github authorization and as a wrapper for firebase.
 * It handles Firebase initialization on instantiation and holds nifty
 * functions for handling github authorization
 */
class GithubAuthorizer {
  constructor() {
    initializeFirebase();
    this.token = "";
    this.firebase = firebase;
  }

  /**
   * The most recent access token for the Github API. To refresh this, the user
   * must be signed out and signed in again. It is important to keep in mind
   * that this token expires and must be used swiftly after calling
   * toggleSignIn(). If sign is yet to be toggled, this will return an empty
   * string
   */
  get token() {
    this.token;
  }

  /**
   * the current user if signed in and null if not. For all intents and
   * purposes, this function will serve as a true/false value in an if
   * statement.
   */
  get user() {
    firebase.auth().currentUser;;
  }

  /**
   * Initialize firebase if firebase is not already initialized. This method can
   * be called as many times as we need without any side effects.
   */
  initializeFirebase() {
    if (firebase.apps.length === 0) { // eslint-disable-line
      firebase.initializeApp(config); // eslint-disable-line
    }
  }

  /**
   * Get firebase instance used with this object
   */
  get firebase() {
    return firebase; // eslint-disable-line
  }


  /**
   * Function called when authorizing user to verify them as a github user.
   * This function doubles as an opt out when the current user wants to leave
   * our site for good.
   * 
   * Side Effects: when authorizing a user, it sets the current objects token
   * to the returned Github API accessToken if a token is returned.
   */
  toggleSignIn() {
    firebase = getFirebase();
    if (!firebase.auth().currentUser) {
      let provider = new firebase.auth.GithubAuthProvider();
      firebase.auth().signInWithPopup(provider).then(function(result) {
        // This gives you a GitHub Access Token.
        // You can use it to access the GitHub API.
        this.token = result.credential.accessToken;
      }).catch(function(error) {
        // Handle Errors here.
        console.error(error);
        // TODO: Change this in future iterations
        alert(error.message);
      });
    } else {
      // if logged in, sign out
      firebase.auth().signOut();

    }
  }

  /**
   * Returns the current user if signed in and null if not. For all intents and
   * purposes, this function will serve as a true/false value in an if
   * statement.
   */
  isSignedIn() {
    return firebase.auth().currentUser;
  }
}

export default new GithubAuthorizer();
