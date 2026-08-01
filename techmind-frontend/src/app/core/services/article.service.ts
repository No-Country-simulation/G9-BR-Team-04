import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/env';
import { Observable } from 'rxjs';


export interface Article {
  id: string
  titulo: string
  texto: string
}


@Injectable({ providedIn: 'root' })
export class ArticleService {
  private http = inject(HttpClient)
  private apiUrl = `${environment.apiUrl}/conteudo`

  getAll(): Observable<Article[]> {
    return this.http.get<Article[]>(this.apiUrl)
  }

  getById(id: string): Observable<Article> {
    return this.http.get<Article>(`${this.apiUrl}/${id}`)
  }

  create(article: Partial<Article>): Observable<Article> {
    return this.http.post<Article>(this.apiUrl, article)
  }

  update(id: string, article: Partial<Article>): Observable<Article> {
    return this.http.put<Article>(`${this.apiUrl}/${id}`, article)
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
  }

}