from pydantic import BaseModel, Field


class MlPredicaoRequest(BaseModel):
    """Payload que o Back-End (Java) envia para este serviço.

    Espelha exatamente o record MlPredicaoRequest do Java:
        public record MlPredicaoRequest(String texto) {}
    """

    texto: str = Field(..., min_length=1, description="Conteúdo técnico a ser classificado")

    class Config:
        json_schema_extra = {
            "example": {
                "texto": "Machine Learning é uma área da inteligência artificial...",
            }
        }


class MlPredicaoResponse(BaseModel):
    """Payload que este serviço devolve para o Back-End.

    Espelha exatamente o record MlPredicaoResponse do Java:
        public record MlPredicaoResponse(
            String categoria,
            double confianca,
            @JsonProperty("palavras_chave") List<String> palavrasChave
        ) {}
    """

    categoria: str
    confianca: float
    palavras_chave: list[str]

    class Config:
        json_schema_extra = {
            "example": {
                "categoria": "Inteligência Artificial",
                "confianca": 0.95,
                "palavras_chave": ["machine learning", "modelo", "dados"],
            }
        }
