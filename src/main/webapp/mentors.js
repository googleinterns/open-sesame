/**
 * Get mentors from the mentor servlet.
 */
/*
function getMentors() {
  console.log('entering get mentors function/n');
  let url = new URL(
      '/mentors', window.location.protocol + '//' + window.location.hostname);

  fetch(url).then(errorHandling).then((response) => response.json())
      .then((mentors) => {
    console.log(mentors);
    const mentorsContainer = document.getElementById('mentors-container');
    mentorsContainer.innerHTML = '';
    for (const mentor of mentors) {
      mentorsContainer
          .appendChild(createMentorElement(mentor.name, mentor.description));
    }
  })
  .catch((error) => {
    console.log(error);
  });
}

/**
 * Basic error handling checks if fetch results are 'ok.'
 * @param {Response} response - A fetch response.
 * @return {Response} the response.
 */
function errorHandling(response) {
  if (!response.ok) {
    throw Error(response.statusText);
  }
  return response;
}

/**
 * Creates a mentor container element from input text.
 * @param {String} name
 * @param {String} description
 * @return {HTMLElement} mentorContainer
 */
function createMentorElement(name, description) {
  const mentorContainer = document.createElement('div');
  mentorContainer.className = 'p-1 col-lg-4';

  const mentorCard = document.createElement('div');
  mentorCard.className = 'card-holder card h-100 border-primary mb-3';

  const mentorCardBody = document.createElement('div');
  mentorCardBody.className = 'card-body pb-0';

  const mentorName = document.createElement('h5');
  mentorName.className = 'card-title text-primary';
  mentorName.innerHTML = name;
  mentorCardBody.appendChild(mentorName);

  const mentorDescription = document.createElement('p');
  mentorDescription.innerHTML = description;
  mentorCardBody.appendChild(mentorDescription);

  mentorCard.appendChild(mentorCardBody);
  mentorContainer.appendChild(mentorCard);

  return mentorContainer;
}
