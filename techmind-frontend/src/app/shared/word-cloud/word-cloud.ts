import { Component, input } from '@angular/core';
import { WordFrequency } from '../../utils/word-frequency';

@Component({
  selector: 'app-word-cloud',
  imports: [],
  templateUrl: './word-cloud.html',
})
export class WordCloud {
  words = input.required<WordFrequency[]>()
}