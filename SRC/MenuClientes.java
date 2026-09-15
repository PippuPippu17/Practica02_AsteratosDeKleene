import java.util.List;

/**
 * Menú que administra a los clientes del centro de entretenimiento.
 *
 * Hereda de {@link MenuEntidad} todo el flujo del CRUD y el manejo de
 * excepciones, y aquí solo se define qué datos se capturan y cómo se
 * reconstruye
 * un cliente a partir de una línea del archivo CSV.
 */
public class MenuClientes extends MenuEntidad<Cliente> {

  /** Cantidad máxima de correos que se le pueden registrar a un cliente. */
  private static final int MAX_CORREOS = 3;

  /** Cantidad máxima de teléfonos que se le pueden registrar a un cliente. */
  private static final int MAX_TELEFONOS = 3;

  /**
   * Construye el menú de clientes.
   *
   * @param rutaArchivo Ruta del archivo CSV de clientes.
   */
  public MenuClientes(String rutaArchivo) {
    super(rutaArchivo, "cliente", "Clientes", "el");
  }

  /**
   * Pide por consola todos los datos de un cliente nuevo.
   *
   * @return El cliente capturado.
   */
  @Override
  protected Cliente capturar() {
    int llave = EntradaConsola.leerEntero("Llave del cliente (debe ser número): ");

    return construir(llave);
  }

  /**
   * Pide por consola los datos nuevos de un cliente existente.
   *
   * @param llave    Llave del cliente que se edita.
   * @param original Cliente tal como está guardado.
   * @return El cliente con los datos nuevos.
   */
  @Override
  protected Cliente capturarEdicion(int llave, Cliente original) {
    return construir(llave);
  }

  /**
   * Reconstruye un cliente a partir de una línea del archivo CSV.
   *
   * @param lineaCSV Línea leída del archivo.
   * @return El cliente reconstruido.
   */
  @Override
  protected Cliente desdeCSV(String lineaCSV) {
    return Cliente.fromCSV(lineaCSV);
  }

  /**
   * Captura los campos comunes al alta y a la edición de un cliente.
   *
   * La edad ya no se pregunta, se calcula a partir de la fecha de nacimiento. Los
   * correos y los teléfonos se capturan como listas, ya que el caso de uso los
   * describe en plural.
   *
   * @param llave Llave que llevará el cliente.
   * @return El cliente construido con los datos capturados.
   */
  private Cliente construir(int llave) {
    String nombre = EntradaConsola.leerTexto("Nombre del cliente: ");
    String apellidoP = EntradaConsola.leerTexto("Apellido paterno: ");
    String apellidoM = EntradaConsola.leerTexto("Apellido materno: ");
    String fechaNac = EntradaConsola.leerFecha("Fecha de nacimiento (DD/MM/AAAA): ");
    String sexo = EntradaConsola.leerDeCatalogo("Sexo", Validador.SEXOS);

    List<String> correos = EntradaConsola.leerVarios("correo", EntradaConsola::leerCorreo, MAX_CORREOS);
    List<String> telefonos = EntradaConsola.leerVarios("teléfono", EntradaConsola::leerTelefonoTexto, MAX_TELEFONOS);

    return new Cliente(llave, nombre, apellidoP, apellidoM, fechaNac, sexo, correos, telefonos);
  }
}
