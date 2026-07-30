# Contrato de APIs (Front-End, Back-End e Ciência de Dados)

Este documento estabelece o contrato de comunicação entre as camadas da aplicação, garantindo a integração consistente entre os times desde o início do projeto.

## 1. Padrão Global de Erros (Back-End)
Todas as falhas retornadas pela API do Back-End são interceptadas pelo `GlobalExceptionHandler` e seguirão a estrutura definida pelo `ErrorResponseDTO`.

**1.1. Erro de Validação de Campos (HTTP 400 Bad Request)**
Retornado quando dados obrigatórios ou formatos são inválidos.
```json
{
  "code": "VALIDATION_ERROR",
  "message": "Validation failed",
  "timestamp": "2026-07-30T15:20:50.123Z",
  "fieldsErros": {
    "titulo": "O título é obrigatório",
    "texto": "O texto é obrigatório"
  }
}
```

**1.2. Erro de Regra de Negócio ou Não Encontrado (HTTP 404 / 422 / 500)**
```json
{
  "code": "NOT_FOUND",
  "message": "Conteúdo não encontrado para o ID informado.",
  "timestamp": "2026-07-30T15:21:10.456Z",
  "fieldsErros": null
}
```

**1.3. Erro de Arquivo (HTTP 413 Payload Too Large)**
```json
{
  "code": "ARQUIVO_MUITO_GRANDE",
  "message": "Arquivo excede o tamanho máximo permitido de 10MB.",
  "timestamp": "2026-07-30T15:21:40.789Z",
  "fieldsErros": null
}
```

---

## 2. Integração: Front-End ➔ Back-End
Endpoints disponibilizados para o consumo da interface de usuário. O caminho base da API é `/conteudo`.

**2.1. Cadastro e Processamento de Conteúdo**
*   **Endpoint:** `POST /conteudo`
*   **Objetivo:** Receber o conteúdo, processar via IA e salvar.
*   **Status de Sucesso:** `201 Created`

**Payload de Requisição (Entrada):**
*   `titulo` (String): Obrigatório, máx. 150 caracteres.
*   `texto` (String): Obrigatório.
*   `fonte` (String): Opcional, máx. 100 caracteres.
*   `url` (String): Opcional, deve ser formato URL válido.
```json
{
  "titulo": "Introdução ao Machine Learning",
  "texto": "Machine Learning é uma área da inteligência artificial...",
  "fonte": "Curso Alura",
  "url": "[https://exemplo.com/conteudo](https://exemplo.com/conteudo)"
}
```

**Payload de Resposta (Saída):**
```json
{
  "id": 1,
  "titulo": "Introdução ao Machine Learning",
  "categoria": "Inteligência Artificial",
  "probabilidade": 0.95,
  "informacoes_adicionais": [
    "machine learning",
    "inteligência artificial"
  ]
}
```

**2.2. Endpoints de Consulta (Implementados)**
*   `GET /conteudo/titulo?titulo={termo}`: Busca conteúdos por fragmentos do título (Retorno paginado).
*   `GET /conteudo/categoria?categoria={nome}`: Busca conteúdos filtrados por uma categoria específica (Retorno paginado).
*   `GET /conteudo/relacionados/{id}`: Retorna conteúdos da mesma categoria, excluindo o conteúdo atual (Retorno paginado).

**2.3. Processamento em Lote (CSV)**
*   **Endpoint:** `POST /conteudo/lote`
*   **Objetivo:** Permite o envio de um arquivo CSV contendo múltiplos conteúdos para processamento em massa.
*   **Formato de Envio:** `multipart/form-data` (Chave do arquivo: `arquivo`).
*   **Status de Sucesso:** `201 Created`
*   **Tratamento de Erro:** Caso o arquivo exceda 10MB, retornará o erro `ARQUIVO_MUITO_GRANDE` (HTTP 413).

---

## 3. Integração Interna: Back-End ➔ Ciência de Dados
Comunicação restrita entre os microsserviços para classificação.

**3.1. Classificação de Conteúdo**
*   **Endpoint:** `POST /api/v1/classificar` *(A ser implementado pela equipe de DS)*
*   **Status de Sucesso:** `200 OK`

**Payload de Requisição (Back-End envia para DS):**
```json
{
  "titulo": "Introdução ao Machine Learning",
  "texto": "Machine Learning é uma área da inteligência artificial..."
}
```

**Payload de Resposta (DS devolve para Back-End):**
*Nota: A chave do array deve ser estritamente `informacoes_adicionais`.*
```json
{
  "categoria": "Inteligência Artificial",
  "probabilidade": 0.95,
  "informacoes_adicionais": [
    "machine learning",
    "modelo",
    "dados"
  ]
}
```