import exceptions.ArchivoCSVException;
import exceptions.HorarioException;
import exceptions.RegistroNoEncontradoException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Clase que gestiona el menú de la aplicacion
 * "PuellaGame".
 **/
public class Main {
  // Declaramos el scanner para la entrada del usuario.
  private static final Scanner scanner = new Scanner(System.in);

  /**
   * Método auxiliar para leer una entrada de texto.
   * 
   * @param mensaje Mensaje para el usuario.
   * @return Entrada de texto válida.
   */
  private static String entradaTexto(String mensaje) {
    System.out.print(mensaje);
    return scanner.nextLine();
  }

  /**
   * Método auxiliar que verifica que la entrada del usuario sea un número válido.
   * 
   * @param mensaje Mensaje de advertencia.
   * @return Entero valido.
   */
  private static long entrada(String mensaje) {
    while (true) {
      try {
        System.out.print(mensaje);
        return scanner.nextLong();
      } catch (InputMismatchException e) {
        // Descartamos el token inválido junto con el resto de la línea.
        // Si aquí se volviera a llamar a nextLong() la excepción se repetiría.
        scanner.nextLine();
        System.out.println("Ingresa un número válido.");
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

        long opcionInt = entrada("Selecciona una operación: ");
        int opcion = (int) opcionInt;
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
          default:
            System.out.println("\nOpción inválida. Elige un número entre 1 y 5.");
        }
      } catch (Exception e) {
        System.out.println("\nIngresa una entrada válida: " + e.getMessage());
        scanner.nextLine();
      }
    }
  }

  /**
   * Método para agregar una sucursal al archivo CSV.
   */
  private static void agregarSucursal(String rutaArchivoSucursales) {
    System.out.println("Ingresa los siguientes datos para añadir una nueva sucursal:");

    // Pedir los datos de la sucursal al usuario y los guardar en variables
    int idSucursal = (int) entrada("ID de la Sucursal (debe ser número): ");
    scanner.nextLine();
    String nombre = entradaTexto("Nombre de la Sucursal: ");
    String calle = entradaTexto("Calle: ");
    int numExt = (int) entrada("Número exterior: ");
    scanner.nextLine();
    String numInter = entradaTexto("Número interior: ");
    String colonia = entradaTexto("Colonia: ");
    String estado = entradaTexto("Estado: ");
    long telefono = entrada("Teléfono: ");
    scanner.nextLine();
    String horario = entradaTexto("Horario: ");

    // Escribir los datos en el archivo CSV
    try {
      Sucursal sucursal = new Sucursal(idSucursal, nombre, calle, numExt, numInter, colonia, estado, telefono, horario);
      HandlerCSV.addRegistro(rutaArchivoSucursales, idSucursal, sucursal.toCSV());

      System.out.println(
          "Se agregó con éxito la sucursal a el archivo '" + rutaArchivoSucursales + "' con los siguientes datos:");
      System.out.println(sucursal);

    } catch (HorarioException | ArchivoCSVException e) {
      System.out.println(e.getMessage());
    } catch (Exception e) {
      System.out.println("Ocurrió algo realmente insperado: " + e.getMessage());
    }
  }

  /**
   * Solicita la llave de una sucursal, la busca en el archivo CSV y muestra sus
   * datos completos en consola.
   *
   * @param rutaArchivoSucursales Ruta del archivo CSV de sucursales.
   */
  private static void consultarSucursal(String rutaArchivoSucursales) {
    int idSucursal = (int) entrada("Ingresa la llave de la sucursal a consultar: ");

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
    int idSucursal = (int) entrada("Ingresa la llave de sucursal a editar: ");
    scanner.nextLine();

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
    String nombre = entradaTexto("Nombre de la Sucursal: ");
    String calle = entradaTexto("Calle: ");
    int numExt = (int) entrada("Número exterior: ");
    scanner.nextLine();
    String numInter = entradaTexto("Número interior: ");
    String colonia = entradaTexto("Colonia: ");
    String estado = entradaTexto("Estado: ");
    long telefono = entrada("Teléfono: ");
    scanner.nextLine();
    String horario = entradaTexto("Horario: ");

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
    int idSucursal = (int) entrada("Ingresa la llave de la sucursal a borrar: ");

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

        long opcionInt = entrada("Selecciona una operación: ");
        int opcion = (int) opcionInt;
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
          default:
            System.out.println("\nOpción inválida. Elige un número entre 1 y 5.");
        }
      } catch (Exception e) {
        System.out.println("\nIngresa una entrada válida: " + e.getMessage());
        scanner.nextLine();
      }
    }
  }

  private static void agregarPremio(String rutaArchivoPremios) {
    System.out.println("Ingresa los siguientes datos para añadir un nuevo premio:");

    // Pedir los datos de el premio al usuario y los guardamos en variables
    int idPremio = (int) entrada("ID del premio (debe ser número): ");
    scanner.nextLine();
    String nombre = entradaTexto("Nombre del premio: ");
    String categoria = entradaTexto("Categoría ('Bajo', 'Medio' o 'Grande'): ");
    String rangoEdad = entradaTexto("Rango de Edad ('Infantil', 'Juvenil' o 'Adulto'): ");
    int puntosRequeridos = (int) entrada("Puntos requeridos: ");
    scanner.nextLine();

    System.out.print("Valor aproximado: ");
    double valorAproximado = scanner.nextDouble();
    scanner.nextLine();

    int idSucursal = (int) entrada("ID de la sucursal en la que el premio está disponible (debe ser número): ");
    scanner.nextLine();
    int stock = (int) entrada("Número de premios que hay en existencia: ");
    scanner.nextLine();

    // Escribir los datos en el archivo CSV
    try {
      Premio premio = new Premio(idPremio, nombre, categoria, rangoEdad, puntosRequeridos, valorAproximado, idSucursal,
          stock);
      HandlerCSV.addRegistro(rutaArchivoPremios, idPremio, premio.toCSV());

      System.out.println(
          "Se agregó con éxito el premio a el archivo '" + rutaArchivoPremios + "' con los siguientes datos:");
      System.out.println(premio);

    } catch (Exception e) {
      System.out.println("Ocurrió algo realmente insperado: " + e.getMessage());
    }
  }

  /**
   * Solicita la llave de un premio, lo busca en el archivo CSV y muestra sus
   * datos completos en consola.
   *
   * @param rutaArchivoPremios Ruta del archivo CSV de premios.
   */
  private static void consultarPremio(String rutaArchivoPremios) {
    int idPremio = (int) entrada("Ingresa la llave del premio a consultar: ");

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
    int idPremio = (int) entrada("Ingresa la llave del premio a editar: ");
    scanner.nextLine();

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
    String nombre = entradaTexto("Nombre del premio: ");
    String categoria = entradaTexto("Categoría ('Bajo', 'Medio' o 'Grande'): ");
    String rangoEdad = entradaTexto("Rango de Edad ('Infantil', 'Juvenil' o 'Adulto'): ");
    int puntosRequeridos = (int) entrada("Puntos requeridos: ");
    scanner.nextLine();

    System.out.print("Valor aproximado: ");
    double valorAproximado = scanner.nextDouble();
    scanner.nextLine();

    int idSucursal = (int) entrada("ID de la sucursal en la que el premio está disponible (debe ser número): ");
    scanner.nextLine();
    int stock = (int) entrada("Número de premios que hay en existencia: ");
    scanner.nextLine();

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
    int idPremio = (int) entrada("Ingresa la llave del premio a borrar: ");

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

        long opcionLong = entrada("Selecciona una operación: ");
        int opcion = (int) opcionLong;

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
          default:
            System.out.println("\nOpción inválida. Elige un número entre 1 y 5.");
        }
        // Manejo de excepciones
      } catch (Exception e) {
        System.out.println("\nIngresa una entrada válida: " + e.getMessage());
        scanner.nextLine();
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
    int idCliente = (int) entrada("ID del cliente (debe ser un número): ");
    scanner.nextLine();
    String nombreCliente = entradaTexto("Nombre del cliente: ");
    String apellidoP = entradaTexto("Apellido paterno: ");
    String apellidoM = entradaTexto("Apellido materno: ");
    int fechaNac = (int) entrada("Fecha de nacimiento (DDMMYYYY): ");
    scanner.nextLine();
    int edad = (int) entrada("Edad: ");
    scanner.nextLine();
    String sexo = entradaTexto("Sexo ('Masculino', 'Femenino' o 'No binario'): ");
    String correoE = entradaTexto("Correo electrónico: ");
    long telefonoC = entrada("Teléfono: ");

    // Escribir los datos en el archivo CSV
    try {
      Cliente cliente = new Cliente(idCliente, nombreCliente, apellidoP, apellidoM, fechaNac, edad, sexo, correoE,
          telefonoC);
      HandlerCSV.addRegistro(rutaArchivoClientes, idCliente, cliente.toCSV());

      System.out.println(
          "Se agregó el ciente con éxito a el archivo '" + rutaArchivoClientes + "' con los siguientes datos:");
      System.out.println(cliente);

    } catch (ArchivoCSVException e) {
      System.out.println(e.getMessage());
    } catch (Exception e) {
      System.out.println("Ocurrió algo realmente insperado: " + e.getMessage());
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
    int idCliente = (int) entrada("Ingresa llave del Cliente: ");

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
    int idCliente = (int) entrada("Ingresa la llave del cliente a editar: ");
    scanner.nextLine();

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
    String nombreCliente = entradaTexto("Nombre del cliente: ");
    String apellidoP = entradaTexto("Apellido Paterno: ");
    String apellidoM = entradaTexto("Apellido Materno: ");
    int fechaNac = (int) entrada("Fecha de Nacimiento (DDMMYYYY): ");
    scanner.nextLine();
    int edad = (int) entrada("Edad: ");
    scanner.nextLine();
    String sexo = entradaTexto("Sexo ('Masculino', 'Femenino' o 'No binario'): ");
    String correoE = entradaTexto("Correo Electrónico: ");
    long telefonoC = entrada("Teléfono: ");
    scanner.nextLine();

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
    int idCliente = (int) entrada("Ingresa la llave del cliente a borrar: ");

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
    // Bucle hasta que el usuario decida cerrar la app
    while (!salir) {
      // Manejo de excepciones para entradas invalidas
      try {
        mostrarMenuP();
        long opcionLong = entrada("Selecciona una opción: ");
        int opcion = (int) opcionLong;
        // Casos que puede eligir el usuario
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
            // Cierra la app, que triste, el usuario ya no la va a usar chale ni modo
            System.out.println("\nSaliendo de la app ):");
            salir = true;
            break;
          // Cualquier entrada invalida arroja una advertencia
          default:
            System.out.println("\nIngresa un número valido (entre 1 y 4.");
        }
        // Atrapa la excepcion con InputMismatchException para entradas invalidas
      } catch (InputMismatchException e) {
        System.out.println("\nIngresa una entrada valida" + e.getMessage());
        // Limpia el scanner para ingresar una nueva entrada
        scanner.nextLine();
      }
    }
  }
}
