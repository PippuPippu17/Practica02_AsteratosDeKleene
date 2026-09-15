package exceptions;

/**
 * Excepción que se lanza cuando se busca un registro por su llave dentro de un
 * archivo CSV y dicha llave no existe.
 *
 * Se separa de {@link ArchivoCSVException} porque no representa un fallo del
 * archivo ni de la entrada y salida, sino una consulta válida cuyo resultado
 * está vacío. Al ser una excepción revisada, el compilador obliga a que todos
 * los métodos que buscan por llave contemplen este caso de manera explícita.
 */
public class RegistroNoEncontradoException extends Exception {

  /**
   * Construye la excepción con un mensaje genérico.
   */
  public RegistroNoEncontradoException() {
    super("No se encontró ningún registro con la llave indicada.");
  }

  /**
   * Construye la excepción con un mensaje personalizado.
   *
   * @param mensaje Descripción del error.
   */
  public RegistroNoEncontradoException(String mensaje) {
    super(mensaje);
  }

  /**
   * Construye la excepción con un mensaje personalizado y la causa original.
   *
   * @param mensaje Descripción del error.
   * @param causa   Excepción que originó este error.
   */
  public RegistroNoEncontradoException(String mensaje, Throwable causa) {
    super(mensaje, causa);
  }

  /**
   * Construye la excepción a partir de la llave y el archivo consultados.
   *
   * @param id          Llave que se buscó.
   * @param rutaArchivo Archivo en el que se realizó la búsqueda.
   */
  public RegistroNoEncontradoException(int id, String rutaArchivo) {
    super("No existe ningún registro con la llave '" + id + "' en el archivo '" + rutaArchivo + "'.");
  }
}
