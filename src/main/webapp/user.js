import {
  standardizeFetchErrors,
  makeRelativeUrlAbsolute,
} from './js/fetch_handler.js';

/**
 * Get the user @param user from the user servlet. gets a user object from the
 * UserServlet
 * @param {string} user
 * @return {dashboard.User}
 */
function getUser(user) { //eslint-disable-line  
  // TODO: switch to standard fetch error handler
  const fetchRequest = fetch(makeRelativeUrlAbsolute('/user?githubID=' + user));

  const errorFormattedFetchRequest = standardizeFetchErrors(
    fetchRequest,
    'Failed to communicate with the server. Please try again later.',
    'An error occcured while retrieving this account.' +
    ' Please try again later.');

  return errorFormattedFetchRequest.then((response) => response.json())
    .then((user) => {
      return user;
    })
}

/**
 * Posts a user to the servlet
 * @param {URLSearchParams} userParams parameters with the users information
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
