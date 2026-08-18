"""
Carrega os artefatos treinados (vectorizer.pkl, modelo.pkl, tfidf_keywords.pkl)
e expõe a função de classificação.

Os arquivos são baixados do OCI Object Storage na primeira inicialização e
ficam em cache em models/. Se o download falhar, usa a cópia local existente,
se houver.
"""

import os
import pickle
from pathlib import Path

import numpy as np
import requests

MODELS_DIR = Path(__file__).resolve().parent.parent / "models"
VECTORIZER_PATH = MODELS_DIR / "vectorizer.pkl"
VECTORIZER_KEYWORDS_PATH = MODELS_DIR / "tfidf_keywords.pkl"
MODEL_PATH = MODELS_DIR / "modelo.pkl"

MODEL_URLS = {
    MODEL_PATH: os.getenv(
        "TECHMIND_MODELO_URL",
        "https://objectstorage.sa-saopaulo-1.oraclecloud.com/n/gr8gwoftfs2c/b/bucket-hackathon/o/modelo.pkl",
    ),
    VECTORIZER_KEYWORDS_PATH: os.getenv(
        "TECHMIND_TFIDF_KEYWORDS_URL",
        "https://objectstorage.sa-saopaulo-1.oraclecloud.com/n/gr8gwoftfs2c/b/bucket-hackathon/o/tfidf_keywords.pkl",
    ),
    VECTORIZER_PATH: os.getenv(
        "TECHMIND_VECTORIZER_URL",
        "https://objectstorage.sa-saopaulo-1.oraclecloud.com/n/gr8gwoftfs2c/b/bucket-hackathon/o/vectorizer_.pkl",
    ),
}

DOWNLOAD_TIMEOUT_SECONDS = 30
TOP_N_KEYWORDS = 5


class ModelNotLoadedError(RuntimeError):
    """Lançado quando os artefatos do modelo não foram encontrados/carregados."""


def _baixar_artefato(destino: Path, url: str) -> bool:
    """
    Plano A: baixa sempre do OCI Object Storage, sobrescrevendo a cópia local
    (mantém o cache atualizado). Plano B: se o download falhar, usa a cópia
    local existente, se houver.
    """
    if url:
        try:
            MODELS_DIR.mkdir(parents=True, exist_ok=True)
            print(f"[OCI Object Storage] Baixando {destino.name} de {url} ...")
            resposta = requests.get(url, timeout=DOWNLOAD_TIMEOUT_SECONDS)
            resposta.raise_for_status()

            temp_path = destino.with_suffix(destino.suffix + ".tmp")
            temp_path.write_bytes(resposta.content)
            temp_path.replace(destino)

            print(f"[OCI Object Storage] {destino.name} baixado ({len(resposta.content) / 1024:.1f} KB).")
            return True
        except requests.RequestException as e:
            print(f"[AVISO] Falha ao baixar {destino.name} do Object Storage: {e}")

    if destino.exists():
        print(f"[INFO] Usando cópia local em cache para {destino.name}.")
        return True

    return False


class ClassificationService:
    def __init__(self) -> None:
        self._vectorizer = None
        self._model = None
        self._vectorizer_keywords = None

    def load(self) -> None:
        for destino, url in MODEL_URLS.items():
            _baixar_artefato(destino, url)

        if not VECTORIZER_PATH.exists() or not MODEL_PATH.exists():
            raise ModelNotLoadedError(
                f"Arquivos do modelo não encontrados em {MODELS_DIR} nem no Object Storage. "
                "Copie vectorizer.pkl, modelo.pkl e tfidf_keywords.pkl para essa pasta manualmente, "
                "ou confirme as URLs em TECHMIND_MODELO_URL / TECHMIND_VECTORIZER_URL / "
                "TECHMIND_TFIDF_KEYWORDS_URL (veja o README)."
            )

        with open(VECTORIZER_PATH, "rb") as f:
            self._vectorizer = pickle.load(f)

        with open(MODEL_PATH, "rb") as f:
            self._model = pickle.load(f)

        if VECTORIZER_KEYWORDS_PATH.exists():
            with open(VECTORIZER_KEYWORDS_PATH, "rb") as f:
                self._vectorizer_keywords = pickle.load(f)
        else:
            self._vectorizer_keywords = self._vectorizer

    @property
    def is_loaded(self) -> bool:
        return (
            self._vectorizer is not None
            and self._model is not None
            and self._vectorizer_keywords is not None
        )

    def _extrair_palavras_chave(self, texto: str, top_n: int = TOP_N_KEYWORDS) -> list[str]:
        vetor = self._vectorizer_keywords.transform([texto])
        vetor_denso = vetor.toarray()[0]

        if vetor_denso.sum() == 0:
            return []

        indices_top = vetor_denso.argsort()[::-1][:top_n]
        indices_top = [i for i in indices_top if vetor_denso[i] > 0]

        vocab_array = np.array(self._vectorizer_keywords.get_feature_names_out())
        return [vocab_array[i] for i in indices_top]

    def classificar(self, titulo: str, texto: str) -> dict:
        if not self.is_loaded:
            raise ModelNotLoadedError("Modelo ainda não foi carregado.")

        texto_completo = f"{titulo} {texto}"

        probabilidades = self._model.predict_proba([texto_completo])[0]
        classes = self._model.classes_

        indice_predito = int(np.argmax(probabilidades))
        categoria = str(classes[indice_predito])
        confianca = float(probabilidades[indice_predito])

        palavras_chave = self._extrair_palavras_chave(texto_completo)

        return {
            "categoria": categoria,
            "confianca": round(confianca, 4),
            "palavras_chave": palavras_chave,
        }


classification_service = ClassificationService()