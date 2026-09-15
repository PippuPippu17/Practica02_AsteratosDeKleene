import exceptions.ValidacionException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un cliente del centro de entretenimiento.
 *
 * La clase refleja tres decisiones que se tomaron en el análisis de
 * requerimientos y que la versión anterior no respetaba:
 *
 * Los correos y los teléfonos son atributos multivaluados, ya que el caso de
 * uso
 * habla de ellos en plural. Dentro del archivo CSV se guardan en una sola
 * columna, separados con punto y coma, para no tener que fijar de antemano
 * cuántos puede tener cada cliente.
 *
 * La fecha de nacimiento se guarda como texto con el formato DD/MM/AAAA. Antes
 * era un número entero, lo cual perdía el cero inicial de los días menores a
 * diez y hacía ambiguo un valor como 1012001.
 *
 * La edad no se almacena, se calcula a partir de la fecha de nacimiento. Era el
 * único dato del sistema que podía contradecir a otro.
 */
public class Cliente implements Registrable {

  /** Carácter que separa los valores dentro de una columna multivaluada. */
  public static final String SEPARADOR_MULTIVALUADO = ";";

  /** Identificador único del cliente. */
  private int idCliente;

  /** Nombre o nombres de pila del cliente. */
  private String nombreCliente;

  /** Apellido paterno del cliente. */
  private String apellidoP;

  /** Apellido materno del cliente. */
  private String apellidoM;

  /** Fecha de nacimiento con formato DD/MM/AAAA. */
  private String fechaNac;

  /** Sexo o género del cliente: "Masculino", "Femenino" o "No binario". */
  private String sexo;

  /** Correos electrónicos de contacto. Es un atributo multivaluado. */
  private List<String> correos;

  /** Números telefónicos de contacto. Es un atributo multivaluado. */
  private List<String> telefonos;

  /**
   * Constructor por omisión.
   */
  public Cliente() {
    this.idCliente = 0;
    this.nombreCliente = "";
    this.apellidoP = "";
    this.apellidoM = "";
    this.fechaNac = "";
    this.sexo = "";
    this.correos = new ArrayList<>();
    this.telefonos = new ArrayList<>();
  }

  /**
   * Construye un cliente con todos sus atributos.
   *
   * @param idCliente     Identificador del cliente.
   * @param nombreCliente Nombre o nombres del cliente.
   * @param apellidoP     Apellido paterno del cliente.
   * @param apellidoM     Apellido materno del cliente.
   * @param fechaNac      Fecha de nacimiento con formato DD/MM/AAAA.
   * @param sexo          Sexo o género del cliente.
   * @param correos       Correos electrónicos de contacto.
   * @param telefonos     Números telefónicos de contacto.
   */
  public Cliente(int idCliente, String nombreCliente, String apellidoP, String apellidoM, String fechaNac,
      String sexo, List<String> correos, List<String> telefonos) {
    this.idCliente = idCliente;
    this.nombreCliente = nombreCliente;
    this.apellidoP = apellidoP;
    this.apellidoM = apellidoM;
    this.fechaNac = fechaNac;
    this.sexo = sexo;
    this.correos = (correos != null) ? new ArrayList<>(correos) : new ArrayList<>();
    this.telefonos = (telefonos != null) ? new ArrayList<>(telefonos) : new ArrayList<>();
  }

  // ------------------------------------------------------------ Getters ----

  /**
   * Regresa el identificador del cliente.
   *
   * @return El identificador del cliente.
   */
  public int getIdCliente() {
    return this.idCliente;
  }

  /**
   * Regresa el nombre del cliente.
   *
   * @return El nombre del cliente.
   */
  public String getNombreCliente() {
    return this.nombreCliente;
  }

  /**
   * Regresa el apellido paterno del cliente.
   *
   * @return El apellido paterno del cliente.
   */
  public String getApellidoP() {
    return this.apellidoP;
  }

  /**
   * Regresa el apellido materno del cliente.
   *
   * @return El apellido materno del cliente.
   */
  public String getApellidoM() {
    return this.apellidoM;
  }

  /**
   * Regresa la fecha de nacimiento del cliente.
   *
   * @return La fecha de nacimiento con formato DD/MM/AAAA.
   */
  public String getFechaNac() {
    return this.fechaNac;
  }

