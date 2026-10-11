## Sistema de Biblioteca Escolar - Java Swing & MySQL

## 👤 Autor del proyecto

● Nombre: Giuseppe Sabaini

● Carrera: Programador Computacional

● Institución: Duoc UC Online

## 📦 Descripción general del sistema

Este proyecto corresponde al desarrollo e implementación de un Sistema de Gestión de Biblioteca Escolar en Java, estructurado mediante interfaces gráficas de usuario con Java Swing y persistencia relacional en MySQL. El sistema se rige bajo una arquitectura MVC (Modelo-Vista-Controlador) optimizada y modular, garantizando una separación limpia de responsabilidades y un código robusto.

El sistema incorpora las siguientes características clave:

Patrón Arquitectónico MVC: Organizado de forma estricta en paquetes de apoyo (app, connection, controller, dao, model, util, view).

Persistencia MySQL: Conexión optimizada mediante un diseño Singleton thread-safe en DatabaseConnection.

Control de Acceso por Roles (RBAC): Permisos y vistas adaptativas para dos tipos de usuarios:

Bibliotecario: Acceso integral a la gestión de libros, control de estudiantes, registro de préstamos/devoluciones y generación de reportes estadísticos.

Estudiante: Acceso enfocado a la consulta del catálogo bibliográfico y solicitud de préstamos personales.

## 🧱 Estructura General del Proyecto:
```
📁 src/
├── app/              # Contiene la clase de ejecución principal del sistema
├── connection/       # Gestión de conexión a base de datos (Singleton thread-safe)
├── controller/       # Controladores encargados de la lógica de negocio y eventos
├── dao/              # Objetos de Acceso a Datos (DAO) para la interacción con MySQL
├── model/            # Clases del dominio (Estudiante, Libro, Prestamo, Usuario, Categoria, ReporteResultado)
├── util/             # Clases de utilidad y gestión global (Sesion activa)
└── view/             # Interfaces gráficas en Java Swing (Ventanas, formularios y utilidades Ui)
```

⚙️ Instrucciones para clonar y ejecutar el proyecto

Clona el repositorio desde GitHub:



Importa y abre el proyecto en IntelliJ IDEA.

Configura la base de datos en tu servidor MySQL local utilizando el esquema relacional provisto por el sistema.

Verifica los parámetros de conexión en el paquete connection.

Ejecuta la clase de inicio en el paquete app para iniciar la aplicación.

Repositorio GitHub: 
