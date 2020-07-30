import * as dashboard from './dashboard.js';
import '@testing-library/jest-dom/extend-expect';
import {screen} from '@testing-library/dom';

/**
 * @fileOverview this file is essentially testing that if correct information
 * about a user is gotten from the UserSevlet, It will be displayed in some way
 * on the Dashboard.html page.
 */
global.mailtouiApp = {run: () => {}};

const mockUserAllFields = {
  interestTags: ['interest1', 'interest2'],
  projects: [
    {repositoryId: 'mockRepoID1', name: 'mockProjectName1'},
    {repositoryId: 'mockRepoID2', name: 'mockProjectName2'},
  ],
  bio: 'This is the mock bio.',
  email: 'samialves@google.com',
  gitHubID: 'mockGitID',
  gitHubURL: 'http://localhost/mockGitID',
  image: 'http://localhost/image.jpg',
  location: 'Mockville, Mocktana',
  name: 'Mocky the Mockster',
};

const mockUserMissingOptionalFields = {
  name: 'Mocky the Mockster',
  image: 'http://localhost/image.jpg',
  gitHubID: 'mockGitID',
  gitHubURL: 'http://localhost/mockGitID',
};

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

  it('shows the right email button', () => {
    const userEmailButton = document.getElementById(dashboard.USER_EMAIL_ID);
    expect(userEmailButton).toBeVisible();
    expect(userEmailButton.href).toBe('mailto:' + mockUserAllFields.email);
  });

  it('has the right GitHub URL', () => {
    const userGithubButton = document.getElementById(dashboard.USER_GITHUB_ID);
    expect(userGithubButton.href).toBe(mockUserAllFields.gitHubURL);
  });

  it('is populated with the correct location', () => {
    expect(screen.getByText(mockUserAllFields.location))
        .not.toBeNull();
  });

  it('is populated with the correct name', () => {
    expect(screen.getByText(mockUserAllFields.name)).not.toBeNull();
  });

  it('is populated with the correct bio', () => {
    expect(screen.getByText(mockUserAllFields.bio)).not.toBeNull();
  });

  it('is populated with the correct interest tags', () => {
    mockUserAllFields.interestTags.forEach(function(interest) {
      expect(screen.getByText(interest)).not.toBeNull();
    });
  });

  it('is populated with the correct project tags', () => {
    mockUserAllFields.projects.forEach(function(project) {
      const projectTag = screen.getByText(project.name);
      expect(projectTag).not.toBeNull();
      expect(projectTag.href)
          .toEqual(
              dashboard.createProjectBreakoutURL(project.repositoryId).toString());
    });
  });

  it('does not display undefined', () => {
    expect(screen.queryByText('undefined')).toBeFalsy();
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

  it('does not display undefined', () => {
    expect(screen.queryByText('undefined')).toBeFalsy();
  });
});
