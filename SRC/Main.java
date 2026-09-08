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
              entidad("Sucursales");
              break;
            case 2:
            //Llama al metodo entidad para hacer operaciones 
            //con el parametro "Premios"
              entidad("Premios");
              break;
            case 3:
              //Llama al metodo entidad para hacer operaciones 
              //con el parametro "Clientes"
              entidad("Clientes");
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
     * Toma como parametro la entrada del usuario para las operaciones de la entidad.
     *  
     * @param entidad Nombre de la entidad seleccionada.
     */
    private static void entidad(String entidad) {
      boolean volvermenup = false;

      // Bucle hasta que el usuario decida salir de la operacion ingresada
      while (!volvermenup) {
        
        try {
          // Despliega las operaciones disponibles con cada entidad
          System.out.println("\n" + entidad);
          System.out.println("1. Agregar " + entidad);
          System.out.println("2. Consultar por Llave de ENtidad");
          System.out.println("3. Editar " + entidad);
          System.out.println("4. Eliminar " + entidad);
          
          int opcion = entrada("Selecciona una operación: ");

          switch (opcion) {
            case 1:
              System.out.println("\n Agregando entidad :v");
              // Aqui se pondra el método para guardar en el CSV
              break;
            case 2:
              int id = entrada("Ingresa llave de Entidad UwU");
              // Aqui se pondra el método para buscar los datos en el CSV
              System.out.println("\n" + entidad + id);
              // Llamada al método de búsqueda por llave
              break;
            case 3:
              int idEdit = entrada("Ingresa la llave de Entidad");
              // Llamada al método de edicion por llave
              System.out.println("\n" + idEdit + "Edicion insana xd");
              break;
            case 4:
              int idElim = entrada("Ingresa la llave a eliminar ");
              //Llama al metodo apara eliminar lave
              System.out.println("hola" + idElim);
              break;
            case 5:
              // Regresa al menu principal rompiendo el bucle
              volvermenup = true;
              break;
            default:
              // Mensaje de advertencia si se ingresa una entrada invalida
              System.out.println("\nOpcion invalida. Elige un numero entre 1 y 5.");
          }
        } catch (Exception e) {
          System.out.println("\nIngresa entrada valida" + e.getMessage());
          // Limpia el scanner para ingresar una nueva entrada
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
