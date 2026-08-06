import { CommonModule } from '@angular/common';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ChartConfiguration, ChartData } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { ButtonModule } from 'primeng/button';
import { Article, ArticleService } from '../../core/services/article.service';
import { Card } from '../../shared/card/card';
import { ListView } from '../../shared/list-view/list-view';
import { UserAvatar } from '../../shared/user-avatar/user-avatar';
import { WordCloud } from '../../shared/word-cloud/word-cloud';
import { getLastMonths } from '../../utils/date-utils';
import { extractWordFrequency } from '../../utils/word-frequency';


@Component({
  selector: 'dashboard-page',
  imports: [BaseChartDirective, ButtonModule, CommonModule, Card, UserAvatar, WordCloud, ListView, RouterLink],
  templateUrl: './dashboard.html',
})
export class DashboardPage implements OnInit {
  private articleService = inject(ArticleService)

  articles = signal<Article[]>([])

  wordFrequency = computed(() =>
    extractWordFrequency(this.articles().map(post => post.texto))
  )

  async ngOnInit() {
    this.articleService.getAll().subscribe({
      next: (data) => this.articles.set(data)
    })
  }

  months = getLastMonths(6)
  labelsForChart = this.months.map(m => m.label)

  knowledgeTotal = "1350"
  peopleActives = "9"

  engagementTotal = "450"
  engagementPercent = "79"


  public knowledgeGrowthData: ChartData<'line'> = {
    labels: this.labelsForChart,
    datasets: [
      {
        data: [34, 58, 60, 68, 76, 64],
        borderColor: '#4A9EFF',
        backgroundColor: 'transparent',
        pointBackgroundColor: 'transparent',
        pointBorderColor: 'transparent',
        pointBorderWidth: 1,
        pointRadius: 0,
        pointHoverRadius: 0,
        tension: 0.35,
        fill: false
      }
    ]
  }

  public lineChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false }
    },
    elements: {
      line: {
        borderWidth: 3,
        tension: 0.35
      }
    },
    scales: {
      x: {
        grid: { display: false }
      },
      y: {
        beginAtZero: true,
        max: 100,
        ticks: { stepSize: 20 }
      }
    }
  }


  public engagementTeamData: ChartData<'bar'> = {
    labels: this.labelsForChart,
    datasets: [{
      data: [65, 59, 80, 81, 56, 55, 40],
      backgroundColor: [
        'rgba(255, 99, 132, 0.2)',
        'rgba(255, 159, 64, 0.2)',
        'rgba(255, 205, 86, 0.2)',
        'rgba(75, 192, 192, 0.2)',
        'rgba(54, 162, 235, 0.2)',
        'rgba(153, 102, 255, 0.2)',
        'rgba(201, 203, 207, 0.2)'
      ],
      borderColor: [
        'rgb(255, 99, 132)',
        'rgb(255, 159, 64)',
        'rgb(255, 205, 86)',
        'rgb(75, 192, 192)',
        'rgb(54, 162, 235)',
        'rgb(153, 102, 255)',
        'rgb(201, 203, 207)'
      ],
      borderWidth: 1
    }]
  }

  public barChartConfig: ChartConfiguration<'bar'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false }
    },
    scales: {
      y: {
        beginAtZero: true
      }
    }
  }


  public contributionOverTimeData: ChartData<'line'> = {
    labels: this.labelsForChart,
    datasets: [
      {
        data: [34, 58, 60, 68, 76, 64],
        borderColor: '#4A9EFF',
        backgroundColor: 'rgba(74, 158, 255, 0.16)',
        pointBackgroundColor: '#FFFFFF',
        pointBorderColor: '#2563eb',
        pointBorderWidth: 2,
        pointRadius: 5,
        pointHoverRadius: 6,
        tension: 0.35,
        fill: false
      }
    ]
  }



  topContributors = [
    { name: 'Sarah J.', initials: 'SJ' },
    { name: 'Mike R.', initials: 'MR' },
    { name: 'David K.', initials: 'DK' },
  ]


  shortcuts = [
    { label: 'Novo Artigo', link: "#" },
    { label: 'Meus Artigos', link: "#" },
    { label: 'Wiki da Equipe', link: "#" },
  ]


  recentContributions = [
    { title: 'React Native Best Practices', author: 'Sarah J.', time: '2 hours ago' },
    { title: 'AWS Lambda Deployment Guide', author: 'David K.', time: '5 hours ago' },
    { title: 'Data Structures in Python', author: 'Mike R.', time: 'yesterday' },
  ]


}