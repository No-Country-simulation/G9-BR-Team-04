import { Component, input } from '@angular/core';
import { PIcon } from '@primeicons/angular/p-icon';


@Component({
  selector: 'app-button',
  imports: [PIcon],
  templateUrl: './button.html',
  styleUrl: './button.css'
})
export class Button {

  iconLeft = input<string>('')

  textButton = input.required<string>()

  iconRight = input<string>('')

}