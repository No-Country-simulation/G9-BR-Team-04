import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Search } from '@primeicons/angular/search';
import { Conteudo } from '../../core/models/conteudo.model';
import { ConteudoService } from '../../core/services/conteudo.service';
import { Footer } from '../../layout/footer/footer';
import { Header } from '../../layout/header/header';


@Component({
  selector: 'articles-list-page',
  imports: [Header, Footer, CommonModule, FormsModule, Search],
  templateUrl: './articles-list.html',
})
export class ArticlesListPage implements OnInit {
  private conteudoService = inject(ConteudoService);

  conteudos = signal<Conteudo[]>([]);
  filtroAtivo = 'Todas';
  termoBusca = '';

  carregando = signal(false);
  erroCarregamento = signal(false);
  mensagemErro = signal('');

  conteudoSelecionado = signal<Conteudo | null>(null);

  ngOnInit(): void {
    this.carregarDados();
  }

  get categorias(): string[] {
    return ['Todas', ...new Set(this.conteudos().map((c) => c.categoria))];
  }

  get conteudosFiltrados(): Conteudo[] {
    const termo = this.termoBusca.trim().toLowerCase();

    return this.conteudos().filter((conteudo) => {
      const matchCategoria =
        this.filtroAtivo === 'Todas' ||
        conteudo.categoria === this.filtroAtivo;

      const matchBusca =
        !termo || conteudo.titulo.toLowerCase().includes(termo);

      return matchCategoria && matchBusca;
    });
  }

  carregarDados(): void {
    this.carregando.set(true);
    this.erroCarregamento.set(false);
    this.mensagemErro.set('');

    this.conteudoService.buscarPorTitulo('', 100).subscribe({
      next: (dados) => {
        this.conteudos.set(
          (dados.content ?? []).map((item) =>
            this.mapConteudo(item as unknown as Record<string, unknown>),
          ),
        );

        if (!this.categorias.includes(this.filtroAtivo)) {
          this.filtroAtivo = 'Todas';
        }

        this.carregando.set(false);
      },
      error: (erro: Error) => {
        console.error('Erro ao buscar dados da API:', erro);
        this.erroCarregamento.set(true);
        this.mensagemErro.set(erro.message);
        this.carregando.set(false);
      },
    });
  }

  selecionarCategoria(categoria: string): void {
    this.filtroAtivo = categoria;
  }

  abrirDetalhes(conteudo: Conteudo): void {
    this.conteudoSelecionado.set(conteudo);
  }

  fecharDetalhes(): void {
    this.conteudoSelecionado.set(null);
  }

  formatarData(data: Date | string): string {
    const temFusoExplicito = /[Zz]|[+-]\d{2}:\d{2}$/.test(String(data));
    const valor =
      typeof data === 'string' && !temFusoExplicito ? `${data}Z` : data;

    return new Date(valor).toLocaleString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  }

  porcentagem(valor: number): number {
    return Math.round(valor * 100);
  }

  preview(texto: string): string {
    return texto.length > 140 ? `${texto.slice(0, 140)}...` : texto;
  }

  private mapConteudo(item: Record<string, unknown>): Conteudo {
    return {
      id: item['id'] as number,
      titulo: (item['titulo'] as string) || 'Sem Título',
      texto: (item['texto'] as string) || '',
      categoria: (item['categoria'] as string) || 'Sem Categoria',
      probabilidade: (item['probabilidade'] as number) || 0,
      palavrasChave:
        (item['palavrasChave'] as string[] | undefined) ||
        (item['informacoesAdicionais'] as string[] | undefined) ||
        (item['informacoes_adicionais'] as string[] | undefined) ||
        [],
      criadoEm: item['criadoEm']
        ? this.formatarData(item['criadoEm'] as string)
        : '-',
    };
  }
}