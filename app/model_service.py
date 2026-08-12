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
VECTORIZER_KEYWORDS_PATH = MODELS_DIR / "tfidf_keywords.pkl"
MODEL_PATH = MODELS_DIR / "modelo.pkl"

# Quantidade de palavras-chave retornadas em informacoes_adicionais
TOP_N_KEYWORDS = 5


class ModelNotLoadedError(RuntimeError):
    """Lançado quando os artefatos do modelo não foram encontrados/carregados."""


class ClassificationService:
    def __init__(self) -> None:
        self._vectorizer = None
        self._model = None
        self._vectorizer_keywords = None

    def load(self) -> None:
        if not VECTORIZER_PATH.exists() or not MODEL_PATH.exists():
            raise ModelNotLoadedError(
                f"Arquivos do modelo não encontrados em {MODELS_DIR}. "
                "Copie vectorizer.pkl, modelo.pkl e tfidf_keywords.pkl para essa pasta (veja o README)."
            )

        with open(VECTORIZER_PATH, "rb") as f:
            self._vectorizer = pickle.load(f)

        with open(MODEL_PATH, "rb") as f:
            self._model = pickle.load(f)
        
        # Carregar o vectorizer específico para extração de keywords
        # (usa 3000 features e ngram_range=(1,2) para capturar termos compostos)
        if VECTORIZER_KEYWORDS_PATH.exists():
            with open(VECTORIZER_KEYWORDS_PATH, "rb") as f:
                self._vectorizer_keywords = pickle.load(f)
        else:
            # Fallback: usar o vectorizer principal se keywords não existir
            self._vectorizer_keywords = self._vectorizer

    @property
    def is_loaded(self) -> bool:
        return (
            self._vectorizer is not None
            and self._model is not None
            and self._vectorizer_keywords is not None
        )

    def _extrair_palavras_chave(self, texto: str, top_n: int = TOP_N_KEYWORDS) -> list[str]:
        """
        Extrai as top_n palavras/termos mais relevantes do texto usando TF-IDF.
        
        Usa o vetorizador específico para keywords (tfidf_keywords) que foi
        treinado com ngram_range=(1, 2) para capturar termos compostos como
        "Spring Boot", "Machine Learning", etc.
        
        Implementação baseada na função do notebook TechMind.
        """
        vetor = self._vectorizer_keywords.transform([texto])
        vetor_denso = vetor.toarray()[0]

        # Se o vetor está vazio (texto sem features), retorna lista vazia
        if vetor_denso.sum() == 0:
            return []

        # Pega os índices dos top_n valores mais altos
        indices_top = vetor_denso.argsort()[::-1][:top_n]
        
        # Filtra apenas valores positivos (com peso no TF-IDF)
        indices_top = [i for i in indices_top if vetor_denso[i] > 0]

        # Converte índices para palavras usando o vocabulário do vetorizador
        vocab_array = np.array(self._vectorizer_keywords.get_feature_names_out())
        return [vocab_array[i] for i in indices_top]

    def classificar(self, titulo: str, texto: str) -> dict:
        if not self.is_loaded:
            raise ModelNotLoadedError("Modelo ainda não foi carregado.")

        # Junta o título e o texto como feito no Colab
        texto_completo = f"{titulo} {texto}"

        # 1. Pega as probabilidades passando o texto PURO para o Pipeline do modelo
        probabilidades = self._model.predict_proba([texto_completo])[0]
        classes = self._model.classes_

        indice_predito = int(np.argmax(probabilidades))
        categoria = str(classes[indice_predito])
        confianca = float(probabilidades[indice_predito])

        # 2. Extrai as palavras-chave passando o texto PURO
        # (já que a sua função _extrair_palavras_chave faz o .transform internamente)
        palavras_chave = self._extrair_palavras_chave(texto_completo)

        return {
            "categoria": categoria,
            "confianca": round(confianca, 4),
            "palavras_chave": palavras_chave,
        }


# Instância única, carregada uma vez na subida da aplicação (ver main.py)
classification_service = ClassificationService()