  /**
   * Regresa el sexo o género del cliente.
   *
   * @return El sexo del cliente.
   */
  public String getSexo() {
    return this.sexo;
  }

  /**
   * Regresa los correos electrónicos del cliente.
   *
   * Se entrega una copia para que nadie modifique la lista interna sin pasar por
   * los métodos de la clase.
   *
   * @return Los correos del cliente.
   */
  public List<String> getCorreos() {
    return new ArrayList<>(this.correos);
  }

  /**
   * Regresa los teléfonos del cliente.
   *
   * Se entrega una copia para que nadie modifique la lista interna sin pasar por
   * los métodos de la clase.
   *
   * @return Los teléfonos del cliente.
   */
  public List<String> getTelefonos() {
    return new ArrayList<>(this.telefonos);
  }

  /**
   * Calcula la edad del cliente a partir de su fecha de nacimiento.
   *
   * La edad no se almacena en ningún lado, se obtiene siempre de la fecha, de
   * modo que no puede quedar desactualizada ni contradecirla.
   *
   * @return La edad en años cumplidos, o -1 si la fecha no puede interpretarse.
   */
  public int getEdad() {
    try {
      String[] partes = this.fechaNac.split("/");
      LocalDate nacimiento = LocalDate.of(
          Integer.parseInt(partes[2]),
          Integer.parseInt(partes[1]),
          Integer.parseInt(partes[0]));

      return Period.between(nacimiento, LocalDate.now()).getYears();

    } catch (RuntimeException e) {
      return -1;
    }
  }

  /**
   * Regresa el rango de edad que le corresponde al cliente para los premios.
   *
   * @return "Infantil", "Juvenil", "Adulto" o "Sin determinar" si no hay fecha.
   */
  public String getRangoEdad() {
    int edad = getEdad();

    if (edad < 0) {
      return "Sin determinar";
    }
    if (edad <= 12) {
      return "Infantil";
    }
    if (edad <= 17) {
      return "Juvenil";
    }

    return "Adulto";
  }

  // ------------------------------------------------------------ Setters ----

  /**
   * Cambia el identificador del cliente.
   *
   * @param idCliente Nuevo identificador.
   */
  public void setIdCliente(int idCliente) {
    this.idCliente = idCliente;
  }

  /**
   * Cambia el nombre del cliente.
   *
   * @param nombreCliente Nuevo nombre.
   */
  public void setNombreCliente(String nombreCliente) {
    this.nombreCliente = nombreCliente;
  }

  /**
   * Cambia el apellido paterno del cliente.
   *
   * @param apellidoP Nuevo apellido paterno.
   */
  public void setApellidoP(String apellidoP) {
    this.apellidoP = apellidoP;
  }

  /**
   * Cambia el apellido materno del cliente.
   *
   * @param apellidoM Nuevo apellido materno.
   */
  public void setApellidoM(String apellidoM) {
    this.apellidoM = apellidoM;
  }

  /**
   * Cambia la fecha de nacimiento del cliente.
   *
   * @param fechaNac Nueva fecha con formato DD/MM/AAAA.
   * @throws ValidacionException Si la fecha no existe en el calendario.
   */
  public void setFechaNac(String fechaNac) throws ValidacionException {
    this.fechaNac = Validador.validarFecha(fechaNac);
  }

  /**
   * Cambia el sexo o género del cliente.
   *
   * @param sexo Nuevo sexo.
   */
  public void setSexo(String sexo) {
    this.sexo = sexo;
  }

  /**
   * Sustituye la lista de correos del cliente.
   *
   * @param correos Nuevos correos.
   */
  public void setCorreos(List<String> correos) {
    this.correos = (correos != null) ? new ArrayList<>(correos) : new ArrayList<>();
  }

  /**
   * Sustituye la lista de teléfonos del cliente.
   *
   * @param telefonos Nuevos teléfonos.
   */
  public void setTelefonos(List<String> telefonos) {
    this.telefonos = (telefonos != null) ? new ArrayList<>(telefonos) : new ArrayList<>();
  }

