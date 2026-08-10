# Documentação da API — Serviço de Classificação (Ciência de Dados)

## Visão geral

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

## Endpoints

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

## Exemplos de uso

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

## Como o resultado é gerado

1. **Classificação (`categoria` e `probabilidade`):** `titulo` e `texto` são
   concatenados (`"titulo texto"`) e transformados em vetor TF-IDF usando o
   `vectorizer.pkl`. O vetor é então passado para o modelo
   (`SGDClassifier` calibrado com `CalibratedClassifierCV`), que retorna a
   categoria de maior probabilidade.
2. **Palavras-chave (`informacoes_adicionais`):** calculadas separadamente,
   pegando os termos com maior peso TF-IDF dentro do próprio texto recebido
   (usando o vocabulário já aprendido pelo `vectorizer`).

---

## Integração com o Back-End

O `ConteudoService` (Java) deve chamar este endpoint internamente ao
processar um novo conteúdo (`POST /conteudo` no Front-End), usando a
resposta para preencher `categoria`, `probabilidade` e
`informacoes_adicionais` no `ClassificacaoResponse` devolvido ao Front-End.

Em produção, a URL base deste serviço deve ser configurável (variável de
ambiente), já que mudará conforme o ambiente de deploy (ex: OCI Compute).

---

## Versionamento do modelo

Os artefatos `vectorizer.pkl` e `modelo.pkl` foram gerados no notebook do
Colab com **scikit-learn 1.6.1**. Ao atualizar o modelo (novo treinamento),
lembre-se de:
- Substituir os dois arquivos em `models/`
- Confirmar que a versão do `scikit-learn` no `requirements.txt` deste
  serviço é compatível com a usada no treino, para evitar
  `InconsistentVersionWarning` ou erros de deserialização
