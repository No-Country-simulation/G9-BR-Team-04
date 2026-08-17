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

## 🏗️ Arquitetura e Tecnologias

| Camada | Tecnologia / Ferramenta |
|---|---|
| **Ciência de Dados / ML** | Python |
| **Back-End** | Java 21, Spring Boot |
| **Arquitetura** | Spring Modulith |
| **API** | REST / JSON |
| **Persistência** | Oracle Database |
| **Cloud** | Oracle Cloud Infrastructure (OCI) — Object Storage |
| **Cache** | Redis |
| **Resiliência** | Resilience4j — Circuit Breaker, Retry e Bulkhead |
| **Observabilidade** | Spring Boot Actuator, Prometheus e OpenTelemetry |
| **Testes** | JUnit e k6 |
| **Build / Dependências** | Maven |

---

## 🔄 Arquitetura Funcional — Fluxo da Solução

O TechMind utiliza uma arquitetura desacoplada, conectando a aplicação ao serviço de Machine Learning responsável pelo processamento e classificação dos conteúdos técnicos.

```text
Conteúdo Técnico
       ↓
Frontend / Aplicação
       ↓
API REST — Spring Boot
       ↓
Serviço de Machine Learning
       ↓
Classificação + Confiança + Palavras-chave
       ↓
Oracle Database / Redis
       ↓
Consulta e Reutilização do Conhecimento

       ↕
OCI Object Storage
```

---

## 🧠 Ciência de Dados

O modelo de Machine Learning é responsável por analisar o conteúdo técnico e gerar sua classificação.

O pipeline contempla etapas de preparação e tratamento dos textos, transformação dos dados, treinamento, avaliação e disponibilização do modelo para consumo pela aplicação.

O serviço de classificação é desacoplado do backend Java e disponibilizado por API, facilitando sua evolução e integração com outros sistemas.

---

## 📋 Como Executar

1. Clone este repositório.
2. Instale as dependências do projeto.
3. Execute o modelo treinado.
4. Inicie a API REST.
5. Utilize o Postman para testar os endpoints.

---

## 📡 Exemplo de Utilização da API

**Requisição**

POST /conteudo

```text
Content-Type: application/json
{
  "titulo": "Introdução ao Spring Boot",
  "texto": "Neste conteúdo são apresentados conceitos para criação de APIs REST utilizando Java e Spring Boot."
}
```

**Resposta**

```text
{
  "categoria": "Backend",
  "probabilidade": 0.89,
  "informacoes_adicionais": [
    "Java",
    "Spring Boot",
    "API REST"
  ]
}
```

> **A estrutura da resposta pode variar conforme o processamento realizado pelo modelo.**

---

## 📦 Processamento em Lote

A plataforma também permite processar múltiplos conteúdos por meio de arquivos CSV:

POST /conteudo/lote

```text
Content-Type: multipart/form-data
```

**Esse recurso permite ampliar o uso da solução para bases maiores de conhecimento.**

---

## ▶️ Como Executar

Pré-requisitos

- Java 21
- Maven
- Python
- Oracle Database
- Redis
- Git

Clone o projeto :

```text
git clone https://github.com/No-Country-simulation/G9-BR-Team-04.git
cd G9-BR-Team-04
```
Configure as variáveis de ambiente necessárias para banco de dados e serviços externos.

Execute o backend :

```text
./mvnw spring-boot:run
```

> **Para funcionamento completo da aplicação, o serviço de Machine Learning e as dependências utilizadas pela arquitetura também devem estar disponíveis.**

---

## ☁️ Oracle Cloud Infrastructure

A Oracle Cloud Infrastructure (OCI) faz parte da arquitetura do TechMind por meio do OCI Object Storage, utilizado para armazenamento de artefatos e recursos do projeto.

Essa integração permite manter os arquivos de forma centralizada e disponível na infraestrutura Cloud, atendendo ao requisito obrigatório de utilização de um serviço OCI no Hackathon.

> OCI utilizada: Oracle Cloud Infrastructure — Object Storage

--- 

## 🧪 Exemplos de Uso

Exemplos de Uso

1. Classificação de conteúdo técnico

Um estudante salva um material sobre desenvolvimento de APIs com Java e Spring Boot. O TechMind processa o texto e identifica automaticamente sua categoria e informações relevantes.

Entrada:

{
  "titulo": "Introdução ao Spring Boot",
  "texto": "Neste conteúdo são apresentados conceitos para criação de APIs REST utilizando Java e Spring Boot."
}

Resultado esperado:

{
  "categoria": "Backend",
  "probabilidade": 0.89,
  "informacoes_adicionais": [
    "Java",
    "Spring Boot",
    "API REST"
  ]
}

2. Consulta de conteúdos organizados

Após o processamento, estudantes ou profissionais podem localizar conteúdos armazenados por título ou categoria, facilitando a recuperação de materiais relacionados a determinado assunto.

Exemplo:

GET /conteudo/categoria/Backend

O sistema retorna os conteúdos classificados naquela categoria, permitindo reutilizar o conhecimento já organizado.

3. Processamento de conteúdos em lote

Uma plataforma educacional ou equipe técnica pode importar vários conteúdos de uma única vez por meio de um arquivo CSV.

POST /conteudo/lote
Content-Type: multipart/form-data

O TechMind processa os registros, identifica conteúdos já existentes e organiza os novos materiais automaticamente.

> Resultado: Menos catalogação manual e uma base de conhecimento mais estruturada, pesquisável e reutilizável.
 
---

## 📂 Estrutura do Projeto

```text
G9-BR-Team-04/
├── src/
│   ├── main/
│   │   ├── java/          # API e regras de negócio
│   │   └── resources/     # Configurações
│   └── test/              # Testes automatizados
├── grafana-load-tests/    # Testes de carga com k6
├── pom.xml                # Dependências Maven
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

## ⭐ Projeto Desenvolvido para o Hackathon Oracle Next Education (ONE) G9 BR.
