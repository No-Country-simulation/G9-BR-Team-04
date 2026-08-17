# 🧠 TechMind
![Hackathon](https://img.shields.io/badge/Hackathon-ONE%20G9-7B2CBF)

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white)
![Python](https://img.shields.io/badge/Python-Machine%20Learning-3776AB?logo=python&logoColor=white)
![Oracle](https://img.shields.io/badge/Oracle-Database-F80000?logo=oracle&logoColor=white)
![OCI](https://img.shields.io/badge/Oracle%20Cloud-OCI-F80000?logo=oracle&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-Cache-DC382D?logo=redis&logoColor=white)
![REST API](https://img.shields.io/badge/API-REST-009688)
![Tests](https://img.shields.io/badge/Tests-Automated-2EA44F)

*Organização inteligente de conteúdos técnicos com Ciência de Dados, Machine Learning e Cloud.*

Projeto desenvolvido pelo **G9-BR-Team-04** para o **Hackathon ONE G9 | Oracle + Alura**, com o objetivo de transformar conteúdos técnicos dispersos em conhecimento estruturado, pesquisável e reutilizável.

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

| 📄 Conteúdo Técnico | 🏷️ Categoria |
|----------------------|--------------|
| Introdução ao Spring Boot | 💻 **Backend** |
| Manipulação de dados utilizando Pandas | 📊 **Data Science** |
| Configuração de ambientes utilizando Docker | ☁️ **DevOps** |

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
| **Arthur Carvalho Ferreira** | 💻 Tech Lead & Back End Developer |
| **Carlos Caique Borges de Souza** | 💻 Back End Developer |
| **Gabriel Leal** | ☁️ DevOps Engineer |
| **Jaqueline Silva Broccolo** | 🔗 Full Stack Developer |
| **Lucas Aoki** | 📊 Data Analyst |
| **Marcus Corrêa Lopes Guedes** | 📌 Project Manager & Front End Developer & Demo |
| **Rayssa Santos** | 🤖 Data Scientist |
| **Simone Silva** | 💻 Back End Developer & 📚 Documentation & Demo |

---

## 🔄 Status do Projeto

- ✅ Definição do escopo
- ✅ Criação do dataset
- ✅ Treinamento do modelo
- ✅ Desenvolvimento da API
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
