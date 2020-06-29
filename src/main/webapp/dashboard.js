/**
 * Div of the About me tab.
 */
const aboutMeDiv = document.getElementById('about-me');

/**
 * Fake information for the hardcoded stage.
 */
const dummyName = 'Dior';
const dummyLocation = 'Sunnyvale CA.';
const dummyBio = 'The cutest pug in The tech industry. Lorem ipsum dolor sit ' +
  'amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut' +
  ' labore et dolore magna aliqua.';
const dummyImg = 'images/dior.jpg';
const dummyTags = ['Weekly', '30 mins', 'Kubernetes', 'Open Sesame',
  'Documentation',
];
const dummyGitSrc = '#';

/**
 * Creates a mentor container element from input text.
 * @param {String} name
 * @param {String} location
 * @param {String} bio
 * @param {String} imgSrc
 * @param {String List} tags
 * @return {HTMLElement} mentorContainer
 */
function createAboutMe(name, location, bio, imgSrc, tags, gitSrc) {
  let AboutMeCard = document.createElement('div');
  AboutMeCard.className = 'card col-12';

  let AboutMeCardBody = document.createElement('div');
  AboutMeCardBody.className = 'card-body';
  AboutMeCard.append(AboutMeCardBody);

  let userImage = document.createElement('img');
  userImage.className = 'card-img-top img-thumbnail w-25 p-2 m-2 mx-auto' +
    ' d-block';
  userImage.src = imgSrc;
  AboutMeCardBody.append(userImage);

  let userNameAndLocation = document.createElement('h4');
  userNameAndLocation.id = 'user-name';
  userNameAndLocation.className = 'card-title dark-emph';
  userNameAndLocation.innerHTML = name + '<br>';
  AboutMeCardBody.append(userNameAndLocation);

  let userLocation = document.createElement('small');
  userLocation.innerText = location;
  userNameAndLocation.append(userLocation);

  let userBio = document.createElement('p');
  userBio.className = 'card-text';
  userBio.innerText = bio;
  AboutMeCardBody.append(userBio);

  let gitButton = document.createElement('a');
  gitButton.className = 'btn btn-primary';
  gitButton.setAttribute('role', 'button');
  gitButton.innerText = 'Github';
  gitButton.href = gitSrc;
  AboutMeCardBody.append(gitButton);

  let tagRow = document.createElement('div');
  tagRow.className = 'row p-3';
  AboutMeCard.append(tagRow);
  for (tag of tags) {
    let tagElement = document.createElement('div');
    tagElement.className = 'border border-muted text-muted mr-1 mb-1 badge';
    tagElement.innerText = tag;
    tagRow.append(tagElement);
  }

  return AboutMeCard;
}

/**
 * Call functions to populate page sections with data.
 */
function setUpPage() {
  aboutMeDiv.append(createAboutMe(dummyName, dummyLocation, dummyBio,
    dummyImg, dummyTags, dummyGitSrc));
}

window.onload = setUpPage;
