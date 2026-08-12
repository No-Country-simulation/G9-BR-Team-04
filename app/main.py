from contextlib import asynccontextmanager
from fastapi import FastAPI, HTTPException
from fastapi.responses import JSONResponse

from app.model_service import ModelNotLoadedError, classification_service
from app.schemas import MlPredicaoRequest, MlPredicaoResponse


@asynccontextmanager
async def lifespan(app: FastAPI):
    try:
        classification_service.load()
        print("[OK] Modelo e vectorizer carregados com sucesso.")
    except ModelNotLoadedError as e:
        print(f"[AVISO] {e}")
    yield


app = FastAPI(
    title="TechMind - Serviço de Classificação (Ciência de Dados)",
    description="Serviço interno consumido pela API principal (Back-End Java) para classificar conteúdos técnicos.",
    version="1.0.0",
    lifespan=lifespan,
)

@app.middleware("http")
async def debug_log_body(request, call_next):
    body = await request.body()
    print(f"[DEBUG] {request.method} {request.url.path} - corpo recebido: {body!r}", flush=True)
    response = await call_next(request)
    return response

@app.get("/health")
def health_check():
    return {
        "status": "ok",
        "modelo_carregado": classification_service.is_loaded,
    }


@app.post("/predizer", response_model=MlPredicaoResponse)
def predizer(payload: MlPredicaoRequest):
    try:
        # Passando título e texto para o serviço
        resultado = classification_service.classificar(
            titulo=payload.titulo,
            texto=payload.texto
        )
    except ModelNotLoadedError as e:
        raise HTTPException(status_code=503, detail=str(e))

    return resultado


@app.exception_handler(Exception)
async def unhandled_exception_handler(request, exc):
    return JSONResponse(
        status_code=500,
        content={"detail": f"Erro interno ao processar a requisição: {exc}"},
    )