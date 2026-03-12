# Proyecto de Tesis Back-end
 Proyecto Back-end de mi Tesis 2025 como Técnico en Programación.


 **Objetivo del Proyecto:**
 El objetivo del sistema es brindar información para la recomendación personalizada de contenido audiovisual, la gestión de listas personalizadas de películas, series o documentales y el 
 seguimiento del historial de visualización, contribuyendo al proceso de toma de decisiones del usuario al momento de elegir qué ver en sus plataformas de streaming.


 **Límites del Proyecto:**
 Desde: La creación de un nuevo usuario, incluyendo la personalización de sus preferencias de contenido audiovisual (géneros, plataformas disponibles).
 Hasta: Que el usuario encuentra una recomendación adecuada para ver, gestione su contenido mediante listas personalizadas o participa en la comunidad a través de valoraciones y comentarios.


**Alcances del Proyecto:**
Gestión de Usuarios: 
* Registrar, Modificar, Consultar usuarios.
* Registrar y Modificar preferencias de contenido audiovisual del usuario.
* Registrar y Modificar plataformas de streaming disponibles del usuario.
* Generar, Modificar, Consultar perfil de usuarios.
* Generar listado de usuarios.
* Generar estadísticas de uso de los usuarios.

Gestión de Listas:
* Registrar listas personalizadas.
* Modificar listas permitiendo agregar o quitar contenido.
* Eliminar listas.
* Consultar listas creadas por los usuarios.
* Generar estadísticas de uso de las listas.

Gestión de Recomendados:
* Consultar recomendaciones de contenido basadas en los gustos y el historial del usuario.
* Generar recomendaciones personalizadas para cada usuario.
* Generar recomendaciones personalizadas para cada usuario basado en sus plataformas de streaming disponibles.

Gestión de Contenido Audiovisual:
* Consultar información detallada sobre cada contenido audiovisual, incluyendo descripción, reparto, plataforma de streaming disponible y calificaciones.
* Consultar contenido audiovisual a través de un buscador.

Gestión de Interacciones:
* Registrar, Modificar, Consultar, Eliminar comentarios en los contenidos audiovisuales.
* Actualizar puntaje de los comentarios.
* Registrar, Modificar, Consultar, Eliminar puntuaciones para contenido audiovisual.
* Generar estadísticas de puntuaciones de contenido audiovisual.


**Tecnologías del Proyecto:**
Para la realización del Back-end del proyecto se utilizaron las siguientes tencologías:
* Java 17
* Spring Boot
* Maven
* PostgreSQL
* API externa de TMDB.
* JWT (para el manejo de usuarios)


**Como correr el proyecto**
Para que el proyecto de back-end funcione correctamente es necesario tener una base de datos ya creada en PostgreSQL llamada tesis-db en el puerto 5433 con username postgres y password 123 (La base de datos tiene que estar vacía, toda la estructura se va a crear sola una vez se ejecuta el proyecto)
Para correr el proyecto clona el repositorio y abrir la carpeta del proyecto en su IDE favorito (preferentemente IntelliJ) y luego presionar el botón de "Run".

**Presentación del Proyecto:**
link: https://youtu.be/5UaU4D24wpM



