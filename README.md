# 🚀 TechMind

Projeto do G9 BR Team 04 (SolutionSquad/Esquadrão das Soluções)

Hackathon ONE G9 BR - Alura + Oracle

[![Hackathon ONE G9 BR](https://img.shields.io/badge/Hackathon-ONE_G9_BR-orange?style=for-the-badge&logo=oracle)](https://www.oracle.com/br/education/next-education/)
![Team](https://img.shields.io/badge/Team-SolutionSquad_/_Esquadrão_das_Soluções-6C2BD9?style=for-the-badge)

---

## 📌 Sobre o Projeto

O TechMind é uma solução inteligente para organizar, classificar e enriquecer conteúdos técnicos utilizando técnicas de Ciência de Dados e integração com o Oracle Cloud Infrastructure (OCI) para armazenamento de dados e arquivos.

A plataforma auxilia estudantes e profissionais de tecnologia a transformar grandes volumes de informações em conhecimento estruturado e reutilizável.

![Python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=python&logoColor=FFD43B)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Oracle Cloud Infrastructure](https://img.shields.io/badge/Oracle_Cloud_Infrastructure-F80000?style=for-the-badge&logo=oracle&logoColor=white)
---

## ❗ Problema

Estudantes e profissionais da área de tecnologia consomem diariamente diversos conteúdos como:

- Documentações
- Cursos
- Artigos
- Tutoriais
- Anotações técnicas
  

Com o grande volume de informações, torna-se difícil organizar, encontrar e reutilizar esses conhecimentos.

O TechMind busca solucionar esse desafio automatizando a organização e classificação desses conteúdos.

---

## 💡 Solução Proposta

A solução recebe textos técnicos e utiliza técnicas de Machine Learning para analisar o conteúdo e retornar informações estruturadas.

O sistema realiza:

- Classificação automática de conteúdos;
- Extração de palavras-chave;
- Identificação de conteúdos relacionados;
- Organização inteligente da base de conhecimento.
  
---
## 🎯 Objetivo

Receber um conteúdo técnico, processá-lo utilizando um modelo de Machine Learning e retornar informações organizadas, como:

- Categoria
- Palavras-chave
- Conteúdos relacionados em formato JSON.

---

  ## ✨ Funcionalidades

✅ Classificação automática de conteúdo técnico

✅ Extração de palavras-chave

✅ Recomendação de conteúdos relacionados

✅ API REST para integração

✅ Persistência utilizando OCI Object Storage

✅ Dashboard de visualização

---

## 🏗️ Arquitetura da Solução

```text

             Usuário
                │
                ▼
      API REST (Spring Boot)
                │
                ▼
 API de Ciência de Dados (FastAPI)
                │
                ▼
      Modelo de Machine Learning
      (TF-IDF + SGDClassifier)
                │
        ┌───────┴────────┐
        ▼                ▼
 Retorno em JSON   OCI Object Storage
```
---

## 🤖 Ciência de Dados

O serviço de Ciência de Dados disponibiliza um modelo de Machine Learning treinado para classificar conteúdos técnicos por categoria e extrair palavras-chave relevantes utilizando técnicas de Processamento de Linguagem Natural (NLP).

### Fluxo de processamento

- Coleta e preparação dos dados;
- Limpeza e tratamento dos textos;
- Vetorização utilizando **TF-IDF**;
- Treinamento e avaliação do modelo (**SGDClassifier**);
- Disponibilização do modelo por meio de uma API desenvolvida com **FastAPI**.

---

## 🛠️ Tecnologias Utilizadas

### Ciência de Dados

- Python
- FastAPI
- Pandas
- Scikit-Learn
- TF-IDF

### Back-end

- Java
- Spring Boot

### Front-end

- Angular v20+
- TypeScript
- Tailwind CSS
- PrimeNG
- PrimeIcons / Angular
- Chart.js + ng2 Charts

### Cloud

- Oracle Cloud Infrastructure (OCI)
- Object Storage

---

## 🌐 Documentação da API de Ciência de Dados

### Visão geral

Este serviço expõe um modelo de Machine Learning treinado para classificar
conteúdos técnicos por categoria e extrair palavras-chave relevantes. É um
serviço **interno**, consumido pela API principal do projeto (Back-End
Java), conforme a seção 3 do contrato de APIs do TechMind.

| | |
|---|---|
| **Base URL (local)** | `http://127.0.0.1:8001` |
| **Formato** | JSON (`application/json`) |
| **Autenticação** | Nenhuma (serviço interno, não exposto publicamente) |
| **Documentação interativa** | `GET /docs` (Swagger UI, gerado automaticamente) |

---

### Endpoints

### 1. `GET /health`

Verifica se o serviço está no ar e se o modelo foi carregado com sucesso.
Útil para checagens de disponibilidade (ex: liveness probe em produção).

**Requisição**
```
GET /health
```

**Resposta — 200 OK**
```json
{
  "status": "ok",
  "modelo_carregado": true
}
```

Se `modelo_carregado` vier `false`, os arquivos `vectorizer.pkl` e/ou
`modelo.pkl` não foram encontrados na pasta `models/` — o serviço sobe
normalmente, mas o endpoint de classificação retornará erro até os
arquivos serem disponibilizados.

---

### 2. `POST /api/v1/classificar`

Recebe um conteúdo técnico (título + texto) e retorna a categoria prevista,
a probabilidade dessa previsão e uma lista de palavras-chave relevantes.

**Requisição**
```
POST /api/v1/classificar
Content-Type: application/json
```

| Campo | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| `titulo` | string | Sim | Título do conteúdo técnico (não pode ser vazio) |
| `texto` | string | Sim | Corpo do conteúdo técnico (não pode ser vazio) |

**Resposta — 200 OK**

| Campo | Tipo | Descrição |
|---|---|---|
| `categoria` | string | Categoria prevista pelo modelo |
| `probabilidade` | float (0 a 1) | Confiança do modelo na categoria prevista |
| `informacoes_adicionais` | array de strings | Até 5 palavras-chave extraídas do texto (termos com maior peso TF-IDF) |

**Respostas de erro**

| Código | Quando ocorre |
|---|---|
| `422 Unprocessable Entity` | `titulo` ou `texto` ausentes/vazios (validação automática) |
| `503 Service Unavailable` | Modelo não carregado (arquivos `.pkl` ausentes) |
| `500 Internal Server Error` | Erro inesperado ao processar a requisição |

---

### Exemplos de uso

### Exemplo 1 — Conteúdo de Backend

**Requisição**
```bash
curl -X POST http://127.0.0.1:8001/api/v1/classificar \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Introdução ao Spring Boot",
    "texto": "Neste conteúdo são apresentados os conceitos básicos para criação de APIs REST utilizando Java e Spring Boot."
  }'
```

**Resposta**
```json
{
  "categoria": "Tecnologia",
  "probabilidade": 0.6129,
  "informacoes_adicionais": ["java", "apis", "introdução", "utilizando", "básicos"]
}
```

### Exemplo 2 — Conteúdo de Machine Learning

**Requisição**
```bash
curl -X POST http://127.0.0.1:8001/api/v1/classificar \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "O que é Machine Learning",
    "texto": "Machine Learning é uma área da inteligência artificial que permite que sistemas aprendam padrões a partir de dados, sem programação explícita para cada tarefa."
  }'
```

**Resposta (formato)**
```json
{
  "categoria": "Ciência",
  "probabilidade": 0.78,
  "informacoes_adicionais": ["machine", "learning", "dados", "aprendam", "sistemas"]
}
```

### Exemplo 3 — Campo obrigatório ausente (erro de validação)

**Requisição**
```bash
curl -X POST http://127.0.0.1:8001/api/v1/classificar \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "",
    "texto": "Texto de exemplo"
  }'
```

**Resposta — 422 Unprocessable Entity**
```json
{
  "detail": [
    {
      "type": "string_too_short",
      "loc": ["body", "titulo"],
      "msg": "String should have at least 1 character"
    }
  ]
}
```

---

### Como o resultado é gerado

1. **Classificação (`categoria` e `probabilidade`):** `titulo` e `texto` são
   concatenados (`"titulo texto"`) e transformados em vetor TF-IDF usando o
   `vectorizer.pkl`. O vetor é então passado para o modelo
   (`SGDClassifier` calibrado com `CalibratedClassifierCV`), que retorna a
   categoria de maior probabilidade.
2. **Palavras-chave (`informacoes_adicionais`):** calculadas separadamente,
   pegando os termos com maior peso TF-IDF dentro do próprio texto recebido
   (usando o vocabulário já aprendido pelo `vectorizer`).

---

### Integração com o Back-End

O `ConteudoService` (Java) deve chamar este endpoint internamente ao
processar um novo conteúdo (`POST /conteudo` no Front-End), usando a
resposta para preencher `categoria`, `probabilidade` e
`informacoes_adicionais` no `ClassificacaoResponse` devolvido ao Front-End.

Em produção, a URL base deste serviço deve ser configurável (variável de
ambiente), já que mudará conforme o ambiente de deploy (ex: OCI Compute).

---

### Versionamento do modelo

Os artefatos `vectorizer.pkl` e `modelo.pkl` foram gerados no notebook do
Colab com **scikit-learn 1.6.1**. Ao atualizar o modelo (novo treinamento),
lembre-se de:
- Substituir os dois arquivos em `models/`
- Confirmar que a versão do `scikit-learn` no `requirements.txt` deste
  serviço é compatível com a usada no treino, para evitar
  `InconsistentVersionWarning` ou erros de deserialização

---

## 📋 Como Executar

1. Clone o repositório.
2. Instale as dependências do Back-end e da API de Ciência de Dados.
3. Inicie a API de Ciência de Dados (FastAPI).
4. Inicie a API REST (Spring Boot).
5. Acesse a documentação da API em `/docs` ou utilize a collection do Postman para testar os endpoints.


```
## 🧪 Exemplos de Uso

| 📄 Conteúdo Técnico | 🏷️ Categoria |
|----------------------|--------------|
| Introdução ao Spring Boot | 💻 **Backend** |
| Manipulação de dados utilizando Pandas | 📊 **Data Science** |
| Configuração de ambientes utilizando Docker | ☁️ **DevOps** |
```
---

## 📂 Estrutura do Projeto

```text
TechMind/
├── backend/
│   └── API REST (Spring Boot)
├── ciencia-dados/
│   ├── API FastAPI
│   ├── Modelos
│   └── Notebooks
├── dashboard/
│   └── Interface visual
├── dataset/
│   └── Dados utilizados
├── postman/
│   └── Collection da API
└── README.md
```
---
## 👥 Equipe

**G9-BR-Team-04 – SolutionSquad (Esquadrão das Soluções)**

| Integrante | Função |
|------------|--------|
| **Arthur Carvalho Ferreira** | 💻 Back End Developer |
| **Carlos Caique Borges de Souza** | 💻 Back End Developer |
| **Gabriel Leal** | ☁️ DevOps Engineer |
| **Jaqueline Silva Broccolo** | 🔗 Full Stack Developer |
| **Lucas Aoki** | 📊 Data Analyst |
| **Marcus Corrêa Lopes Guedes** | 📌 Project Manager / Front End Developer |
| **Rayssa Santos** | 🤖 Data Scientist |
| **Simone Silva** | 💻 Back End Developer / 📚 Documentation & Demo |

---

## 🔄 Status do Projeto

- ✅ Definição do escopo
- ✅ Criação do dataset
- ✅ Treinamento do modelo
- ✅ Desenvolvimento da API
- ✅ API de Ciência de Dados (FastAPI)  
- 🔄 Integração com OCI
- 🔄 Dashboard
- 🔄 Deploy
- ✅ Documentação inicial

🚧 Projeto em desenvolvimento contínuo.
---

## 🙏 Agradecimentos

- **Oracle Next Education (ONE) G9 BR** - Pela Oportunidade e Mentoria
- **OCI** - Pela Infraestrutura
- **Mentores e Organizadores** - Pelo Suporte e Orientação

---

## ⭐ Projeto desenvolvido para o Hackathon Oracle Next Education (ONE) G9 BR.
