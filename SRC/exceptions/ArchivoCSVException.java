package exceptions;

public class ArchivoCSVException extends Exception {
    public ArchivoCSVException() {
        super("El archivo CSV no es válido.");
    }

    public ArchivoCSVException(String mensaje) {
        super(mensaje);
    }

    public ArchivoCSVException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}