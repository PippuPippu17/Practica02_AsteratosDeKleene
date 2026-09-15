/**
 * Esta clase representa a la entidad Cliente.
 */
public class Cliente implements Registrable {

  private int idCliente;
  private String nombreCliente;
  private String apellidoP;
  private String apellidoM;
  private int fechaNac;
  private int edad;
  private String sexo;
  private String correoE;
  private long telefonoC;

  /**
   * Constructor por omisión
   */
  public Cliente() {

    this.idCliente = 0;
    this.nombreCliente = "";
    this.apellidoP = "";
    this.apellidoM = "";
    this.fechaNac = 0;
    this.edad = 0;
    this.sexo = "";
    this.correoE = "";
    this.telefonoC = 0L;
  }

  /**
   * Constructor para inicializar un CLiente con sus atributos.
   *
   * @param idCliente     Identificador del cliente.
   * @param nombreCliente Nombre(s) del cliente.
   * @param apellidoP     Apellido paterno del cliente.
   * @param apellidoM     Apellido materno del cliente.
   * @param fechaNac      Fecha de nacimiento del cliente (DDMMAAAA)
   * @param edad          Edad del cliente.
   * @param sexo          Sexo o género del cliente.
   * @param correoE       Correo electrónico de contacto.
   * @param telefonoC     Número de teléfono celular o de contacto.
   */
  public Cliente(int idCliente, String nombreCliente, String apellidoP, String apellidoM, int fechaNac, int edad,
      String sexo,
      String correoE, long telefonoC) {

    this.idCliente = idCliente;
    this.nombreCliente = nombreCliente;
    this.apellidoP = apellidoP;
    this.apellidoM = apellidoM;
    this.fechaNac = fechaNac;
    this.edad = edad;
    this.sexo = sexo;
    this.correoE = correoE;
    this.telefonoC = telefonoC;
  }

  /**
   * Getter del identificador del cliente.
   *
   * @return El ID del cliente.
   */
  public int getIdCliente() {
    return idCliente;
  }

  /**
   * Establece el identificador del cliente.
   *
   * @param idCliente El nuevo ID a asignar.
   */
  public void setIdCliente(int idCliente) {
    this.idCliente = idCliente;
  }

  /**
   * Getter del nombre del cliente.
   *
   * @return El nombre del cliente.
   */
  public String getNombreCliente() {
    return nombreCliente;
  }

  /**
   * Establece el nombre del cliente.
   *
   * @param nombreCliente El nuevo nombre a asignar.
   */
  public void setNombreCliente(String nombreCliente) {
    this.nombreCliente = nombreCliente;
  }

  /**
   * Getter del apellido paterno del cliente.
   *
   * @return El apellido paterno.
   */
  public String getApellidoP() {
    return apellidoP;
  }

  /**
   * Establece el apellido paterno del cliente.
   *
   * @param apellidoP El nuevo apellido paterno a asignar.
   */
  public void setApellidoP(String apellidoP) {
    this.apellidoP = apellidoP;
  }

  /**
   * Getter del apellido materno del cliente.
   *
   * @return El apellido materno.
   */
  public String getApellidoM() {
    return apellidoM;
  }

  /**
   * Establece el apellido materno del cliente.
   *
   * @param apellidoM El nuevo apellido materno a asignar.
   */
  public void setApellidoM(String apellidoM) {
    this.apellidoM = apellidoM;
  }

  /**
   * Getter de la fecha de nacimiento del cliente.
   *
   * @return La fecha de nacimiento.
   */
  public int getFechaNac() {
    return fechaNac;
  }

  /**
   * Establece la fecha de nacimiento del cliente.
   *
   * @param fechaNac La nueva fecha de nacimiento a asignar.
   */
  public void setFechaNac(int fechaNac) {
    this.fechaNac = fechaNac;
  }

  /**
   * Getter de la edad del cliente.
   *
   * @return La edad del cliente.
   */
  public int getEdad() {
    return edad;
  }

  /**
   * Establece la edad del cliente.
   *
   * @param edad La nueva edad a asignar.
   */
  public void setEdad(int edad) {
    this.edad = edad;
  }

