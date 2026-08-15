import { Routes } from '@angular/router';
import { ArticleCreatePage } from './pages/article-create/article-create';
import { ArticlesListPage } from './pages/articles-list/articles-list';
import { DashboardPage } from './pages/dashboard/dashboard';
import { NotFoundPage } from './pages/notfound/notfound';
import { ObservabilityPage } from './pages/observability/observability';

export const routes: Routes = [

    { path: '', component: DashboardPage },
    { path: 'articles-list', component: ArticlesListPage },
    { path: 'new-article', component: ArticleCreatePage },
    { path: 'analytics', component: ObservabilityPage },

    { path: '**', component: NotFoundPage }
]