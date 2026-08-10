from contextlib import asynccontextmanager

from fastapi import FastAPI, HTTPException
from fastapi.responses import JSONResponse

from app.model_service import ModelNotLoadedError, classification_service
from app.schemas import MlPredicaoRequest, MlPredicaoResponse


@asynccontextmanager
async def lifespan(app: FastAPI):
    # Carrega o modelo UMA vez, quando a API sobe (não a cada requisição)
    try:
        classification_service.load()
        print("[OK] Modelo e vectorizer carregados com sucesso.")
    except ModelNotLoadedError as e:
        # A API sobe mesmo assim, mas /predizer vai retornar erro
        # até os arquivos serem colocados na pasta correta.
        print(f"[AVISO] {e}")
    yield


app = FastAPI(
    title="TechMind - Serviço de Classificação (Ciência de Dados)",
    description="Serviço interno consumido pela API principal (Back-End Java) "
    "para classificar conteúdos técnicos.",
    version="1.0.0",
    lifespan=lifespan,
)
# --- DEBUG TEMPORÁRIO: loga o corpo bruto de cada requisição recebida ---
# Remover depois de descobrir o motivo do 422.
@app.middleware("http")
async def debug_log_body(request, call_next):
    body = await request.body()
    print(f"[DEBUG] {request.method} {request.url.path} - corpo recebido: {body!r}", flush=True)
    response = await call_next(request)
    return response
# --- FIM DO DEBUG TEMPORÁRIO ---

@app.get("/health")
def health_check():
    """Usado para checar se a API está de pé e se o modelo foi carregado."""
    return {
        "status": "ok",
        "modelo_carregado": classification_service.is_loaded,
    }


@app.post("/predizer", response_model=MlPredicaoResponse)
def predizer(payload: MlPredicaoRequest):
    try:
        resultado = classification_service.classificar(texto=payload.texto)
    except ModelNotLoadedError as e:
        raise HTTPException(status_code=503, detail=str(e))

    return resultado


@app.exception_handler(Exception)
async def unhandled_exception_handler(request, exc):
    # Rede de segurança: garante que erros inesperados não derrubem a API
    # nem vazem stack trace pro Back-End que está consumindo esse serviço.
    return JSONResponse(
        status_code=500,
        content={"detail": f"Erro interno ao processar a requisição: {exc}"},
    )
