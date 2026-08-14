# 🚀 TechMind

Projeto do **G9 BR Team 04 – SolutionSquad (Esquadrão das Soluções)**

**Hackathon ONE G9 BR – Alura + Oracle**

---

## 📌 Sobre o Projeto

O **TechMind** é uma solução inteligente para organizar, classificar e enriquecer conteúdos técnicos utilizando técnicas de **Ciência de Dados**, uma **API REST desenvolvida em Java e Spring Boot** e integração com a **Oracle Cloud Infrastructure (OCI)**.

A plataforma transforma conteúdos técnicos em informações estruturadas, facilitando sua organização, consulta e reutilização.

---

## ❗ Problema

Estudantes e profissionais de tecnologia consomem diariamente uma grande quantidade de:

* Documentações;
* Cursos;
* Artigos;
* Tutoriais;
* Anotações;
* Conteúdos sobre diferentes tecnologias.

Com esse volume de informações, torna-se difícil organizar, encontrar e reutilizar o conhecimento.

O **TechMind** busca solucionar esse problema automatizando a análise e classificação dos conteúdos técnicos.

---

## 💡 Solução

O sistema recebe um conteúdo técnico e utiliza **Machine Learning** e **Processamento de Linguagem Natural (NLP)** para analisar suas informações.

O TechMind realiza:

* Classificação automática;
* Extração de palavras-chave;
* Identificação de informações relevantes;
* Processamento individual;
* Processamento em lote por CSV;
* Retorno estruturado em JSON;
* Integração entre Back-end e Ciência de Dados.

---

## 🎯 Objetivo

Disponibilizar uma API capaz de receber conteúdos técnicos, processá-los por meio de um modelo de Machine Learning e retornar informações estruturadas.

O resultado inclui:

* Categoria;
* Probabilidade da classificação;
* Palavras-chave;
* Informações adicionais.

---

## ✨ Funcionalidades

* ✅ Classificação automática de conteúdos
* ✅ Extração de palavras-chave
* ✅ API REST
* ✅ Processamento individual
* ✅ Processamento em lote por CSV
* ✅ Validação de dados
* ✅ Tratamento de erros
* ✅ Persistência
* ✅ Integração com Ciência de Dados
* ✅ Integração preparada para OCI
* ✅ Testes automatizados
* 🔄 Estrutura preparada para busca e recomendação

---

# 🏗️ Arquitetura

A aplicação foi organizada em camadas, separando responsabilidades entre API, regras de negócio, persistência e serviços externos.

```text
                    Usuário / Front-end
                           │
                           ▼
                 API REST - Spring Boot
                           │
                           ▼
                     Controllers
                           │
                           ▼
                       Services
                           │
              ┌────────────┼────────────┐
              │            │            │
              ▼            ▼            ▼
            DTOs      Validação    Persistência
                                       │
                                       ▼
                                Repository / JPA
              │
              ▼
      Serviço de Classificação
              │
              ▼
       API Ciência de Dados
             FastAPI
              │
              ▼
       Modelo de Machine Learning
          TF-IDF + SGDClassifier
              │
              ▼
        Resultado em JSON
```

A integração com serviços externos é isolada por meio de uma camada específica, representada pelo **OciClassifierService**.

---

# 🤖 Ciência de Dados

O serviço de Ciência de Dados disponibiliza um modelo de Machine Learning treinado para classificar conteúdos técnicos e extrair palavras-chave relevantes.

## Fluxo

```text
Dados
  ↓
Limpeza e preparação
  ↓
TF-IDF
  ↓
Treinamento
  ↓
SGDClassifier
  ↓
Avaliação
  ↓
API FastAPI
  ↓
Classificação
```

### Tecnologias

* Python
* Pandas
* Scikit-Learn
* FastAPI
* TF-IDF
* SGDClassifier
* CalibratedClassifierCV

### Processamento

O título e o texto do conteúdo são utilizados para gerar a representação TF-IDF.

O modelo **SGDClassifier**, calibrado com **CalibratedClassifierCV**, retorna a categoria prevista e sua probabilidade.

As palavras-chave são identificadas a partir dos termos com maior peso TF-IDF no texto recebido.

---

# 🔧 Back-end

O Back-end é responsável por coordenar o funcionamento da aplicação.

Foi desenvolvido em **Java com Spring Boot**, seguindo uma arquitetura REST.

### Principais responsabilidades

* Receber requisições HTTP;
* Validar os dados;
* Aplicar regras de negócio;
* Comunicar-se com a API de Ciência de Dados;
* Processar arquivos CSV;
* Persistir informações;
* Tratar exceções;
* Retornar respostas estruturadas em JSON.

---

## 🧩 Estrutura do Back-end

```text
backend/
├── controller/
├── service/
├── dto/
├── repository/
├── entity/
├── exception/
├── config/
└── integration/
```

### Controllers

Recebem as requisições HTTP e disponibilizam os endpoints.

