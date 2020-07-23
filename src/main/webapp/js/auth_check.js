import {
  standardizeFetchErrors,
  makeRelativeUrlAbsolute,
} from './fetch_handler.js';

/**
 * Creates the authentication fetch request.
 * @return {Promise} Returns the authentication fetch request.
 */
function createAuthFetch() {
  const errorFormattedRequest = standardizeFetchErrors(
      fetch(makeRelativeUrlAbsolute('/auth')),
      'Failed to communicate with authentication server.',
      'Failed to verify authentication.');
  return errorFormattedRequest
      .then((response) => response.json()).then((authData) => {
        // Any uses of authDataFetch after the fetch request has already been
        // completed will immediately resolve with the authentication data.
        authDataFetch = Promise.resolve(authData);
        return authData;
      }).catch((errorResponse) => {
        // Any uses of authDataFetch after the fetch request has already failed
        // will immediately reject with the error.
        authDataFetch = Promise.reject(errorResponse);
        return Promise.reject(errorResponse);
      });
}

/**
 * A promise that returns the authentication data once it has been fetched.
 * This is useful if you need to define more advanced behavior with the
 * authentication data.
 * @type {Promise}
 */
export let authDataFetch = createAuthFetch();

/**
 * Performs an authentication check and redirects if the user is not
 * authenticated. If a no profile redirect is not provided, the user will be
 * logged out in the event that they do not have a profile.
 * @param {string} [noAuthRedirect=null] Where to redirect if the user is not
 *    authenticated. By default the user will not be redirected.
 * @param {string} [authRedirect=null] Where to redirect if the user is
 *    authenticated. By default the user will not be redirected.
 * @param {string} [noProfileRedirect=null] Where to redirect if the user does
 *    not have a profile. By default the user wil not be redirected.
 * @return {Promise} Returns a promise for the authentication check.
 */
export function authRedirect(
    noAuthRedirect = null, authRedirect = null, noProfileRedirect = null) {
  return authDataFetch.then((authData) => {
    if (authData.authorized) {
      if (!authData.hasProfile) {
        if (noProfileRedirect) {
          window.location.href = noProfileRedirect;
        }
      } else if (authRedirect) {
        window.location.href = authRedirect;
      }
    } else if (noAuthRedirect) {
      window.location.href = noAuthRedirect;
    }
  }).catch((errorRespone) => {
    console.error(errorRespone);
    if (noAuthRedirect) {
      window.location.href = noAuthRedirect;
    }
  });
}
