/**
 * Get the user @param user from the user servlet.
 * @param {string} user
 * @return a user object
 */
function getUser(user) {
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
 */
function errorHandling(response) {
  if (!response.ok) {
    throw Error(response.statusText);
  }
  return response;
}