### Services

Concentram as regras de negócio e coordenam o processamento.

### DTOs

Representam os dados de entrada e saída da API, evitando a exposição direta das entidades.

### Repositories

Realizam o acesso à camada de persistência.

### Entities

Representam os dados armazenados.

### Exceptions

Centralizam o tratamento de erros.

### Integration

Concentra a comunicação com serviços externos.

---

# 🌐 API REST

## `POST /conteudo`

Endpoint principal para processamento de um conteúdo técnico.

### Requisição

```json
{
  "titulo": "Introdução ao Spring Boot",
  "texto": "Conteúdo sobre criação de APIs REST utilizando Java e Spring Boot."
}
```

### Fluxo

```text
Requisição
    ↓
Controller
    ↓
Validação
    ↓
ConteudoService
    ↓
API de Ciência de Dados
    ↓
Classificação
    ↓
Resposta
```

### Resposta

```json
{
  "categoria": "Tecnologia",
  "probabilidade": 0.6129,
  "informacoes_adicionais": [
    "java",
    "apis",
    "spring",
    "básicos"
  ]
}
```

| Campo                    | Descrição                    |
| ------------------------ | ---------------------------- |
| `categoria`              | Categoria identificada       |
| `probabilidade`          | Confiança da classificação   |
| `informacoes_adicionais` | Palavras-chave identificadas |

---

# 📄 Processamento em Lote

## `POST /conteudo/lote`

Permite processar vários conteúdos por meio de um arquivo CSV.

```text
CSV
 ↓
Validação
 ↓
Leitura dos registros
 ↓
Processamento
 ↓
Classificação
 ↓
Resultados
```

O processamento em lote permite:

* Automatizar tarefas;
* Processar grandes volumes;
* Utilizar bases existentes;
* Reduzir trabalho manual.

---

# 🔗 Integração com Ciência de Dados

O Back-end Java realiza a comunicação interna com a API FastAPI.

O serviço envia:

```json
{
  "titulo": "Título do conteúdo",
  "texto": "Texto do conteúdo"
}
```

E recebe:

```json
{
  "categoria": "Tecnologia",
  "probabilidade": 0.6129,
  "informacoes_adicionais": [
    "java",
    "spring",
    "apis"
  ]
}
```

O resultado é utilizado pelo **ConteudoService** para construir a resposta final da API.

Essa separação permite que o consumidor utilize o Back-end sem precisar conhecer os detalhes do modelo de Machine Learning.

---

# ☁️ Integração com OCI

O projeto possui uma camada específica para integração com a **Oracle Cloud Infrastructure (OCI)**.

A integração é representada pelo:

```text
OciClassifierService
```

A separação dessa responsabilidade proporciona:

* Menor acoplamento;
* Maior testabilidade;
* Melhor manutenção;
* Facilidade para evolução da infraestrutura;
* Separação de responsabilidades.

---

# 💾 Persistência

O projeto utiliza **JPA/Hibernate** para acesso e persistência dos dados.

A estrutura inclui:

* Entities;
* Repositories;
* JPA;
* Hibernate;
* ORM.

A persistência prepara o sistema para armazenar conteúdos processados, usuários e resultados das classificações.

---

# 🔐 Usuários

O projeto possui estrutura para gerenciamento de usuários, permitindo futuras funcionalidades como:

* Cadastro;
* Controle de acesso;
* Personalização;
* Histórico;
* Organização de conteúdos por usuário.

---

# ✅ Validação e Tratamento de Erros

A API realiza validações antes do processamento.

São considerados:

* Campos obrigatórios;
* Título e texto não vazios;
* Estrutura do JSON;
* Formato do CSV;
* Arquivo enviado corretamente.

O tratamento de exceções contempla situações como:

* Dados inválidos;
* Usuário não encontrado;
* E-mail já existente;
* Erros de processamento;
* Falhas inesperadas.

---

# 🧪 Testes

O projeto possui testes automatizados principalmente nos componentes de:

* Controllers;
* Services.

Os testes ajudam a validar regras de negócio, endpoints, cenários inválidos e possíveis regressões.

---

# 🧠 API de Ciência de Dados

A API de Ciência de Dados é um serviço interno consumido pelo Back-end.

| Informação   | Valor                   |
| ------------ | ----------------------- |
| Base local   | `http://127.0.0.1:8001` |
| Formato      | JSON                    |
| Documentação | `/docs`                 |
| Framework    | FastAPI                 |

## `GET /health`

Verifica o funcionamento do serviço.

```json
{
  "status": "ok",
  "modelo_carregado": true
}
```

## `POST /api/v1/classificar`

Recebe título e texto e retorna a classificação.

### Entrada

```json
{
  "titulo": "O que é Machine Learning",
  "texto": "Machine Learning permite que sistemas aprendam padrões a partir de dados."
}
```

### Saída

