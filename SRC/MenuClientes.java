/**
 * Menú que administra a los clientes del centro de entretenimiento.
 *
 * Hereda de {@link MenuEntidad} todo el flujo del CRUD y el manejo de
 * excepciones, y aquí solo se define qué datos se capturan y cómo se
 * reconstruye
 * un cliente a partir de una línea del archivo CSV.
 */
public class MenuClientes extends MenuEntidad<Cliente> {

  /**
   * Construye el menú de clientes.
   *
   * @param rutaArchivo Ruta del archivo CSV de clientes.
   */
  public MenuClientes(String rutaArchivo) {
    super(rutaArchivo, "cliente", "Clientes");
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
   * @param llave Llave que llevará el cliente.
   * @return El cliente construido con los datos capturados.
   */
  private Cliente construir(int llave) {
    String nombre = EntradaConsola.leerTexto("Nombre del cliente: ");
    String apellidoP = EntradaConsola.leerTexto("Apellido paterno: ");
    String apellidoM = EntradaConsola.leerTexto("Apellido materno: ");
    int fechaNac = EntradaConsola.leerFecha("Fecha de nacimiento (DDMMAAAA): ");
    int edad = EntradaConsola.leerEntero("Edad: ", Validador.EDAD_MINIMA, Validador.EDAD_MAXIMA);
    String sexo = EntradaConsola.leerDeCatalogo("Sexo", Validador.SEXOS);
    String correo = EntradaConsola.leerCorreo("Correo electrónico: ");
    long telefono = EntradaConsola.leerTelefono("Teléfono (10 dígitos): ");

    return new Cliente(llave, nombre, apellidoP, apellidoM, fechaNac, edad, sexo, correo, telefono);
  }
}
