package exceptions;

/**
 * Excepción que se lanza cuando ocurre un problema con un archivo CSV.
 *
 * Cubre los fallos propios del archivo y de su manipulación: que no exista, que
 * no se pueda leer o escribir, que le falte el encabezado, que una línea tenga
 * un formato que no se puede interpretar, o que se intente agregar un registro
 * con una llave que ya está ocupada.
 *
 * No debe usarse para indicar que una búsqueda no encontró nada, ya que para
 * eso
 * existe {@link RegistroNoEncontradoException}, ni para señalar que un dato
 * capturado incumple una regla, que corresponde a {@link ValidacionException}.
 */
public class ArchivoCSVException extends Exception {

  /**
   * Construye la excepción con un mensaje genérico.
   */
  public ArchivoCSVException() {
    super("El archivo CSV no es válido.");
  }

  /**
   * Construye la excepción con un mensaje personalizado.
   *
   * @param mensaje Descripción del error.
   */
  public ArchivoCSVException(String mensaje) {
    super(mensaje);
  }

  /**
   * Construye la excepción con un mensaje personalizado y la causa original.
   *
   * Conservar la causa permite rastrear el error de entrada y salida que
   * realmente ocurrió, sin exponerlo directamente al usuario.
   *
   * @param mensaje Descripción del error.
   * @param causa   Excepción que originó este error.
   */
  public ArchivoCSVException(String mensaje, Throwable causa) {
    super(mensaje, causa);
  }
}
