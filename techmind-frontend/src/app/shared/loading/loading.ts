import { Component, signal } from '@angular/core';
import { Check } from '@primeicons/angular/check';
import { Spinner } from '@primeicons/angular/spinner';


@Component({
  selector: 'app-loading',
  imports: [Spinner, Check],
  templateUrl: './loading.html',
})
export class Loading { 

    loading = signal(false);

    load() {
        this.loading.set(true);
        
        setTimeout(() => {
            this.loading.set(false);
        }, 2000);
    }

}