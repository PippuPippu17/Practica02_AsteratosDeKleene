public class Cliente {

    int idCliente;
    String nombreCliente;
    String apellidoP;
    String apellidoM;
    int fechaNac;
    int edad;
    String sexo;
    String correoE;
    long telefonoC;

    // Constructor por omisión
    public Cliente() {

        this.idCliente = 0;
        this.nombreCliente = "";
        this.apellidoP = "";
        this.apellidoM = "";
        this.fechaNac = 0;
        this.edad = 0;
        this.sexo = "";
        this.correoE = "";
        this.telefonoC = 0;
    }

    // Constructor con parámetros
    public Cliente(int idCliente, String nombreCliente, String apellidoP, String apellidoM, int fechaNac, int edad, String sexo, 
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
    
    public int getIdCliente(){
        return idCliente;
    }

    public void setIdCliente(int idCliente){
        this.idCliente = idCliente;
    }

    public String getNombreCliente(){
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente){
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoP(){
        return apellidoP;
    }

    public void setApellidoP(String apellidoP){
        this.apellidoP = apellidoP;
    }

    public String getApellidoM(){
        return apellidoM;
    }

    public void setApellidoM(String apellidoM){
        this.apellidoM = apellidoM;
    }

    public int getFechaNac(){
        return fechaNac;
    }

    public void setFechaNac(int fechaNac){
        this.fechaNac = fechaNac;
    }

    public int getEdad(){
        return edad;
    }

    public void setEdad(int edad){
        this.edad = edad;
    }

    public String getSexo(){
        return sexo;
    }

    public void setSexo(String sexo){
        this.sexo = sexo;
    }

    public String getCorreoE(){
        return correoE;
    }

    public void setCorreoE(String correoE){
        this.correoE = correoE;
    }

    public long getTelefonoC(){
        return telefonoC;
    }

    public void setTelefonoC(long telefonoC){
        this.telefonoC = telefonoC;
    }

    /**
     * Método para pasar las instancias de Cliente al formato del archivo CSV.
     */
    public String toCSV() {
        return this.idCliente + "," +
                this.nombreCliente + "," +
                this.apellidoP + "," +
                this.apellidoM + "," +
                this.fechaNac + "," +
                this.edad + "," +
                this.correoE + "," +
                this.telefonoC;
    }

    /**
     * Método para reconstruir una instancia de Sucursal a partir de una línea del
     * CSV.
     */
    public static Cliente fromCSV(String lineaCSV) throws NumberFormatException {
        String[] datos = lineaCSV.split(",");
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

    @Override
    public String toString() {
        return "----------------------------------------Cliente----------------------------------------\n" +
                "ID Cliente\t: " + idCliente + "\n" +
                "Nombre\t \t: " + nombreCliente + "\n" +
                "Apellidos\t: " + apellidoP + " " + apellidoM +
                "Fecha de Nacimiento\t: " + fechaNac + "\n" +
                "Edad\t: " + edad + "\n" +
                "Sexo\t: " + sexo + "\n" +
                "Correo Electrónico\t: " + correoE + "\n" +
                "Teléfono\t: " + telefonoC + "\n" +
                "----------------------------------------------------------------------------------------";
    }


}
