package exceptions;

public class HorarioException extends Exception {
    public HorarioException() {
        super("El horario no es válido.");
    }
        
    public HorarioException(String mensaje) {
        super(mensaje);
    }

    public HorarioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
