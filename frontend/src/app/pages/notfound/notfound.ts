import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { RectangleXmark } from '@primeicons/angular/rectangle-xmark';
import { Header } from '../../layout/header/header';
import { Button } from '../../shared/button/button';
import { goBackOrHome } from '../../utils/navigation-utils';


@Component({
  selector: 'notfound-page',
  imports: [RectangleXmark, Header, Button],
  templateUrl: './notfound.html',
})
export class NotFoundPage {

  private readonly router = inject(Router);

  goBack(): void {
    goBackOrHome(this.router);
  }

}