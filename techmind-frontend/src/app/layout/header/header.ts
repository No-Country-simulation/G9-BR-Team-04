import { Component, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { Bars } from '@primeicons/angular/bars';
import { PIcon } from '@primeicons/angular/p-icon';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, Bars, PIcon],
  templateUrl: './header.html',
})
export class Header {
  logoURL: string = '/imgs/logo_techmind--no-bg.png'
  imgAlt: string = 'Logo TechMind'

  pagesList = [
    { icon: 'plus-circle', label: 'Novo Conteúdo', path: '/new-article' },
    { icon: 'list', label: 'Artigos', path: '/articles-list' },
    { icon: 'eye', label: 'Analisar API', path: '/analytics' },
  ]

  sidebarOpen = signal(false)

  toggleSidebar() {
    this.sidebarOpen.update(open => !open)
  }

}