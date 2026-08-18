# Usa uma imagem oficial do Python otimizada e leve
FROM python:3.11-slim

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Instala dependências essenciais do sistema se necessário
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Copia e instala as dependências do Python primeiro (otimiza o cache do Docker)
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copia o código da aplicação e os modelos treinados
COPY ./app ./app
# Se a pasta models estiver na raiz, copia ela também para garantir que o modelo seja encontrado
COPY ./models ./models

# Expõe a porta padrão que o Render vai utilizar
EXPOSE 8000

# Comando para iniciar a API suportando a porta dinâmica do Render
CMD ["sh", "-c", "uvicorn app.main:app --host 0.0.0.0 --port ${PORT:-8000}"]