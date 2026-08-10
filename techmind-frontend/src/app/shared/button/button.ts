import { Component, input } from '@angular/core';
import { PIcon } from '@primeicons/angular/p-icon';


@Component({
  selector: 'app-button',
  imports: [PIcon],
  templateUrl: './button.html',
})
export class Button {

  iconLeft = input<string>('')

  textButton = input.required<string>()

  iconRight = input<string>('')

}