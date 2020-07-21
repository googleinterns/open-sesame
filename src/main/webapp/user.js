import {
  standardizeFetchErrors,
  makeRelativeUrlAbsolute,
} from './js/fetch_handler.js';

/**
 * Get the user with @param userID from the user servlet. If no @param userID is
 * supplied, function returns the currently signed in user.
 * @param {string=} userID (optional)
 * @return {dashboard.User}
 */
function getUser(userID = null) { //eslint-disable-line  
  const fetchURL = '/user' + (userID ? '?userID=' + userID : '');
  const fetchRequest = fetch(makeRelativeUrlAbsolute(fetchURL));

  return standardizeFetchErrors(
      fetchRequest,
      'Failed to communicate with the server. Please try again later.',
      'An error occcured while retrieving this account.' +
      ' Please try again later.')
    .then((response) => response.json());
}

export { getUser };
