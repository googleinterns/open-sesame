/**
 * Get the user @param user from the user servlet. gets a user object from the
 * UserServlet
 * @param {string} user
 * @return {dashboard.User}
 */
function getUser(user) { //eslint-disable-line  
  // TODO: switch to standard fetch error handler
  return fetch('/user?githubID=' + user)
      .then(errorHandling).then((response) => response.json())
      .then((user) => {
        return user;
      })
      .catch((error) => {
        console.error(error);
        return null;
      });
}

/**
 * Posts a user to the servlet
 * @param {URLSearchParams} userParams parameters with the users information
 */
function postUser(userParams) {
  fetch('/user', {method: 'POST', body: userParams})
      .catch((error) => {
        console.error(error);
      });
}

/**
 * Basic error handling checks if fetch results are 'ok.'
 * @param {Response} response the HTTP response
 * @return {Response}an 'ok' HTTP response
 */
function errorHandling(response) {
  if (!response.ok) {
    throw Error(response.statusText);
  }
  return response;
}

export {getUser, postUser};
