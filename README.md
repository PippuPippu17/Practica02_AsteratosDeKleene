# Práctica 02: Sistema de Archivos vs. Bases de Datos

**Equipo:** Asteratos de Kleene
**Caso de uso:** Centro de Entretenimiento Familiar "PuellaGame"

Prototipo de consola que captura información de sucursales, premios, clientes e
inventario, y la guarda en archivos `.CSV` para poblar más adelante la base de
datos del centro de entretenimiento.

## Requisitos

- JDK 21 o superior
- `make`

## Uso

Desde la raíz del repositorio:

```bash
make          # compila el proyecto
make run      # compila si hace falta y ejecuta la aplicación
```

Para volver a ejecutar basta con `make run`. Solo se recompila cuando algún
archivo fuente cambió.

## Comandos disponibles

| Comando                 | Qué hace                                                 |
| ----------------------- | -------------------------------------------------------- |
| `make` o `make compile` | Compila las clases y deja los `.class` dentro de `SRC/`. |
| `make run`              | Compila y ejecuta la aplicación.                         |
| `make doc`              | Genera la documentación Javadoc en `SRC/Doc/`.           |
| `make all`              | Compila y genera la documentación.                       |
| `make clean`            | Borra los `.class` y la documentación generada.          |
| `make help`             | Muestra la lista de comandos.                            |

## Cómo funciona la aplicación

Al arrancar, el menú principal ofrece cuatro entidades:

1. Sucursales
2. Premios
3. Clientes
4. Inventario de premios

En cada una se puede agregar, consultar por llave, editar y eliminar. Toda la
información se guarda en los archivos `.CSV` de la carpeta `SRC/`, así que
persiste entre ejecuciones.

Los archivos se crean solos con su encabezado la primera vez que se ejecuta el
programa, de modo que se puede empezar desde cero borrándolos.

## Estructura

```
SRC/
├── Main.java                    Arranque y menú principal
├── MenuEntidad.java             CRUD común a los cuatro menús
├── MenuSucursales.java
├── MenuPremios.java
├── MenuClientes.java
├── MenuInventario.java
├── Sucursal.java                Entidades
├── Premio.java
├── Cliente.java
├── InventarioPremio.java
├── Horario.java
├── Registrable.java             Contrato de las entidades
├── HandlerCSV.java              Lectura y escritura de los archivos
├── CSVUtil.java                 Escape y separación de campos
├── EntradaConsola.java          Lectura validada del teclado
├── Validador.java               Reglas de dominio y formato
├── IntegridadReferencial.java   Referencias entre entidades
├── exceptions/                  Excepciones propias
├── *.csv                        Datos
└── Doc/                         Javadoc (se genera con make doc)
```

## Notas

- La aplicación debe ejecutarse desde la raíz del repositorio, ya que las rutas
  de los archivos CSV se resuelven como `./SRC/archivo.csv`. `make run` ya lo
  hace por ti.
- Los correos y los teléfonos de un cliente se guardan en una sola columna,
  separados con punto y coma, porque son atributos multivaluados.
- La edad del cliente no se almacena: se calcula a partir de su fecha de
  nacimiento.
