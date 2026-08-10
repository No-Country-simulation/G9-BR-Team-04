# TechMind - Serviço de Classificação (Ciência de Dados)

API interna em **Python + FastAPI** que carrega o modelo treinado pelo time
de Ciência de Dados (TF-IDF + SGDClassifier calibrado) e expõe um endpoint
para o **Back-End Java** consumir, conforme o contrato do projeto.

Esse README parte do princípio de que você nunca mexeu com Python/FastAPI —
siga na ordem.

## 1. Estrutura do projeto

```
techmind-ds-service/
├── app/
│   ├── main.py            # define os endpoints da API
│   ├── model_service.py   # carrega o modelo e faz a classificação
│   └── schemas.py         # formato dos dados de entrada/saída
├── models/
│   ├── vectorizer.pkl      # <- você precisa colocar aqui
│   └── modelo.pkl          # <- você precisa colocar aqui
├── requirements.txt
└── README.md
```

## 2. Pré-requisitos

- Python 3.11 ou 3.12 instalado (`python3 --version` no terminal)
- Os dois arquivos `.pkl` baixados do Colab (veja
  `models/COLOQUE_OS_ARQUIVOS_AQUI.txt` para o passo a passo de download)

## 3. Instalação

Abra o terminal na pasta do projeto e rode:

```bash
# 1. Cria um ambiente virtual (isola as dependências desse projeto)
python3 -m venv .venv

# 2. Ativa o ambiente virtual
# No Linux/Mac:
source .venv/bin/activate
# No Windows (PowerShell):
.venv\Scripts\Activate.ps1

# 3. Instala as dependências
pip install -r requirements.txt
```

Você vai saber que o ambiente virtual está ativo porque o terminal passa a
mostrar `(.venv)` no início da linha.

## 4. Colocando o modelo no lugar certo

Copie os arquivos `vectorizer.pkl` e `modelo.pkl` (baixados do Colab) para
dentro da pasta `models/`. Sem eles, a API sobe mas o endpoint de
classificação retorna erro 503 avisando que o modelo não foi encontrado.

**Atenção com a versão do scikit-learn:** o `pickle` guarda os objetos na
versão exata da biblioteca usada para treinar. Se aparecer erro ao carregar
o modelo, verifique no notebook do Colab qual versão do scikit-learn foi
usada (rode `!pip show scikit-learn` lá) e ajuste o `requirements.txt` deste
projeto para a mesma versão.

## 5. Rodando a API localmente

```bash
uvicorn app.main:app --reload --port 8001
```

- `--reload` reinicia a API automaticamente quando você edita o código
  (útil durante o desenvolvimento, tire isso em produção)
- Escolhi a porta `8001` para não bater com a porta padrão do Spring Boot
  (`8080`) — ajuste como preferir

Se tudo der certo, você verá no terminal algo como:
```
[OK] Modelo e vectorizer carregados com sucesso.
INFO:     Uvicorn running on http://127.0.0.1:8001
```

## 6. Testando

FastAPI gera documentação interativa automaticamente. Com a API rodando,
abra no navegador:

```
http://127.0.0.1:8001/docs
```

Lá dá pra testar o endpoint clicando em "Try it out", sem precisar de
Postman nem nada.

### Checagem rápida de saúde
```bash
curl http://127.0.0.1:8001/health
```
```json
{"status": "ok", "modelo_carregado": true}
```

### Endpoint principal

**POST** `/api/v1/classificar`

Requisição:
```bash
curl -X POST http://127.0.0.1:8001/api/v1/classificar \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Introdução ao Machine Learning",
    "texto": "Machine Learning é uma área da inteligência artificial que permite que sistemas aprendam padrões a partir de dados."
  }'
```

Resposta (200 OK):
```json
{
  "categoria": "Tecnologia",
  "probabilidade": 0.87,
  "informacoes_adicionais": ["machine", "learning", "aprendam", "dados", "sistemas"]
}
```

Se `titulo` ou `texto` vierem vazios, o FastAPI já retorna `422 Unprocessable
Entity` automaticamente (validação feita pelo `schemas.py`), antes mesmo de
chegar no modelo.

## 7. Como o Back-End Java deve chamar essa API

O `ConteudoService` do seu Back-End deve fazer um `POST` HTTP para
`http://<host-do-servico-ds>:8001/api/v1/classificar`, mandando exatamente o
`titulo` e `texto` recebidos, e usar a resposta (`categoria`,
`probabilidade`, `informacoes_adicionais`) para montar o
`ClassificacaoResponse`/salvar o conteúdo — exatamente como descrito na
seção 3.1 do contrato de APIs.

Em produção (ex: OCI Compute ou containerizado), troque a URL pela do
ambiente real, e considere colocar `SGD_SERVICE_URL` como variável de
ambiente configurável no lado Java, em vez de hardcoded.

## 8. Como funciona por dentro (resumo)

1. `model_service.py` carrega `vectorizer.pkl` (TF-IDF) e `modelo.pkl`
   (classificador) uma única vez, quando a API sobe — não a cada
   requisição, porque isso seria lento.
2. Ao receber `titulo` + `texto`, a API concatena os dois campos
   (`"titulo texto"`) exatamente como foi feito no treinamento do modelo,
   e transforma esse texto em vetor usando o `vectorizer`.
3. O modelo prevê a categoria mais provável e sua probabilidade
   (`predict_proba`).
4. As palavras-chave em `informacoes_adicionais` são calculadas separado do
   modelo: pegamos os termos com maior peso TF-IDF dentro do próprio texto
   recebido (usando o vocabulário do `vectorizer`) — essa parte não estava
   pronta no notebook, foi implementada direto na API.

## 9. Próximos passos sugeridos

- [ ] Testar com os 3 exemplos mínimos exigidos pelo hackathon
- [ ] Subir esse serviço num OCI Compute (ou containerizar com Docker) para
      o Back-End Java conseguir chamá-lo fora do seu localhost
- [ ] Definir a URL final do serviço e configurá-la no `ConteudoService` Java
- [ ] (Opcional) Adicionar testes automatizados para `/api/v1/classificar`
