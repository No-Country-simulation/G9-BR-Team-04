import { Component } from '@angular/core';
import { Spinner } from '@primeicons/angular/spinner';


@Component({
  selector: 'app-loading',
  imports: [Spinner],
  templateUrl: './loading.html',
})
export class Loading { }