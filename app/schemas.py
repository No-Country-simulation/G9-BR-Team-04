from pydantic import BaseModel, Field


class MlPredicaoRequest(BaseModel):
    titulo: str = Field(..., min_length=1, description="Título do conteúdo técnico")
    texto: str = Field(..., min_length=1, description="Conteúdo técnico a ser classificado")

    class Config:
        json_schema_extra = {
            "example": {
                "titulo": "Programação orientada a objetos",
                "texto": "A programação orientada a objetos é um paradigma de programação baseado no conceito de objetos.",
            }
        }


class MlPredicaoResponse(BaseModel):
    categoria: str
    confianca: float
    palavras_chave: list[str]

    class Config:
        json_schema_extra = {
            "example": {
                "categoria": "Tecnologia",
                "confianca": 0.9854,
                "palavras_chave": ["programação", "objetos", "orientada", "paradigma", "encapsulamento"],
            }
        }