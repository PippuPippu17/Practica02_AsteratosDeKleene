
import exceptions.ArchivoCSVException;
import exceptions.HorarioException;
import java.io.FileWriter;
import java.io.IOException;
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
        System.out.println("Ingresa un numero valido.");
        scanner.nextLong();
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
    try {
      String encabezado = "idSucursal,nombre,calle,numExterior,numeroInterior,colonia,estado,telefono,horario";
      HandlerCSV.inicializarArchivo(rutaArchivoSucursales, encabezado);
    } catch (ArchivoCSVException e) {
      System.out.println(e.getMessage());
    }

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

        switch (opcion) {
          case 1:
            agregarSucursal(rutaArchivoSucursales);
            break;
          case 2:
            consultarSucursal(rutaArchivoSucursales);
            break;
          case 3:
            long idEdit = entrada("Ingresa la llave de Sucursal: ");
            System.out.println("\nEditando Sucursal con llave: " + idEdit);
            // Método para editar en el CSV
            break;
          case 4:
            long idElim = entrada("Ingresa la llave de Sucursal a eliminar: ");
            System.out.println("\nEliminando Sucursal con llave: " + idElim);
            // Método para eliminar en el CSV
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

    System.out.println("Ingresa los siguientes datos para la sucursal:");

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

      System.out.println("Se agregó con éxito la sucursal a el archivo '" + rutaArchivoSucursales + "' con los siguientes datos:");
      System.out.println(sucursal);

    } catch (HorarioException | ArchivoCSVException e) {
      System.out.println(e.getMessage());
    } catch (Exception e) {
      System.out.println("Ocurrió algo realmente insperado: " + e.getMessage());
    }
  }

  private static void consultarSucursal(String rutaArchivoSucursales) {
    int idSucursal = (int) entrada("Ingresa llave de Sucursal: ");
    System.out.println("Mostrando datos de la sucursal con llave '" + idSucursal + "' en el archivo '" + rutaArchivoSucursales + "'.");

    try {
      String datosSucursal = HandlerCSV.buscarPorId(rutaArchivoSucursales, idSucursal);
      Sucursal sucursalConsultada = Sucursal.fromCSV(datosSucursal);
      System.out.println(sucursalConsultada);
    } catch (ArchivoCSVException e) {
      System.out.println("Id inválido: " + e.getMessage());
    } catch (HorarioException | NumberFormatException e) {
      System.out.println("Datos en formato incorrecto: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("Ocurrió algo realmente insperado: " + e.getMessage());
    }
  }

  /**
   * Metodo para gestionar Premios.
   * Se puede agregar, consultar, editar y eliminar.
   */
  private static void gestionarPremios() {
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
        switch (opcion) {
          case 1:
            agregarPremio();
            break;
          case 2:
            long id = entrada("Ingresa llave de Premio: ");
            System.out.println("\nConsultando Premio con llave: " + id);
            // Método para buscar en el CSV
            break;
          case 3:
            long idEdit = entrada("Ingresa la llave de Premio: ");
            System.out.println("\nEditando Premio con llave: " + idEdit);
            // Método para editar en el CSV
            break;
          case 4:
            long idElim = entrada("Ingresa la llave de Premio a eliminar: ");
            System.out.println("\nEliminando Premio con llave: " + idElim);
            // Método para eliminar en el CSV
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

  /**
   * Método para agregar premios
   */
  public static void agregarPremio() {
    try {
      System.out.println("Ingresa los siguientes datos para el premio:");

      // Pedir los datos del premio al usuario y los guardar en variables
      long idPremio = entrada("ID del Premio (debe ser un número): ");
      scanner.nextLine();
      String nombreP = entradaTexto("Nombre del Premio: ");
      String categoria = entradaTexto("Categoría del Premio: (Bajo, Medio o Alto): ");
      String rangoEdad = entradaTexto("Rango de Edad (infantil 3-12, juvenil 13-17, adulto 18+): ");
      long puntos = entrada("Puntos necesarios para canjear el premio: ");
      scanner.nextLine();
      long cantidadS = entrada("Cantidad de premios en sucursal: ");
      scanner.nextLine();
      long idSucursal = entrada("ID de la Sucursal donde se encuentra el premio: ");

      // Escribir los datos en el archivo CSV
      try (FileWriter writer = new FileWriter("premios.csv", true)) {
        writer.append(idPremio + "," + nombreP + "," + categoria + "," + rangoEdad + "," + puntos + "," + cantidadS
            + "," + idSucursal + "\n");
        System.out.println("Premio agregado exitosamente.");
      } catch (IOException e) {
        System.out.println("Error al escribir en el archivo CSV: " + e.getMessage());
      }
      // Manejo de excepciones
    } catch (Exception e) {
      System.out.println("Error al agregar el premio: " + e.getMessage());
    }
  }

  /**
   * Metodo para gestionar Clientes.
   * Se puede agregar, consultar, editar y eliminar.
   */
  private static void gestionarClientes() {
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
            agregarCliente();
            break;
          case 2:
            long id = entrada("Ingresa llave de Cliente: ");
            System.out.println("\nConsultando Cliente con llave: " + id);
            // Método para buscar en el CSV
            break;
          case 3:
            long idEdit = entrada("Ingresa la llave de Cliente: ");
            System.out.println("\nEditando Cliente con llave: " + idEdit);
            // Método para editar en el CSV
            break;
          case 4:
            long idElim = entrada("Ingresa la llave de Cliente a eliminar: ");
            System.out.println("\nEliminando Cliente con llave: " + idElim);
            // Método para eliminar en el CSV
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
   * Método para agregar clientes
   */
  public static void agregarCliente() {
    try {
      System.out.println("Ingresa los siguientes datos para el cliente:");

      // Pedir los datos del cliente al usuario y los guardar en variables
      long idCliente = entrada("ID del cliente (debe ser un número): ");
      scanner.nextLine();
      String nombreC = entradaTexto("Nombre del cliente: ");
      String apellidoP = entradaTexto("Apellido paterno: ");
      String apellidoM = entradaTexto("Apellido materno: ");
      long fechaNac = entrada("Fecha de nacimiento (DDMMYYYY): ");
      scanner.nextLine();
      long edad = entrada("Edad:");
      scanner.nextLine();
      String sexo = entradaTexto("Sexo (H/M/otro): ");
      String correoE = entradaTexto("Correo electrónico: ");
      long telefonoC = entrada("Teléfono: ");

      // Escribir los datos en el archivo CSV
      try (FileWriter writer = new FileWriter("clientes.csv", true)) {
        writer.append(idCliente + "," + nombreC + "," + apellidoP + "," + apellidoM + "," + fechaNac + "," + edad + ","
            + sexo + "," + correoE + "," + telefonoC + "\n");
        System.out.println("Cliente agregado exitosamente.");
      } catch (IOException e) {
        System.out.println("Error al escribir en el archivo CSV: " + e.getMessage());
      }
      // Manejo de excepciones
    } catch (Exception e) {
      System.out.println("Error al agregar el cliente: " + e.getMessage());
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
            gestionarPremios();
            break;
          case 3:
            gestionarClientes();
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