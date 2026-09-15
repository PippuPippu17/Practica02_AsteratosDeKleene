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

  /** Encabezado del archivo de sucursales. */
  private static final String ENCABEZADO_SUCURSALES = "idSucursal,nombre,calle,numExterior,numeroInterior,colonia,estado,telefono,horario";

  /** Encabezado del archivo de premios. */
  private static final String ENCABEZADO_PREMIOS = "idPremio,nombre,categoria,rangoEdad,puntosRequeridos,valorAproximado,idSucursal,stock";

  /** Encabezado del archivo de clientes. */
  private static final String ENCABEZADO_CLIENTES = "idCliente,nombreCliente,apellidoP,apellidoM,fechaNac,edad,sexo,correoE,telefonoC";

  /**
   * Muestra las opciones del menú principal.
   */
  private static void mostrarMenuPrincipal() {
    System.out.println("\nPUELLA GAME");
    System.out.println("1. Gestionar sucursales");
    System.out.println("2. Gestionar premios");
    System.out.println("3. Gestionar clientes");
    System.out.println("4. Salir");
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

    MenuSucursales menuSucursales = new MenuSucursales(RUTA_SUCURSALES);
    MenuPremios menuPremios = new MenuPremios(RUTA_PREMIOS);
    MenuClientes menuClientes = new MenuClientes(RUTA_CLIENTES);

    boolean salir = false;

    while (!salir) {
      try {
        mostrarMenuPrincipal();
        int opcion = EntradaConsola.leerOpcion("Selecciona una opción: ", 1, 4);

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
