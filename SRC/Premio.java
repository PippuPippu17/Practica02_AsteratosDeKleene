import java.util.Locale;

/**
 * Representa un premio del catálogo del centro de entretenimiento.
 *
 * El premio describe únicamente al artículo: su nombre, su categoría, el rango
 * de edad al que va dirigido, su valor aproximado y los puntos que cuesta
 * canjearlo. La cantidad disponible no vive aquí, porque cada sucursal tiene su
 * propio inventario y un mismo premio puede existir en varias de ellas. Esa
 * relación se guarda en {@link InventarioPremio}.
 */
public class Premio implements Registrable {

  /** Identificador único del premio dentro del catálogo. */
  private int idPremio;

  /** Nombre con el que se conoce al premio. */
  private String nombre;

  /** Categoría del premio: "Bajo", "Medio" o "Grande". */
  private String categoria;

  /** Rango de edad al que va dirigido: "Infantil", "Juvenil" o "Adulto". */
  private String rangoEdad;

  /** Puntos que un jugador necesita acumular para canjear el premio. */
  private int puntosRequeridos;

  /** Valor aproximado del premio en pesos mexicanos. */
  private double valorAproximado;

  /**
   * Constructor por omisión, que deja el premio con valores vacíos.
   */
  public Premio() {
    idPremio = 0;
    nombre = "";
    categoria = "";
    rangoEdad = "";
    puntosRequeridos = 0;
    valorAproximado = 0;
  }

  /**
   * Construye un premio con todos sus atributos.
   *
   * @param idPremio         Identificador del premio.
   * @param nombre           Nombre del premio.
   * @param categoria        Categoría: "Bajo", "Medio" o "Grande".
   * @param rangoEdad        Rango de edad: "Infantil", "Juvenil" o "Adulto".
   * @param puntosRequeridos Puntos necesarios para canjearlo.
   * @param valorAproximado  Valor aproximado en pesos mexicanos.
   */
  public Premio(int idPremio, String nombre, String categoria, String rangoEdad, int puntosRequeridos,
      double valorAproximado) {
    this.idPremio = idPremio;
    this.nombre = nombre;
    this.categoria = categoria;
    this.rangoEdad = rangoEdad;
    this.puntosRequeridos = puntosRequeridos;
    this.valorAproximado = valorAproximado;
  }

  /**
   * Regresa el identificador del premio.
   *
   * @return El identificador del premio.
   */
  public int getIdPremio() {
    return idPremio;
  }

  /**
   * Cambia el identificador del premio.
   *
   * @param idPremio Nuevo identificador.
   */
  public void setIdPremio(int idPremio) {
    this.idPremio = idPremio;
  }

  /**
   * Regresa el nombre del premio.
   *
   * @return El nombre del premio.
   */
  public String getNombre() {
    return nombre;
  }

  /**
   * Cambia el nombre del premio.
   *
   * @param nombre Nuevo nombre.
   */
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  /**
   * Regresa la categoría del premio.
   *
   * @return La categoría del premio.
   */
  public String getCategoria() {
    return categoria;
  }

  /**
   * Cambia la categoría del premio.
   *
   * @param categoria Nueva categoría.
   */
  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  /**
   * Regresa el rango de edad al que va dirigido el premio.
   *
   * @return El rango de edad del premio.
   */
  public String getRangoEdad() {
    return rangoEdad;
  }

  /**
   * Cambia el rango de edad del premio.
   *
   * @param rangoEdad Nuevo rango de edad.
   */
  public void setRangoEdad(String rangoEdad) {
    this.rangoEdad = rangoEdad;
  }

  /**
   * Regresa los puntos necesarios para canjear el premio.
   *
   * @return Los puntos requeridos.
   */
  public int getPuntosRequeridos() {
    return puntosRequeridos;
  }

  /**
   * Cambia los puntos necesarios para canjear el premio.
   *
   * @param puntosRequeridos Nuevos puntos requeridos.
   */
  public void setPuntosRequeridos(int puntosRequeridos) {
    this.puntosRequeridos = puntosRequeridos;
  }

  /**
   * Regresa el valor aproximado del premio.
   *
   * @return El valor aproximado en pesos mexicanos.
   */
  public double getValorAproximado() {
    return valorAproximado;
  }

  /**
   * Cambia el valor aproximado del premio.
   *
   * @param valorAproximado Nuevo valor aproximado.
   */
  public void setValorAproximado(double valorAproximado) {
    this.valorAproximado = valorAproximado;
  }

  /**
   * Convierte el premio a una línea con formato CSV.
   *
   * El valor aproximado se formatea con {@code Locale.US} para que el separador
   * decimal sea siempre un punto, sin importar el idioma de la computadora donde
   * se ejecute el programa.
   *
   * @return Una línea lista para escribirse en el archivo CSV.
   */
  @Override
  public String toCSV() {
    return CSVUtil.unir(
        String.valueOf(this.idPremio),
        this.nombre,
        this.categoria,
        this.rangoEdad,
        String.valueOf(this.puntosRequeridos),
        String.format(Locale.US, "%.2f", this.valorAproximado));
  }

  /**
   * Reconstruye una instancia de Premio a partir de una línea del archivo CSV.
   *
   * @param lineaCSV Línea del archivo con los datos de un premio.
   * @return Una nueva instancia de Premio con los datos de la línea.
   * @throws IllegalArgumentException Si la línea no tiene todas las columnas.
   * @throws NumberFormatException    Si alguna columna numérica no lo es.
   */
  public static Premio fromCSV(String lineaCSV) throws IllegalArgumentException, NumberFormatException {
    String[] datos = CSVUtil.parsear(lineaCSV);
    if (datos.length < 6) {
      throw new IllegalArgumentException("Error: la línea CSV no tiene todas las columnas que necestiamos.");
    }

    int idPremio = Integer.parseInt(datos[0].trim());
    String nombre = datos[1].trim();
    String categoria = datos[2].trim();
    String rangoEdad = datos[3].trim();
    int puntosRequeridos = Integer.parseInt(datos[4].trim());
    double valorAproximado = Double.parseDouble(datos[5].trim());

    return new Premio(idPremio, nombre, categoria, rangoEdad, puntosRequeridos, valorAproximado);
  }

  /**
   * Regresa los datos del premio con un formato legible para la consola.
   *
   * @return Los datos del premio listos para imprimirse.
   */
  @Override
  public String toString() {
    return "-----------------------------------------Premio-----------------------------------------\n" +
        "ID Premio\t\t: " + idPremio + "\n" +
        "Nombre\t\t\t: " + nombre + "\n" +
        "Categoría\t\t: " + categoria + "\n" +
        "Rango de Edad\t\t: " + rangoEdad + "\n" +
        "Puntos Requeridos\t: " + puntosRequeridos + " pts\n" +
        "Valor Aproximado\t: $" + String.format(Locale.US, "%.2f", valorAproximado) + " MXN\n" +
        "---------------------------------------------------------------------------------------";
  }

  /**
   * Regresa la llave que identifica a el premio dentro de su archivo CSV.
   *
   * @return La llave del premio.
   */
  @Override
  public int getLlave() {
    return this.idPremio;
  }

}
