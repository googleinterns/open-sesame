import React from 'react';
import {ExpandedProject} from '../projects/react/components/ExpandedProject.js';
import '@testing-library/jest-dom/extend-expect';
import {render, screen, getByText} from '@testing-library/react';

const mockProject = {
  name: 'Project Name',
  description: 'Project Description',
  topicTags: ['Topic 1', 'Topic 2', 'Topic 3'],
  primaryLanguage: 'Primary Language',
  numMentors: 2,
  repositoryId: '273537467',
  mentors: [
    {
      userID: '1',
      bio: 'Mentor Bio 1',
      email: 'mentorEmail1@test.com',
      interestTags: ['Interest 1', 'Interest 2'],
      gitHubURL: 'https://github.com/Richie78321',
      image: 'https://avatars3.githubusercontent.com/u/31116115?v=4',
      location: 'Pennsylvania, USA',
      name: 'Richie Goulazian',
    },
    {
      userID: '2',
      interestTags: ['Interest 1', 'Interest 2'],
      gitHubURL: 'https://github.com/Sami-2000',
      image: 'https://avatars1.githubusercontent.com/u/50716380?v=4',
      name: 'Sami Alves',
    },
  ],
};

describe('Project breakout page', () => {
  it('renders', () => {
    const elem = render(<ExpandedProject
      loading={false}
      project={mockProject} />);

    expect(elem.container.firstChild).not.toBeNull();
  });

  it('renders while loading', () => {
    const elem = render(<ExpandedProject loading={true} />);

    expect(elem.container.firstChild).not.toBeNull();
  });

  it('renders the mentor cards', () => {
    render(<ExpandedProject loading={false} project={mockProject} />);

    expect(screen.getByText(mockProject.mentors[0].name)).not.toBeNull();
    expect(screen.getByText(mockProject.mentors[1].name)).not.toBeNull();
  });

  it('provides the connect button on mentor cards with email', () => {
    render(<ExpandedProject loading={false} project={mockProject} />);

    const mentorCard =
        screen.getByText(mockProject.mentors[0].name).closest('.card');
    expect(getByText(mentorCard, 'Connect'))
        .toHaveAttribute('href', `mailto:${mockProject.mentors[0].email}`);
  });
});
