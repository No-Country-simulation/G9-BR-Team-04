import { Router } from '@angular/router';


export function goBackOrHome(router: Router): void {
  if (typeof window !== 'undefined' && window.history.length > 1) {
    window.history.back();
    return;
  }

  router.navigate(['/']);
}


export function returnNavigation(router: Router): Promise<boolean> {
  return router.navigate(['/']);
}