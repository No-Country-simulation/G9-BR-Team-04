import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Search } from '@primeicons/angular/search';
import { Conteudo } from '../../core/models/conteudo.model';
import { Footer } from '../../layout/footer/footer';
import { Header } from '../../layout/header/header';


@Component({
  selector: 'articles-list-page',
  imports: [Header, Footer, CommonModule, CommonModule, FormsModule, Search],
  templateUrl: './articles-list.html',
})
export class ArticlesListPage implements OnInit {

  readonly API_URL = 'https://techmind-java.onrender.com/conteudo';

  conteudos: Conteudo[] = [];
  filtroAtivo = 'Todas';
  termoBusca = '';

  titulo = '';
  texto = '';

  carregando = false;
  erroCarregamento = false;
  mensagemErro = '';

  resultado: Conteudo | null = null;
  erroResultado = '';

  ngOnInit(): void {
    this.carregarDados();
  }

  get charCount(): number {
    return this.texto.length;
  }

  get categorias(): string[] {
    return ['Todas', ...new Set(this.conteudos.map((c) => c.categoria))];
  }

  get conteudosFiltrados(): Conteudo[] {
    const termo = this.termoBusca.trim().toLowerCase();

    return this.conteudos.filter((conteudo) => {
      const matchCategoria =
        this.filtroAtivo === 'Todas' ||
        conteudo.categoria === this.filtroAtivo;

      const matchBusca =
        !termo || conteudo.titulo.toLowerCase().includes(termo);

      return matchCategoria && matchBusca;
    });
  }

  async carregarDados(): Promise<void> {
    this.carregando = true;
    this.erroCarregamento = false;

    try {
      const resposta = await fetch(
        `${this.API_URL}/titulo?titulo=&size=100`,
      );

      if (!resposta.ok) {
        throw new Error(`Erro na API: ${resposta.status}`);
      }

      const dados = await resposta.json();
      const lista = dados.content || [];

      this.conteudos = lista.map((item: any) => this.mapConteudo(item));

      if (!this.categorias.includes(this.filtroAtivo)) {
        this.filtroAtivo = 'Todas';
      }
    } catch (erro) {
      console.error('Erro ao buscar dados reais da API:', erro);
      this.erroCarregamento = true;
    } finally {
      this.carregando = false;
    }
  }

  selecionarCategoria(categoria: string): void {
    this.filtroAtivo = categoria;
  }

  formatarData(data: Date | string): string {
    return new Date(data).toLocaleString('pt-BR', {
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

  private mapConteudo(item: any): Conteudo {
    return {
      id: item.id,
      titulo: item.titulo || 'Sem Título',
      texto: item.texto || '',
      categoria: item.categoria || 'Sem Categoria',
      probabilidade: item.probabilidade || 0,
      palavrasChave:
        item.informacoesAdicionais ||
        item.informacoes_adicionais ||
        [],
      criadoEm: item.criadoEm
        ? this.formatarData(item.criadoEm)
        : '-',
    };
  }

}