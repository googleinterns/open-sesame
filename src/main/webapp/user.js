import {
  standardizeFetchErrors,
  makeRelativeUrlAbsolute,
} from './js/fetch_handler.js';

/**
 * Get the user with @param userID from the user servlet. If no @param userID is
 * supplied, function returns the currently signed in user.
 * @param {string=} userID OPTIONAL
 * @return {dashboard.User}
 */
function getUser(userID = null) { //eslint-disable-line  
  const fetchURL = '/user' + (userID ? '?userID=' + userID : '');
  const fetchRequest = fetch(makeRelativeUrlAbsolute(fetchURL));

  const errorFormattedFetchRequest = standardizeFetchErrors(
    fetchRequest,
    'Failed to communicate with the server. Please try again later.',
    'An error occcured while retrieving this account.' +
    ' Please try again later.');

  return errorFormattedFetchRequest.then((response) => response.json())
    .then((user) => {
      return user;
    });
}

/**
 * Posts a user to the servlet
 * @param {URLSearchParams} userParams parameters with the users information
 * @return {?Promise} Returns a prepared post user fetch request or null if the
 * fetch request could not be prepared.
 */
function postUser(userParams) {
  const fetchRequest = fetch(makeRelativeUrlAbsolute('/user'), {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: userParams,
  });

  const errorFormattedFetchRequest = standardizeFetchErrors(
    fetchRequest,
    'Failed to communicate with the server. Please try again later.',
    'An error occcured while creating your account.' +
    ' Please try again later.');

  return errorFormattedFetchRequest;
}

export { getUser, postUser };
