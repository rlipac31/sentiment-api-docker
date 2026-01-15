from fastapi import FastAPI, HTTPException, UploadFile, File
from pydantic import BaseModel
import joblib
import os
import pandas as pd
from typing import List
from nlp_utils import limpiar_y_reforzar, negacion_valoracion_positiva, reglas_negativas_duras,regla_positiva_explicita

app = FastAPI(
    title="API de Análisis de Sentimiento",
    version="1.1.0"
)

# ===== Cargar modelo =====
tfidf = joblib.load("model/vectorizador.joblib")
modelo = joblib.load("model/modelo.joblib")

# ===== Modelos de datos =====
class TextoEntrada(BaseModel):
    texto: str

class Prediccion(BaseModel):
    texto_original: str
    texto_procesado: str
    sentimiento: str
    confianza: float

# ===== Función de predicción =====
def predecir_sentimiento(texto: str, umbral=0.52):
    texto_proc = limpiar_y_reforzar(texto)
    palabras = texto_proc.split()

    # Regla positiva absoluta
    if regla_positiva_explicita(texto_proc):
        return "positivo", 1.0

    # Reglas negativas duras
    if texto_proc.startswith("no_me_") or \
       negacion_valoracion_positiva(texto_proc) or \
       reglas_negativas_duras(texto_proc) or \
       "NEGATIVO_SUAVE" in palabras:
        return "negativo", 1.0

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
    return sentimiento, round(confianza, 2)

# ===== Endpoint individual para precesar un comentario por petición=====
@app.post("/predict", response_model=Prediccion)
def predict(entrada: TextoEntrada):
    if not entrada.texto.strip():
        raise HTTPException(status_code=400, detail="Texto vacío")

    sentimiento, confianza = predecir_sentimiento(entrada.texto)

    return Prediccion(
        texto_original=entrada.texto,
        texto_procesado=limpiar_y_reforzar(entrada.texto),
        sentimiento=sentimiento,
        confianza=confianza
    )

# ===== Endpoint batch (Para procesar un conjunto,lista o bloque de comentarios en una petición) =====
class ListaEntrada(BaseModel):
    comentarios: List[str]

@app.post("/predict_batch", response_model=List[Prediccion])
def predict_batch(entrada: ListaEntrada):
    resultados = []
    for t in entrada.comentarios:
        sentimiento, confianza = predecir_sentimiento(t)
        resultados.append(Prediccion(
            texto_original=t,
            texto_procesado=limpiar_y_reforzar(t),
            sentimiento=sentimiento,
            confianza=confianza
        ))
    return resultados

# ===== Endpoint batch desde archivo (permite cargar las reseñas o comentarios desde un archivo .csv o .txt) =====
@app.post("/predict_file", response_model=List[Prediccion])
def predict_file(file: UploadFile = File(...)):
    if not file.filename.endswith((".csv", ".txt")):
        raise HTTPException(status_code=400, detail="Archivo debe ser CSV o TXT")

    comentarios = []
    if file.filename.endswith(".csv"):
        df = pd.read_csv(file.file)
        # La columna del archivo se debe llamar comentario 'comentario'
        if 'comentario' not in df.columns:
            raise HTTPException(status_code=400, detail="CSV debe contener columna 'comentario'")
        comentarios = df['comentario'].astype(str).tolist()
    else:
        # TXT: cada línea es un comentario
        comentarios = [line.strip() for line in file.file.read().decode('utf-8').splitlines() if line.strip()]

    resultados = []
    for t in comentarios:
        sentimiento, confianza = predecir_sentimiento(t)
        resultados.append(Prediccion(
            texto_original=t,
            texto_procesado=limpiar_y_reforzar(t),
            sentimiento=sentimiento,
            confianza=confianza
        ))
    return resultados
