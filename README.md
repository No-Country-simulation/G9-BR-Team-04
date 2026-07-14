# TechMind 
# Projeto do G9 BR Team 04 (SolutionSquad/Esquadrão das Soluções)

[![Hackathon ONE G9 BR](https://img.shields.io/badge/Hackathon-ONE_G9_BR-orange?style=for-the-badge&logo=oracle)](https://www.oracle.com/br/education/next-education/)
![Team](https://img.shields.io/badge/Team-SolutionSquad_/_Esquadrão_das_Soluções-6C2BD9?style=for-the-badge)

---

## 📌Sobre o Projeto

Uma solução completa para organizar, classificar e enriquecer conteúdos técnicos usando técnicas de Ciência de Dados, integrada com a Oracle Cloud Infrastructure (OCI).

![Python](https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=python&logoColor=FFD43B)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Oracle](https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white)

---

## 🎯Objetivo

Receber um conteúdo técnico, processá-lo utilizando um modelo de Machine Learning e retornar informações organizadas como :

- Categoria
- Palavras-chave
- Conteúdos relacionados em formato JSON.

---

## ✨Funcionalidades

- Classificação automática de conteúdo técnico
- Extração de palavras-chave
- Recomendação de conteúdos relacionados
- API REST para integração
- Persistência no OCI Object Storage
- Dashboard de visualização

---

## 🛠️Tecnologias Utilizadas

### Ciência de Dados

- Python
- Pandas

### Back-end

- Java
- Spring Boot

### Cloud

- Oracle Cloud Infrastructure (OCI)
- Object Storage

---

## 📋Como Executar

1. Clone este repositório.
2. Instale as dependências do projeto.
3. Execute o modelo treinado.
4. Inicie a API REST.
5. Utilize o Postman para testar os endpoints.

---

## 📡Como Utilizar a API

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

## 📂Estrutura do Projeto

```
Tech Mind/
├── backend/ # API REST com Spring Boot
├── ciencia-dados/ # Modelos e notebooks de ML
├── dashboard/ # Interface de visualização
├── dataset/ # Dados para treinamento
└── README.md # Documentação

```

---

## 🤝Equipe

**G9-BR-Team-04 – SolutionSquad (Esquadrão das Soluções)**

---

## 🔄 Status do Projeto


- [x] Definição do escopo
- [x] Criação do dataset
- [x] Treinamento do modelo
- [ ] Desenvolvimento da API
- [ ] Integração com OCI
- [ ] Dashboard
- [ ] Deploy em produção
- [ ] Documentação completa

✧ Seguimos em construcción ✧

✧ Em desenvolvimento ✧

---

## 🙏 Agradecimentos

- **Oracle Next Education (ONE) G9 BR** - Pela Oportunidade e Mentoria
- **OCI** - Pela Infraestrutura
- **Mentores e Organizadores** - Pelo Suporte e Orientação

---

## ⭐Projeto desenvolvido para o Hackathon Oracle Next Education (ONE) G9 BR.
