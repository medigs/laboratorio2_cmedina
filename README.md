# Laboratorio 2 — Sistema de Gestión de Usuarios

Aplicación de escritorio desarrollada en Java con interfaz gráfica (Swing) que implementa un sistema de autenticación y administración de usuarios con control de roles y seguridad de acceso.

---

## Requisitos

- Java 11 o superior (probado con OpenJDK 25)
- No requiere dependencias externas ni herramientas de build

---

## Ejecución

```bash
# Compilar
javac *.java

# Ejecutar
java Login
```

---

## Credenciales de acceso

| Usuario   | Contraseña       | Rol            |
|-----------|------------------|----------------|
| admin     | Admin123!        | Administrador  |
| usuario1  | Clave2024@       | Usuario        |
| usuario2  | Passw0rd#        | Usuario        |
| usuario3  | Segura456$       | Usuario        |
| usuario4  | MiClave789%      | Usuario        |
| usuario5  | Contraseña321&   | Usuario        |

---

## Flujo de la aplicación

```
Login
  └── Credenciales correctas → Menú Principal
        ├── Mantenimiento de Usuarios  (JDialog modal)
        └── Reinicio de Clave          (JDialog modal)
  └── Cerrar Sesión → Login
```

---

## Funcionalidades

### Login
- Valida usuario y contraseña contra el registro interno
- Muestra intentos restantes en cada fallo
- Bloquea automáticamente la cuenta tras **3 intentos fallidos** y notifica al usuario que contacte al administrador
- Rechaza el acceso a cuentas desactivadas

### Menú Principal
- Muestra tarjetas de acceso rápido (clic abre el módulo correspondiente)
- Barra de menú con acceso a los mismos módulos
- Opción **Cerrar Sesión** con confirmación

### Mantenimiento de Usuarios
Accesible para todos los usuarios, pero las acciones están restringidas por rol:

| Acción              | Usuario | Administrador |
|---------------------|---------|---------------|
| Ver usuarios        | ✅      | ✅            |
| Agregar usuario     | ❌      | ✅            |
| Asignar rol         | ❌      | ✅            |
| Cambiar rol         | ❌      | ✅            |
| Desactivar usuario  | ❌      | ✅            |
| Reactivar usuario   | ❌      | ✅            |

- Muestra dos pestañas: **Usuarios Activos** e **Usuarios Inactivos**
- Los usuarios desactivados no son eliminados, solo se marcan como inactivos
- Al reactivar un usuario se reinician sus intentos fallidos
- El usuario `admin` no puede ser desactivado ni cambiarle el rol

### Reinicio de Clave
- **Administrador:** puede cambiar la contraseña de cualquier usuario activo sin necesidad de ingresar la clave actual
- **Usuario regular:** solo puede cambiar su propia contraseña, requiere ingresar la clave actual
- Indicador visual de fortaleza de contraseña en tiempo real

---

## Política de contraseñas

Toda contraseña nueva debe cumplir:
- Mínimo **13 caracteres**
- Al menos **1 letra mayúscula**
- Al menos **1 carácter especial** (`!@#$%^&*()_+-=[]{}|;':",./<>?`)

---

## Estructura del proyecto

```
lab2/
├── Login.java                  # Pantalla de autenticación y punto de entrada
├── MenuPrincipal.java          # Ventana principal con JMenuBar y tarjetas
├── MantenimientoUsuarios.java  # JDialog: gestión de usuarios y roles
└── ReinicioClave.java          # JDialog: cambio de contraseña
```

---

## Roles disponibles

| Rol            | Descripción                                      |
|----------------|--------------------------------------------------|
| Usuario        | Acceso de solo lectura a la gestión de usuarios  |
| Administrador  | Control total: crear, desactivar y asignar roles |
