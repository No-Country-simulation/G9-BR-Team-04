# 🧠 TechMind
![Hackathon](https://img.shields.io/badge/Hackathon-ONE%20G9-7B2CBF)
![Squad Solutions](https://img.shields.io/badge/Squad%20Solutions-Esquadr%C3%A3o%20de%20Solu%C3%A7%C3%B5es-6C3EB8?style=flat)

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

## 🎬 TechMind em funcionamento

Confira a demonstração do **TechMind funcionando de ponta a ponta**, desde o cadastro de um conteúdo técnico até sua classificação automática, identificação do nível de confiança, extração de palavras-chave e organização na base de conhecimento.

[![TechMind - Demonstração Oficial](assets/techmind-video-capa.jpg)](https://youtu.be/ts-b0Md-Qoo)

<p align="center">
  <strong>▶️ Clique na imagem para assistir à demonstração do TechMind</strong>
</p>

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

✅ Interface web para cadastro e consulta

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
| **Ciência de Dados / ML** | Python, TF-IDF, modelo de classificação |
| **API de ML** | Python |
| **Back-End** | Java 21, Spring Boot 4.1 |
| **Arquitetura** | Spring Modulith |
| **API** | REST / JSON |
| **Front-End** |	Angular | TypeScript| Tailwind CSS| PrimeNG|
| **Persistência** | Oracle Database (executado localmente, via Docker)|
| **Cloud** | Oracle Cloud Infrastructure (OCI) — Object Storage (armazenamento dos artefatos do modelo)|
| **Cache** | Redis |
| **Resiliência** | Resilience4j — Circuit Breaker, Retry e Bulkhead |
| **Observabilidade** | Spring Boot Actuator, Prometheus e OpenTelemetry |
| **Testes** | JUnit e k6 |
| **Build / Dependências** | Maven |

---

## 🔄 Arquitetura Funcional — Fluxo da Solução

O TechMind utiliza uma arquitetura desacoplada, separando a aplicação Back-End do serviço responsável pela inferência de Machine Learning.

Os artefatos do modelo treinado são armazenados no **OCI Object Storage** e carregados pela **API Python durante sua inicialização**.

```text
                     OCI Object Storage
                            │
              ┌─────────────┴─────────────┐
              │  modelo.pkl               │
              │  vectorizer_.pkl          │
              │  tfidf_keywords.pkl       │
              └─────────────┬─────────────┘
                            │
                     download no startup
                            ↓
                      API Python / ML
                            │
                            ↓
Conteúdo Técnico → API REST Spring Boot
                            │
                            ↓
                    Serviço de ML
                            │
                            ↓
          Categoria + Confiança + Palavras-chave
                            │
                            ↓
                  Oracle Database / Redis
                            │
                            ↓
             Consulta e Reutilização
```

---

## 🧠 Ciência de Dados

O modelo de Machine Learning é responsável por analisar o conteúdo técnico e gerar sua classificação.

O pipeline contempla etapas de preparação e tratamento dos textos, transformação dos dados, treinamento, avaliação e disponibilização do modelo para consumo pela aplicação.

O dataset utilizado no treinamento possui 2.560 textos distribuídos em 13 categorias, com um vocabulário TF-IDF de 23.860 termos. O modelo escolhido para produção foi um SGDClassifier combinado com CalibratedClassifierCV, alcançando 82,81% de acurácia no conjunto de teste.

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

O **OCI Object Storage** é utilizado para armazenar os três artefatos gerados durante o treinamento do modelo de Machine Learning:

- `modelo.pkl` — classificador treinado;
- `vectorizer_.pkl` — vetorizador TF-IDF utilizado na classificação;
- `tfidf_keywords.pkl` — vetorizador TF-IDF utilizado na extração de palavras-chave.

Na inicialização, a **API Python realiza o download desses artefatos diretamente do OCI Object Storage** e os carrega para execução das inferências.

O banco de dados Oracle utilizado pela aplicação roda localmente, via Docker. Neste momento, a integração com a nuvem está concentrada no armazenamento dos artefatos do modelo de ML; o deploy da aplicação como um todo na OCI ainda não foi realizado e faz parte das próximas evoluções do projeto.

Essa arquitetura desacopla os artefatos de Machine Learning da aplicação, facilitando seu armazenamento, distribuição e atualização.
--- 

## 🧪 Exemplos de Uso

Exemplos de Uso

1. Classificação de conteúdo técnico

Um estudante salva um material sobre desenvolvimento de APIs com Java e Spring Boot. O TechMind processa o texto e identifica automaticamente sua categoria e informações relevantes.

Entrada:

```text
{
  "titulo": "Introdução ao Spring Boot",
  "texto": "Neste conteúdo são apresentados conceitos para criação de APIs REST utilizando Java e Spring Boot."
}
```
Resultado esperado:

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

2. Consulta de conteúdos organizados

Após o processamento, estudantes ou profissionais podem localizar conteúdos armazenados por título ou categoria, facilitando a recuperação de materiais relacionados a determinado assunto.

Exemplo:

```text
GET /conteudo/categoria/Backend
```

O sistema retorna os conteúdos classificados naquela categoria, permitindo reutilizar o conhecimento já organizado.

3. Processamento de conteúdos em lote

Uma plataforma educacional ou equipe técnica pode importar vários conteúdos de uma única vez por meio de um arquivo CSV.

POST /conteudo/lote
```
Content-Type: multipart/form-data

```
O TechMind processa os registros, identifica conteúdos já existentes e organiza os novos materiais automaticamente.

> Resultado: Menos catalogação manual e uma base de conhecimento mais estruturada, pesquisável e reutilizável.
 
---

## 📂 Estrutura do Projeto

```text
G9-BR-Team-04/
├── backend/       # API REST - Java / Spring Boot
├── api-python/    # API de Machine Learning - Python / FastAPI
├── frontend/      # Aplicação web - Angular
├── .gitignore
└── README.md
```
---
## 👥 Equipe

## 👥 G9-BR-Team-04 – SolutionSquad (Esquadrão das Soluções)

| Integrante                        | Função                                          | GitHub | LinkedIn |
| --------------------------------- | ----------------------------------------------- | ------ | -------- |
| **Arthur Carvalho Ferreira**      | 💻 Back End Developer                           | [GitHub](https://github.com/ArthurFerreira13) | [LinkedIn](https://www.linkedin.com/in/arthur-fernando-carvalho-ferreira-96542772/) |
| **Carlos Caique Borges de Souza** | 💻 Back End Developer                           |  [GitHub](https://github.com/devcaiqueborges) | [LinkedIn](https://www.linkedin.com/in/devcaiqueborges) |
| **Gabriel Leal**                  | ☁️ DevOps Engineer                              | [GitHub](https://github.com/Gabriel-Lincoln-Leal) | [LinkedIn](https://www.linkedin.com/in/gabriellincolnleal) |
| **Jaqueline Silva Broccolo**      | 🔗 Full Stack Developer                         | [GitHub](https://github.com/jlinebsilva ) | [LinkedIn](https://www.linkedin.com/in/jaqueline-silva-broccolo/) |
| **Marcus Corrêa Lopes Guedes**    | 📌 Project Manager / Front End Developer        | [GitHub](https://github.com/MCLG1661) | [LinkedIn](https://www.linkedin.com/in/marcusguedes/) |
| **Rayssa Santos**                 | 🤖 Data Scientist                               | [GitHub](https://github.com/rayssasnt) | [LinkedIn](https://www.linkedin.com/in/rayssasnt) |
| **Simone Silva**                  | 💻 Back End Developer / 📚 Documentation        | [GitHub](https://github.com/Simoneerp ) | [LinkedIn](https://www.linkedin.com/in/simone-fsilva/) |


---
### Implementado

* ✅ Dataset e preparação dos dados
* ✅ Treinamento e avaliação do modelo
* ✅ API de Ciência de Dados
* ✅ API REST Java
* ✅ Classificação automática
* ✅ Extração de palavras-chave
* ✅ Persistência no Oracle
* ✅ Cadastro individual
* ✅ Cadastro em lote via CSV
* ✅ Busca e filtro de conteúdos
* ✅ Deduplicação por SHA-256
* ✅ Circuit Breaker, Retry e Bulkhead
* ✅ Interface Angular
* ✅ Teste ponta a ponta
* ✅ Documentação
* ✅ Integração com OCI Object Storage

### Próximas evoluções

* 🔄 Login e autenticação no front-end
* 🔄 Dashboard de visualização
* 🔄 Deploy da aplicação na OCI
* 🔄 Configuração para deploy em domínios separados
* 🔄 Autenticação entre Back-end e API de Ciência de Dados
* 🔄 Busca semântica
* 🔄 Sistema de recomendação
---

## 🙏 Agradecimentos

- **Oracle Next Education (ONE) G9 BR** - Pela Oportunidade e Mentoria
- **OCI** - Pela Infraestrutura
- **Mentores e Organizadores** - Pelo Suporte e Orientação

---

## ⭐ Projeto Desenvolvido para o Hackathon Oracle Next Education (ONE) G9 BR.
