# ClassRoomManagement

Información General
--------------
ClassRoomManagement es una aplicación de realidad aumentada que se basa en el reconocimiento de marcadores para proporcionar infomración sobre los horarios de aulas a los usuarios,
que principalmente serán alumnos de universidades y los profesores. 
Algunas de las funcionalidades incluidas:
- Localización de edificios de interés (facultades, escuelas, etc) vía GPS, al mismo tiempo que se muestra la localización actual.
- Login con la cuenta de Google e inclusión de un formulario de registro, donde se indica el rol (Profesor o alumno).
- Reconocimiento de marcadores mediante la cámara que servirá para obtener la información de los horarios y clases impartidas en las distintas aulas.

Tecnologías utilizadas
--------------
- Plataforma Android (Google Play Services SDK, Google Maps API)
- Vuforia SDK (https://developer.vuforia.com/downloads/sdk)
- Unity 3D (se utilizarán los paquetes de Vuforia Unity. Página oficial Unity: https://unity3d.com/es)
- Amazon RDS (https://aws.amazon.com/es/rds/)

Compilación
--------------
El proyecto está listo para ser compilado desde el Android Studio.
Para utilizar Android Studio (incluye el SDK de Android), instalarlo desde aquí: https://developer.android.com/studio/index.html
Se puede seguir el siguiente tutorial de instalación: https://developer.android.com/studio/install.html?hl=es-419
Una vez instalado y configurado, utilizar la opción de "Project from version control" escogiendo la opción de GitHub. Introduzca la URL del repositorio (https://github.com/AlejandroFernandezGarcia/ClassRoomManagement.git) y tus credenciales de GitHub.
Una vez clonado el repositorio, es necesario añadir las herramientas necesarias al SDK. Para ello, se accede al menú "Tools" -> "Android" -> "SDK Manager". Será necesario tener incluidas y actualizadas las siguientes herramientas:

![Android SDK Tools](http://i.imgur.com/sEQhw0C.png)

- Llegado a este punto, se podrá compilar la aplicación para ejecutarla en el emulador o bien en un dispositivo Android.
También se puede generar el APK desde la opción de "Build" -> "Build APK" y luego copiarlo a nuestro dispositivo para instalar la aplicación.