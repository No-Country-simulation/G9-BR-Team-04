import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Sidebar } from '../../layout/sidebar/sidebar';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, Sidebar],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  menuItems = [
    { icon: '📊', label: 'Overview', active: true },
    { icon: '📚', label: 'Knowledge Base', active: false },
    { icon: '📁', label: 'Projects', active: false },
    { icon: '👥', label: 'Team', active: false },
    { icon: '📈', label: 'Analytics', active: false },
    { icon: '⚙️', label: 'Settings', active: false },
  ];

  topContributors = [
    { name: 'Sarah J.', initials: 'SJ', color: '#FF6B6B' },
    { name: 'Mike R.', initials: 'MR', color: '#4ECDC4' },
    { name: 'David K.', initials: 'DK', color: '#95E1D3' },
  ];

  recentContributions = [
    { title: 'React Native Best Practices', author: 'Maria G.', time: '2 hours ago' },
    { title: 'AWS Lambda Deployment Guide', author: 'Tom B.', time: '5 hours ago' },
    { title: 'Data Structures in Python', author: 'Lisa K.', time: 'yesterday' },
  ];

  trendingTopics = [
    'Machine Learning', 'Kubernetes', 'React', 'Python', 'DevOps',
    'Cloud Computing', 'Microservices', 'GraphQL', 'AI', 'Cybersecurity',
    'Docker', 'Node.js'
  ];

  shortcuts = [
    { label: 'New Article' },
    { label: 'My Projects' },
    { label: 'Team Wiki' },
    { label: 'Help Center' },
  ];
}
