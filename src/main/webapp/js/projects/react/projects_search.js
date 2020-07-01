import ProjectList from './components/ProjectList.js';

/**
 * Gets mock data for project previews.
 * @return {ProjectPreviewData[]} Returns mock project preview data.
 */
function getMockProjectPreviewData() {
  return [
    {
      title: 'Tensorflow',
      shortDescription: 'An Open Source Machine Learning '
          + 'Framework for Everyone',
      tags: [
        {
          text: 'tensorflow',
        },
        {
          text: 'machine-learning',
        },
        {
          text: 'python',
        },
        {
          text: 'deep-learning',
        },
        {
          text: 'deep-neural-networks',
        },
        {
          text: 'neural-network',
        },
        {
          text: 'ml',
        },
        {
          text: 'distributed',
        },
      ],
      numMentors: 5,
    },
    {
      title: 'Kubernetes',
      shortDescription: 'Production-Grade Container '
          + 'Scheduling and Management',
      tags: [
        {
          text: 'kubernetes',
        },
        {
          text: 'go',
        },
        {
          text: 'cncf',
        },
        {
          text: 'containers',
        },
      ],
      numMentors: 1,
    },
    {
      title: 'open-sesame',
      shortDescription: 'An open source mentorship platform',
      tags: [
        {
          text: 'javascript',
        },
        {
          text: 'mentorship',
        },
        {
          text: 'react',
        },
      ],
      numMentors: 3,
    },
  ];
}

const projectsContainer = document.getElementById('projects-container');
ReactDOM.render(
    <ProjectList projectPreviews={getMockProjectPreviewData()} />,
    projectsContainer);
