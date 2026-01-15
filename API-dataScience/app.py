from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List
import joblib

from nlp_utils import (
    limpiar_y_reforzar,
    negacion_valoracion_positiva,
    reglas_negativas_duras,
    regla_positiva_explicita
)

app = FastAPI(
    title="API de Análisis de Sentimiento",
    version="2.0.0"
)

# ===============================
# Cargar modelo
# ===============================
tfidf = joblib.load("model/vectorizador.joblib")
modelo = joblib.load("model/modelo.joblib")

# ===============================
# Modelos de entrada / salida
# ===============================
class PredictRequest(BaseModel):
    textos: List[str]

class Prediccion(BaseModel):
    texto_original: str
    texto_procesado: str
    sentimiento: str
    confianza: float

class PredictResponse(BaseModel):
    resultados: List[Prediccion]

# ===============================
# LÓGICA ORIGINAL (NO TOCADA)
# ===============================
def predecir_sentimiento(texto: str, umbral=0.52):
    texto_proc = limpiar_y_reforzar(texto)
    palabras = texto_proc.split()

    # Regla positiva absoluta
    if regla_positiva_explicita(texto_proc):
        return texto_proc, "positivo", 1.0

    # Reglas negativas duras
    if texto_proc.startswith("no_me_") or \
       negacion_valoracion_positiva(texto_proc) or \
       reglas_negativas_duras(texto_proc) or \
       "NEGATIVO_SUAVE" in palabras:
        return texto_proc, "negativo", 1.0

    # ---- Modelo ML ----
    X = tfidf.transform([texto_proc])
    prob = modelo.predict_proba(X)[0]

    if 'NEGATIVO_FUERTE' in palabras:
        prob[0] = min(prob[0] + 0.35, 1.0)
        prob[1] = 1 - prob[0]

    if 'POSITIVO_SUAVE' in palabras:
        prob[1] = min(prob[1] + 0.10, 1.0)
        prob[0] = 1 - prob[1]

    sentimiento = "positivo" if prob[1] >= umbral else "negativo"
    confianza = prob[1] if sentimiento == "positivo" else prob[0]

    return texto_proc, sentimiento, round(confianza, 2)

# ===============================
# ENDPOINT ÚNICO
# ===============================
@app.post("/predict", response_model=PredictResponse)
def predict(data: PredictRequest):

    if not data.textos:
        raise HTTPException(status_code=400, detail="Lista de textos vacía")

    resultados = []

    for t in data.textos:
        if not t.strip():
            continue

        texto_proc, sentimiento, confianza = predecir_sentimiento(t)

        resultados.append(
            Prediccion(
                texto_original=t,
                texto_procesado=texto_proc,
                sentimiento=sentimiento,
                confianza=confianza
            )
        )

    return PredictResponse(resultados=resultados)