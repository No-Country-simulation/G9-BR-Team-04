import { Component, input } from '@angular/core';

@Component({
  selector: 'app-card',
  imports: [],
  standalone: true,
  templateUrl: './card.html',
})
export class Card {
  titleCard = input<string>('')
  
  subtitle = input<string>('')
  imageUrl = input<string>('')
  
  articleText = input<string>('')
  authorName = input<string>('')

}
