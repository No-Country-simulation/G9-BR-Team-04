import { Component } from '@angular/core';
import { getCurrentYear } from '../../utils/date-utils';

@Component({
  selector: 'app-footer',
  imports: [],
  templateUrl: './footer.html',
})
export class Footer {

  currentYear = getCurrentYear()

}