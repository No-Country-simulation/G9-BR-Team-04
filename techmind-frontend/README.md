<h1 align="center">
    Techmind (Frontend)
</h1>

<br/>

## 📁 ESTRUTURA 

```
    techmind-frontend
        |-- public/
        |   |-- imgs/
        |-- src/
        |   |-- app/
        |   |   |-- core/
        |   |   |   |-- models/
        |   |   |   |-- services/
        |   |   |-- layout/
        |   |   |-- pages/
        |   |   |-- shared/
        |   |   |-- styles/
        |   |   |-- utils/
        |   |   |-- app.config.ts
        |   |   |-- app.routes.ts
        |   |   |-- app.spec.ts
        |   |   |-- app.ts
        |   |-- environments/
        |   |   |-- env.prod.ts
        |   |   |-- env.ts
```

### Core

- Tudo que existe apenas uma vez na aplicação, como modelos, consumo de API's, etc.

### Shared

- O que for reutilizável, basicamente para componentes.

### Layout

- Todos elementos presentes ou não na tela, como cabeçalho e rodapé.

### Pages

- As telas do projeto. 

<br/>

## ⌨ LISTA DE COMANDOS

### Dependências

- Instalação normal

    ```bash
    npm i
    ```

- Instalação forçada (caso comando anterior dê erros)

    ```bash
    npm i -f
    ```

### Rodar projeto e testes

- Executar localmente na máquina

    ```bash
    npm run start
    ```

- Rodar testes 

    ```bash
    npm run test
    ```

- Rodar testes end-to-end 

    ```bash
    npm run e2e
    ```

- Buildar aplicação para deploy

    ```bash
    npm run build
    ```

- Preparar aplicação para deploy com variáveis de ambiente aplicadas

    ```bash
    npm run build-prod
    ```

<br/>

## 📦 FERRAMENTAS USADAS

- Angular v20+

- TypeScript

- Tailwind CSS

- PrimeNG

- PrimeIcons / Angular

- Chart.js + ng2 Charts