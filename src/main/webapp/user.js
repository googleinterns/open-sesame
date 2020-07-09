/**
 * Get the user @param user from the user servlet. gets a user object from the
 * UserServlet
 * @param {string} user
 */
function getUser(user) { //eslint-disable-line
  console.log('entering get user function/n');
  fetch('/user?user=' + user)
    .then(errorHandling).then((response) => response.json())
    .then((user) => {
      return user;
    })
    .catch((error) => {
      console.log(error);
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
