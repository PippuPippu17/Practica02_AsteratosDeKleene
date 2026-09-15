import exceptions.HorarioException;

public class Sucursal implements Registrable {
  private int idSucursal;
  private String nombre;
  private String calle;
  private int numExterior;
  private String numeroInterior;
  private String colonia;
  private String estado;
  private long telefono;
  private Horario horario;

  // Constructor por omisión
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

  // Constructor con parámetros
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

  public int getIdSucursal() {
    return idSucursal;
  }

  public void setIdSucursal(int idSucursal) {
    this.idSucursal = idSucursal;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getCalle() {
    return calle;
  }

  public void setCalle(String calle) {
    this.calle = calle;
  }

  public int getNumExterior() {
    return numExterior;
  }

  public void setNumExterior(int numExterior) {
    this.numExterior = numExterior;
  }

  public String getNumeroInterior() {
    return numeroInterior;
  }

  public void setNumeroInterior(String numeroInterior) {
    this.numeroInterior = (numeroInterior == null || numeroInterior.trim().isEmpty()) ? "S/N" : numeroInterior;
  }

  public String getColonia() {
    return colonia;
  }

  public void setColonia(String colonia) {
    this.colonia = colonia;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public long getTelefono() {
    return telefono;
  }

  public void setTelefono(long telefono) {
    this.telefono = telefono;
  }

  public String getHorario() {
    return horario.toString();
  }

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
