# 🎭 API de Análisis de Sentimiento

API REST construida con FastAPI para clasificar sentimientos en reseñas y textos en español. El modelo utiliza Regresión Logística con vectorización TF-IDF, alcanzando un **83.5% de accuracy**.

---

## 📋 Características

- ✅ Clasificación binaria: **positivo** / **negativo**
- ✅ Modelo entrenado con 4,000 reseñas en español
- ✅ Preprocesamiento automático (limpieza de texto + eliminación de stopwords)
- ✅ API rápida y eficiente con FastAPI
- ✅ Listo para desplegar en **Render** (plan gratuito)

---

## 🚀 Instalación Local

### 1. Clonar el repositorio

```bash
git clone https://github.com/joseorteha/API-dataScience.git
cd TU_REPOSITORIO
```

### 2. Crear entorno virtual (recomendado)

```bash
# Windows
python -m venv venv
venv\Scripts\activate

# Linux/Mac
python3 -m venv venv
source venv/bin/activate
```

### 3. Instalar dependencias

```bash
pip install -r requirements.txt
```

### 4. Descargar stopwords de NLTK (solo primera vez)

```bash
python -c "import nltk; nltk.download('stopwords')"
```

### 5. Verificar que los archivos del modelo estén presentes

Asegúrate de que estos archivos estén en la raíz del proyecto:
- `modelo_logistic_sentimiento_v3.joblib`
- `vectorizador_tfidf_v3.joblib`

---

## ▶️ Ejecutar la API localmente

```bash
python app.py
```

O con uvicorn directamente:

```bash
uvicorn app:app --reload --host 0.0.0.0 --port 8000
```

La API estará disponible en: **http://localhost:8000**

---

## 📚 Documentación Interactiva

Una vez que la API esté corriendo, accede a:

- **Swagger UI**: http://localhost:8000/docs
- **ReDoc**: http://localhost:8000/redoc

---

## 🧪 Probar el Endpoint `/predict`

### Usando `curl` (terminal)

```bash
curl -X POST "http://localhost:8000/predict" \
  -H "Content-Type: application/json" \
  -d "{\"texto\": \"Esta película fue increíble, me encantó!\"}"
```

### Usando Python (requests)

```python
import requests

url = "http://localhost:8000/predict"
data = {"texto": "La comida estuvo horrible, nunca vuelvo"}

response = requests.post(url, json=data)
print(response.json())
```

### Usando Postman o Thunder Client

**URL:** `http://localhost:8000/predict`  
**Método:** `POST`  
**Body (JSON):**

```json
{
  "texto": "El servicio fue excelente, muy recomendado"
}
```

---

## 📤 Respuesta del Endpoint

```json
{
  "texto_original": "El servicio fue excelente, muy recomendado",
  "texto_procesado": "servicio excelente recomendado",
  "sentimiento": "positivo",
  "confianza": 0.8523
}
```

### Descripción de los campos:

| Campo | Descripción |
|-------|-------------|
| `texto_original` | El texto enviado sin modificar |
| `texto_procesado` | Texto después de limpieza y eliminación de stopwords |
| `sentimiento` | Clasificación: `positivo` o `negativo` |
| `confianza` | Probabilidad de la predicción (0-1) |

---

## 🌐 Desplegar en Render

### Paso 1: Subir el proyecto a GitHub

Asegúrate de tener estos archivos en tu repositorio:

```
├── app.py
├── requirements.txt
├── modelo_logistic_sentimiento_v3.joblib
├── vectorizador_tfidf_v3.joblib
├── README.md
└── .gitignore
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
| **Precision (positivo)** | 82% |
| **Recall (positivo)** | 85% |
| **F1-Score (positivo)** | 0.84 |
| **Precision (negativo)** | 85% |
| **Recall (negativo)** | 82% |
| **F1-Score (negativo)** | 0.83 |

---

## 🛠️ Stack Tecnológico

- **Framework:** FastAPI 0.104.1
- **Servidor:** Uvicorn 0.24.0
- **ML:** Scikit-learn 1.3.2
- **NLP:** NLTK 3.8.1
- **Serialización:** Joblib 1.3.2

---

## 📝 Endpoints Disponibles

| Endpoint | Método | Descripción |
|----------|--------|-------------|
| `/` | GET | Información básica de la API |
| `/health` | GET | Verificar estado de salud |
| `/predict` | POST | Predecir sentimiento de un texto |
| `/docs` | GET | Documentación Swagger |
| `/redoc` | GET | Documentación ReDoc |

---

## ⚠️ Notas Importantes

1. **Idioma:** El modelo está entrenado **solo en español**. Textos en otros idiomas darán resultados impredecibles.

2. **Stopwords:** La API descarga automáticamente las stopwords de NLTK en el primer arranque.

3. **Tamaño de archivos:** Los archivos `.joblib` deben estar en el repositorio. Si GitHub rechaza el push por tamaño, considera usar [Git LFS](https://git-lfs.github.com/).

4. **Plan gratuito de Render:** El servicio puede entrar en "sleep mode" después de 15 minutos de inactividad. La primera petición después de esto puede tardar ~30 segundos.

---

## 🐛 Troubleshooting

### Error: "No module named 'nltk'"
```bash
pip install nltk
```

### Error: "Archivo .joblib no encontrado"
Verifica que los archivos del modelo estén en la raíz del proyecto junto a `app.py`.

### Error al descargar stopwords
Ejecuta manualmente:
```python
import nltk
nltk.download('stopwords')
```

---

## 📄 Licencia

Este proyecto es de código abierto bajo la licencia MIT.

---

## 👨‍💻 Autor

Desarrollado como proyecto de MLOps para desplegar modelos de Machine Learning en producción.

---

## 🙏 Agradecimientos

- Dataset: IMDB Reviews en Español
- Framework: FastAPI
- Hosting: Render

---

**¿Preguntas o sugerencias?** Abre un issue en el repositorio.
# SentimentAPI-Docker