  /**
   * Obtiene el sexo del cliente.
   *
   * @return El sexo del cliente.
   */
  public String getSexo() {
    return sexo;
  }

  /**
   * Establece el sexo del cliente.
   *
   * @param sexo El nuevo sexo a asignar.
   */
  public void setSexo(String sexo) {
    this.sexo = sexo;
  }

  /**
   * Obtiene el correo electrónico del cliente.
   *
   * @return El correo electrónico.
   */
  public String getCorreoE() {
    return correoE;
  }

  /**
   * Establece el correo electrónico del cliente.
   *
   * @param correoE El nuevo correo electrónico a asignar.
   */
  public void setCorreoE(String correoE) {
    this.correoE = correoE;
  }

  /**
   * Getter del número de teléfono del cliente.
   *
   * @return El número telefónico.
   */
  public long getTelefonoC() {
    return telefonoC;
  }

  /**
   * Establece el número de teléfono del cliente.
   *
   * @param telefonoC El nuevo teléfono a asignar.
   */
  public void setTelefonoC(long telefonoC) {
    this.telefonoC = telefonoC;
  }

  /**
   * Convierte una instancia de Cliente a una línea con formato CSV.
   * Los campos de texto se escapan con {@link CSVUtil}.
   * 
   * @return Una cadena de texto con los datos del cliente separados por comas.
   */
  public String toCSV() {
    return CSVUtil.unir(
        String.valueOf(this.idCliente),
        this.nombreCliente,
        this.apellidoP,
        this.apellidoM,
        String.valueOf(this.fechaNac),
        String.valueOf(this.edad),
        this.sexo,
        this.correoE,
        String.valueOf(this.telefonoC));
  }

  /**
   * Método que recostruye una instancia de Cliente a partir de una línea de texto
   * del archvio CSV.
   * 
   * @param lineaCSV Línea del archivo CSV que tiene los datos de un cliente.
   * 
   * @return Una nueva instancia de la clase Cliente con los datos de la línea.
   * 
   * @throws IllegalArgumentException Si la línea CSV no tiene el número mínimo de
   *                                  columnas requeridas.
   * @throws NumberFormatException    Si los valores numéricos (ID, fechaNac, edad
   *                                  o teléfono) no tienen un formato válido para
   *                                  ser convertidos.
   */
  public static Cliente fromCSV(String lineaCSV) throws NumberFormatException {
    String[] datos = CSVUtil.parsear(lineaCSV);
    if (datos.length < 9) {
      throw new IllegalArgumentException("Error: la línea CSV no tiene todas las columnas que necestiamos.");
    }

    int idCliente = Integer.parseInt(datos[0].trim());
    String nombreCliente = datos[1].trim();
    String apellidoP = datos[2].trim();
    String apellidoM = datos[3].trim();
    int fechaNac = Integer.parseInt(datos[4].trim());
    int edad = Integer.parseInt(datos[5].trim());
    String sexo = datos[6].trim();
    String correoE = datos[7].trim();
    long telefonoC = Long.parseLong(datos[8].trim());

    return new Cliente(idCliente, nombreCliente, apellidoP, apellidoM, fechaNac, edad, sexo, correoE, telefonoC);
  }

  /**
   * Devuelve una representación en formato de texto de una instancia de Cliente.
   * 
   * @return Cadena de texto formateada con la información detallada del cliente.
   */
  @Override
  public String toString() {
    return "----------------------------------------Cliente----------------------------------------\n" +
        "ID Cliente\t \t: " + idCliente + "\n" +
        "Nombre\t \t \t: " + nombreCliente + "\n" +
        "Apellidos\t \t: " + apellidoP + " " + apellidoM + "\n" +
        "Fecha de Nacimiento\t: " + fechaNac + "\n" +
        "Edad\t \t \t: " + edad + "\n" +
        "Sexo\t \t \t: " + sexo + "\n" +
        "Correo Electrónico\t: " + correoE + "\n" +
        "Teléfono\t \t: " + telefonoC + "\n" +
        "----------------------------------------------------------------------------------------";
  }

  /**
   * Regresa la llave que identifica a el cliente dentro de su archivo CSV.
   *
   * @return La llave del cliente.
   */
  @Override
  public int getLlave() {
    return this.idCliente;
  }

}
