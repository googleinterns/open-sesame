// GCP Generated GitHub OAuth configuration
const firebaseConfig = {
  apiKey: 'AIzaSyAR88Giah8cCEAvT_zDSIREWvgIIAeS8yY',
  authDomain: 'step2020-279820.firebaseapp.com',
};

/**
 * Object used for GitHub authorization and as a wrapper for Firebase.
 * It handles Firebase initialization on instantiation and holds nifty
 * functions for handling GitHub authorization. We are using a class to have
 * access tokens persist for ease of use on the backend,
 *
 * Make sure to include the scripts below in any HTML file that uses this class.
 *
 * <script src="https://www.gstatic.com/firebasejs/7.15.5/firebase.js"></script>
 * <script type="module" src="authorization.js"></script>
 *
 * NOTE: The authorization.js file location might change.
 */
class GitHubAuthorizer {
  /**
   * Create a GitHubAuthorizer.
   */
  constructor() {

    /** @type {string} GitHub API token */
    this.token = null;

    /**
     * the Firebase instance associated with a given
     * GitHubAuthorizer
     * @type {Firebase}
     */
    this.firebase = firebase; // eslint-disable-line

    this.initializeFirebase();
  }

  /**
   * The most recent access token for the GitHub API. To refresh this, the user
   * must be signed out and signed in again. It is important to keep in mind
   * that this token expires and must be used swiftly after calling
   * toggleSignIn(). If authorization is yet to be toggled, this will return
   * null. If an error occurs during authorization, it will again be set to
   * null. This property will be set to null on sign out as well.
   *
   * NOTE: In the event that an access token times out, the token will still be
   * available here and so, a non-null token is not a good sign of a valid
   * token.
   *
   * @return {string} most recent token or null
   */
  getToken() {
    return this.token;
  }

  /**
   * The current user if authorized and null if not. For all intents and
   * purposes, this function will serve as a true/false value in an if
   * statement.
   *
   * @return {Firebase.User} current user or null
   */
  getUser() {
    return this.getFirebase().auth().currentUser;
  }

  /**
   * Get firebase instance used with this object
   * @return {Firebase} firebase instance
   */
  getFirebase() {
    return this.firebase;
  }

  /**
   * Function called when authorizing user to verify them as a gitHub user.
   *
   * Side Effects: it sets the current objects token
   * to the returned GitHub API accessToken if a token is returned.
   *
   * NOTE: this function is asynchronous and should be used with an await
   *
   * @return {<Promise> Firebase.UserCredential} credentials of the 
   * authorized user. Can throw errors.
   */
  async signIn() {
    let provider = new this.firebase.auth.GithubAuthProvider();
    let gitHubProviderResults =
      await this.getFirebase().auth().signInWithPopup(provider);
    this.token = await authorizationResults.credential.accessToken;
    return gitHubProviderResults;
  }

  /**
   * Function called as an authorization opt out when the current user wants to
   * leave our site for good.
   *
   * Side Effects: Sets the current objects token to null if successful.
   *
   * NOTE: this function is asynchronous and should be used with an await
   *
   * @return {<Promise> null} can throw errors
   */

  async signOut() {
    await this.getFirebase().auth().signOut();
    this.token = null;
    return null;
  }

  /**
   * Function called when authorizing user to verify them as a gitHub user.
   * This function doubles as an opt out when the current user wants to leave
   * our site for good.
   *
   * Side Effects: when authorizing a user, it sets the current objects token
   * to the returned GitHub API accessToken if a token is returned.
   *
   * NOTE: this function is asynchronous and should be used with an await
   *
   * @return {<Promise> Firebase.UserCredential | null} credentials of the 
   * authorized user or null the user is signed out
   */
  toggleSignIn() {
    if (this.getUser()) {
      return this.signOut();
    } else {
      return this.signIn();
    }
  }

  /**
   * Initialize firebase if firebase is not already initialized. This method can
   * be called as many times as we need without any side effects.
   *
   * NOTE: This is not an instance function and will not be exported.
   */
  initializeFirebase() {
    if (this.getFirebase().apps.length === 0) {
      this.getFirebase().initializeApp(firebaseConfig);
    }
  }
}

export let gitHubAuthorizer = new GitHubAuthorizer();
