package exceptions;

/**
 * Excepción que se lanza cuando el horario de una sucursal no cumple con el
 * formato o con las reglas que debe respetar.
 *
 * Se usa para todos los problemas del horario: que esté vacío, que un día no
 * exista, que los días no vayan en orden cronológico, que un mismo día reciba
 * dos rangos distintos, que la hora o los minutos queden fuera de su rango, o
 * que la hora de cierre no sea posterior a la de apertura.
 */
public class HorarioException extends Exception {

  /**
   * Construye la excepción con un mensaje genérico.
   */
  public HorarioException() {
    super("El horario no es válido.");
  }

  /**
   * Construye la excepción con un mensaje personalizado.
   *
   * @param mensaje Descripción del error.
   */
  public HorarioException(String mensaje) {
    super(mensaje);
  }

  /**
   * Construye la excepción con un mensaje personalizado y la causa original.
   *
   * @param mensaje Descripción del error.
   * @param causa   Excepción que originó este error.
   */
  public HorarioException(String mensaje, Throwable causa) {
    super(mensaje, causa);
  }
}
