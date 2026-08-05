import { Component, input } from '@angular/core'
import { File } from '@primeicons/angular/file'


@Component({
  selector: 'app-list-view',
  imports: [File],
  templateUrl: './list-view.html',
})
export class ListView {

  titleContent = input<string>('')
  author = input<string>('')
  createdAt = input<string>()

}