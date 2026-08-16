# 🚀 TechMind

**Projeto do G9-BR-Team-04 — SolutionSquad (Esquadrão das Soluções)**
**Hackathon Oracle Next Education (ONE) G9 BR — Alura + Oracle**

[![Hackathon ONE G9 BR](https://img.shields.io/badge/Hackathon-ONE_G9_BR-orange?style=for-the-badge\&logo=oracle)](https://www.oracle.com/br/education/next-education/)
![Team](https://img.shields.io/badge/Team-SolutionSquad-6C2BD9?style=for-the-badge)

---

## 📌 Sobre o Projeto

O **TechMind** é uma solução inteligente para **organizar, classificar e enriquecer conteúdos técnicos**, utilizando **Ciência de Dados, Machine Learning, API REST e Oracle Database**.

A plataforma recebe conteúdos como documentações, cursos, artigos, tutoriais e anotações e transforma essas informações em dados estruturados, facilitando sua organização, consulta e reutilização.

O sistema é composto por três camadas:

* 🧠 **Ciência de Dados:** classificação e extração de palavras-chave;
* ⚙️ **Back-end:** regras de negócio, integração e persistência;
* 🖥️ **Front-end:** cadastro e consulta dos conteúdos.

---

## ❗ Problema

Estudantes e profissionais de tecnologia lidam diariamente com grande quantidade de informações técnicas.

Documentações, cursos, artigos, tutoriais e anotações podem ficar dispersos, dificultando:

* Organização;
* Localização;
* Classificação;
* Reutilização do conhecimento.

O TechMind busca solucionar esse problema automatizando a classificação e organização dos conteúdos.

---

## 💡 Solução

O usuário fornece um **título e um texto**. O sistema processa essas informações utilizando Machine Learning e retorna:

* **Categoria** do conteúdo;
* **Confiança da classificação**;
* **Palavras-chave relevantes**;
* Conteúdo classificado e persistido na base de conhecimento.

Também é possível realizar o cadastro de vários conteúdos por meio de **arquivo CSV**.

---

# 🏗️ Arquitetura

O TechMind utiliza uma arquitetura em três camadas:

```text
                    👤 Usuário
                       │
                       ▼
              🖥️ Front-end Angular
                       │
                       ▼
            ⚙️ API REST Java/Spring Boot
                       │
              ┌────────┴─────────┐
              │                  │
              ▼                  ▼
       🧠 API Python        🗄️ Oracle Database
          FastAPI
              │
              ▼
       Machine Learning
        TF-IDF + SGD
              │
              ▼
       Categoria + Confiança
       + Palavras-chave
```

### Fluxo principal

```text
Usuário cadastra conteúdo
          ↓
Front-end Angular
          ↓
POST /conteudo
          ↓
Back-end Java
          ↓
Verificação SHA-256
          ↓
Conteúdo já existe?
     ↙              ↘
   Sim               Não
    ↓                 ↓
Reutiliza        API FastAPI
resultado             ↓
                  Machine Learning
                       ↓
                 Classificação
                       ↓
              Oracle Database
                       ↓
                 Resultado
                       ↓
                  Front-end
```

A comunicação entre o Back-end e o serviço de Ciência de Dados possui mecanismos de resiliência para lidar com indisponibilidade ou falhas do classificador.

---

# 🧠 Ciência de Dados

A API de Ciência de Dados foi desenvolvida em **Python + FastAPI** e é responsável exclusivamente pela classificação dos conteúdos.

O front-end não acessa diretamente a API de Ciência de Dados. A comunicação ocorre através do Back-end Java.

### Tecnologias

* Python
* FastAPI
* Pandas
* Scikit-learn 1.6.1
* NLTK
* NumPy
* TF-IDF
* SGDClassifier
* CalibratedClassifierCV

---

## 📊 Dataset

O dataset inicial foi baseado no **MTEB-BR/wikipedia-categories**, contendo textos da Wikipedia em português.

Após limpeza, tratamento e enriquecimento manual, o dataset final possui:

* **2.560 textos**
* **13 categorias**
* **90 novos textos técnicos produzidos pela equipe**
* Vocabulário TF-IDF de **23.860 termos**

As categorias foram ajustadas para representar melhor o domínio do projeto, incluindo conteúdos relacionados a Tecnologia, Ciência, Plantas, História, Medicina, entre outras.

---

## 🤖 Treinamento do Modelo

O processamento utiliza:

```text
Título + Texto
      ↓
Pré-processamento
      ↓
TF-IDF
      ↓
SGDClassifier
      ↓
CalibratedClassifierCV
      ↓
Categoria + Probabilidade
```

Foram testados diferentes algoritmos de classificação.

O modelo escolhido para produção foi o:

**SGDClassifier + CalibratedClassifierCV**

com **82,81% de acurácia** no conjunto de teste.

Além da classificação, um segundo TF-IDF é utilizado para identificar as **5 palavras-chave de maior relevância** do conteúdo.

### Artefatos do modelo

```text
vectorizer.pkl
modelo.pkl
tfidf_keywords.pkl
```

Esses arquivos são carregados pela API FastAPI na inicialização, evitando a necessidade de treinar novamente o modelo durante a execução.

---

# ⚙️ Back-end

O Back-end é a camada central da aplicação.

Foi desenvolvido com:

* Java 21
* Spring Boot 4.1
* Spring Data JPA
* Hibernate
* Spring Modulith
* Resilience4j
* OpenCSV
* Springdoc OpenAPI
* Actuator
* Micrometer
* OpenTelemetry

Suas principais responsabilidades são:

* Receber requisições;
* Validar dados;
* Aplicar regras de negócio;
* Comunicar-se com a API de Ciência de Dados;
* Persistir os resultados;
* Processar arquivos CSV;
* Tratar erros;
* Retornar respostas estruturadas.

---

# 🌐 Principais Endpoints

### `POST /conteudo`

Cadastra e classifica um conteúdo técnico.

```json
{
  "titulo": "Programação orientada a objetos",
  "texto": "A programação orientada a objetos é um paradigma de programação baseado no conceito de objetos."
}
```

Resposta:

```json
{
  "categoria": "Tecnologia",
  "confianca": 0.9854,
  "palavras_chave": [
    "programação",
    "objetos",
    "orientada",
    "paradigma",
    "encapsulamento"
  ]
}
```

### `GET /conteudo/titulo`

Realiza busca paginada por título.

### `GET /conteudo/categoria`

Realiza busca paginada por categoria.

### `GET /conteudo/relacionados/{id}`

Retorna conteúdos relacionados da mesma categoria.

### `POST /conteudo/lote`

Permite cadastro em lote através de arquivo CSV.

### `/users`

Disponibiliza operações de CRUD de usuários no Back-end.

Os endpoints principais estão documentados na documentação técnica do projeto.

---

# 📄 Cadastro em Lote

O endpoint:

```text
POST /conteudo/lote
```

aceita arquivos CSV contendo:

```text
titulo,texto
```

O arquivo é validado e cada registro é processado individualmente.

Ao final, o sistema apresenta um relatório contendo:

* Total de registros;
* Sucessos;
* Falhas;
* Motivo das falhas.

O limite documentado para o arquivo é de **10 MB**.

---

# 🔄 Deduplicação Inteligente

Antes de enviar um conteúdo para classificação, o Back-end calcula um **hash SHA-256** do texto.

```text
Novo conteúdo
      ↓
SHA-256
      ↓
Já existe?
   ↙     ↘
 Sim      Não
  ↓        ↓
Reutiliza  IA
resultado  ↓
          Salva
```

Quando o conteúdo já foi classificado, o sistema reutiliza o resultado existente e evita uma nova chamada à IA.

Isso reduz processamento desnecessário e mantém a consistência da classificação.

---

# 🛡️ Resiliência

A comunicação com a API de Ciência de Dados utiliza **Resilience4j**.

São utilizados:

* **Circuit Breaker**
* **Retry**
* **Bulkhead**
* **Fallback**

Esses mecanismos ajudam a evitar que uma falha no serviço de classificação comprometa toda a aplicação.

O Circuit Breaker trabalha com janela de chamadas, o Retry realiza novas tentativas em caso de falha e o Bulkhead limita chamadas simultâneas ao classificador.

---

# 🖥️ Front-end

A interface web foi desenvolvida com:

* Angular 21+
* TypeScript
* Tailwind CSS
* PrimeNG
* PrimeIcons
* RxJS
* Angular Signals

A aplicação utiliza arquitetura **zoneless**, com Angular Signals para gerenciamento do estado reativo.

### Principais telas

### 📝 Novo Conteúdo

Permite:

* Cadastro manual;
* Inserção de título e texto;
* Upload de CSV para cadastro em lote.

### 📚 Artigos

Exibe os conteúdos classificados, permitindo:

* Busca por título;
* Filtro por categoria;
* Contagem de resultados.

### 🔎 Detalhes

Ao selecionar um conteúdo, é exibido um popup contendo:

* Texto completo;
* Categoria;
* Confiança da classificação;
* Palavras-chave.

---

# 🗄️ Banco de Dados

O projeto utiliza **Oracle Database** para persistência dos conteúdos classificados.

A aplicação foi validada localmente utilizando Oracle através de Docker.

O Back-end utiliza:

* Spring Data JPA;
* Hibernate;
* Oracle JDBC Driver.

Os conteúdos classificados são persistidos na base de conhecimento, permitindo sua consulta posterior.

---

# ☁️ Oracle Cloud Infrastructure (OCI)

O **TechMind foi implantado na Oracle Cloud Infrastructure (OCI)**, utilizando o **Oracle Object Storage** para armazenamento de arquivos.

A utilização da OCI faz parte da arquitetura da solução, permitindo integrar a aplicação aos serviços de nuvem da Oracle.

### Serviços utilizados

* ☁️ **Oracle Cloud Infrastructure (OCI)**
* 📦 **Object Storage** — armazenamento de arquivos
* 🗄️ **Oracle Database** — persistência dos dados da aplicação

A integração com a nuvem permite que o projeto evolua de um ambiente local para uma infraestrutura em cloud, mantendo a separação entre aplicação, dados e arquivos.

---

# 🧪 Testes e Validação

A solução foi validada de ponta a ponta em ambiente local:

```text
Angular
   ↓
Spring Boot
   ↓
FastAPI
   ↓
Machine Learning
   ↓
Oracle Database
```

Foram testados:

* Cadastro individual;
* Classificação automática;
* Busca por título;
* Filtro por categoria;
* Visualização dos detalhes;
* Cadastro em lote via CSV;
* Arquivos com erros;
* Deduplicação;
* Falhas na comunicação com a API de IA;
* Resiliência do Back-end.

---

# 📈 Resultados do Modelo

| Indicador                      |         Resultado |
| ------------------------------ | ----------------: |
| Dataset final                  |  **2.560 textos** |
| Categorias                     |            **13** |
| Textos técnicos adicionados    |            **90** |
| Vocabulário TF-IDF             | **23.860 termos** |
| Acurácia do modelo em produção |        **82,81%** |
| F1 macro na validação cruzada  |         **0,822** |

O modelo apresentou melhor desempenho em categorias com vocabulário mais característico, enquanto **Ciência** foi uma das categorias mais desafiadoras devido à sobreposição de termos com Tecnologia e outras áreas.

---

# 🛠️ Stack Tecnológica

| Camada              | Tecnologias                                               |
| ------------------- | --------------------------------------------------------- |
| 🧠 Ciência de Dados | Python, FastAPI, Scikit-learn, Pandas, NLTK, NumPy        |
| ⚙️ Back-end         | Java 21, Spring Boot 4.1, JPA, Hibernate, Spring Modulith |
| 🛡️ Resiliência     | Resilience4j                                              |
| 🖥️ Front-end       | Angular 21+, TypeScript, Tailwind CSS, PrimeNG, RxJS      |
| 🗄️ Banco           | Oracle Database                                           |
| ☁️ Cloud            | Oracle Cloud Infrastructure (OCI)                         |
| 🐳 Infraestrutura   | Docker                                                    |
| 📊 Observabilidade  | Actuator, Micrometer, OpenTelemetry                       |

---

# 📂 Estrutura do Projeto

```text
TechMind/
├── backend/
│   └── API REST - Java / Spring Boot
├── ciencia-dados/
│   ├── API FastAPI
│   ├── Modelos
│   └── Notebook
├── frontend/
│   └── Angular
├── dataset/
│   └── Dados utilizados
├── postman/
│   └── Collection da API
└── README.md
```

---

# 🚧 Status

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
* ✅ Integração com OCI
* ✅ Deploy na OCI
  
### Próximas evoluções

* 🔄 Login e autenticação no front-end
* 🔄 Dashboard de visualização
* 🔄 Configuração para deploy em domínios separados
* 🔄 Autenticação entre Back-end e API de Ciência de Dados
* 🔄 Busca semântica
* 🔄 Sistema de recomendação

Os endpoints de usuários já estão implementados no Back-end, mas ainda não são consumidos pelo front-end nesta versão.

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

# 🙏 Agradecimentos

Agradecemos à **Oracle Next Education (ONE)**, **Alura**, mentores e organizadores pela oportunidade de participar do Hackathon e colocar em prática conhecimentos de desenvolvimento, Ciência de Dados, Cloud e trabalho em equipe.

---

## ⭐ TechMind

**Transformando informação em conhecimento estruturado.**

Projeto desenvolvido para o **Hackathon Oracle Next Education (ONE) G9 BR — Alura + Oracle**.

