import exceptions.ValidacionException;

/**
 * Menú que administra el inventario de premios de cada sucursal.
 *
 * Hereda de {@link MenuEntidad} todo el flujo del CRUD. Lo propio de este menú
 * es que las dos llaves que captura, la de la sucursal y la del premio, se
 * verifican contra sus archivos antes de guardar, y que la pareja no puede
 * repetirse.
 */
public class MenuInventario extends MenuEntidad<InventarioPremio> {

  /** Ruta del archivo CSV de sucursales, para verificar la referencia. */
  private final String rutaSucursales;

  /** Ruta del archivo CSV de premios, para verificar la referencia. */
  private final String rutaPremios;

  /**
   * Construye el menú del inventario.
   *
   * @param rutaArchivo    Ruta del archivo CSV del inventario.
   * @param rutaSucursales Ruta del archivo CSV de sucursales.
   * @param rutaPremios    Ruta del archivo CSV de premios.
   */
  public MenuInventario(String rutaArchivo, String rutaSucursales, String rutaPremios) {
    super(rutaArchivo, "existencia", "Inventario de premios", "la");
    this.rutaSucursales = rutaSucursales;
    this.rutaPremios = rutaPremios;
  }

  /**
   * Pide por consola los datos de un renglón nuevo del inventario.
   *
   * @return El renglón capturado.
   * @throws ValidacionException Si la sucursal o el premio no existen, o si la
   *                             pareja ya estaba registrada.
   */
  @Override
  protected InventarioPremio capturar() throws ValidacionException {
    int llave = EntradaConsola.leerEntero("Llave de la existencia (debe ser número): ");
    int idSucursal = leerSucursal();
    int idPremio = leerPremio();

    IntegridadReferencial.validarParejaLibre(rutaArchivo, idSucursal, idPremio);

    int cantidad = EntradaConsola.leerEntero("Cantidad disponible: ", 0, 99999);

    return new InventarioPremio(llave, idSucursal, idPremio, cantidad);
  }

  /**
   * Pide por consola los datos nuevos de un renglón existente.
   *
   * La sucursal y el premio no se vuelven a preguntar, ya que son los que
   * identifican al renglón. Lo que cambia es la cantidad disponible.
   *
   * @param llave    Llave del renglón que se edita.
   * @param original Renglón tal como está guardado.
   * @return El renglón con la cantidad nueva.
   */
  @Override
  protected InventarioPremio capturarEdicion(int llave, InventarioPremio original) {
    int cantidad = EntradaConsola.leerEntero("Nueva cantidad disponible: ", 0, 99999);

    return new InventarioPremio(llave, original.getIdSucursal(), original.getIdPremio(), cantidad);
  }

  /**
   * Reconstruye un renglón del inventario a partir de una línea del archivo CSV.
   *
   * @param lineaCSV Línea leída del archivo.
   * @return El renglón reconstruido.
   */
  @Override
  protected InventarioPremio desdeCSV(String lineaCSV) {
    return InventarioPremio.fromCSV(lineaCSV);
  }

  /**
   * Lee la llave de una sucursal y no la acepta hasta que exista.
   *
   * @return La llave de la sucursal.
   */
  private int leerSucursal() {
    while (true) {
      int llave = EntradaConsola.leerEntero("Llave de la sucursal: ");

      try {
        return IntegridadReferencial.validarExiste(rutaSucursales, llave, "sucursal");

      } catch (ValidacionException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Lee la llave de un premio y no la acepta hasta que exista.
   *
   * @return La llave del premio.
   */
  private int leerPremio() {
    while (true) {
      int llave = EntradaConsola.leerEntero("Llave del premio: ");

      try {
        return IntegridadReferencial.validarExiste(rutaPremios, llave, "premio");

      } catch (ValidacionException e) {
        System.out.println(e.getMessage());
      }
    }
  }
}
