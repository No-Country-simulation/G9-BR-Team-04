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

O modelo utiliza técnicas de **Processamento de Linguagem Natural (NLP)** para analisar e classificar conteúdos técnicos.

### Fluxo de processamento

- Coleta e preparação dos dados;
- Limpeza e tratamento dos textos;
- Vetorização utilizando **TF-IDF**;
- Treinamento e avaliação do modelo (**SGDClassifier**);
- Disponibilização do modelo por meio de uma API desenvolvida com **FastAPI**.

---

## 🌐 API de Ciência de Dados

A API de Ciência de Dados foi desenvolvida em **Python** utilizando **FastAPI** para disponibilizar o modelo de Machine Learning ao Back-end da aplicação.

Ela recebe um conteúdo técnico, realiza a classificação utilizando o modelo treinado e retorna:

- Categoria prevista;
- Probabilidade da classificação;
- Informações adicionais (palavras-chave relevantes).

Além disso, a API disponibiliza:

- Documentação interativa via **Swagger** (`/docs`);
- Endpoint de monitoramento (`/health`).

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

## 📋 Como Executar

1. Clone este repositório.
2. Instale as dependências do projeto.
3. Inicie a API de Ciência de Dados (FastAPI).
4. Inicie a API REST (Spring Boot).
5. Importe a collection do Postman e teste os endpoints da API ou acesse a documentação interativa em `/docs`.

---

## 📡 Como Utilizar a API

### Endpoint

`POST /api/v1/classificar`

### Exemplo de Requisição

```json
{
  "titulo": "Introdução ao Spring Boot",
  "texto": "Neste conteúdo são apresentados os conceitos básicos para criação de APIs REST utilizando Java e Spring Boot."
}
```

### Exemplo de Resposta

```json
{
  "categoria": "Tecnologia",
  "probabilidade": 0.61,
  "informacoes_adicionais": [
    "java",
    "apis",
    "introdução",
    "utilizando",
    "básicos"
  ]
}
```
### Endpoints disponíveis

| Método | Endpoint | Descrição |
|---------|----------|-----------|
| POST | `/api/v1/classificar` | Classifica conteúdos técnicos. |
| GET | `/health` | Verifica se a API está ativa. |
| GET | `/docs` | Documentação interativa (Swagger). |
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
