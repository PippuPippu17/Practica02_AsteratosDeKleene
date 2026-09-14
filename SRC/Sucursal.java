
import java.util.ArrayList;
import java.util.List;

public class Sucursal {
    int idSucursal = 0;
    String nombre = "";
    String calle = "";
    int numExterior = 0;
    String numeroInterior = "";
    String colonia = "";
    String estado = "";
    long telefono = 0;
    Horario horario = new Horario(); // Formato "Lun-Vie 11:00-21:00/Sab-Dom 10:00-22:00"
}

class Horario {
    List<String> horarios = new ArrayList<>();
    
}
