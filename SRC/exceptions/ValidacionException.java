package exceptions;

/**
 * Excepción que se lanza cuando un dato capturado por el usuario no cumple con
 * las reglas definidas para su campo.
 *
 * Cubre tanto las restricciones de dominio, como que el sexo de un cliente sea
 * uno de los tres valores permitidos, como las restricciones de formato, por
 * ejemplo que un teléfono tenga exactamente diez dígitos.
 *
 * Se distingue de {@link ArchivoCSVException} porque no indica un problema con
 * el archivo, sino con la información que se pretende guardar en él.
 */
public class ValidacionException extends Exception {

  /**
   * Construye la excepción con un mensaje genérico.
   */
  public ValidacionException() {
    super("El dato ingresado no es válido.");
  }

  /**
   * Construye la excepción con un mensaje personalizado.
   *
   * @param mensaje Descripción del error.
   */
  public ValidacionException(String mensaje) {
    super(mensaje);
  }

  /**
   * Construye la excepción con un mensaje personalizado y la causa original.
   *
   * @param mensaje Descripción del error.
   * @param causa   Excepción que originó este error.
   */
  public ValidacionException(String mensaje, Throwable causa) {
    super(mensaje, causa);
  }

  /**
   * Construye la excepción indicando el campo y el valor que resultaron
   * inválidos.
   *
   * @param campo Nombre del campo que se estaba validando.
   * @param valor Valor que el usuario intentó capturar.
   * @param regla Descripción de la regla que no se cumplió.
   */
  public ValidacionException(String campo, String valor, String regla) {
    super("El campo '" + campo + "' no admite el valor '" + valor + "': " + regla);
  }
}
