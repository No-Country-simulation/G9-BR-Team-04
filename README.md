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
 Modelo de Machine Learning
            (Python)
                │
                ▼
 Processamento do Conteúdo
          │              │
          ▼              ▼
 Retorno em JSON   OCI Object Storage
```
---

## 🤖 Ciência de Dados

O modelo utiliza técnicas de Processamento de Linguagem Natural (NLP) para analisar conteúdos técnicos.

Etapas:

- Coleta e preparação dos dados;
- Limpeza dos textos;
- Tratamento dos dados;
- Transformação utilizando TF-IDF;
- Treinamento do modelo;
- Avaliação dos resultados;
- Disponibilização do modelo para consumo pela API REST.

## 🛠️ Tecnologias Utilizadas

### Ciência de Dados

- Python
- Pandas
- Scikit-Learn 
- TF-IDF

### Back-end

- Java
- Spring Boot

### Cloud

- Oracle Cloud Infrastructure (OCI)
- Object Storage

---

## 📋 Como Executar

1. Clone este repositório.
2. Instale as dependências do projeto.
3. Execute o modelo treinado.
4. Inicie a API REST.
5. Utilize o Postman para testar os endpoints.

---

## 📡 Como Utilizar a API

### Endpoint

POST `/conteudo`

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
  "categoria": "Backend",
  "probabilidade": 0.94,
  "palavras_chave": [
    "Java",
    "Spring Boot",
    "API REST"
  ]
}
```
## 🧪 Exemplos de Uso

### Exemplo 1

**Entrada**

Introdução ao Spring Boot

**Saída**

Categoria: Backend


### Exemplo 2

**Entrada**

Manipulação de dados utilizando Pandas

**Saída**

Categoria: Data Science


### Exemplo 3

**Entrada**

Configuração de ambientes utilizando Docker

**Saída**

Categoria: DevOps

---

## 📂 Estrutura do Projeto

```text
TechMind/
├── backend/
│   └── API REST Spring Boot
├── ciencia-dados/
│   └── Modelos e notebooks ML
├── dashboard/
│   └── Interface visual
├── dataset/
│   └── Dados utilizados
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
```
---

## 🔄 Status do Projeto

  ✅ Definição do escopo  
  ✅ Criação do dataset  
  ✅ Treinamento do modelo  
  ✅ Desenvolvimento da API  
  🔄 Integração com OCI  
  🔄 Dashboard  
  🔄 Deploy  
  ✅ Documentação inicial

🚧 Projeto em desenvolvimento contínuo.
---

## 🙏 Agradecimentos

- **Oracle Next Education (ONE) G9 BR** - Pela Oportunidade e Mentoria
- **OCI** - Pela Infraestrutura
- **Mentores e Organizadores** - Pelo Suporte e Orientação

---

## ⭐ Projeto desenvolvido para o Hackathon Oracle Next Education (ONE) G9 BR.
