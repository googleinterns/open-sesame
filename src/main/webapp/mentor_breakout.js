import {standardizeFetchErrors} from '/js/fetch_handler.js';
import {createMentorElement} from '/mentors.js';

populateMentorInfo();

/**
 * Populates mentor info for the mentor breakout pages.
 */
function populateMentorInfo() {
  const mentorContainer = document.getElementById('mentor-breakout-container');
  if (!mentorContainer) {
    return;
  }
  const currentUrl = new URL(window.location.href);
  const userID = currentUrl.searchParams.get('mentorID');
  console.log(userID);
  const url = new URL('/mentor_breakout', window.location.origin);
  url.searchParams.append('mentorID', userID);
  const fetchRequest = standardizeFetchErrors(
      fetch(url),
      'Failed to communicate with the server, please try again later.',
      'Encountered a server error.',
  );

  fetchRequest.then((response) => response.json()).then((mentor) => {
    console.log(mentor);
    mentorContainer.appendChild(createMentorElement(mentor));
    mailtouiApp.run();
  })
      .catch((errorResponse) => {
        console.error(errorResponse);
        if (typeof(errorResponse.userMessage) !== 'undefined') {
          alert(errorResponse.userMessage);
        }
      });
}