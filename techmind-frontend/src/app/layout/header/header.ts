import { Component, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { Bars } from '@primeicons/angular/bars';
import { ChartBar } from '@primeicons/angular/chart-bar';
import { Home } from '@primeicons/angular/home';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, Bars, Home, ChartBar
  ],
  templateUrl: './header.html',
})
export class Header {
  logoURL: string = '/imgs/logo_techmind--no-bg.png'
  imgAlt: string = 'Logo TechMind'

  sidebarOpen = signal(false)

  toggleSidebar() {
    this.sidebarOpen.update(open => !open)
  }
}