  /**
   * Agrega un correo a la lista del cliente.
   *
   * @param correo Correo a agregar.
   */
  public void agregarCorreo(String correo) {
    this.correos.add(correo);
  }

  /**
   * Agrega un teléfono a la lista del cliente.
   *
   * @param telefono Teléfono a agregar.
   */
  public void agregarTelefono(String telefono) {
    this.telefonos.add(telefono);
  }

  // --------------------------------------------------------------- CSV ----

  /**
   * Regresa la llave que identifica al cliente dentro de su archivo CSV.
   *
   * @return La llave del cliente.
   */
  @Override
  public int getLlave() {
    return this.idCliente;
  }

  /**
   * Convierte el cliente a una línea con formato CSV.
   *
   * Los correos y los teléfonos se unen con punto y coma dentro de su columna, y
   * el campo completo se escapa con {@link CSVUtil} igual que los demás.
   *
   * @return Una línea lista para escribirse en el archivo CSV.
   */
  @Override
  public String toCSV() {
    return CSVUtil.unir(
        String.valueOf(this.idCliente),
        this.nombreCliente,
        this.apellidoP,
        this.apellidoM,
        this.fechaNac,
        this.sexo,
        String.join(SEPARADOR_MULTIVALUADO, this.correos),
        String.join(SEPARADOR_MULTIVALUADO, this.telefonos));
  }

  /**
   * Reconstruye un cliente a partir de una línea del archivo CSV.
   *
   * @param lineaCSV Línea del archivo con los datos de un cliente.
   * @return Una nueva instancia de Cliente con los datos de la línea.
   * @throws IllegalArgumentException Si la línea no tiene todas las columnas.
   * @throws NumberFormatException    Si la llave no es un número.
   */
  public static Cliente fromCSV(String lineaCSV) throws IllegalArgumentException, NumberFormatException {
    String[] datos = CSVUtil.parsear(lineaCSV);

    if (datos.length < 8) {
      throw new IllegalArgumentException("La línea del CSV no tiene todas las columnas de un cliente.");
    }

    int idCliente = Integer.parseInt(datos[0].trim());
    String nombreCliente = datos[1].trim();
    String apellidoP = datos[2].trim();
    String apellidoM = datos[3].trim();
    String fechaNac = datos[4].trim();
    String sexo = datos[5].trim();
    List<String> correos = separarValores(datos[6]);
    List<String> telefonos = separarValores(datos[7]);

    return new Cliente(idCliente, nombreCliente, apellidoP, apellidoM, fechaNac, sexo, correos, telefonos);
  }

  /**
   * Separa el contenido de una columna multivaluada en la lista de valores que la
   * componen.
   *
   * @param columna Texto de la columna, con los valores unidos por punto y coma.
   * @return Lista con los valores, sin espacios sobrantes ni entradas vacías.
   */
  private static List<String> separarValores(String columna) {
    List<String> valores = new ArrayList<>();

    if (columna == null || columna.trim().isEmpty()) {
      return valores;
    }

    for (String valor : columna.split(SEPARADOR_MULTIVALUADO)) {
      if (!valor.trim().isEmpty()) {
        valores.add(valor.trim());
      }
    }

    return valores;
  }

  /**
   * Regresa los datos del cliente con un formato legible para la consola.
   *
   * @return Los datos del cliente listos para imprimirse.
   */
  @Override
  public String toString() {
    String separador = "-".repeat(40);
    int edad = getEdad();

    return separador + "Cliente" + separador + "\n" +
        "ID Cliente\t\t: " + this.idCliente + "\n" +
        "Nombre\t\t\t: " + this.nombreCliente + "\n" +
        "Apellidos\t\t: " + this.apellidoP + " " + this.apellidoM + "\n" +
        "Fecha de Nacimiento\t: " + this.fechaNac + "\n" +
        "Edad\t\t\t: " + ((edad >= 0) ? (edad + " años (" + getRangoEdad() + ")") : "Sin determinar") + "\n" +
        "Sexo\t\t\t: " + this.sexo + "\n" +
        "Correos\t\t\t: " + String.join(", ", this.correos) + "\n" +
        "Teléfonos\t\t: " + String.join(", ", this.telefonos) + "\n" +
        separador + separador.substring(0, 7) + "\n";
  }
}
