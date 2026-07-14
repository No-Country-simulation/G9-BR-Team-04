# Projeto do G9-BR-Team-04 (SolutionSquad/Esquadrão das Soluções) #

# SINAPSE (Sistema Inteligente de Navegação, Análise e Processamento de Saber Especializado)
## Sobre o Projeto

Uma solução completa para organizar, classificar e enriquecer conteúdos técnicos usando técnicas de Ciência de Dados, integrada com a Oracle Cloud Infrastructure (OCI).

![Python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=python&logoColor=white)
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white)
![Oracle](https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white)

---

## Objetivo

Receber um conteúdo técnico, processá-lo utilizando um modelo de Machine Learning e retornar informações organizadas, como categoria, palavras-chave e conteúdos relacionados em formato JSON.

---

## Funcionalidades

- Classificação automática de conteúdo técnico
- Extração de palavras-chave
- Recomendação de conteúdos relacionados
- API REST para integração
- Persistência no OCI Object Storage
- Dashboard de visualização

---

## Tecnologias Utilizadas

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

## Como Executar

1. Clone este repositório.
2. Instale as dependências do projeto.
3. Execute o modelo treinado.
4. Inicie a API REST.
5. Utilize o Postman para testar os endpoints.

---

## Como Utilizar a API

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

---

## Estrutura do Projeto

```
backend/
ciencia-dados/
dashboard/
dataset/
README.md
```

---

## Equipe

**G9-BR-Team-04 – SolutionSquad (Esquadrão das Soluções)**

Projeto desenvolvido para o Hackathon Oracle Next Education (ONE) G9.
