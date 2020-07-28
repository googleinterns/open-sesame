import {createMentorElement, createEmailLink, createLocationHeader, createImg,
  createBioParagraph, createNameHeader, createGitHubLink, createInterestTagsDiv}
  from '../mentors.js';
import '@testing-library/jest-dom/extend-expect';

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

describe('Mentor card', () => {
  it('is created', () => {
    const mentorElement = createMentorElement(mockMentor);
    expect(mentorElement).not.toBeNull();
  });

  it('has the correct image element', () => {
    const imgElement = createImg(mockMentor.image);
    expect(imgElement.getAttribute('class')).toBe('mentor-picture');
    expect(imgElement.getAttribute('src')).toBe(mockMentor.image);
  });

  it('has the correct name element', () => {
    const nameElement = createNameHeader(mockMentor.name);
    expect(nameElement.getAttribute('class')).toBe('card-title text-primary');
    expect(nameElement.innerHTML).toBe(mockMentor.name);
  });

  it('has the correct location element', () => {
    const locationElement = createLocationHeader(mockMentor.location);
    expect(locationElement.getAttribute('class'))
        .toBe('card-subtitle text-muted text-center mb-1');
    expect(locationElement.innerText).toBe(mockMentor.location);
  });

  it('has the correct bio element', () => {
    const bioElement = createBioParagraph(mockMentor.bio);
    expect(bioElement.innerHTML).toBe(mockMentor.bio);
  });

  it('has the correct email element', () => {
    const emailElement = createEmailLink(mockMentor.email);
    expect(emailElement.innerText).toBe('Send Email Introduction');
    expect(emailElement.getAttribute('href'))
        .toBe('mailto: ' + mockMentor.email);
    expect(emailElement.getAttribute('class')).toBe('mailtoui');
  });

  it('has the correct GitHub element', () => {
    const gitHubElement = createGitHubLink(mockMentor.gitHubID);
    expect(gitHubElement.innerText).toBe('GitHub Profile');
    expect(gitHubElement.getAttribute('href'))
        .toBe('https://github.com/' + mockMentor.gitHubID);
    expect(gitHubElement.getAttribute('class')).toBe('btn btn-primary');
  });

  it('has the correct interest list', () => {
    const tagsElement = createInterestTagsDiv(mockMentor.interestTags);
    expect(tagsElement.getAttribute('class'))
        .toBe('d-flex justify-content-center p-3');
    const tags = tagsElement
        .getElementsByClassName('border border-muted ' +
        'text-muted mr-1 mb-1 badge text-center');
    expect(tags.item(0).innerText).toBe('interest1');
    expect(tags.item(1).innerText).toBe('interest2');
  });
});
