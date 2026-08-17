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

## ❗ O problema

Profissionais e estudantes de tecnologia consomem diariamente grandes volumes de documentação, artigos, cursos, tutoriais e anotações. Com o tempo, organizar e recuperar essas informações se torna um desafio.

O **TechMind** foi desenvolvido para reduzir esse esforço, utilizando Inteligência Artificial e Ciência de Dados para automatizar a organização do conhecimento técnico.

---

## 💡 A solução

A plataforma recebe conteúdos técnicos e utiliza um modelo de Machine Learning para processá-los e retornar informações estruturadas, como:

classificação temática;
nível de confiança da classificação;
palavras-chave;
conteúdos relacionados;
consulta por categorias;
processamento individual ou em lote.

Os resultados são disponibilizados em **JSON por meio de uma API REST**, permitindo integração com outras aplicações.
  
---

## 🎯 Objetivo

Desenvolver um **MVP funcional para organização inteligente de conteúdos técnicos**, utilizando Ciência de Dados e Machine Learning para automatizar sua classificação e enriquecimento, facilitando a consulta, descoberta e reutilização do conhecimento por estudantes, profissionais e equipes.

---

  ## ✨ Funcionalidades
  
**MVP**

✅ Processamento de conteúdos técnicos

✅ Classificação automática por Machine Learning

✅ Retorno estruturado em JSON

✅ API REST

✅ Validação de entrada e tratamento de erros

✅ Persistência de dados

✅ Integração com OCI

**Funcionalidades Adicionais**

🔎 Consulta por título e categoria

🔗 Recomendação de conteúdos relacionados

📄 Processamento em lote via CSV

⚡ Cache com Redis

🔐 Deduplicação de conteúdo com SHA-256

🛡️ Resiliência com Circuit Breaker, Retry e Bulkhead

🧪 Testes automatizados

📊 Observabilidade e métricas

📈 Testes de carga com k6

---

## 🏗️ Arquitetura e Ttecnologias

```text
Camada	Tecnologias
Machine Learning	Python / Ciência de Dados
Backend	Java 21 / Spring Boot
API	REST / JSON
Persistência	Oracle Database
Cloud	Oracle Cloud Infrastructure — OCI
Cache	Redis
Resiliência	Resilience4j
Observabilidade	Actuator / Prometheus / OpenTelemetry
Testes	JUnit / k6
Arquitetura	Spring Modulith
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
