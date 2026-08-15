import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from '../../../environments/env';


export interface HealthStatus {
  status: 'UP' | 'DOWN' | 'UNKNOWN';
  components?: Record<string, { status: string; details?: unknown }>;
}

export interface MetricsSummary {
  names: string[];
}

export interface CircuitBreakerStatus {
  circuitBreakers: Record<string, unknown>;
}

@Injectable({ providedIn: 'root' })
export class ObservabilityService {
  private http = inject(HttpClient);
  private actuatorUrl = `${environment.apiUrl}/actuator`;

  getHealth(): Observable<HealthStatus> {
    return this.http.get<HealthStatus>(`${this.actuatorUrl}/health`)
      .pipe(catchError(this.handleError));
  }

  getMetricsNames(): Observable<MetricsSummary> {
    return this.http.get<MetricsSummary>(`${this.actuatorUrl}/metrics`)
      .pipe(catchError(this.handleError));
  }

  getMetricDetail(nome: string): Observable<unknown> {
    return this.http.get(`${this.actuatorUrl}/metrics/${nome}`)
      .pipe(catchError(this.handleError));
  }

  getCircuitBreakers(): Observable<CircuitBreakerStatus> {
    return this.http.get<CircuitBreakerStatus>(`${this.actuatorUrl}/circuitbreakers`)
      .pipe(catchError(this.handleError));
  }

  // Prometheus retorna texto puro (formato specífico), não JSON
  getPrometheusRaw(): Observable<string> {
    return this.http.get(`${this.actuatorUrl}/prometheus`, { responseType: 'text' })
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    console.error('Erro ao consultar observability:', error);
    return throwError(() => new Error('Não foi possível obter dados de observabilidade.'));
  }

}