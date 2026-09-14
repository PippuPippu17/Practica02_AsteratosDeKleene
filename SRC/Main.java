
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * Clase que gestiona el menú  de la aplicacion 
 * "PuellaGame".
 **/
public class Main {

  // Declaramos el scanner para la entraada del usuario.
  private static final Scanner scanner = new Scanner(System.in);

    /**
     * Metodo main donde se despliega el menu
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
      boolean salir = false;
      
      // Bucle hasta que el usuario decida cerrar la app
      while (!salir) {

        // Manejo de excepciones para entradas invalidas
        try {
          mostrarMenuP();
          int opcion = entrada("Selecciona una opción: ");

          switch (opcion) {
            //Llama al metodo entidad para hacer operaciones 
            //con el parametro "Sucursales"
            case 1:
              gestionarSucursales();
              break;
            case 2:
            //Llama al metodo entidad para hacer operaciones 
            //con el parametro "Premios"
              gestionarPremios();
              break;
            case 3:
              //Llama al metodo entidad para hacer operaciones 
              //con el parametro "Clientes"
              gestionarClientes();
              break;
            case 4:
              // Cierra la app, que triste, el usuario ya no la va a usar chale ni modo
              System.out.println("\nSaliendo de la app ):");
              salir = true;
              break;
              //Cualquier entrada invalida arroja una advertencia
            default:
              System.out.println("\nIngresa un número valido (entre 1 y 4.");
          }
          //Atrapa la excepcion con InputMismatchException para entradas invalidas
        } catch (InputMismatchException e) {
          System.out.println("\nIngresa una entrada valida" + e.getMessage());
          // Limpia el scanner para ingresar una nueva entrada
          scanner.nextLine();
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
     private static void gestionarSucursales() {
      boolean volvermenup = false;

      while (!volvermenup) {
        try {
          System.out.println("\nSucursales");
          System.out.println("1. Agregar Sucursal");
          System.out.println("2. Consultar por Llave de Sucursal");
          System.out.println("3. Editar Sucursal");
          System.out.println("4. Eliminar Sucursal");
          System.out.println("5. Volver al menú principal");

          int opcion = entrada("Selecciona una operación: ");

          switch (opcion) {
            case 1:
              System.out.println("\nAgregando Sucursal...");
              // Método para guardar en el CSV
              break;
            case 2:
              int id = entrada("Ingresa llave de Sucursal: ");
              System.out.println("\nConsultando Sucursal con llave: " + id);
              // Método para buscar en el CSV
              break;
            case 3:
              int idEdit = entrada("Ingresa la llave de Sucursal: ");
              System.out.println("\nEditando Sucursal con llave: " + idEdit);
              // Método para editar en el CSV
              break;
            case 4:
              int idElim = entrada("Ingresa la llave de Sucursal a eliminar: ");
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
     * Metodo para gestionar Premios.
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

          int opcion = entrada("Selecciona una operación: ");

          switch (opcion) {
            case 1:
              System.out.println("\nAgregando Premio...");
              // Método para guardar en el CSV
              break;
            case 2:
              int id = entrada("Ingresa llave de Premio: ");
              System.out.println("\nConsultando Premio con llave: " + id);
              // Método para buscar en el CSV
              break;
            case 3:
              int idEdit = entrada("Ingresa la llave de Premio: ");
              System.out.println("\nEditando Premio con llave: " + idEdit);
              // Método para editar en el CSV
              break;
            case 4:
              int idElim = entrada("Ingresa la llave de Premio a eliminar: ");
              System.out.println("\nEliminando Premio con llave: " + idElim);
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
     * Metodo para gestionar Clientes.
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

          int opcion = entrada("Selecciona una operación: ");

          switch (opcion) {
            case 1:
              System.out.println("\nAgregando Cliente...");
              // Método para guardar en el CSV
              break;
            case 2:
              int id = entrada("Ingresa llave de Cliente: ");
              System.out.println("\nConsultando Cliente con llave: " + id);
              // Método para buscar en el CSV
              break;
            case 3:
              int idEdit = entrada("Ingresa la llave de Cliente: ");
              System.out.println("\nEditando Cliente con llave: " + idEdit);
              // Método para editar en el CSV
              break;
            case 4:
              int idElim = entrada("Ingresa la llave de Cliente a eliminar: ");
              System.out.println("\nEliminando Cliente con llave: " + idElim);
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
     * Verifica que la entrada sea un numero entero
     * @param mensaje Mensaje de advertencia.
     * @return Entero valido.
     */
    private static int entrada(String mensaje) {
      while (true) {
        try {
          System.out.print(mensaje);
          return scanner.nextInt();
        } catch (InputMismatchException e) {
          System.out.println("Ingresa un numero entero.");
          // Limpia el scanner para ingresar una nueva entrada
          scanner.nextLine();
        }
      }
    }
  }
