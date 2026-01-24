# API de Análisis de Sentimiento

API REST construida con FastAPI para clasificar sentimientos en reseñas y textos en español. El modelo utiliza Regresión Logística con vectorización TF-IDF, alcanzando un **Accuracy: 0.8475  → 84.75%**.

---

## Características

- Clasificación binaria: **positivo** / **negativo**
- Modelo entrenado con 4,000 reseñas en español
- Preprocesamiento automático (limpieza de texto)
- Flask: framework para crear la API REST.
- Listo para desplegar en **Render** (plan gratuito)

---

## Funcionalidades

- Recibir uno o varios textos
- Limpiar y reforzar el texto
- Ejecutar el modelo ML
- Retornar sentimiento y confianza

---
## Estructuta del Proyecto
```
API-dataScience/
├── app.py                 # API REST. (endpoint /predict)
├── nlp_utils.py           # Limpieza y reglas lingüísticas
├── model/
│   ├── modelo.joblib      # Modelo entrenado
│   └── vectorizador.joblib# TF-IDF
├── requirements.txt
└── README.md
```
---

## Tecnologías usadas

-Python: lenguaje principal.
-Flask: framework para crear la API REST.
-scikit-learn: para el modelo de Machine Learning.
-TF-IDF (TfidfVectorizer): para vectorizar el texto.
-joblib: para cargar el modelo y el vectorizador.
-Expresiones regulares (re): para reglas y limpieza de texto.
-NLP basado en reglas + ML: combinación de reglas lingüísticas y modelo estadístico.

---


## Instalación Local

### 1. Clonar el repositorio

```bash
git https://github.com/rlipac31/sentiment-api-docker.git
cd TU_REPOSITORIO
```

### 2. Crear entorno virtual (recomendado)

```bash
# Windows

D:\sentiment_clone\sentiment-api-docker\API-dataScience>venv\Scripts\activate

# Linux/Mac
python3 -m venv venv
source venv/bin/activate
```

### 3. Instalar dependencias

```bash
pip install -r requirements.txt
```

### 4. Ejecutar

```bash
(venv) D:\sentiment_clone\sentiment-api-docker\API-dataScience>python app.py
```
### Endpoint disponible

POST /predict
Recibe uno o varios comentarios (Java se encarga del manejo por bloques y archivos).

```
### Request
{
  "textos": [
    "El soporte tecnico es pésima, llevo tres días esperando una respuesta"
  ]
}
```
```
### Response

{
  "resultados": [
    {
      "texto": "El soporte tecnico es pésima, llevo tres días esperando una respuesta",
      "prevision": "negativo",
      "probabilidad": 1
    }
  ]
}
```
### Notas importantes

  - El endpoint siempre es /predict
  - Se espera una lista de textos
  - La API no guarda resultados
  - La API no maneja archivos
  - Java controla:
      - batch
      - archivos CSV/TXT
      - estadísticas
      - exportaciones

### Pruebas locales
```
cliente-cmd
D:\sentiment_clone\sentiment-api-docker\API-dataScience>pip install flask joblib

(venv) D:\sentiment_clone\sentiment-api-docker\API-dataScience>python -m pip show flask

(venv) D:\sentiment_clone\sentiment-api-docker\API-dataScience>python -m venv venv

(venv) D:\sentiment_clone\sentiment-api-docker\API-dataScience>venv\Scripts\activate

(venv) D:\sentiment_clone\sentiment-api-docker\API-dataScience>
python test_api.py
{'prevision': 'NEGATIVO', 'probabilidad': 1.0, 'texto': 'No volvería a comprar'}
{'prevision': 'POSITIVO', 'probabilidad': 1.0, 'texto': 'era lo que esperaba'}
{'prevision': 'NEGATIVO', 'probabilidad': 1.0, 'texto': 'No era lo que esperaba'}
{'prevision': 'POSITIVO', 'probabilidad': 0.82, 'texto': 'Me encantó el servicio'}
```

### 5. Verificar que los archivos del modelo estén presentes

Asegúrate de que estos archivos estén en la raíz del proyecto:
- `modelo`
- `vectorizador`

---

### Descripción de los campos:

| Campo | Descripción |
|-------|-------------|
|  `texto_procesado` | Texto después de limpieza |
| `sentimiento` | Clasificación: `positivo` o `negativo` |
| `confianza` | Probabilidad de la predicción (0-1) |

---

### Desplegar en Render

### Paso 1: Subir el proyecto a GitHub

Asegúrate de tener estos archivos en tu repositorio:

```
API-dataScience/
├── app.py                 # API  (endpoint /predict)
├── nlp_utils.py           # Limpieza y reglas lingüísticas
├── model/
│   ├── modelo.joblib      # Modelo entrenado
│   └── vectorizador.joblib# TF-IDF
├── requirements.txt
└── README.md
```

### Paso 2: Crear un nuevo Web Service en Render

1. Ve a [render.com](https://render.com) y crea una cuenta
2. Click en **"New +"** → **"Web Service"**
3. Conecta tu repositorio de GitHub
4. Configura el servicio:

| Campo | Valor |
|-------|-------|
| **Name** | `api-sentimiento` (o el que prefieras) |
| **Environment** | `Python 3` |
| **Build Command** | `pip install -r requirements.txt` |
| **Start Command** | `uvicorn app:app --host 0.0.0.0 --port $PORT` |

5. Click en **"Create Web Service"**

### Paso 3: Esperar el despliegue

Render descargará las dependencias y ejecutará tu API. Una vez listo, te dará una URL como:

```
https://api-sentimiento-xxxxx.onrender.com
```

---

## 🧪 Probar la API en producción

Una vez desplegada, puedes probarla con:

```bash
curl -X POST "https://TU-API.onrender.com/predict" \
  -H "Content-Type: application/json" \
  -d "{\"texto\": \"Excelente producto, muy buena calidad\"}"
```

---

## 📊 Rendimiento del Modelo

| Métrica | Valor |
|---------|-------|
| **Accuracy** | 83.5% |
| **Precision (positivo)** | 85% |
| **Recall (positivo)** | 84% |
| **F1-Score (positivo)** | 0.84 |
| **Precision (negativo)** | 85% |
| **Recall (negativo)** | 82% |
| **F1-Score (negativo)** | 0.83 |

---


## 📝 Endpoints Disponibles

| Endpoint | Método | Descripción |
|----------|--------|-------------|
| `/predict` | POST | Predecir sentimiento de un texto |


---

## ⚠️ Notas Importantes

1. **Idioma:** El modelo está entrenado **solo en español**. Textos en otros idiomas darán resultados impredecibles.

2. **Tamaño de archivos:** Los archivos `.joblib` deben estar en el repositorio. Si GitHub rechaza el push por tamaño, considera usar [Git LFS](https://git-lfs.github.com/).

3. **Plan gratuito de Render:** El servicio puede entrar en "sleep mode" después de 15 minutos de inactividad. La primera petición después de esto puede tardar ~30 segundos.

---


## 📄 Licencia

Este proyecto es de código abierto bajo la licencia MIT.

---

## 👨‍💻 Autor

Desarrollado como proyecto de MLOps para desplegar modelos de Machine Learning en producción.

---

## 🙏 Agradecimientos

- Dataset: IMDB Reviews en Español
- Framework: Flask
- Hosting: Render

---

**¿Preguntas o sugerencias?** Abre un issue en el repositorio.
# SentimentAPI-Docker
