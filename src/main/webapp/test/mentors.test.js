import * as mentors from '../mentors.js';
import '@testing-library/jest-dom/extend-expect';
import {getByText} from '@testing-library/dom';


/**
 * @fileOverview This file tests if mentor cards are correctly added
 * to the mentor search page.
 */
const mockMentor = {
  interestTags: ['interest1', 'interest2'],
  projects: [{respositoryID: 'mockRepoID', name: 'mockProjectName'}],
  bio: 'This is the mock bio.',
  email: 'samialves@google.com',
  gitHubID: 'mockGitID',
  image: '../images/dior.jpg',
  location: 'Mockville, Mocktana',
  name: 'Mocky the Mockster',
};

let mentorElement = {};

describe('Mentor card', () => {
  beforeEach(() => {
    mentorElement = mentors.createMentorElement(mockMentor);
  });

  it('is created', () => {
    expect(mentorElement).not.toBeNull();
  });

  it('contains the name', () => {
    expect(getByText(mentorElement, mockMentor.name)).not.toBeNull();
  });

  it('contains the location', () => {
    expect(getByText(mentorElement, mockMentor.location)).not.toBeNull();
  });

  it('contains the bio', () => {
    expect(getByText(mentorElement, mockMentor.bio)).not.toBeNull();
  });

  it('contains the interests', () => {
    mockMentor.interestTags.forEach(function(interest) {
      expect(getByText(mentorElement, interest)).not.toBeNull();
    });
  });

  it('contains the projects', () => {
    mockMentor.projects.forEach(function(project) {
      expect(getByText(mentorElement, project.name)).not.toBeNull();
    });
  });

  it('contains the email link', () => {
    const emailElement = getByText(mentorElement, 'Send Email Introduction');
    expect(emailElement.getAttribute('href'))
        .toBe('mailto: ' + mockMentor.email);
  });

  it('contains the GitHub link', () => {
    const gitHubElement = getByText(mentorElement, 'GitHub Profile');
    expect(gitHubElement.getAttribute('href'))
        .toBe('https://github.com/' + mockMentor.gitHubID);
  });

  it('contains the profile picture', () => {
    const imgElements = mentorElement.getElementsByClassName('mentor-picture');
    expect(imgElements[0].getAttribute('src')).toBe(mockMentor.image);
  });
});
