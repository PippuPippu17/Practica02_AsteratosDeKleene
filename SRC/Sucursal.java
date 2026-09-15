import exceptions.HorarioException;

/**
 * Representa una sucursal del centro de entretenimiento familiar.
 *
 * La dirección se guarda desglosada en calle, número exterior, número interior,
 * colonia y estado, tal como lo pide el caso de uso. El horario de atención no
 * es una cadena cualquiera: se delega en {@link Horario}, que se encarga de
 * validar los días, su orden y las horas.
 */
public class Sucursal implements Registrable {

  /** Identificador único de la sucursal. */
  private int idSucursal;

  /** Nombre con el que se conoce a la sucursal. */
  private String nombre;

  /** Calle donde se encuentra la sucursal. */
  private String calle;

  /** Número exterior del domicilio. */
  private int numExterior;

  /** Número interior del domicilio, o "S/N" cuando no aplica. */
  private String numeroInterior;

  /** Colonia donde se encuentra la sucursal. */
  private String colonia;

  /** Estado de la república donde se encuentra la sucursal. */
  private String estado;

  /** Número telefónico de contacto de la sucursal. */
  private long telefono;

  /** Horario de atención, ya validado. */
  private Horario horario;

  // Constructor por omisión
  /**
   * Constructor por omisión, que deja la sucursal con valores vacíos.
   */
  public Sucursal() {
    this.idSucursal = 0;
    this.nombre = "";
    this.calle = "";
    this.numExterior = 0;
    this.numeroInterior = "";
    this.colonia = "";
    this.estado = "";
    this.telefono = 0L;
    this.horario = null;
  }

  /**
   * Construye una sucursal con todos sus atributos.
   *
   * @param idSucursal     Identificador de la sucursal.
   * @param nombre         Nombre de la sucursal.
   * @param calle          Calle del domicilio.
   * @param numExterior    Número exterior del domicilio.
   * @param numeroInterior Número interior del domicilio, o "S/N" si no aplica.
   * @param colonia        Colonia del domicilio.
   * @param estado         Estado de la república.
   * @param telefono       Teléfono de contacto.
   * @param horario        Horario de atención, que será validado.
   * @throws HorarioException Si el horario no tiene un formato válido.
   */
  public Sucursal(int idSucursal, String nombre, String calle, int numExterior,
      String numeroInterior, String colonia, String estado,
      long telefono, String horario) throws HorarioException {
    this.idSucursal = idSucursal;
    this.nombre = nombre;
    this.calle = calle;
    this.numExterior = numExterior;
    this.numeroInterior = ((numeroInterior == null || numeroInterior.trim().isEmpty()) ? "S/N" : numeroInterior);
    this.colonia = colonia;
    this.estado = estado;
    this.telefono = telefono;
    this.horario = new Horario(horario);
  }

  /**
   * Regresa el identificador de la sucursal.
   *
   * @return El identificador de la sucursal.
   */
  public int getIdSucursal() {
    return idSucursal;
  }

  /**
   * Cambia el identificador de la sucursal.
   *
   * @param idSucursal Nuevo identificador.
   */
  public void setIdSucursal(int idSucursal) {
    this.idSucursal = idSucursal;
  }

  /**
   * Regresa el nombre de la sucursal.
   *
   * @return El nombre de la sucursal.
   */
  public String getNombre() {
    return nombre;
  }

  /**
   * Cambia el nombre de la sucursal.
   *
   * @param nombre Nuevo nombre.
   */
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  /**
   * Regresa la calle donde se encuentra la sucursal.
   *
   * @return La calle de la sucursal.
   */
  public String getCalle() {
    return calle;
  }

  /**
   * Cambia la calle del domicilio.
   *
   * @param calle Nueva calle.
   */
  public void setCalle(String calle) {
    this.calle = calle;
  }

  /**
   * Regresa el número exterior del domicilio.
   *
   * @return El número exterior.
   */
  public int getNumExterior() {
    return numExterior;
  }

  /**
   * Cambia el número exterior del domicilio.
   *
   * @param numExterior Nuevo número exterior.
   */
  public void setNumExterior(int numExterior) {
    this.numExterior = numExterior;
  }

  /**
   * Regresa el número interior del domicilio.
   *
   * @return El número interior, o "S/N" si no aplica.
   */
  public String getNumeroInterior() {
    return numeroInterior;
  }

  /**
   * Cambia el número interior del domicilio.
   *
   * @param numeroInterior Nuevo número interior.
   */
  public void setNumeroInterior(String numeroInterior) {
    this.numeroInterior = (numeroInterior == null || numeroInterior.trim().isEmpty()) ? "S/N" : numeroInterior;
  }

  /**
   * Regresa la colonia donde se encuentra la sucursal.
   *
   * @return La colonia de la sucursal.
   */
  public String getColonia() {
    return colonia;
  }

