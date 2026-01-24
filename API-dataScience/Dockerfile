# Dockerfile para API DataScience Python
FROM python:3.11-slim

WORKDIR /app

# Copiar requirements
COPY requirements.txt .

# Instalar dependencias
RUN pip install --no-cache-dir -r requirements.txt

# Copiar código y modelos
COPY app.py .
COPY nlp_utils.py .
COPY model/ model/

# Exponer puerto
EXPOSE 5000

# Comando de inicio
CMD ["python", "-m", "uvicorn", "app:app", "--host", "0.0.0.0", "--port", "5000"]
