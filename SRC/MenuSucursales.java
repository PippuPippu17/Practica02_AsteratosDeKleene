/**
 * Menú que administra las sucursales del centro de entretenimiento.
 *
 * Hereda de {@link MenuEntidad} todo el flujo del CRUD y el manejo de
 * excepciones, y aquí solo se define qué datos se capturan y cómo se
 * reconstruye
 * una sucursal a partir de una línea del archivo CSV.
 */
public class MenuSucursales extends MenuEntidad<Sucursal> {

  /** Ruta del archivo del inventario, para avisar antes de borrar. */
  private final String rutaInventario;

  /**
   * Construye el menú de sucursales.
   *
   * @param rutaArchivo    Ruta del archivo CSV de sucursales.
   * @param rutaInventario Ruta del archivo CSV del inventario.
   */
  public MenuSucursales(String rutaArchivo, String rutaInventario) {
    super(rutaArchivo, "sucursal", "Sucursales", "la");
    this.rutaInventario = rutaInventario;
  }

  /**
   * Pide por consola todos los datos de una sucursal nueva.
   *
   * @return La sucursal capturada.
   * @throws Exception Si el horario capturado no es válido.
   */
  @Override
  protected Sucursal capturar() throws Exception {
    int llave = EntradaConsola.leerEntero("Llave de la sucursal (debe ser número): ");

    return construir(llave);
  }

  /**
   * Pide por consola los datos nuevos de una sucursal existente.
   *
   * @param llave    Llave de la sucursal que se edita.
   * @param original Sucursal tal como está guardada.
   * @return La sucursal con los datos nuevos.
   * @throws Exception Si el horario capturado no es válido.
   */
  @Override
  protected Sucursal capturarEdicion(int llave, Sucursal original) throws Exception {
    return construir(llave);
  }

  /**
   * Reconstruye una sucursal a partir de una línea del archivo CSV.
   *
   * @param lineaCSV Línea leída del archivo.
   * @return La sucursal reconstruida.
   * @throws Exception Si la línea no tiene el formato esperado.
   */
  @Override
  protected Sucursal desdeCSV(String lineaCSV) throws Exception {
    return Sucursal.fromCSV(lineaCSV);
  }

  /**
   * Avisa cuántos renglones del inventario dependen de la sucursal antes de
   * borrarla.
   *
   * @param llave Llave de la sucursal que se va a eliminar.
   * @return true si el borrado puede continuar.
   */
  @Override
  protected boolean confirmarEliminacion(int llave) {
    try {
      int dependientes = IntegridadReferencial.inventarioDeSucursal(rutaInventario, llave).size();

      if (dependientes > 0) {
        System.out.println("Atención: esta sucursal tiene " + dependientes
            + " premio(s) en su inventario. Esos renglones quedarían apuntando a una sucursal que ya no existe.");
      }

    } catch (Exception e) {
      System.out.println("No se pudo revisar el inventario: " + e.getMessage());
    }

    return super.confirmarEliminacion(llave);
  }

  /**
   * Captura los campos comunes al alta y a la edición de una sucursal.
   *
   * @param llave Llave que llevará la sucursal.
   * @return La sucursal construida con los datos capturados.
   * @throws Exception Si el horario capturado no es válido.
   */
  private Sucursal construir(int llave) throws Exception {
    String nombre = EntradaConsola.leerTexto("Nombre de la sucursal: ");
    String calle = EntradaConsola.leerTexto("Calle: ");
    int numExterior = EntradaConsola.leerEntero("Número exterior: ", 1, 99999);
    String numInterior = EntradaConsola.leerTextoOpcional("Número interior (deja vacío si no aplica): ", "S/N");
    String colonia = EntradaConsola.leerTexto("Colonia: ");
    String estado = EntradaConsola.leerTexto("Estado: ");
    long telefono = EntradaConsola.leerTelefono("Teléfono (10 dígitos): ");
    String horario = EntradaConsola.leerHorario("Horario (ejemplo: Lun-Vie 11:00-21:00|Sab-Dom 10:00-22:00): ");

    return new Sucursal(llave, nombre, calle, numExterior, numInterior, colonia, estado, telefono, horario);
  }
}
