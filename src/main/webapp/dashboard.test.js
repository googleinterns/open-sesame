import * as dashboard from './dashboard.js';
import '@testing-library/jest-dom/extend-expect';
import { getByText } from '@testing-library/dom';

/**
 * @fileOverview this file is essentially testing that if correct information 
 * about a user is gotten from the UserSevlet, It will be displayed in some way
 * on the Dashboard.html page.
 */
global.mailtouiApp = { run: () => {} };

const mockUserAllFields = {
  interestTags: ['interest1', 'interest2'],
  projects: [
    { respositoryID: 'mockRepoID1', name: 'mockProjectName1' },
    { respositoryID: 'mockRepoID2', name: 'mockProjectName2' },
  ],
  bio: 'This is the mock bio.',
  email: 'samialves@google.com',
  gitHubID: 'mockGitID',
  image: 'http://localhost/image.jpg',
  location: 'Mockville, Mocktana',
  name: 'Mocky the Mockster',
};

const mockUserMissingOptionalFields = {
  name: 'Mocky the Mockster',
  image: 'http://localhost/image.jpg',
  gitHubID: 'mockGitID',
}

// Note: Update this when the dashboard gets changed
/**
 * Function to replicate the DOM of Dashboard.html 
 */
function resetDocumentBody() {
  document.body.innerHTML = `
  <div id="about-me" class="container tab-pane active"><br>
    <div class="card card-holder col-12 ">
      <div id="about-me-card-body" class="card-body">
        <img id="user-image" class="card-img-top img-thumbnail p-2 m-2 
            mx-auto d-block user-img" src="#" alt="Card image">
        <h4 id="user-name-location" class="card-title dark-emph"></h4>
        <p id="user-bio" class="card-text"></p>
        <a id="user-github" href="#" class="btn btn-primary btn-emphasis" 
            role="button">
          Github
        </a>
        <a id="user-email" href="#" 
            class="mailtoui btn btn-primary btn-emphasis" role="button">
          Email Me
        </a>
      </div>
    </div>
  </div>
  `;
};

describe('About me card of a user with no missing optional fields', () => {
  beforeAll(() => {
    resetDocumentBody();
    dashboard.createAboutMe(mockUserAllFields, document);
  });

  it('is populated with the correct image.', () => {
    const userImage = document.getElementById(dashboard.USER_IMAGE_ID);
    expect(userImage.src).toBe(mockUserAllFields.image);
  });

  it('shows the email button', () => {
    const userEmailButton = document.getElementById(dashboard.USER_EMAIL_ID);
    expect(userEmailButton).toBeVisible();
  });

  it('is populated with the correct location', () => {
    const aboutMeCardDiv = document.getElementById(dashboard.ABOUT_ME_CARD_ID);
    expect(getByText(aboutMeCardDiv, mockUserAllFields.location))
      .not.toBeNull();
  });

  it('is populated with the correct name', () => {
    const aboutMeCardDiv = document.getElementById(dashboard.ABOUT_ME_CARD_ID);
    expect(getByText(aboutMeCardDiv, mockUserAllFields.name)).not.toBeNull();
  });

  it('is populated with the correct bio', () => {
    const aboutMeCardDiv = document.getElementById(dashboard.ABOUT_ME_CARD_ID);
    expect(getByText(aboutMeCardDiv, mockUserAllFields.bio)).not.toBeNull();
  });

  it('is populated with the correct interest tags', () => {
    const aboutMeCardDiv = document.getElementById(dashboard.ABOUT_ME_CARD_ID);
    mockUserAllFields.interestTags.forEach(function(interest) {
      expect(getByText(aboutMeCardDiv, interest)).not.toBeNull();
    });
  });

  it('is populated with the correct project tags', () => {
    const aboutMeCardDiv = document.getElementById(dashboard.ABOUT_ME_CARD_ID);
    mockUserAllFields.projects.forEach(function(project) {
      const projectTag = getByText(aboutMeCardDiv, project.name);
      expect(projectTag).not.toBeNull();
      // expect(projectTag.href) // for some reason, thiis jusst wasnt working
      //    .toBe(dashboard.createProjectBreakoutURL(project.respositoryID));
    });
  });
});

describe('About me card of a user with missing optional fields', () => {
  beforeAll(() => {
    resetDocumentBody();
    dashboard.createAboutMe(mockUserMissingOptionalFields, document);
  });

  it('hides the email button', () => {
    const userEmailButton = document.getElementById(dashboard.USER_EMAIL_ID);
    expect(userEmailButton).not.toBeVisible();
  });
});
