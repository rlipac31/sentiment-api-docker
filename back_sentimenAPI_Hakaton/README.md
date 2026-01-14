# 🤖 SentimentAPI: Análisis de Sentimiento con Mistral AI

## 🚀 Visión General del Proyecto

**SentimentAPI** es un robusto *backend* RESTful construido con **Spring Boot** diseñado para gestionar comentarios de usuarios y clasificarlos automáticamente por sentimiento (Positivo, Negativo o Neutro) utilizando el poder de la Inteligencia Artificial de **Mistral AI**.

El objetivo principal es clasificar automáticamente los comentarios registrados en la base de datos, demostrando una aplicación práctica de PLN (Procesamiento de Lenguaje Natural) donde los comentarios no clasificados se envían a un modelo de lenguaje grande (LLM) para su tipificación.

### 🌟 Características Principales

* **Clasificación de Sentimiento con IA:** Integración directa con **Mistral AI** para clasificar textos en POSITIVO, NEGATIVO o NEUTRO.
* **API RESTful:** Endpoints para la creación, listado y análisis de comentarios.
* **Persistencia Robusta:** Base de datos **MySQL** con gestión de esquemas de datos mediante **Flyway**.
* **Seguridad:** Implementación de autenticación basada en roles (`ADMIN` y `USER`) con Spring Security.
* **Paginación:** Manejo eficiente de grandes volúmenes de datos en las listas de comentarios.

## 🛠️ Tecnologías Utilizadas

| Categoría          | Tecnología                                    | Versión Clave |
|:-------------------|:----------------------------------------------| :--- |
| **Backend**        | Spring Boot                                   | 3.x |
| **Colap**          | Modelo entrenado por el equipo de Data Ciense |
| **Persistencia**   | MySQL                                         | Base de datos principal |
| **Migraciones DB** | Flyway                                        | Gestión de esquemas de BD |
| **Utilidades**     | Lombok                                        | Reducción de código boilerplate |
| **Seguridad**      | Spring Security                               | Autenticación y Autorización |
| **Serialización**  | Jackson (`ObjectMapper`)                      | Manejo de JSON de la IA |

## ⚙️ Configuración y Despliegue Local

### 1. Prerrequisitos

Asegúrate de tener instalado lo siguiente:

* Java Development Kit (JDK) 17 o superior.
* Maven.
* MySQL Server (o Docker para facilitar la ejecución).
* Una clave API válida para **Mistral AI** (necesaria para el servicio de clasificación).



Crea o modifica tu archivo `src/main/resources/application.properties` (o `application.yml`) y añade las siguientes configuraciones, reemplazando los valores de ejemplo con tus credenciales reales:

```properties
# CONFIGURACIÓN DE BASE DE DATOS MYSQL
spring.datasource.url=jdbc:mysql://localhost:3306/sentimentdb
spring.datasource.username=tu_usuario_mysql
spring.datasource.password=tu_contraseña_mysql
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
```
### Configuracion de base de datos usando variables de entorno
```
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
```

### 3. Rutas

## endPoinds
LISTANDO COMENTARIOS
  * formato json (Importante si no da error 403)
  [
    { "texto": "comentario................"}
  ]
  
    * Home
    http://localhost:8090/
    
    * lista los 20 ultimos comentarios y susu estadisticas
    http://localhost:8090/stats
    
    * se puede ingresar el numero deseado de comentarios a anzalizar  como parametro
    http://localhost:8090/stats?size=50
    

  * mediante archivos .CSV
  * BODY(multipart:FormData)  name:file
   http://localhost:8090/sentiment/upload-csv
  
    

## Proyecto Preeliminar(todas los requisitos basicos listos)

### URL del SentimenAPI(montado en azure capa de prueba)(YA SE PUEDEN PROBAR EN POSTMANT INSOMNIA, ETC)
```
https://lipa-sentiment-api.azurewebsites.net/sentiment
```
### URL API PYTHON
```
https://rlipac-python-api.hf.space/
```
### Repositorio de Backend Java(elaborado por el equipo backend)
```
https://github.com/rlipac31/back_sentimenAPI_Hakaton.git
```

### Repositorio de API PYTHON ( elaborado por el equipo de Data)
```
https://github.com/rlipac31/API-PYTHON_sentimentAPI.git

```
### URL  Dataset 
```
https://drive.google.com/drive/folders/1-83KeJKAytLJoX0y9JmYJ3u9yQMMdcro?usp=sharing
```



