import { Routes } from '@angular/router';
import { DashboardPage } from './pages/dashboard-page/dashboard-page';
import { NotFoundPage } from './pages/notfound-page/notfound-page';

export const routes: Routes = [

    { path: '', component: DashboardPage },

    { path: '**', component: NotFoundPage },
]