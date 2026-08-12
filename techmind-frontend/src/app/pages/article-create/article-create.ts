import { AfterViewInit, Component, inject, signal, TemplateRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ConteudoService } from '../../core/services/conteudo.service';
import { Header } from '../../layout/header/header';
import { Tabs, type TabItem } from '../../shared/tabs/tabs';

@Component({
    selector: 'article-create-page',
    imports: [Header, FormsModule, Tabs],
    templateUrl: './article-create.html',
})
export class ArticleCreatePage implements AfterViewInit {
    tabs: TabItem[] = []

    @ViewChild('dadosTpl', { read: TemplateRef }) dadosTpl!: TemplateRef<unknown>
    @ViewChild('csvTpl', { read: TemplateRef }) csvTpl!: TemplateRef<unknown>

    private conteudoService = inject(ConteudoService)
    private router = inject(Router)

    titulo = ''
    texto = ''

    saving = signal(false)
    errorMsg = signal<string | null>(null)

    // CSV upload state
    selectedFile: File | null = null
    dragOver = signal(false)

    ngAfterViewInit(): void {
        this.tabs = [
            { id: 'dados', label: 'Dados do artigo', icon: 'pi pi-file', template: this.dadosTpl },
            { id: 'visual', label: 'Enviar CSV', icon: 'pi pi-upload', template: this.csvTpl },
        ]
    }

    onSubmit() {
        if (!this.titulo.trim() || !this.texto.trim()) {
            this.errorMsg.set('Preencha título e conteúdo')
            return
        }

        this.saving.set(true)
        this.errorMsg.set(null)

        this.conteudoService.criar({ titulo: this.titulo, texto: this.texto }).subscribe({
            next: () => {
                this.saving.set(false)
                this.router.navigate(['/'])
            },
            error: (err: Error) => {
                this.saving.set(false)
                this.errorMsg.set(err.message)
            }
        })
    }

    onFileSelected(event: Event) {
        const input = event.target as HTMLInputElement
        if (input.files && input.files[0]) {
            this.selectedFile = input.files[0]
        }
    }

    onDragOver(ev: DragEvent) {
        ev.preventDefault()
        this.dragOver.set(true)
    }

    onDragLeave(ev: DragEvent) {
        ev.preventDefault()
        this.dragOver.set(false)
    }

    onDrop(ev: DragEvent) {
        ev.preventDefault()
        this.dragOver.set(false)
        const file = ev.dataTransfer?.files?.[0]
        if (file) this.selectedFile = file
    }

    uploadCsv() {
        if (!this.selectedFile) {
            this.errorMsg.set('Selecione um arquivo CSV')
            return
        }

        this.saving.set(true)
        this.errorMsg.set(null)

        this.conteudoService.enviarLote(this.selectedFile).subscribe({
            next: () => {
                this.saving.set(false)
                this.selectedFile = null
            },
            error: (err: Error) => {
                this.saving.set(false)
                this.errorMsg.set(err.message)
            }
        })
    }
}