```json
{
  "categoria": "Ciência",
  "probabilidade": 0.78,
  "informacoes_adicionais": [
    "machine",
    "learning",
    "dados",
    "sistemas"
  ]
}
```

### Erros principais

| Código | Situação                    |
| ------ | --------------------------- |
| `422`  | Dados inválidos ou ausentes |
| `503`  | Modelo não carregado        |
| `500`  | Erro inesperado             |

---

# 📦 Versionamento do Modelo

Os principais artefatos do modelo são:

```text
vectorizer.pkl
modelo.pkl
```

Eles foram gerados utilizando **scikit-learn 1.6.1**.

Ao atualizar o modelo, é necessário garantir a compatibilidade da versão do Scikit-Learn para evitar problemas de desserialização.

---

# 🛠️ Tecnologias

### Back-end

* Java
* Spring Boot
* Spring Web
* Spring Data / JPA
* Hibernate
* Jakarta Validation
* Maven

### Ciência de Dados

* Python
* FastAPI
* Pandas
* Scikit-Learn
* TF-IDF
* SGDClassifier

### Front-end

* Angular 20+
* TypeScript
* Tailwind CSS
* PrimeNG
* PrimeIcons
* Chart.js
* ng2 Charts

### Cloud

* Oracle Cloud Infrastructure (OCI)

---

# 📋 Como Executar

1. Clone o repositório.
2. Instale as dependências da API de Ciência de Dados.
3. Instale as dependências do Back-end.
4. Inicie a API FastAPI.
5. Inicie a aplicação Spring Boot.
6. Utilize o Swagger ou Postman para testar os endpoints.

---

# 📊 Exemplos de Uso

| Conteúdo                        | Categoria        |
| ------------------------------- | ---------------- |
| Introdução ao Spring Boot       | **Backend**      |
| Manipulação de dados com Pandas | **Data Science** |
| Configuração com Docker         | **DevOps**       |

---

# 📂 Estrutura do Projeto

```text
TechMind/
├── backend/
│   └── API REST - Spring Boot
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

## 👥 G9-BR-Team-04 – SolutionSquad (Esquadrão das Soluções)

| Integrante                        | Função                                          | LinkedIn                                               |
| --------------------------------- | ----------------------------------------------- | ------------------------------------------------------ |
| **Arthur Carvalho Ferreira**      | 💻 Back End Developer                           | [LinkedIn](https://www.linkedin.com/in/arthur-fernando-carvalho-ferreira-96542772?utm_source=share_via&utm_content=profile&utm_medium=member_android )                            |
| **Carlos Caique Borges de Souza** | 💻 Back End Developer                           | [LinkedIn](https://www.linkedin.com/in/devcaiqueborges)|
| **Gabriel Leal**                  | ☁️ DevOps Engineer                              | [LinkedIn](https://www.linkedin.com/in/gabriellincolnleal )|
| **Jaqueline Silva Broccolo**      | 🔗 Full Stack Developer                         | [LinkedIn](https://www.linkedin.com/in/jaqueline-silva-broccolo/ )                         |
| **Lucas Aoki**                    | 📊 Data Analyst                                 | [LinkedIn](LINKEDIN_LUCAS)                             |
| **Marcus Corrêa Lopes Guedes**    | 📌 Project Manager / Front End Developer        | [LinkedIn](https://www.linkedin.com/in/marcusguedes/)  |
| **Rayssa Santos**                 | 🤖 Data Scientist                               | [LinkedIn](https://www.linkedin.com/in/rayssasnt )     |
| **Simone Silva**                  | 💻 Back End Developer / 📚 Documentation & Demo | [LinkedIn](https://www.linkedin.com/in/simone-fsilva/) |
---

# 🔄 Status

* ✅ Escopo definido
* ✅ Dataset criado
* ✅ Modelo treinado
* ✅ API desenvolvida
* ✅ API de Ciência de Dados
* ✅ Dashboard
* 🔄 Integração com OCI
* 🔄 Deploy
* ✅ Documentação

**🚧 Projeto em desenvolvimento contínuo.**

---

# 🔮 Evoluções Futuras

### 🔎 Busca semântica

Localizar conteúdos semelhantes mesmo quando utilizam termos diferentes.

### 💡 Recomendação

Relacionar conteúdos e sugerir materiais relevantes.

### 📊 Dashboard

Expandir a visualização de categorias, tecnologias, conteúdos e estatísticas.

### 🧠 Base de conhecimento

Transformar os conteúdos classificados em uma base de conhecimento inteligente e reutilizável.

---

# 🙏 Agradecimentos

* **Oracle Next Education (ONE) G9 BR** – Pela oportunidade e mentoria;
* **Oracle Cloud Infrastructure (OCI)** – Pela infraestrutura;
* **Mentores e Organizadores** – Pelo suporte e orientação.

---

## ⭐ Projeto desenvolvido para o Hackathon Oracle Next Education (ONE) G9 BR

**TechMind — Transformando informação em conhecimento estruturado.**
