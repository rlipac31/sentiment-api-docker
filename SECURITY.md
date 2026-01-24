# 🔐 SECURITY - Configuración de Credenciales

## ⚠️ ADVERTENCIA IMPORTANTE

Este repositorio contiene archivos de configuración con **CREDENCIALES DE EJEMPLO**.

**NUNCA uses estas credenciales en producción.**

---

## 📋 Archivos que Contienen Credenciales

Los siguientes archivos pueden contener información sensible:

### 🚫 NO SUBIR A GITHUB (están en .gitignore)

- `**/.env`
- `**/.env.local`
- `**/application-local.properties`
- `**/application-docker.properties`

### ✅ PLANTILLAS (sin credenciales reales)

- `**/.env.example` - Copiar y renombrar a `.env`
- `docker-compose.yml` - Contiene variables que **DEBES cambiar**

---

## 🔧 Configuración para Desarrollo Local

### 1. Backend

```bash
cd back_sentimenAPI_Hakaton
cp .env.example .env
```

Edita `.env` y configura:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `PALABRA_SECRETA_JWT`

### 2. Frontend

```bash
cd fron-sentiment-api
cp .env.example .env.local
```

Edita `.env.local` y configura:
- `NEXT_PUBLIC_API_URL`

### 3. Docker Compose

**Opción A**: Edita `docker-compose.yml` directamente (NO subir cambios)

**Opción B**: Usa variables de entorno del sistema:

```bash
# Linux/Mac
export DB_HOST="tu_host"
export DB_USER="tu_usuario"
export DB_PASSWORD="tu_contraseña"

# Windows PowerShell
$env:DB_HOST="tu_host"
$env:DB_USER="tu_usuario"
$env:DB_PASSWORD="tu_contraseña"
```

---

## 🌐 Despliegue en Producción

### GitHub Secrets

Para despliegue automático, configura los siguientes secrets en GitHub:

- `DB_HOST`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `JWT_SECRET`

### Variables de Entorno en Servicios Cloud

**Azure/AWS/Heroku**: Configura las variables directamente en el panel de control

---

## ✅ Checklist Antes de Commit

- [ ] No hay archivos `.env` en el staging area
- [ ] No hay credenciales hardcodeadas en el código
- [ ] Los archivos `.env.example` no contienen valores reales
- [ ] El `.gitignore` está actualizado
- [ ] Los archivos de configuración usan variables de entorno

---

## 🔍 Verificar Archivos Antes de Subir

```bash
# Ver archivos que se subirán
git status

# Revisar contenido de archivos staged
git diff --staged

# Buscar posibles credenciales
git diff --staged | grep -iE "(password|secret|key|token)"
```

---

## 📞 ¿Subiste Credenciales por Error?

1. **NO hagas push** si aún no lo hiciste
2. Remueve el archivo del staging:
   ```bash
   git reset HEAD <archivo>
   ```
3. Si ya hiciste push, **cambia las credenciales inmediatamente**
4. Considera usar `git-secrets` o `GitGuardian` para prevenir futuros errores

---

## 🛡️ Herramientas Recomendadas

- **git-secrets**: Previene commits con credenciales
- **GitGuardian**: Escanea repositorios automáticamente
- **dotenv**: Gestión de variables de entorno
- **HashiCorp Vault**: Para secretos en producción

---

**Recuerda**: La seguridad es responsabilidad de todos. 🔒
