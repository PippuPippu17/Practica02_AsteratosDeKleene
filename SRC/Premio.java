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
  private int idPremio;
  private String nombre;
  private String categoria;
  private String rangoEdad;
  private int puntosRequeridos;
  private double valorAproximado;

  public Premio() {
    idPremio = 0;
    nombre = "";
    categoria = "";
    rangoEdad = "";
    puntosRequeridos = 0;
    valorAproximado = 0;
  }

  public Premio(int idPremio, String nombre, String categoria, String rangoEdad, int puntosRequeridos,
      double valorAproximado) {
    this.idPremio = idPremio;
    this.nombre = nombre;
    this.categoria = categoria;
    this.rangoEdad = rangoEdad;
    this.puntosRequeridos = puntosRequeridos;
    this.valorAproximado = valorAproximado;
  }

  public int getIdPremio() {
    return idPremio;
  }

  public void setIdPremio(int idPremio) {
    this.idPremio = idPremio;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public String getRangoEdad() {
    return rangoEdad;
  }

  public void setRangoEdad(String rangoEdad) {
    this.rangoEdad = rangoEdad;
  }

  public int getPuntosRequeridos() {
    return puntosRequeridos;
  }

  public void setPuntosRequeridos(int puntosRequeridos) {
    this.puntosRequeridos = puntosRequeridos;
  }

  public double getValorAproximado() {
    return valorAproximado;
  }

  public void setValorAproximado(double valorAproximado) {
    this.valorAproximado = valorAproximado;
  }

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
