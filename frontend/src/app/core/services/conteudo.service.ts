import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from '../../../environments/env';
import { Conteudo, PageResponse } from '../models/conteudo.model';


@Injectable({ providedIn: 'root' })
export class ConteudoService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/conteudo`;


  buscarTodos(): Observable<Conteudo[]> {
    return this.http.get<Conteudo[]>(this.baseUrl)
      .pipe(catchError(this.handleError))
  }

  buscarPorId(id: number): Observable<Conteudo> {
    return this.http.get<Conteudo>(`${this.baseUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  buscarPorTitulo(titulo = '', size = 100): Observable<PageResponse<Conteudo>> {
    return this.http.get<PageResponse<Conteudo>>(`${this.baseUrl}/titulo`, {
      params: { titulo, size: size.toString() }
    }).pipe(catchError(this.handleError));
  }

  buscarPorCategoria(categoria: string): Observable<Conteudo[]> {
    return this.http.get<Conteudo[]>(`${this.baseUrl}/categoria`, {
      params: { categoria }
    }).pipe(catchError(this.handleError));
  }

  buscarRelacionados(id: number): Observable<Conteudo[]> {
    return this.http.get<Conteudo[]>(`${this.baseUrl}/relacionados/${id}`)
      .pipe(catchError(this.handleError));
  }

  criar(conteudo: Pick<Conteudo, 'titulo' | 'texto'>): Observable<Conteudo> {
    return this.http.post<Conteudo>(this.baseUrl, conteudo)
      .pipe(catchError(this.handleError));
  }

  atualizar(id: number, conteudo: Partial<Conteudo>): Observable<Conteudo> {
    return this.http.put<Conteudo>(`${this.baseUrl}/${id}`, conteudo)
      .pipe(catchError(this.handleError));
  }

  apagar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }


  /**
   * Upload em lote via arquivo (ex: CSV).
   * O endpoint no Postman está como urlencoded/vazio — o mais correto para
   * upload de arquivo real é multipart/form-data, então uso FormData aqui.
   * Confirme com o backend se a rota realmente espera multipart.
   */
  enviarLote(arquivo: File): Observable<Conteudo[]> {
    const formData = new FormData();
    formData.append('arquivo', arquivo);

    return this.http.post<Conteudo[]>(`${this.baseUrl}/lote`, formData)
      .pipe(catchError(this.handleError));
  }


  private handleError(error: HttpErrorResponse) {
    let mensagem = 'Ocorreu um erro inesperado.';

    if (error.status === 0) {
      mensagem = 'Não foi possível conectar ao servidor.';
    } else if (error.status === 400) {
      mensagem = 'Dados inválidos. Verifique o formulário.';
    } else if (error.status === 404) {
      mensagem = 'Conteúdo não encontrado.';
    } else if (error.status >= 500) {
      mensagem = 'Erro no servidor. Tente novamente mais tarde.';
    }

    console.error('Erro na requisição de Conteúdo:', error);
    return throwError(() => new Error(mensagem));
  }

}