import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Dashboard } from './features/dashboard/dashboard';
import { Header } from './layout/header/header';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Dashboard],
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('techmind-frontend');
}