  /**
   * Cambia la colonia del domicilio.
   *
   * @param colonia Nueva colonia.
   */
  public void setColonia(String colonia) {
    this.colonia = colonia;
  }

  /**
   * Regresa el estado donde se encuentra la sucursal.
   *
   * @return El estado de la sucursal.
   */
  public String getEstado() {
    return estado;
  }

  /**
   * Cambia el estado del domicilio.
   *
   * @param estado Nuevo estado.
   */
  public void setEstado(String estado) {
    this.estado = estado;
  }

  /**
   * Regresa el teléfono de contacto de la sucursal.
   *
   * @return El teléfono de la sucursal.
   */
  public long getTelefono() {
    return telefono;
  }

  /**
   * Cambia el teléfono de contacto de la sucursal.
   *
   * @param telefono Nuevo teléfono.
   */
  public void setTelefono(long telefono) {
    this.telefono = telefono;
  }

  /**
   * Regresa el horario de atención de la sucursal.
   *
   * @return El horario con el formato en que se capturó.
   */
  public String getHorario() {
    return horario.toString();
  }

  /**
   * Cambia el horario de atención de la sucursal.
   *
   * El texto se entrega a {@link Horario}, que lo valida antes de aceptarlo.
   *
   * @param horario Nuevo horario.
   * @throws HorarioException Si el horario no tiene un formato válido.
   */
  public void setHorario(String horario) throws HorarioException {
    this.horario = new Horario(horario);
  }

  /**
   * Convierte la sucursal a una línea con formato CSV.
   *
   * Los campos de texto se escapan con {@link CSVUtil}, de manera que una
   * dirección o un nombre que contenga comas no rompa la estructura del
   * archivo.
   *
   * @return Una línea lista para escribirse en el archivo CSV.
   */
  /**
   * Convierte la sucursal a una línea con formato CSV.
   *
   * Los campos de texto se escapan con {@link CSVUtil}, de manera que una
   * dirección o un nombre que contenga comas no rompa la estructura del archivo.
   *
   * @return Una línea lista para escribirse en el archivo CSV.
   */
  @Override
  public String toCSV() {
    String strHorario = ((this.horario != null) ? this.horario.toString() : "");

    return CSVUtil.unir(
        String.valueOf(this.idSucursal),
        this.nombre,
        this.calle,
        String.valueOf(this.numExterior),
        this.numeroInterior,
        this.colonia,
        this.estado,
        String.valueOf(this.telefono),
        strHorario);
  }

  /**
   * Reconstruye una instancia de Sucursal a partir de una línea del archivo CSV.
   *
   * @param lineaCSV Línea del archivo con los datos de una sucursal.
   * @return Una nueva instancia de Sucursal con los datos de la línea.
   * @throws HorarioException         Si el horario almacenado no es válido.
   * @throws IllegalArgumentException Si la línea no tiene todas las columnas.
   * @throws NumberFormatException    Si alguna columna numérica no lo es.
   */
  public static Sucursal fromCSV(String lineaCSV) throws HorarioException, NumberFormatException {
    String[] datos = CSVUtil.parsear(lineaCSV);
    if (datos.length < 9) {
      throw new IllegalArgumentException("Error: la línea CSV no tiene todas las columnas que necestiamos.");
    }

    int id = Integer.parseInt(datos[0].trim());
    String nombre = datos[1].trim();
    String calle = datos[2].trim();
    int numExt = Integer.parseInt(datos[3].trim());
    String numInter = datos[4].trim();
    String colonia = datos[5].trim();
    String estado = datos[6].trim();
    long telefono = Long.parseLong(datos[7].trim());
    String horario = datos[8].trim();

    return new Sucursal(id, nombre, calle, numExt, numInter, colonia, estado, telefono, horario);
  }

  /**
   * Regresa los datos de la sucursal con un formato legible para la consola.
   *
   * @return Los datos de la sucursal listos para imprimirse.
   */
  @Override
  public String toString() {
    return "----------------------------------------Sucursal----------------------------------------\n" +
        "ID Sucursal\t: " + idSucursal + "\n" +
        "Nombre\t \t: " + nombre + "\n" +
        "Dirección\t: " + calle + ", Num.Ext." + numExterior +
        ", Num.Int." + numeroInterior + ", Col." + colonia + ", " + estado + "\n" +
        "Teléfono\t: " + telefono + "\n" +
        "Horario\t \t: " + horario.toString() + "\n" +
        "----------------------------------------------------------------------------------------";
  }

  /**
   * Regresa la llave que identifica a la sucursal dentro de su archivo CSV.
   *
   * @return La llave de la sucursal.
   */
  @Override
  public int getLlave() {
    return this.idSucursal;
  }

}
