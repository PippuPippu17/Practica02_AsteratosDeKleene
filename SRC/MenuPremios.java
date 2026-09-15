import exceptions.ValidacionException;

/**
 * Menú que administra el catálogo de premios del centro de entretenimiento.
 *
 * Hereda de {@link MenuEntidad} todo el flujo del CRUD y el manejo de
 * excepciones, y aquí solo se define qué datos se capturan y cómo se
 * reconstruye
 * un premio a partir de una línea del archivo CSV.
 */
public class MenuPremios extends MenuEntidad<Premio> {

  /**
   * Construye el menú de premios.
   *
   * @param rutaArchivo Ruta del archivo CSV de premios.
   */
  public MenuPremios(String rutaArchivo) {
    super(rutaArchivo, "premio", "Premios");
  }

  /**
   * Pide por consola todos los datos de un premio nuevo.
   *
   * @return El premio capturado.
   */
  @Override
  protected Premio capturar() {
    int llave = EntradaConsola.leerEntero("Llave del premio (debe ser número): ");

    return construir(llave);
  }

  /**
   * Pide por consola los datos nuevos de un premio existente.
   *
   * @param llave    Llave del premio que se edita.
   * @param original Premio tal como está guardado.
   * @return El premio con los datos nuevos.
   */
  @Override
  protected Premio capturarEdicion(int llave, Premio original) {
    return construir(llave);
  }

  /**
   * Reconstruye un premio a partir de una línea del archivo CSV.
   *
   * @param lineaCSV Línea leída del archivo.
   * @return El premio reconstruido.
   */
  @Override
  protected Premio desdeCSV(String lineaCSV) {
    return Premio.fromCSV(lineaCSV);
  }

  /**
   * Captura los campos comunes al alta y a la edición de un premio.
   *
   * @param llave Llave que llevará el premio.
   * @return El premio construido con los datos capturados.
   */
  private Premio construir(int llave) {
    String nombre = EntradaConsola.leerTexto("Nombre del premio: ");
    String categoria = EntradaConsola.leerDeCatalogo("Categoría", Validador.CATEGORIAS_PREMIO);
    String rangoEdad = EntradaConsola.leerDeCatalogo("Rango de edad", Validador.RANGOS_EDAD);
    int puntosRequeridos = leerPuntos(categoria);
    double valorAproximado = EntradaConsola.leerMontoPositivo("Valor aproximado en MXN: ", "valor aproximado");
    int idSucursal = EntradaConsola.leerEntero("Llave de la sucursal donde está disponible: ");
    int stock = EntradaConsola.leerEntero("Cantidad disponible en existencia: ", 0, 99999);

    return new Premio(llave, nombre, categoria, rangoEdad, puntosRequeridos, valorAproximado, idSucursal, stock);
  }

  /**
   * Lee los puntos que pide un premio y no los acepta hasta que correspondan a su
   * categoría.
   *
   * La regla proviene del caso de uso: los premios bajos van de 20 a 1,000
   * puntos, los medios de 1,001 a 3,999 y los grandes de 4,000 en adelante.
   *
   * @param categoria Categoría del premio, ya validada contra su catálogo.
   * @return Los puntos capturados.
   */
  private int leerPuntos(String categoria) {
    while (true) {
      int puntos = EntradaConsola.leerEntero("Puntos requeridos: ");

      try {
        return Validador.validarPuntosSegunCategoria(categoria, puntos);

      } catch (ValidacionException e) {
        System.out.println(e.getMessage());
      }
    }
  }
}
