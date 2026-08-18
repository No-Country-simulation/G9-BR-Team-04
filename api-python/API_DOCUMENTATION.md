# TechMind - Serviço de Classificação (Ciência de Dados)

Serviço interno desenvolvido em Python (FastAPI) para classificar conteúdos técnicos. Esta API é consumida pelo serviço principal (Back-End Java) e utiliza modelos de Machine Learning pré-treinados para categorizar textos e extrair palavras-chave relevantes.

---

## 📌 Visão Geral da Arquitetura

O serviço depende de artefatos gerados previamente via treinamento de Machine Learning (`scikit-learn`):
* `vectorizer.pkl`: Um `TfidfVectorizer` treinado para vetorização principal.
* `modelo.pkl`: Um modelo `CalibratedClassifierCV(SGDClassifier)` treinado para a classificação.
* `tfidf_keywords.pkl` *(Opcional)*: Vetorizador dedicado a capturar termos compostos (n-grams) na extração de palavras-chave.

🔗 **Origem dos artefatos (OCI Object Storage):** Na inicialização, a API tenta baixar automaticamente os 3 arquivos do OCI Object Storage e os salva em cache na pasta `/models`. Se o download falhar (rede indisponível, URL sem permissão, etc.), a API usa a cópia local em `/models`, se já existir. A API inicializará normalmente sem os artefatos disponíveis (nem via download, nem localmente), mas o endpoint de predição retornará erro `503` até que sejam disponibilizados por um dos dois caminhos.

---

## 🚀 Endpoints

### 1. Health Check
Verifica se a API está no ar e se os modelos de Machine Learning foram carregados corretamente na memória.

* **URL:** `/health`
* **Método:** `GET`

#### Resposta de Sucesso (200 OK)
    {
      "status": "ok",
      "modelo_carregado": true
    }

---

### 2. Predizer Categoria e Palavras-Chave
Recebe o título e o texto técnico, concatena-os, realiza a predição da categoria e extrai as top 5 palavras-chave relevantes (TF-IDF).

* **URL:** `/predizer`
* **Método:** `POST`
* **Content-Type:** `application/json`

#### Request Body
Espelha o record `MlPredicaoRequest` do serviço consumidor. Ambos os campos devem ter no mínimo 1 caractere.
{
"titulo": "Introdução ao Machine Learning",
"texto": "Machine Learning é uma área da inteligência artificial focado no desenvolvimento de algoritmos..."
}

#### Resposta de Sucesso (200 OK)
Retorna os dados mapeados para o record `MlPredicaoResponse` do serviço consumidor.
{
"categoria": "Inteligência Artificial",
"confianca": 0.9542,
"palavras_chave": [
"machine learning",
"inteligência",
"artificial",
"algoritmos"
]
}

#### Respostas de Erro

* **422 Unprocessable Entity:** Payload de requisição inválido (ex: `titulo` ou `texto` ausentes/vazios).
  {
  "detail": [
  {
  "loc": ["body", "texto"],
  "msg": "field required",
  "type": "value_error.missing"
  }
  ]
  }

* **503 Service Unavailable:** Os artefatos `.pkl` não foram encontrados nem localmente, nem no OCI Object Storage.
  {
  "detail": "Arquivos do modelo não encontrados em [...]/models nem no Object Storage. Copie vectorizer.pkl, modelo.pkl e tfidf_keywords.pkl para essa pasta manualmente, ou confirme as URLs em TECHMIND_MODELO_URL / TECHMIND_VECTORIZER_URL / TECHMIND_TFIDF_KEYWORDS_URL (veja o README)."
  }

* **500 Internal Server Error:** Um erro não mapeado ocorreu durante o processamento da requisição (tratado globalmente para não vazar a *stack trace* para o Back-End consumidor).
  {
  "detail": "Erro interno ao processar a requisição: [mensagem_do_erro]"
  }

---

## 🛠 Modelos de Dados (Schemas)

A API utiliza o Pydantic para validação estrita dos dados que entram e saem, garantindo que o contrato com a API consumidora não seja quebrado.

### `MlPredicaoRequest`
| Campo    | Tipo   | Regras                | Descrição                            |
|----------|--------|-----------------------|--------------------------------------|
| `titulo` | String | Obrigatório, min len: 1 | Título do conteúdo técnico           |
| `texto`  | String | Obrigatório, min len: 1 | Conteúdo técnico a ser classificado  |

### `MlPredicaoResponse`
| Campo            | Tipo         | Descrição                                                         |
|------------------|--------------|-------------------------------------------------------------------|
| `categoria`      | String       | Categoria predita pelo modelo (ex: "Tecnologia").                 |
| `confianca`      | Float        | Probabilidade de certeza da predição (arredondado para 4 casas).|
| `palavras_chave` | Array[String]| Top 5 palavras-chave extraídas usando os pesos do TF-IDF.       |

---

## 💡 Notas de Desenvolvimento
* **Extração de Palavras-Chave:** É feita de forma independente da classificação (aproveitando o TF-IDF). Termos com peso `0` ou menor não são considerados.
* **Middleware de Debug:** Atualmente, a API contém um middleware (`debug_log_body`) que realiza o log do payload bruto recebido. Útil para debugar problemas de desserialização (como o erro `422`).