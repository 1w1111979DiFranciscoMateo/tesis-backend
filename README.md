# Proyecto de Tesis Back-end: Sistema de Recomendación Audiovisual
> Proyecto Back-end de mi Tesis 2025 como Técnico en Programación.


 ## Objetivo del Proyecto
 El objetivo del sistema es brindar información para la recomendación personalizada de contenido audiovisual, la gestión de listas personalizadas de películas, series o documentales y el 
 seguimiento del historial de visualización, contribuyendo al proceso de toma de decisiones del usuario al momento de elegir qué ver en sus plataformas de streaming.


## Límites y Alcances


### Límites
* **Desde:** La creación de un nuevo usuario, incluyendo la personalización de sus preferencias de contenido audiovisual (géneros, plataformas disponibles).
* **Hasta:** Que el usuario encuentra una recomendación adecuada para ver, gestione su contenido mediante listas personalizadas o participa en la comunidad a través de valoraciones y comentarios.


### Alcances del Proyecto


#### Gestión de Usuarios
* Registrar, Modificar, Consultar usuarios.
* Registrar y Modificar preferencias de contenido audiovisual del usuario.
* Registrar y Modificar plataformas de streaming disponibles del usuario.
* Generar, Modificar, Consultar perfil de usuarios.
* Generar listado de usuarios.
* Generar estadísticas de uso de los usuarios.

#### Gestión de Listas
* Registrar listas personalizadas.
* Modificar listas permitiendo agregar o quitar contenido.
* Eliminar listas.
* Consultar listas creadas por los usuarios.
* Generar estadísticas de uso de las listas.

#### Gestión de Recomendados
* Consultar recomendaciones de contenido basadas en los gustos y el historial del usuario.
* Generar recomendaciones personalizadas para cada usuario.
* Generar recomendaciones personalizadas para cada usuario basado en sus plataformas de streaming disponibles.

#### Gestión de Contenido Audiovisual
* Consultar información detallada sobre cada contenido audiovisual, incluyendo descripción, reparto, plataforma de streaming disponible y calificaciones.
* Consultar contenido audiovisual a través de un buscador.

#### Gestión de Interacciones
* Registrar, Modificar, Consultar, Eliminar comentarios en los contenidos audiovisuales.
* Actualizar puntaje de los comentarios.
* Registrar, Modificar, Consultar, Eliminar puntuaciones para contenido audiovisual.
* Generar estadísticas de puntuaciones de contenido audiovisual.


## Tecnologías Utilizadas
* **Lenguaje:** Java 17
* **Framework:** Spring Boot
* **Gestor de Dependencias:** Maven
* **Base de Datos:** PostgreSQL
* **API Externa:** [TMDB API](https://www.themoviedb.org/documentation/api)
* **Seguridad:** JWT (JSON Web Token)


## Configuración y Ejecución
### Requisitos Previos
Para que el proyecto funcione correctamente, debes tener instalada una instancia de **PostgreSQL**.

1. Crear una base de datos llamada `tesis-db`.
2. Configurar la conexión en el puerto `5433` (o modificar el `application.properties`).
3. Credenciales por defecto:
   - **Usuario:** `postgres`
   - **Password:** `123`

> La base de datos debe estar vacía; Hibernate se encargará de crear la estructura automáticamente al iniciar la aplicación.

> Para correr el proyecto clona el repositorio y abrir la carpeta del proyecto en su IDE favorito (preferentemente IntelliJ) y luego presionar el botón de "Run".


## Presentación del Proyecto
link: https://youtu.be/5UaU4D24wpM



