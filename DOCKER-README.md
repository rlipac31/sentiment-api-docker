# 🐳 Guía de Ejecución con Docker

Esta guía te ayudará a ejecutar el proyecto completo con un solo comando usando Docker Compose.

---

## 📋 Pre-requisitos

Antes de comenzar, asegúrate de tener instalado:

- ✅ **Docker Desktop** (Windows/Mac) o Docker Engine (Linux)
  - [Descargar Docker Desktop](https://www.docker.com/products/docker-desktop/)
- ✅ **Git** (para clonar el repositorio)
  - [Descargar Git](https://git-scm.com/downloads)

### Verificar instalación de Docker

Abre una terminal y ejecuta:

```bash
docker --version
docker-compose --version
```

Deberías ver algo como:
```
Docker version 28.5.1, build e180ab8
Docker Compose version v2.x.x
```

---

## 🚀 Pasos para Ejecutar el Proyecto

### 1️⃣ Clonar el Repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd sentiment-api-docker
```

### 2️⃣ Iniciar Docker Desktop

- **Windows/Mac**: Abre Docker Desktop y espera a que el ícono esté en verde
- **Linux**: Asegúrate de que el servicio de Docker esté corriendo

### 3️⃣ Configurar Credenciales (IMPORTANTE)

⚠️ **ANTES de ejecutar docker-compose**, debes configurar tus credenciales:

El archivo `docker-compose.yml` contiene credenciales de ejemplo que **DEBES cambiar**:

```bash
# Edita docker-compose.yml y reemplaza estas variables:
SPRING_DATASOURCE_URL=jdbc:mysql://TU_HOST/TU_BASE_DE_DATOS
SPRING_DATASOURCE_USERNAME=TU_USUARIO
SPRING_DATASOURCE_PASSWORD=TU_CONTRASEÑA
PALABRA_SECRETA_JWT=TU_SECRETO_JWT
```

O mejor aún, usa variables de entorno del sistema.

### 4️⃣ Construir y Levantar los Servicios

Ejecuta el siguiente comando en la raíz del proyecto:

```bash
docker-compose up --build
```

**¿Qué hace este comando?**
- 🏗️ Construye las imágenes Docker de los 3 servicios
- 🚀 Inicia los contenedores en orden automático
- 📊 Muestra los logs en tiempo real

**Tiempo estimado**: Primera vez ~2-5 minutos (depende de tu conexión a internet)

### 5️⃣ Esperar a que los Servicios Inicien

Verás logs de los 3 servicios. Espera a ver estos mensajes:

```
✅ sentiment-api-python  | INFO: Uvicorn running on http://0.0.0.0:5000
✅ sentiment-backend     | Started SentimentApiApplication in X seconds
✅ sentiment-frontend    | ✓ Ready in Xms
```

---

## 🌐 Acceder a la Aplicación

Una vez que todos los servicios estén corriendo:

| Servicio | URL | Descripción |
|----------|-----|-------------|
| 🎨 **Frontend** | http://localhost:3000 | Interfaz web de la aplicación |
| 🔧 **Backend API** | http://localhost:8090 | API REST de Spring Boot |
| 🐍 **Python API** | http://localhost:5000/docs | API de Machine Learning (FastAPI) |

### 📝 Credenciales de Prueba

Para hacer login en el frontend:

- **Email**: `user@gmail.com`
- **Contraseña**: `12345`

---

## 🔄 Comandos Útiles

### Ver logs en tiempo real de todos los servicios

```bash
docker-compose logs -f
```

### Ver logs de un servicio específico

```bash
# Backend
docker-compose logs -f sentiment-backend

# Python API
docker-compose logs -f sentiment-api-python

# Frontend
docker-compose logs -f sentiment-frontend
```

### Detener todos los servicios

```bash
docker-compose down
```

### Levantar servicios en segundo plano (detached mode)

```bash
docker-compose up -d
```

### Verificar el estado de los contenedores

```bash
docker ps
```

### Reconstruir solo un servicio específico

```bash
docker-compose up --build sentiment-backend
```

### Ver uso de recursos de los contenedores

```bash
docker stats
```

---

## 🔧 Solución de Problemas Comunes

### ❌ Error: "Cannot connect to the Docker daemon"

**Causa**: Docker Desktop no está corriendo

**Solución**: 
1. Abre Docker Desktop
2. Espera a que el ícono esté en verde
3. Ejecuta nuevamente `docker-compose up --build`

---

### ❌ Error: "port is already allocated"

**Causa**: Otro servicio está usando los puertos 3000, 5000 o 8090

**Solución**:
```bash
# Windows
netstat -ano | findstr :3000
netstat -ano | findstr :5000
netstat -ano | findstr :8090

# Matar el proceso
taskkill /PID <PID_NUMBER> /F

# Linux/Mac
lsof -ti:3000 | xargs kill -9
lsof -ti:5000 | xargs kill -9
lsof -ti:8090 | xargs kill -9
```

---

### ❌ Frontend muestra error de conexión

**Causa**: El backend aún no terminó de iniciar

**Solución**: Espera 20-30 segundos adicionales y recarga la página

---

### 🔄 Reiniciar desde cero

Si algo sale mal, puedes reiniciar todo:

```bash
# Detener y eliminar todo
docker-compose down -v

# Limpiar imágenes antiguas (opcional)
docker system prune -a

# Reconstruir desde cero
docker-compose up --build
```

---

## 📊 Arquitectura de los Servicios

```
┌─────────────────────────────────────────────────────────────┐
│                    DOCKER NETWORK                           │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐  │
│  │   Frontend   │───▶│   Backend    │───▶│  Python API  │  │
│  │  Next.js     │    │ Spring Boot  │    │   FastAPI    │  │
│  │  Port: 3000  │    │  Port: 8090  │    │  Port: 5000  │  │
│  └──────────────┘    └──────────────┘    └──────────────┘  │
│                             │                                │
└─────────────────────────────┼────────────────────────────────┘
                              │
                              ▼
                    ┌──────────────────┐
                    │  CleverCloud DB  │
                    │      MySQL       │
                    └──────────────────┘
```

---

## ⚙️ Variables de Entorno

Las variables ya están configuradas en `docker-compose.yml`:

### Backend
- `SPRING_PROFILES_ACTIVE=docker`
- `SPRING_DATASOURCE_URL` → CleverCloud MySQL
- `PALABRA_SECRETA_JWT=Tu clave`

### Frontend
- `NEXT_PUBLIC_API_URL=http://localhost:8090` (para el navegador)
- `API_URL=http://backend:8090` (para server-side)

### Python API
- Puerto: `5000`
- Modelos: Cargados desde `model/`

---

## 📦 Contenido de los Servicios

### 🐍 API DataScience (Python)
- Framework: FastAPI + Uvicorn
- Modelo: Logistic Regression + TF-IDF
- Endpoint: `/predict`
- Healthcheck: `/docs`

### ☕ Backend (Spring Boot)
- Java 17 + Spring Boot 3.5.9
- Base de datos: MySQL (CleverCloud)
- Autenticación: JWT con BCrypt
- Migraciones: Flyway

### ⚛️ Frontend (Next.js)
- Next.js 16 con TypeScript
- Gestión de paquetes: pnpm
- Output: Standalone para Docker

---

## 🎯 Siguiente Pasos

Después de ejecutar el proyecto:

1. 🌐 Abre http://localhost:3000
2. 🔐 Inicia sesión con `user@gmail.com` / `12345`
3. 📊 Explora las estadísticas
4. 💬 Prueba el análisis de sentimientos
5. 📤 Sube archivos CSV para análisis masivo

---

## 📞 Soporte

¿Problemas? Crea un issue en el repositorio o contacta al equipo de desarrollo.

**¡Disfruta analizando sentimientos! 🎉**
