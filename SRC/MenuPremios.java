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

  /** Ruta del archivo del inventario, para avisar antes de borrar un premio. */
  private final String rutaInventario;

  /**
   * Construye el menú de premios.
   *
   * @param rutaArchivo    Ruta del archivo CSV de premios.
   * @param rutaInventario Ruta del archivo CSV del inventario.
   */
  public MenuPremios(String rutaArchivo, String rutaInventario) {
    super(rutaArchivo, "premio", "Premios", "el");
    this.rutaInventario = rutaInventario;
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
   * Avisa cuántos renglones del inventario dependen del premio antes de borrarlo.
   *
   * @param llave Llave del premio que se va a eliminar.
   * @return true si el borrado puede continuar.
   */
  @Override
  protected boolean confirmarEliminacion(int llave) {
    try {
      int dependientes = IntegridadReferencial.inventarioDePremio(rutaInventario, llave).size();

      if (dependientes > 0) {
        System.out.println("Atención: este premio aparece en el inventario de " + dependientes
            + " sucursal(es). Esos renglones quedarían apuntando a un premio que ya no existe.");
      }

    } catch (Exception e) {
      System.out.println("No se pudo revisar el inventario: " + e.getMessage());
    }

    return super.confirmarEliminacion(llave);
  }

  /**
   * Captura los campos comunes al alta y a la edición de un premio.
   *
   * La sucursal y la cantidad disponible ya no se piden aquí, porque pertenecen
   * al inventario y no al premio en sí.
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

    return new Premio(llave, nombre, categoria, rangoEdad, puntosRequeridos, valorAproximado);
  }

  /**
   * Lee los puntos que pide un premio y no los acepta hasta que correspondan a su
   * categoría.
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
