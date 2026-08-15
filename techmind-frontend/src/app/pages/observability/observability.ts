import { Component, inject, OnInit, signal } from '@angular/core';
import { HealthStatus, ObservabilityService } from '../../core/services/observability.service';
import { Footer } from '../../layout/footer/footer';
import { Header } from '../../layout/header/header';
import { Card } from '../../shared/card/card';


@Component({
  selector: 'observability-page',
  imports: [Header, Card, Footer],
  standalone: true,
  templateUrl: './observability.html',
})
export class ObservabilityPage implements OnInit {
  private service = inject(ObservabilityService);

  health = signal<HealthStatus | null>(null);
  metricsNames = signal<string[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);

  ngOnInit() {
    this.carregar();
  }

  carregar() {
    this.loading.set(true);
    this.error.set(null);

    this.service.getHealth().subscribe({
      next: (h) => this.health.set(h),
      error: (err) => this.error.set(err.message)
    });

    this.service.getMetricsNames().subscribe({
      next: (m) => {
        this.metricsNames.set(m.names ?? []);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.message);
        this.loading.set(false);
      }
    });
  }
}