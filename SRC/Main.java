import exceptions.ArchivoCSVException;

/**
 * Punto de entrada de la aplicación "PuellaGame".
 *
 * Su única responsabilidad es preparar los archivos de datos, mostrar el menú
 * principal y delegar cada opción al menú de la entidad correspondiente. Toda
 * la
 * lógica del CRUD vive en {@link MenuEntidad} y en sus tres subclases, la
 * lectura del teclado en {@link EntradaConsola} y las reglas de los datos en
 * {@link Validador}.
 */
public class Main {

  /** Ruta del archivo CSV donde se guardan las sucursales. */
  private static final String RUTA_SUCURSALES = "./SRC/sucursales.csv";

  /** Ruta del archivo CSV donde se guardan los premios. */
  private static final String RUTA_PREMIOS = "./SRC/premios.csv";

  /** Ruta del archivo CSV donde se guardan los clientes. */
  private static final String RUTA_CLIENTES = "./SRC/clientes.csv";

  /** Ruta del archivo CSV donde se guarda el inventario de premios. */
  private static final String RUTA_INVENTARIO = "./SRC/inventario.csv";

  /** Encabezado del archivo de sucursales. */
  private static final String ENCABEZADO_SUCURSALES = "idSucursal,nombre,calle,numExterior,numeroInterior,colonia,estado,telefono,horario";

  /** Encabezado del archivo de premios. */
  private static final String ENCABEZADO_PREMIOS = "idPremio,nombre,categoria,rangoEdad,puntosRequeridos,valorAproximado";

  /** Encabezado del archivo de clientes. */
  private static final String ENCABEZADO_CLIENTES = "idCliente,nombreCliente,apellidoP,apellidoM,fechaNac,sexo,correos,telefonos";

  /** Encabezado del archivo del inventario de premios. */
  private static final String ENCABEZADO_INVENTARIO = "idInventario,idSucursal,idPremio,cantidadDisponible";

  /**
   * Muestra las opciones del menú principal.
   */
  private static void mostrarMenuPrincipal() {
    System.out.println("\nPUELLA GAME");
    System.out.println("1. Gestionar sucursales");
    System.out.println("2. Gestionar premios");
    System.out.println("3. Gestionar clientes");
    System.out.println("4. Gestionar inventario de premios");
    System.out.println("5. Salir");
  }

  /**
   * Crea los tres archivos CSV con su encabezado en caso de que no existan o
   * estén vacíos.
   *
   * Se invoca una única vez al arrancar la aplicación. Si los archivos ya tienen
   * contenido el método no los modifica, de modo que la información capturada en
   * ejecuciones anteriores se conserva.
   *
   * @return true si los tres archivos quedaron listos, false si alguno falló.
   */
  private static boolean inicializarArchivos() {
    try {
      if (HandlerCSV.inicializarArchivo(RUTA_SUCURSALES, ENCABEZADO_SUCURSALES)) {
        System.out.println("Se creó el archivo '" + RUTA_SUCURSALES + "'.");
      }
      if (HandlerCSV.inicializarArchivo(RUTA_PREMIOS, ENCABEZADO_PREMIOS)) {
        System.out.println("Se creó el archivo '" + RUTA_PREMIOS + "'.");
      }
      if (HandlerCSV.inicializarArchivo(RUTA_CLIENTES, ENCABEZADO_CLIENTES)) {
        System.out.println("Se creó el archivo '" + RUTA_CLIENTES + "'.");
      }
      if (HandlerCSV.inicializarArchivo(RUTA_INVENTARIO, ENCABEZADO_INVENTARIO)) {
        System.out.println("Se creó el archivo '" + RUTA_INVENTARIO + "'.");
      }

      return true;

    } catch (ArchivoCSVException e) {
      System.out.println("No se pudieron preparar los archivos de datos: " + e.getMessage());
      System.out.println("Revisa que exista la carpeta 'SRC' y que tengas permisos de escritura.");
      return false;
    }
  }

  /**
   * Arranca la aplicación.
   *
   * @param args No se utilizan.
   */
  public static void main(String[] args) {
    if (!inicializarArchivos()) {
      return;
    }

    MenuSucursales menuSucursales = new MenuSucursales(RUTA_SUCURSALES, RUTA_INVENTARIO);
    MenuPremios menuPremios = new MenuPremios(RUTA_PREMIOS, RUTA_INVENTARIO);
    MenuClientes menuClientes = new MenuClientes(RUTA_CLIENTES);
    MenuInventario menuInventario = new MenuInventario(RUTA_INVENTARIO, RUTA_SUCURSALES, RUTA_PREMIOS);

    boolean salir = false;

    while (!salir) {
      try {
        mostrarMenuPrincipal();
        int opcion = EntradaConsola.leerOpcion("Selecciona una opción: ", 1, 5);

        switch (opcion) {
          case 1:
            menuSucursales.mostrar();
            break;
          case 2:
            menuPremios.mostrar();
            break;
          case 3:
            menuClientes.mostrar();
            break;
          case 4:
            menuInventario.mostrar();
            break;
          case 5:
            System.out.println("\nGracias por usar PuellaGame. Hasta pronto.");
            salir = true;
            break;
        }

        // Última red de seguridad: si algo se escapa de los menús, la aplicación
        // lo reporta y sigue funcionando en lugar de terminar.
      } catch (Exception e) {
        System.out.println("\nOcurrió un error inesperado: " + e.getMessage());
      }
    }

    EntradaConsola.cerrar();
  }
}
