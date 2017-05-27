# ClassRoomManagement

Información General
--------------
ClassRoomManagement es una aplicación para Android que se basa en el uso del gps y reconocimiento de marcadores, para poder ubicar y proporcionar información sobre las aulas, a miembros de la comunidad universitaria como: alumnos, profesores y PAS.
Las funcionalidades más destacadas son las siguientes:
- Login con la cuenta de Google e inclusión de un formulario de registro, donde se indica, entre otra cosas, el rol u ocupación dentro de la comunidad universitaria.
- Localización de edificios de interés (facultades, escuelas, etc) vía GPS, mostrando al mismo tiempo la posición real del usuario para facilitar su guidado hasta el edificio.
- Reconocimiento de marcadores situados en las salas de los edificios mediante la cámara, que servirá para proporcionar información de los horarios de los eventos producidos en esa aula.

[Demostración de la aplicación](https://www.dropbox.com/s/l7p7v4i39a3wexk/Video_APM_ClassRoomManagment.mp4?dl=0)

Tecnologías utilizadas
--------------
- Java (https://www.java.com/es/)
- Gradle (https://gradle.org/)
- Plataforma Android (Google Play Services SDK, Google Maps API)
- Vuforia SDK (https://developer.vuforia.com/downloads/sdk)
- Amazon RDS (https://aws.amazon.com/es/rds/)
- Android Studio (https://developer.android.com/studio/index.html?hl=es-419)

Compilación
--------------
El proyecto tiene la estructura que define Android Studio para los proyectos Android. Para incorporarlo al Android Studio y compilarlo, solo es necesario ir al menu "File" -> "New" -> "Project From Version Control" e introducir la url del repositorio (https://github.com/AlejandroFernandezGarcia/ClassRoomManagement.git) para clonarlo en local. Una vez clonado el repositorio, es necesario añadir las herramientas necesarias del SDK. Para ello, se accede al menú "Tools" -> "Android" -> "SDK Manager". Será necesario tener incluidas y actualizadas las siguientes herramientas:

![Android SDK Tools](http://i.imgur.com/sEQhw0C.png)

Llegado a este punto, se podrá compilar la aplicación para ejecutarla en el emulador o bien en un dispositivo Android.

Además, también se proporciona en el repositorio dentro de la carpeta "Build", un apk de prueba de la aplicación.
