import {getMentors, createMentorElement, createEmailLink, createLocationHeader, createImg, createBioParagraph, createNameHeader, createGitHubLink, createInterestTagsDiv} from '../mentors.js';
import '@testing-library/jest-dom/extend-expect';

 global.mailtouiApp = {run: () => {}};

const mockMentor = {
  interestTags: ['interest1', 'interest2'],
  projects: [{respositoryID: 'mockRepoID', name: 'mockProjectName',}],
  bio: 'This is the mock bio.',
  email: 'samialves@google.com',
  gitHubID: 'mockGitID',
  image: null,
  location: 'Mockville, Mocktana',
  name: 'Mocky the Mockster',
};

describe('Mentor card', () => {
  it('is created', () => {
    const mentorElement = createMentorElement(mockMentor);
    expect(mentorElement).not.toBeNull();
  });

  it('has the correct name', () => {
    const mentorElement = createMentorElement(mockMentor);
    expect(mentorElement).not.toBeNull();
  });
});
