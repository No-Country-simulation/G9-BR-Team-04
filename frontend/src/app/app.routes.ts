import { Routes } from '@angular/router';
import { ArticleCreatePage } from './pages/article-create/article-create';
import { ArticlesListPage } from './pages/articles-list/articles-list';
import { NotFoundPage } from './pages/notfound/notfound';

export const routes: Routes = [
    { path: '', redirectTo: 'articles-list', pathMatch: 'full' },
    { path: 'articles-list', component: ArticlesListPage },
    { path: 'new-article', component: ArticleCreatePage },
    { path: '**', component: NotFoundPage },
]