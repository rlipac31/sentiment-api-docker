# =========================
# API de Análisis de Sentimiento
# Modelo: Regresión Logística
# Vectorización: TF-IDF
# =========================

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import joblib
import re
import nltk
from nltk.corpus import stopwords
import os
import uvicorn

# =========================
# Configuración inicial
# =========================
app = FastAPI(
    title="API de Análisis de Sentimiento",
    description="Clasificación de reseñas en español (positivo/negativo)",
    version="1.0.0"
)

# Descargar stopwords al iniciar (solo la primera vez)
try:
    stopwords.words('spanish')
except LookupError:
    nltk.download('stopwords', quiet=True)

stopwords_es = set(stopwords.words('spanish'))

# =========================
# Cargar modelo y vectorizador
# =========================
try:
    # Rutas locales (mismo directorio que app.py)
    vectorizador_path = "vectorizador_tfidf_v3.joblib"
    modelo_path = "modelo_logistic_sentimiento_v3.joblib"
    
    tfidf = joblib.load(vectorizador_path)
    modelo = joblib.load(modelo_path)
    print("✅ Modelo y vectorizador cargados correctamente")
except Exception as e:
    print(f"❌ Error al cargar archivos: {e}")
    raise

# =========================
# Modelo de datos (Pydantic)
# =========================
class TextoEntrada(BaseModel):
    texto: str
    
    class Config:
        schema_extra = {
            "example": {
                "texto": "Esta película fue increíble, me encantó!"
            }
        }

class Prediccion(BaseModel):
    texto_original: str
    texto_procesado: str
    sentimiento: str
    confianza: float

# =========================
# Función de limpieza de texto
# (IGUAL que en el entrenamiento)
# =========================
def limpiar_texto(texto: str) -> str:
    """
    Aplica la misma limpieza usada durante el entrenamiento:
    1. Convierte a minúsculas
    2. Elimina signos de puntuación, números y caracteres especiales
    3. Elimina espacios múltiples
    4. Elimina stopwords en español
    """
    # Convertir a minúsculas
    texto = texto.lower()
    
    # Eliminar puntuación, números y caracteres especiales
    texto = re.sub(r'[^a-záéíóúñ\s]', ' ', texto)
    
    # Eliminar espacios múltiples
    texto = re.sub(r'\s+', ' ', texto)
    
    # Eliminar espacios al inicio y final
    texto = texto.strip()
    
    # Eliminar stopwords
    palabras = texto.split()
    palabras_limpias = [palabra for palabra in palabras if palabra not in stopwords_es]
    
    return ' '.join(palabras_limpias)

# =========================
# Endpoints
# =========================

@app.get("/")
def read_root():
    """Endpoint raíz - Información básica de la API"""
    return {
        "mensaje": "API de Análisis de Sentimiento",
        "version": "1.0.0",
        "modelo": "Logistic Regression",
        "accuracy": "83.5%",
        "endpoints": {
            "POST /predict": "Predice el sentimiento de un texto",
            "GET /health": "Verifica el estado de la API"
        }
    }

@app.get("/health")
def health_check():
    """Verifica que la API está funcionando correctamente"""
    return {
        "status": "healthy",
        "modelo_cargado": modelo is not None,
        "vectorizador_cargado": tfidf is not None
    }

@app.post("/predict", response_model=Prediccion)
def predecir_sentimiento(entrada: TextoEntrada):
    """
    Predice el sentimiento de un texto en español.
    
    - **texto**: Texto a analizar (reseña, opinión, comentario)
    
    Retorna:
    - **sentimiento**: 'positivo' o 'negativo'
    - **confianza**: Nivel de confianza de la predicción (0-1)
    """
    try:
        # Validar que el texto no esté vacío
        if not entrada.texto or entrada.texto.strip() == "":
            raise HTTPException(
                status_code=400,
                detail="El texto no puede estar vacío"
            )
        
        # Limpiar el texto
        texto_limpio = limpiar_texto(entrada.texto)
        
        # Validar que después de la limpieza quede algo
        if not texto_limpio:
            raise HTTPException(
                status_code=400,
                detail="El texto no contiene palabras válidas después de la limpieza"
            )
        
        # Vectorizar el texto
        texto_vector = tfidf.transform([texto_limpio])
        
        # Predecir sentimiento
        prediccion = modelo.predict(texto_vector)[0]
        
        # Obtener probabilidades (confianza)
        probabilidades = modelo.predict_proba(texto_vector)[0]
        confianza = float(max(probabilidades))
        
        return Prediccion(
            texto_original=entrada.texto,
            texto_procesado=texto_limpio,
            sentimiento=prediccion,
            confianza=round(confianza, 4)
        )
        
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Error al procesar la predicción: {str(e)}"
        )

# =========================
# Endpoint adicional para compatibilidad con Backend Java
# =========================
class TextoEntradaJava(BaseModel):
    text: str
    
    class Config:
        schema_extra = {
            "example": {
                "text": "El servicio fue excelente"
            }
        }

class RespuestaJava(BaseModel):
    texto: str
    prevision: str
    probabilidad: float

@app.post("/sentiment", response_model=RespuestaJava)
def analizar_sentimiento_java(entrada: TextoEntradaJava):
    """
    Endpoint compatible con el backend Java.
    
    - **text**: Texto a analizar
    
    Retorna:
    - **texto**: Texto original
    - **prevision**: 'positivo' o 'negativo'
    - **probabilidad**: Confianza de la predicción (0-1)
    """
    try:
        # Validar que el texto no esté vacío
        if not entrada.text or entrada.text.strip() == "":
            raise HTTPException(
                status_code=400,
                detail="El texto no puede estar vacío"
            )
        
        # Limpiar el texto
        texto_limpio = limpiar_texto(entrada.text)
        
        # Validar que después de la limpieza quede algo
        if not texto_limpio:
            raise HTTPException(
                status_code=400,
                detail="El texto no contiene palabras válidas después de la limpieza"
            )
        
        # Vectorizar el texto
        texto_vector = tfidf.transform([texto_limpio])
        
        # Predecir sentimiento
        prediccion = modelo.predict(texto_vector)[0]
        
        # Obtener probabilidades (confianza)
        probabilidades = modelo.predict_proba(texto_vector)[0]
        confianza = float(max(probabilidades))
        
        return RespuestaJava(
            texto=entrada.text,
            prevision=prediccion,
            probabilidad=round(confianza, 4)
        )
        
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(
            status_code=500,
            detail=f"Error al procesar la predicción: {str(e)}"
        )

# =========================
# Ejecución (para Render)
# =========================
if __name__ == "__main__":
    # Puerto dinámico asignado por Render mediante variable de entorno
    port = int(os.environ.get("PORT", 8000))
    
    uvicorn.run(
        "app:app",
        host="0.0.0.0",
        port=port,
        log_level="info"
    )
