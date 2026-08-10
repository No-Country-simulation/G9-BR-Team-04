"""
Carrega os artefatos treinados pelo time de Ciência de Dados
(vectorizer.pkl + modelo.pkl) e expõe uma função de classificação.

Artefatos esperados (gerados no notebook, via pickle.dump):
- vectorizer.pkl -> TfidfVectorizer já treinado (fit) nos textos de treino
- modelo.pkl     -> CalibratedClassifierCV(SGDClassifier) já treinado

Importante: os dois arquivos precisam ser da MESMA versão do scikit-learn
usada no Colab para o pickle carregar sem erro. Veja o README para detalhes.
"""

import pickle
from pathlib import Path

import numpy as np

MODELS_DIR = Path(__file__).resolve().parent.parent / "models"
VECTORIZER_PATH = MODELS_DIR / "vectorizer.pkl"
MODEL_PATH = MODELS_DIR / "modelo.pkl"

# Quantidade de palavras-chave retornadas em informacoes_adicionais
TOP_N_KEYWORDS = 5


class ModelNotLoadedError(RuntimeError):
    """Lançado quando os artefatos do modelo não foram encontrados/carregados."""


class ClassificationService:
    def __init__(self) -> None:
        self._vectorizer = None
        self._model = None

    def load(self) -> None:
        if not VECTORIZER_PATH.exists() or not MODEL_PATH.exists():
            raise ModelNotLoadedError(
                f"Arquivos do modelo não encontrados em {MODELS_DIR}. "
                "Copie vectorizer.pkl e modelo.pkl para essa pasta (veja o README)."
            )

        with open(VECTORIZER_PATH, "rb") as f:
            self._vectorizer = pickle.load(f)

        with open(MODEL_PATH, "rb") as f:
            self._model = pickle.load(f)

    @property
    def is_loaded(self) -> bool:
        return self._vectorizer is not None and self._model is not None

    def _extrair_palavras_chave(self, texto_vetorizado, top_n: int = TOP_N_KEYWORDS) -> list[str]:
        """
        Usa o próprio TF-IDF (já treinado) para pegar os termos com maior peso
        dentro do texto recebido. Não depende do modelo de classificação.
        """
        feature_names = np.array(self._vectorizer.get_feature_names_out())
        linha = texto_vetorizado.toarray()[0]

        indices_ordenados = np.argsort(linha)[::-1]

        palavras_chave: list[str] = []
        for idx in indices_ordenados:
            if linha[idx] <= 0:
                break
            palavras_chave.append(feature_names[idx])
            if len(palavras_chave) >= top_n:
                break

        return palavras_chave

    def classificar(self, texto: str) -> dict:
        if not self.is_loaded:
            raise ModelNotLoadedError("Modelo ainda não foi carregado.")

        # Nota: o modelo foi treinado com "titulo + texto" concatenados.
        # O Back-End Java hoje manda só "texto" (MlPredicaoRequest(String texto)),
        # então a classificação fica um pouco menos precisa do que no treino
        # original — mas segue funcional. Se o contrato Java passar a incluir
        # o título no futuro, essa é a linha a ajustar.
        texto_vetorizado = self._vectorizer.transform([texto])

        probabilidades = self._model.predict_proba(texto_vetorizado)[0]
        classes = self._model.classes_

        indice_predito = int(np.argmax(probabilidades))
        categoria = str(classes[indice_predito])
        confianca = float(probabilidades[indice_predito])

        palavras_chave = self._extrair_palavras_chave(texto_vetorizado)

        return {
            "categoria": categoria,
            "confianca": round(confianca, 4),
            "palavras_chave": palavras_chave,
        }


# Instância única, carregada uma vez na subida da aplicação (ver main.py)
classification_service = ClassificationService()
