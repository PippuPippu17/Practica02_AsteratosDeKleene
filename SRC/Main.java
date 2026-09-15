import exceptions.ArchivoCSVException;
import exceptions.HorarioException;
import exceptions.RegistroNoEncontradoException;
import exceptions.ValidacionException;

/**
 * Clase que gestiona el menú de la aplicacion
 * "PuellaGame".
 **/
public class Main {
  // Toda la lectura del teclado se delega a EntradaConsola, que valida el tipo
  // de cada dato y vuelve a preguntar cuando la captura es incorrecta.

  /**
   * Lee los puntos que pide un premio y no los acepta hasta que correspondan a
   * su categoría.
   *
   * La regla proviene del caso de uso: los premios bajos van de 20 a 1,000
   * puntos, los medios de 1,001 a 3,999 y los grandes de 4,000 en adelante.
   *
   * @param categoria Categoría del premio, ya validada contra su catálogo.
   * @return Los puntos capturados.
   */
  private static int leerPuntosDePremio(String categoria) {
    while (true) {
      int puntos = EntradaConsola.leerEntero("Puntos requeridos: ");

      try {
        return Validador.validarPuntosSegunCategoria(categoria, puntos);

      } catch (ValidacionException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Muestra el menu principal
   */
  private static void mostrarMenuP() {
    System.out.println("PUELLA GAME");
    System.out.println("1. Gestionar Sucursales");
    System.out.println("2. Gestionar Premios");
    System.out.println("3. Gestionar Clientes");
    System.out.println("4. Salir");
  }

  /**
   * Metodo para gestionar Sucursales.
   */
  private static void gestionarSucursales(String rutaArchivoSucursales) {
    boolean volvermenup = false;

    while (!volvermenup) {
      try {
        System.out.println("\nSucursales");
        System.out.println("1. Agregar Sucursal");
        System.out.println("2. Consultar por Llave de Sucursal");
        System.out.println("3. Editar Sucursal");
        System.out.println("4. Eliminar Sucursal");
        System.out.println("5. Volver al menú principal");

        int opcion = EntradaConsola.leerOpcion("Selecciona una operación: ", 1, 5);
        System.out.println();

        switch (opcion) {
          case 1:
            agregarSucursal(rutaArchivoSucursales);
            break;
          case 2:
            consultarSucursal(rutaArchivoSucursales);
            break;
          case 3:
            editarSucursal(rutaArchivoSucursales);
            break;
          case 4:
            eliminarSucursal(rutaArchivoSucursales);
            break;
          case 5:
            volvermenup = true;
            break;
        }
      } catch (Exception e) {
        System.out.println("\nOcurrió un error inesperado: " + e.getMessage());
      }
    }
  }

  /**
   * Método para agregar una sucursal al archivo CSV.
   */
  private static void agregarSucursal(String rutaArchivoSucursales) {
    System.out.println("Ingresa los siguientes datos para añadir una nueva sucursal:");

    // Pedir los datos de la sucursal al usuario y los guardar en variables
    int idSucursal = EntradaConsola.leerEntero("ID de la Sucursal (debe ser número): ");
    String nombre = EntradaConsola.leerTexto("Nombre de la Sucursal: ");
    String calle = EntradaConsola.leerTexto("Calle: ");
    int numExt = EntradaConsola.leerEntero("Número exterior: ", 1, 99999);
    String numInter = EntradaConsola.leerTextoOpcional("Número interior (deja vacío si no aplica): ", "S/N");
    String colonia = EntradaConsola.leerTexto("Colonia: ");
    String estado = EntradaConsola.leerTexto("Estado: ");
    long telefono = EntradaConsola.leerTelefono("Teléfono (10 dígitos): ");
    String horario = EntradaConsola.leerHorario("Horario (ejemplo: Lun-Vie 11:00-21:00|Sab-Dom 10:00-22:00): ");

    // Escribir los datos en el archivo CSV
    try {
      Sucursal sucursal = new Sucursal(idSucursal, nombre, calle, numExt, numInter, colonia, estado, telefono, horario);
      HandlerCSV.addRegistro(rutaArchivoSucursales, idSucursal, sucursal.toCSV());

      System.out.println(
          "Se agregó con éxito la sucursal al archivo '" + rutaArchivoSucursales + "' con los siguientes datos:");
      System.out.println(sucursal);

    } catch (HorarioException | ArchivoCSVException e) {
      System.out.println(e.getMessage());
    } catch (Exception e) {
      System.out.println("Ocurrió un error inesperado: " + e.getMessage());
    }
  }

  /**
   * Solicita la llave de una sucursal, la busca en el archivo CSV y muestra sus
   * datos completos en consola.
   *
   * @param rutaArchivoSucursales Ruta del archivo CSV de sucursales.
   */
  private static void consultarSucursal(String rutaArchivoSucursales) {
    int idSucursal = EntradaConsola.leerEntero("Ingresa la llave de la sucursal a consultar: ");

    try {
      String datosSucursal = HandlerCSV.buscarPorId(rutaArchivoSucursales, idSucursal);
      Sucursal sucursalConsultada = Sucursal.fromCSV(datosSucursal);

      System.out.println("Datos de la sucursal con llave '" + idSucursal + "':");
      System.out.println(sucursalConsultada);

    } catch (RegistroNoEncontradoException e) {
      System.out.println(e.getMessage());
    } catch (ArchivoCSVException e) {
      System.out.println("No se pudo leer el archivo: " + e.getMessage());
    } catch (HorarioException | IllegalArgumentException e) {
      System.out.println("El registro está en un formato incorrecto: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("Ocurrió un error inesperado: " + e.getMessage());
    }
  }

  /**
   * Solicita la llave de una sucursal y, si existe, pide los nuevos datos para
   * sobrescribir su registro en el archivo CSV.
   *
   * @param rutaArchivoSucursales Ruta del archivo CSV de sucursales.
   */
  private static void editarSucursal(String rutaArchivoSucursales) {
    String datosSucursal = "";

    // Solicitamos la llave.
    int idSucursal = EntradaConsola.leerEntero("Ingresa la llave de la sucursal a editar: ");

    // Si la llave no existe salimos de inmediato, antes de pedirle al usuario
    // datos que no se van a poder guardar.
    try {
      datosSucursal = HandlerCSV.buscarPorId(rutaArchivoSucursales, idSucursal);
    } catch (RegistroNoEncontradoException e) {
      System.out.println(e.getMessage());
      return;
    } catch (ArchivoCSVException e) {
      System.out.println("No se pudo leer el archivo: " + e.getMessage());
      return;
    }

    System.out.println(
        "Ingresa los nuevos datos para la sucursal con llave '" + idSucursal + "' en el archivo '"
            + rutaArchivoSucursales
            + "':");

    // Pedimos los nuevos datos de la sucursal al usuario y los guardamos en
    // variables.
    String nombre = EntradaConsola.leerTexto("Nombre de la Sucursal: ");
    String calle = EntradaConsola.leerTexto("Calle: ");
    int numExt = EntradaConsola.leerEntero("Número exterior: ", 1, 99999);
    String numInter = EntradaConsola.leerTextoOpcional("Número interior (deja vacío si no aplica): ", "S/N");
    String colonia = EntradaConsola.leerTexto("Colonia: ");
    String estado = EntradaConsola.leerTexto("Estado: ");
    long telefono = EntradaConsola.leerTelefono("Teléfono (10 dígitos): ");
    String horario = EntradaConsola.leerHorario("Horario (ejemplo: Lun-Vie 11:00-21:00|Sab-Dom 10:00-22:00): ");

    try {
      Sucursal sucursalAEditar = Sucursal.fromCSV(datosSucursal);

      // Pedir los datos de la sucursal al usuario y los guardamos en variables
      sucursalAEditar.setNombre(nombre);
      sucursalAEditar.setCalle(calle);
      sucursalAEditar.setNumExterior(numExt);
      sucursalAEditar.setNumeroInterior(numInter);
      sucursalAEditar.setColonia(colonia);
      sucursalAEditar.setEstado(estado);
      sucursalAEditar.setTelefono(telefono);
      sucursalAEditar.setHorario(horario);

      System.out.println("Los nuevos datos de la sucursal con id '" + idSucursal + "' en el archivo '"
          + rutaArchivoSucursales + "' son:");
      HandlerCSV.setRegistro(rutaArchivoSucursales, idSucursal, sucursalAEditar.toCSV());
      System.out.println(sucursalAEditar);

    } catch (RegistroNoEncontradoException e) {
      System.out.println(e.getMessage());
    } catch (ArchivoCSVException e) {
      System.out.println("No se pudo guardar el registro: " + e.getMessage());
    } catch (HorarioException | IllegalArgumentException e) {
      System.out.println("Datos en formato incorrecto: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("Ocurrió un error inesperado: " + e.getMessage());
    }
  }

  /**
   * Solicita la llave de una sucursal y elimina su registro del archivo CSV.
   *
   * @param rutaArchivoSucursales Ruta del archivo CSV de sucursales.
   */
  private static void eliminarSucursal(String rutaArchivoSucursales) {
    int idSucursal = EntradaConsola.leerEntero("Ingresa la llave de la sucursal a borrar: ");

    try {
      HandlerCSV.removeRegistro(rutaArchivoSucursales, idSucursal);
      System.out.println("Se eliminó la sucursal con llave '" + idSucursal + "'.");

    } catch (RegistroNoEncontradoException e) {
      System.out.println(e.getMessage());
    } catch (ArchivoCSVException e) {
      System.out.println("No se pudo modificar el archivo: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("Ocurrió un error inesperado: " + e.getMessage());
    }
  }

  /**
   * Metodo para gestionar Premios.
   * Se puede agregar, consultar, editar y eliminar.
   */
  private static void gestionarPremios(String rutaArchivoPremios) {
    boolean volvermenup = false;

    while (!volvermenup) {
      try {
        System.out.println("\nPremios");
        System.out.println("1. Agregar Premio");
        System.out.println("2. Consultar por Llave de Premio");
        System.out.println("3. Editar Premio");
        System.out.println("4. Eliminar Premio");
        System.out.println("5. Volver al menú principal");

        int opcion = EntradaConsola.leerOpcion("Selecciona una operación: ", 1, 5);
        System.out.println();

        switch (opcion) {
          case 1:
            agregarPremio(rutaArchivoPremios);
            break;
          case 2:
            consultarPremio(rutaArchivoPremios);
            break;
          case 3:
            editarPremio(rutaArchivoPremios);
            break;
          case 4:
            eliminarPremio(rutaArchivoPremios);
            break;
          case 5:
            // fuga
            volvermenup = true;
            break;
        }
      } catch (Exception e) {
        System.out.println("\nOcurrió un error inesperado: " + e.getMessage());
      }
    }
  }

  private static void agregarPremio(String rutaArchivoPremios) {
    System.out.println("Ingresa los siguientes datos para añadir un nuevo premio:");

    // Pedir los datos de el premio al usuario y los guardamos en variables
    int idPremio = EntradaConsola.leerEntero("ID del premio (debe ser número): ");
    String nombre = EntradaConsola.leerTexto("Nombre del premio: ");
    String categoria = EntradaConsola.leerDeCatalogo("Categoría", Validador.CATEGORIAS_PREMIO);
    String rangoEdad = EntradaConsola.leerDeCatalogo("Rango de edad", Validador.RANGOS_EDAD);
    int puntosRequeridos = leerPuntosDePremio(categoria);

    double valorAproximado = EntradaConsola.leerMontoPositivo("Valor aproximado en MXN: ", "valor aproximado");

    int idSucursal = EntradaConsola.leerEntero("ID de la sucursal donde está disponible: ");
    int stock = EntradaConsola.leerEntero("Cantidad disponible en existencia: ", 0, 99999);

    // Escribir los datos en el archivo CSV
    try {
      Premio premio = new Premio(idPremio, nombre, categoria, rangoEdad, puntosRequeridos, valorAproximado, idSucursal,
          stock);
      HandlerCSV.addRegistro(rutaArchivoPremios, idPremio, premio.toCSV());

      System.out.println(
          "Se agregó con éxito el premio al archivo '" + rutaArchivoPremios + "' con los siguientes datos:");
      System.out.println(premio);

    } catch (ArchivoCSVException e) {
      System.out.println(e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("Datos en formato incorrecto: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("Ocurrió un error inesperado: " + e.getMessage());
    }
  }

  /**
   * Solicita la llave de un premio, lo busca en el archivo CSV y muestra sus
   * datos completos en consola.
   *
   * @param rutaArchivoPremios Ruta del archivo CSV de premios.
   */
  private static void consultarPremio(String rutaArchivoPremios) {
    int idPremio = EntradaConsola.leerEntero("Ingresa la llave del premio a consultar: ");

    try {
      String datosPremio = HandlerCSV.buscarPorId(rutaArchivoPremios, idPremio);
      Premio premioConsultado = Premio.fromCSV(datosPremio);

      System.out.println("Datos del premio con llave '" + idPremio + "':");
      System.out.println(premioConsultado);

    } catch (RegistroNoEncontradoException e) {
      System.out.println(e.getMessage());
    } catch (ArchivoCSVException e) {
      System.out.println("No se pudo leer el archivo: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("El registro está en un formato incorrecto: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("Ocurrió un error inesperado: " + e.getMessage());
    }
  }

  /**
   * Solicita la llave de un premio y, si existe, pide los nuevos datos para
   * sobrescribir su registro en el archivo CSV.
   *
   * @param rutaArchivoPremios Ruta del archivo CSV de premios.
   */
  private static void editarPremio(String rutaArchivoPremios) {
    String datosPremio = "";

    // Solicitamos la llave.
    int idPremio = EntradaConsola.leerEntero("Ingresa la llave del premio a editar: ");

    // Si la llave no existe salimos de inmediato, antes de pedirle al usuario
    // datos que no se van a poder guardar.
    try {
      datosPremio = HandlerCSV.buscarPorId(rutaArchivoPremios, idPremio);
    } catch (RegistroNoEncontradoException e) {
      System.out.println(e.getMessage());
      return;
    } catch (ArchivoCSVException e) {
      System.out.println("No se pudo leer el archivo: " + e.getMessage());
      return;
    }

    System.out.println(
        "Ingresa los nuevos datos para el premio con llave '" + idPremio + "' en el archivo '"
            + rutaArchivoPremios
            + "':");

    // Pedimos los nuevos datos de el premio al usuario y los guardamos en
    // variables.
    String nombre = EntradaConsola.leerTexto("Nombre del premio: ");
    String categoria = EntradaConsola.leerDeCatalogo("Categoría", Validador.CATEGORIAS_PREMIO);
    String rangoEdad = EntradaConsola.leerDeCatalogo("Rango de edad", Validador.RANGOS_EDAD);
    int puntosRequeridos = leerPuntosDePremio(categoria);

    double valorAproximado = EntradaConsola.leerMontoPositivo("Valor aproximado en MXN: ", "valor aproximado");

    int idSucursal = EntradaConsola.leerEntero("ID de la sucursal donde está disponible: ");
    int stock = EntradaConsola.leerEntero("Cantidad disponible en existencia: ", 0, 99999);

    try {
      Premio PremioAEditar = Premio.fromCSV(datosPremio);

      // Pedir los datos de la sucursal al usuario y los guardamos en variables
      PremioAEditar.setNombre(nombre);
      PremioAEditar.setCategoria(categoria);
      PremioAEditar.setRangoEdad(rangoEdad);
      PremioAEditar.setPuntosRequeridos(puntosRequeridos);
      PremioAEditar.setValorAproximado(valorAproximado);
      PremioAEditar.setIdSucursal(idSucursal);
      PremioAEditar.setStock(stock);

      System.out.println("Los nuevos datos del premio con id '" + idPremio + "' en el archivo '"
          + rutaArchivoPremios + "' son:");
      HandlerCSV.setRegistro(rutaArchivoPremios, idPremio, PremioAEditar.toCSV());
      System.out.println(PremioAEditar);

    } catch (RegistroNoEncontradoException e) {
      System.out.println(e.getMessage());
    } catch (ArchivoCSVException e) {
      System.out.println("No se pudo guardar el registro: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("Datos en formato incorrecto: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("Ocurrió un error inesperado: " + e.getMessage());
    }
  }

  /**
   * Solicita la llave de un premio y elimina su registro del archivo CSV.
   *
   * @param rutaArchivoPremios Ruta del archivo CSV de premios.
   */
  private static void eliminarPremio(String rutaArchivoPremios) {
    int idPremio = EntradaConsola.leerEntero("Ingresa la llave del premio a borrar: ");

    try {
      HandlerCSV.removeRegistro(rutaArchivoPremios, idPremio);
      System.out.println("Se eliminó el premio con llave '" + idPremio + "'.");

    } catch (RegistroNoEncontradoException e) {
      System.out.println(e.getMessage());
    } catch (ArchivoCSVException e) {
      System.out.println("No se pudo modificar el archivo: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("Ocurrió un error inesperado: " + e.getMessage());
    }
  }

  /**
   * Despliega el menú interactivo en consola para la gestión de los Clientes.
   * Este método inicializa el archivo CSV, creando los encabezados si no existe
   * el archivo
   * y permite navergar a traves de las opciones de agregar, consultar,
   * editar y eliminar clientes.
   *
   * @param rutaArchivoClientes La ruta del archivo CSV donde se almacenan los
   *                            datos de los clientes.
   */
  private static void gestionarClientes(String rutaArchivoClientes) {
    boolean volvermenup = false;

    while (!volvermenup) {
      try {
        System.out.println("\nClientes");
        System.out.println("1. Agregar Cliente");
        System.out.println("2. Consultar por Llave de Cliente");
        System.out.println("3. Editar Cliente");
        System.out.println("4. Eliminar Cliente");
        System.out.println("5. Volver al menú principal");

        int opcion = EntradaConsola.leerOpcion("Selecciona una operación: ", 1, 5);

        switch (opcion) {
          case 1:
            agregarCliente(rutaArchivoClientes);
            break;
          case 2:
            consultarCliente(rutaArchivoClientes);
            break;
          case 3:
            editarCliente(rutaArchivoClientes);
            break;
          case 4:
            eliminarCliente(rutaArchivoClientes);
            break;
          case 5:
            // Fuga
            volvermenup = true;
            break;
        }
        // Manejo de excepciones
      } catch (Exception e) {
        System.out.println("\nOcurrió un error inesperado: " + e.getMessage());
      }
    }
  }

  /**
   * Solicita al usuario los datos necesarios para registrar un nuevo cliente a
   * través de la consola,
   * crea una instancia de Cliente con la información dada y agrega la información
   * al archivo CSV en el archivo especificado.
   * 
   * @param rutaArchivoClientes La ruta del archivo CSV donde se guardará el nuevo
   *                            cliente.
   */
  public static void agregarCliente(String rutaArchivoClientes) {

    System.out.println("Ingresa los datos para el cliente:");

    // Pedir los datos del cliente al usuario y los guardar en variables
    int idCliente = EntradaConsola.leerEntero("ID del cliente (debe ser un número): ");
    String nombreCliente = EntradaConsola.leerTexto("Nombre del cliente: ");
    String apellidoP = EntradaConsola.leerTexto("Apellido paterno: ");
    String apellidoM = EntradaConsola.leerTexto("Apellido materno: ");
    int fechaNac = EntradaConsola.leerFecha("Fecha de nacimiento (DDMMAAAA): ");
    int edad = EntradaConsola.leerEntero("Edad: ", Validador.EDAD_MINIMA, Validador.EDAD_MAXIMA);
    String sexo = EntradaConsola.leerDeCatalogo("Sexo", Validador.SEXOS);
    String correoE = EntradaConsola.leerCorreo("Correo electrónico: ");
    long telefonoC = EntradaConsola.leerTelefono("Teléfono (10 dígitos): ");

    // Escribir los datos en el archivo CSV
    try {
      Cliente cliente = new Cliente(idCliente, nombreCliente, apellidoP, apellidoM, fechaNac, edad, sexo, correoE,
          telefonoC);
      HandlerCSV.addRegistro(rutaArchivoClientes, idCliente, cliente.toCSV());

      System.out
          .println("Se agregó con éxito el cliente al archivo '" + rutaArchivoClientes + "' con los siguientes datos:");
      System.out.println(cliente);

    } catch (ArchivoCSVException e) {
      System.out.println(e.getMessage());
    } catch (Exception e) {
      System.out.println("Ocurrió un error inesperado: " + e.getMessage());
    }
  }

  /**
   * Solicita al usuario el ID de un cliente, lo busca dentro del archivo CSV y,
   * si lo encuentra, reconstruye la
   * instancia del cliente para imprimir sus datos en la consola.
   *
   * @param rutaArchivoClientes La ruta del archivo CSV donde se realizará la
   *                            búsqueda del cliente.
   */
  private static void consultarCliente(String rutaArchivoClientes) {
    int idCliente = EntradaConsola.leerEntero("Ingresa la llave del cliente a consultar: ");

    try {
      String datosCliente = HandlerCSV.buscarPorId(rutaArchivoClientes, idCliente);
      Cliente clienteConsultado = Cliente.fromCSV(datosCliente);

      System.out.println("Datos del cliente con llave '" + idCliente + "':");
      System.out.println(clienteConsultado);

    } catch (RegistroNoEncontradoException e) {
      System.out.println(e.getMessage());
    } catch (ArchivoCSVException e) {
      System.out.println("No se pudo leer el archivo: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("El registro está en un formato incorrecto: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("Ocurrió un error inesperado: " + e.getMessage());
    }
  }

  /**
   * Solicita al usuario el ID de un cliente para buscarlo en el archivo.
   * Si el cliente existe, pide los nuevos datos por consola, actualiza la
   * información del cliente y sobrescribe
   * el registro en el archivo CSV con la nueva información.
   *
   * @param rutaArchivoClientes La ruta del archivo CSV donde se buscará y
   *                            actualizará el cliente.
   */
  private static void editarCliente(String rutaArchivoClientes) {
    String datosCliente = "";

    // Solicitamos la llave
    int idCliente = EntradaConsola.leerEntero("Ingresa la llave del cliente a editar: ");

    // Si la llave no existe salimos de inmediato, antes de pedirle al usuario
    // datos que no se van a poder guardar.
    try {
      datosCliente = HandlerCSV.buscarPorId(rutaArchivoClientes, idCliente);
    } catch (RegistroNoEncontradoException e) {
      System.out.println(e.getMessage());
      return;
    } catch (ArchivoCSVException e) {
      System.out.println("No se pudo leer el archivo: " + e.getMessage());
      return;
    }

    System.out.println(
        "Ingresa los nuevos datos para el cliente con llave '" + idCliente + "' en el archivo '"
            + rutaArchivoClientes
            + "':");

    // Pedimos los nuevos datos del cliente al usuario y los guardamos en variables.
    String nombreCliente = EntradaConsola.leerTexto("Nombre del cliente: ");
    String apellidoP = EntradaConsola.leerTexto("Apellido paterno: ");
    String apellidoM = EntradaConsola.leerTexto("Apellido materno: ");
    int fechaNac = EntradaConsola.leerFecha("Fecha de nacimiento (DDMMAAAA): ");
    int edad = EntradaConsola.leerEntero("Edad: ", Validador.EDAD_MINIMA, Validador.EDAD_MAXIMA);
    String sexo = EntradaConsola.leerDeCatalogo("Sexo", Validador.SEXOS);
    String correoE = EntradaConsola.leerCorreo("Correo electrónico: ");
    long telefonoC = EntradaConsola.leerTelefono("Teléfono (10 dígitos): ");

    try {
      Cliente clienteAEditar = Cliente.fromCSV(datosCliente);

      // Pedir los datos del cliente al usuario y los guardamos en variables
      clienteAEditar.setNombreCliente(nombreCliente);
      clienteAEditar.setApellidoP(apellidoP);
      clienteAEditar.setApellidoM(apellidoM);
      clienteAEditar.setFechaNac(fechaNac);
      clienteAEditar.setEdad(edad);
      clienteAEditar.setSexo(sexo);
      clienteAEditar.setCorreoE(correoE);
      clienteAEditar.setTelefonoC(telefonoC);

      System.out.println("Los nuevos datos del cliente con id '" + idCliente + "' en el archivo '"
          + rutaArchivoClientes + "' son:");
      HandlerCSV.setRegistro(rutaArchivoClientes, idCliente, clienteAEditar.toCSV());
      System.out.println(clienteAEditar);

    } catch (RegistroNoEncontradoException e) {
      System.out.println(e.getMessage());
    } catch (ArchivoCSVException e) {
      System.out.println("No se pudo guardar el registro: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("Datos en formato incorrecto: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("Ocurrió un error inesperado: " + e.getMessage());
    }
  }

  /**
   * Solicita al usuario el ID de un cliente y lo elimina del archivo CSV
   * especificado.
   *
   * @param rutaArchivoClientes La ruta del archivo CSV del cual se eliminará el
   *                            registro del cliente.
   */
  private static void eliminarCliente(String rutaArchivoClientes) {
    int idCliente = EntradaConsola.leerEntero("Ingresa la llave del cliente a borrar: ");

    try {
      HandlerCSV.removeRegistro(rutaArchivoClientes, idCliente);
      System.out.println("Se eliminó el cliente con llave '" + idCliente + "'.");

    } catch (RegistroNoEncontradoException e) {
      System.out.println(e.getMessage());
    } catch (ArchivoCSVException e) {
      System.out.println("No se pudo modificar el archivo: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("Ocurrió un error inesperado: " + e.getMessage());
    }
  }

  /**
   * Crea los tres archivos CSV con su encabezado en caso de que no existan.
   *
   * Se invoca una única vez al arrancar la aplicación. Si los archivos ya
   * existen el método no los modifica, de modo que la información capturada en
   * ejecuciones anteriores se conserva.
   *
   * @param rutaArchivoSucursales Ruta del archivo CSV de sucursales.
   * @param rutaArchivoPremios    Ruta del archivo CSV de premios.
   * @param rutaArchivoClientes   Ruta del archivo CSV de clientes.
   * @return true si los tres archivos quedaron listos, false si alguno falló.
   */
  private static boolean inicializarArchivos(String rutaArchivoSucursales, String rutaArchivoPremios,
      String rutaArchivoClientes) {
    String encabezadoSucursales = "idSucursal,nombre,calle,numExterior,numeroInterior,colonia,estado,telefono,horario";
    String encabezadoPremios = "idPremio,nombre,categoria,rangoEdad,puntosRequeridos,valorAproximado,idSucursal,stock";
    String encabezadoClientes = "idCliente,nombreCliente,apellidoP,apellidoM,fechaNac,edad,sexo,correoE,telefonoC";

    try {
      if (HandlerCSV.inicializarArchivo(rutaArchivoSucursales, encabezadoSucursales)) {
        System.out.println("Se creó el archivo '" + rutaArchivoSucursales + "'.");
      }
      if (HandlerCSV.inicializarArchivo(rutaArchivoPremios, encabezadoPremios)) {
        System.out.println("Se creó el archivo '" + rutaArchivoPremios + "'.");
      }
      if (HandlerCSV.inicializarArchivo(rutaArchivoClientes, encabezadoClientes)) {
        System.out.println("Se creó el archivo '" + rutaArchivoClientes + "'.");
      }

      return true;

    } catch (ArchivoCSVException e) {
      System.out.println("No se pudieron preparar los archivos de datos: " + e.getMessage());
      System.out.println("Revisa que exista la carpeta 'SRC' y que tengas permisos de escritura.");
      return false;
    }
  }

  /**
   * Metodo main donde se despliega el menu
   * 
   * @param args Argumentos de la línea de comandos.
   */
  public static void main(String[] args) {
    String rutaArchivoSucursales = "./SRC/sucursales.csv";
    String rutaArchivoClientes = "./SRC/clientes.csv";
    String rutaArchivoPremios = "./SRC/premios.csv";

    // Los archivos se preparan una sola vez, al arrancar la aplicación.
    // Si alguno no puede crearse no tiene sentido continuar.
    if (!inicializarArchivos(rutaArchivoSucursales, rutaArchivoPremios, rutaArchivoClientes)) {
      return;
    }

    boolean salir = false;

    // Bucle hasta que el usuario decida cerrar la aplicación.
    while (!salir) {
      try {
        mostrarMenuP();
        int opcion = EntradaConsola.leerOpcion("Selecciona una opción: ", 1, 4);

        switch (opcion) {
          case 1:
            gestionarSucursales(rutaArchivoSucursales);
            break;
          case 2:
            gestionarPremios(rutaArchivoPremios);
            break;
          case 3:
            gestionarClientes(rutaArchivoClientes);
            break;
          case 4:
            System.out.println("\nGracias por usar PuellaGame. Hasta pronto.");
            salir = true;
            break;
        }

        // Última red de seguridad: si algo se escapa de los menús, la aplicación
        // lo reporta y sigue funcionando en lugar de terminar.
      } catch (Exception e) {
        System.out.println("\nOcurrió un error inesperado: " + e.getMessage());
      }
    }

    EntradaConsola.cerrar();
  }
}
