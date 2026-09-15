/**
 * Representa la existencia de un premio dentro de una sucursal.
 *
 * El análisis de requerimientos dejó claro que la cantidad disponible no es un
 * dato del premio sino de la relación entre la sucursal y el premio, ya que el
 * caso de uso dice que cada sucursal tiene su propio inventario y un mismo
 * premio puede ofrecerse en varias de ellas.
 *
 * Lo que identifica de manera natural a un renglón del inventario es la pareja
 * formada por la sucursal y el premio. Como el manejador de archivos trabaja
 * con
 * una sola llave numérica, aquí se usa además un identificador propio, y la
 * unicidad de la pareja se revisa aparte al dar de alta un renglón.
 */
public class InventarioPremio implements Registrable {

  /** Identificador propio del renglón, usado como llave en el archivo CSV. */
  private int idInventario;

  /** Llave de la sucursal que tiene el premio en existencia. */
  private int idSucursal;

  /** Llave del premio del que se lleva la existencia. */
  private int idPremio;

  /** Cantidad de piezas del premio que hay en esa sucursal. */
  private int cantidadDisponible;

  /**
   * Constructor por omisión.
   */
  public InventarioPremio() {
    this.idInventario = 0;
    this.idSucursal = 0;
    this.idPremio = 0;
    this.cantidadDisponible = 0;
  }

  /**
   * Construye un renglón del inventario.
   *
   * @param idInventario       Identificador del renglón.
   * @param idSucursal         Llave de la sucursal que tiene el premio.
   * @param idPremio           Llave del premio disponible.
   * @param cantidadDisponible Cantidad de piezas en existencia.
   */
  public InventarioPremio(int idInventario, int idSucursal, int idPremio, int cantidadDisponible) {
    this.idInventario = idInventario;
    this.idSucursal = idSucursal;
    this.idPremio = idPremio;
    this.cantidadDisponible = cantidadDisponible;
  }

  // ------------------------------------------------------------ Getters ----

  /**
   * Regresa el identificador del renglón del inventario.
   *
   * @return El identificador del renglón.
   */
  public int getIdInventario() {
    return this.idInventario;
  }

  /**
   * Regresa la llave de la sucursal.
   *
   * @return La llave de la sucursal.
   */
  public int getIdSucursal() {
    return this.idSucursal;
  }

  /**
   * Regresa la llave del premio.
   *
   * @return La llave del premio.
   */
  public int getIdPremio() {
    return this.idPremio;
  }

  /**
   * Regresa la cantidad de piezas en existencia.
   *
   * @return La cantidad disponible.
   */
  public int getCantidadDisponible() {
    return this.cantidadDisponible;
  }

  // ------------------------------------------------------------ Setters ----

  /**
   * Cambia el identificador del renglón.
   *
   * @param idInventario Nuevo identificador.
   */
  public void setIdInventario(int idInventario) {
    this.idInventario = idInventario;
  }

  /**
   * Cambia la sucursal del renglón.
   *
   * @param idSucursal Nueva llave de sucursal.
   */
  public void setIdSucursal(int idSucursal) {
    this.idSucursal = idSucursal;
  }

  /**
   * Cambia el premio del renglón.
   *
   * @param idPremio Nueva llave de premio.
   */
  public void setIdPremio(int idPremio) {
    this.idPremio = idPremio;
  }

  /**
   * Cambia la cantidad de piezas en existencia.
   *
   * @param cantidadDisponible Nueva cantidad.
   */
  public void setCantidadDisponible(int cantidadDisponible) {
    this.cantidadDisponible = cantidadDisponible;
  }

  // --------------------------------------------------------------- CSV ----

  /**
   * Regresa la llave que identifica al renglón dentro de su archivo CSV.
   *
   * @return La llave del renglón.
   */
  @Override
  public int getLlave() {
    return this.idInventario;
  }

  /**
   * Convierte el renglón del inventario a una línea con formato CSV.
   *
   * @return Una línea lista para escribirse en el archivo CSV.
   */
  @Override
  public String toCSV() {
    return CSVUtil.unir(
        String.valueOf(this.idInventario),
        String.valueOf(this.idSucursal),
        String.valueOf(this.idPremio),
        String.valueOf(this.cantidadDisponible));
  }

  /**
   * Reconstruye un renglón del inventario a partir de una línea del archivo CSV.
   *
   * @param lineaCSV Línea del archivo con los datos del renglón.
   * @return Una nueva instancia con los datos de la línea.
   * @throws IllegalArgumentException Si la línea no tiene todas las columnas.
   * @throws NumberFormatException    Si alguna columna no es un número.
   */
  public static InventarioPremio fromCSV(String lineaCSV) throws IllegalArgumentException, NumberFormatException {
    String[] datos = CSVUtil.parsear(lineaCSV);

    if (datos.length < 4) {
      throw new IllegalArgumentException("La línea del CSV no tiene todas las columnas del inventario.");
    }

    int idInventario = Integer.parseInt(datos[0].trim());
    int idSucursal = Integer.parseInt(datos[1].trim());
    int idPremio = Integer.parseInt(datos[2].trim());
    int cantidadDisponible = Integer.parseInt(datos[3].trim());

    return new InventarioPremio(idInventario, idSucursal, idPremio, cantidadDisponible);
  }

  /**
   * Regresa los datos del renglón con un formato legible para la consola.
   *
   * @return Los datos del renglón listos para imprimirse.
   */
  @Override
  public String toString() {
    String separador = "-".repeat(38);

    return separador + "Inventario" + separador + "\n" +
        "ID Inventario\t\t: " + this.idInventario + "\n" +
        "ID Sucursal\t\t: " + this.idSucursal + "\n" +
        "ID Premio\t\t: " + this.idPremio + "\n" +
        "Cantidad Disponible\t: " + this.cantidadDisponible + " piezas\n" +
        separador + separador.substring(0, 10) + "\n";
  }
}
