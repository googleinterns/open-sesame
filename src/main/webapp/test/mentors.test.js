import * as Mentors from '../mentors.js';
import '@testing-library/jest-dom/extend-expect';
import {getByText} from '@testing-library/dom';

const mockMentor = {
  interestTags: ['interest1', 'interest2'],
  projects: [{respositoryID: 'mockRepoID', name: 'mockProjectName'},],
  bio: 'This is the mock bio.',
  email: 'samialves@google.com',
  gitHubID: 'mockGitID',
  image: '../images/dior.jpg',
  location: 'Mockville, Mocktana',
  name: 'Mocky the Mockster',
};

describe('Mentor card', () => {
  it('is created with all expected componenets', () => {
    const mentorElement = Mentors.createMentorElement(mockMentor);
    expect(mentorElement).not.toBeNull();
    expect(getByText(mentorElement, mockMentor.name)).not.toBeNull();
    expect(getByText(mentorElement, mockMentor.location)).not.toBeNull();
    expect(getByText(mentorElement, mockMentor.bio)).not.toBeNull();

    mockMentor.interestTags.forEach(function(interest) {
      expect(getByText(mentorElement, interest)).not.toBeNull();
    });
    mockMentor.projects.forEach(function(project) {
      expect(getByText(mentorElement, project.name)).not.toBeNull();
    });

    const emailElement = getByText(mentorElement, 'Send Email Introduction');
    expect(emailElement.getAttribute('href'))
        .toBe('mailto: ' + mockMentor.email);

    const gitHubElement = getByText(mentorElement, 'GitHub Profile');
    expect(gitHubElement.getAttribute('href'))
        .toBe('https://github.com/' + mockMentor.gitHubID);

    const imgElements = mentorElement.getElementsByClassName('mentor-picture');
    expect(imgElements[0].getAttribute('src')).toBe(mockMentor.image);
  });
});
