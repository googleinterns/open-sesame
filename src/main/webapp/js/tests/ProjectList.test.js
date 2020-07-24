import React from 'react';
import {ProjectList} from '../projects/react/components/ProjectList.js';
import '@testing-library/jest-dom/extend-expect';
import {render, screen} from '@testing-library/react';

const mockProjectPreviews = [
  {
    name: 'Project Name 1',
    description: 'Project Description 1',
    topicTags: ['Topic 1', 'Topic 2', 'Topic 3'],
    primaryLanguage: 'Primary Language',
    numMentors: 2,
    repositoryId: '273537467',
  },
  {
    name: 'Project Name 2',
    description: 'Project Description 2',
    topicTags: ['Topic 1', 'Topic 2', 'Topic 3'],
    primaryLanguage: 'Primary Language',
    numMentors: 1,
    repositoryId: '45717250',
  }
];

describe('Project preview list', () => {
  it('renders', () => {
    const elem = render(<ProjectList
        loading={false}
        projectPreviews={mockProjectPreviews}
        inRouter={false} />);

    expect(elem.container.firstChild).not.toBeNull();
  });
  
  it('renders while loading', () => {
    const elem = render(<ProjectList loading={true} inRouter={false} />);

    expect(elem.container.firstChild).not.toBeNull();
  });

  it('displays project previews', () => {
    render(<ProjectList
        loading={false}
        projectPreviews={mockProjectPreviews}
        inRouter={false} />);

    expect(screen.queryByText(mockProjectPreviews[0].name)).not.toBeNull();
    expect(screen.queryByText(mockProjectPreviews[1].name)).not.toBeNull();
  });

  it('provides links to project breakout pages', () => {
    render(<ProjectList
      loading={false}
      projectPreviews={mockProjectPreviews}
      inRouter={false} />);

    expect(screen.queryByText(mockProjectPreviews[0].name)).toHaveAttribute(
        'href', `/projects.html#/${mockProjectPreviews[0].repositoryId}`);
    expect(screen.queryByText(mockProjectPreviews[1].name)).toHaveAttribute(
        'href', `/projects.html#/${mockProjectPreviews[1].repositoryId}`);
  });
});
