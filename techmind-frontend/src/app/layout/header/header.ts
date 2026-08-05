import { Component, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { Bars } from '@primeicons/angular/bars';
import { ChartBar } from '@primeicons/angular/chart-bar';
import { Home } from '@primeicons/angular/home';
import { PIcon } from '@primeicons/angular/p-icon';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, PIcon, Bars, Home, ChartBar],
  templateUrl: './header.html',
})
export class Header {
  logoURL: string = '/imgs/logo_techmind--no-bg.png'
  imgAlt: string = 'Logo TechMind'

  pagesList = [
    { icon: 'home', label: 'Início', path: '' },

    { icon: 'chart-bar', label: 'Dashboard', path: '' },
  ]

  sidebarOpen = signal(false)

  toggleSidebar() {
    this.sidebarOpen.update(open => !open)
  }
}