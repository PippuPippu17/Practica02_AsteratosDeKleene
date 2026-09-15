import exceptions.HorarioException;
import exceptions.ValidacionException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Centraliza toda la lectura de datos desde el teclado.
 *
 * Existe una sola razón de peso para concentrarla aquí: todos los métodos leen
 * la línea completa con {@code nextLine()} y después la convierten al tipo que
 * corresponde. Nunca se usan {@code nextInt()} ni {@code nextDouble()}, que
 * dejan el salto de línea pendiente en el buffer y obligan a intercalar
 * llamadas de limpieza por todo el programa.
 *
 * Cuando el usuario captura algo que no corresponde al tipo solicitado, el
 * método vuelve a preguntar en lugar de propagar una excepción, de manera que
 * la aplicación nunca termina por un dato mal escrito.
 *
 * Todos los métodos son estáticos, ya que la clase funciona como una utilidad y
 * comparte un único lector sobre la entrada estándar.
 */
public class EntradaConsola {

  /** Lector compartido sobre la entrada estándar. */
  private static final Scanner scanner = new Scanner(System.in);

  /**
   * Constructor privado para impedir que la clase se instancie, ya que solo
   * ofrece métodos estáticos.
   */
  private EntradaConsola() {
  }

  /**
   * Lee una línea completa desde el teclado.
   *
   * Si la entrada estándar se agota, por ejemplo cuando el programa se ejecuta
   * con un archivo redirigido, se termina la aplicación de forma ordenada en
   * lugar de dejar escapar una excepción.
   *
   * @param mensaje Texto que se muestra antes de leer.
   * @return La línea capturada, sin espacios al inicio ni al final.
   */
  private static String leerLinea(String mensaje) {
    System.out.print(mensaje);

    try {
      return scanner.nextLine().trim();

    } catch (NoSuchElementException e) {
      System.out.println();
      System.out.println("Se terminó la entrada de datos. Cerrando la aplicación.");
      System.exit(0);
      return "";
    }
  }

  /**
   * Lee un texto con contenido, repitiendo la pregunta mientras el usuario deje
   * el campo vacío.
   *
   * @param mensaje Texto que se muestra antes de leer.
   * @return El texto capturado.
   */
  public static String leerTexto(String mensaje) {
    while (true) {
      String entrada = leerLinea(mensaje);

      if (!entrada.isEmpty()) {
        return entrada;
      }

      System.out.println("Este campo no puede quedar vacío.");
    }
  }

  /**
   * Lee un texto que puede quedar vacío, en cuyo caso se sustituye por un valor
   * por omisión.
   *
   * Se usa, por ejemplo, para el número interior de una sucursal, donde la
   * ausencia de dato se registra como 'S/N'.
   *
   * @param mensaje    Texto que se muestra antes de leer.
   * @param porOmision Valor que se regresa cuando el usuario no escribe nada.
   * @return El texto capturado o el valor por omisión.
   */
  public static String leerTextoOpcional(String mensaje, String porOmision) {
    String entrada = leerLinea(mensaje);
    return entrada.isEmpty() ? porOmision : entrada;
  }

  /**
   * Lee un texto que debe pertenecer a un dominio cerrado de valores.
   *
   * Muestra las opciones disponibles y repite la pregunta hasta que el usuario
   * escriba una de ellas. No distingue mayúsculas de minúsculas y regresa el
   * valor tal como aparece en el catálogo.
   *
   * @param etiqueta Nombre del campo que se está capturando.
   * @param opciones Lista de valores permitidos.
   * @return El valor elegido, escrito como aparece en el catálogo.
   */
  public static String leerDeCatalogo(String etiqueta, List<String> opciones) {
    String mensaje = etiqueta + " (" + String.join(" / ", opciones) + "): ";

    while (true) {
      String entrada = leerLinea(mensaje);

      try {
        return Validador.validarEnCatalogo(entrada, opciones, etiqueta);

      } catch (ValidacionException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Lee un número entero, repitiendo la pregunta mientras el usuario capture
   * algo que no sea un número.
   *
   * @param mensaje Texto que se muestra antes de leer.
   * @return El número capturado.
   */
  public static int leerEntero(String mensaje) {
    while (true) {
      String entrada = leerLinea(mensaje);

      try {
        return Integer.parseInt(entrada);

      } catch (NumberFormatException e) {
        System.out.println("'" + entrada + "' no es un número entero. Escribe solo dígitos.");
      }
    }
  }

  /**
   * Lee un número entero que debe quedar dentro de un intervalo cerrado.
   *
   * @param mensaje Texto que se muestra antes de leer.
   * @param min     Valor mínimo permitido.
   * @param max     Valor máximo permitido.
   * @return El número capturado.
   */
  public static int leerEntero(String mensaje, int min, int max) {
    while (true) {
      int valor = leerEntero(mensaje);

      if (valor >= min && valor <= max) {
        return valor;
      }

      System.out.println("El valor debe estar entre " + min + " y " + max + ".");
    }
  }

  /**
   * Lee un número entero largo, pensado para los campos que guardan teléfonos.
   *
   * @param mensaje Texto que se muestra antes de leer.
   * @return El número capturado.
   */
  public static long leerLong(String mensaje) {
    while (true) {
      String entrada = leerLinea(mensaje);

      try {
        return Long.parseLong(entrada);

      } catch (NumberFormatException e) {
        System.out.println("'" + entrada + "' no es un número. Escribe solo dígitos, sin espacios ni guiones.");
      }
    }
  }

  /**
   * Lee un número telefónico y no lo acepta hasta que tenga la cantidad de
   * dígitos que exige el sistema.
   *
   * @param mensaje Texto que se muestra antes de leer.
   * @return El teléfono capturado.
   */
  public static long leerTelefono(String mensaje) {
    while (true) {
      long telefono = leerLong(mensaje);

      try {
        return Validador.validarTelefono(telefono);

      } catch (ValidacionException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Lee un número telefónico como texto y no lo acepta hasta que tenga la
   * cantidad de dígitos que exige el sistema.
   *
   * @param mensaje Texto que se muestra antes de leer.
   * @return El teléfono capturado.
   */
  public static String leerTelefonoTexto(String mensaje) {
    while (true) {
      String entrada = leerLinea(mensaje);

      try {
        return Validador.validarTelefono(entrada);

      } catch (ValidacionException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Lee un correo electrónico y no lo acepta hasta que tenga una estructura
   * válida.
   *
   * @param mensaje Texto que se muestra antes de leer.
   * @return El correo capturado.
   */
  public static String leerCorreo(String mensaje) {
    while (true) {
      String entrada = leerLinea(mensaje);

      try {
        return Validador.validarCorreo(entrada);

      } catch (ValidacionException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Lee una cantidad con decimales, repitiendo la pregunta mientras el usuario
   * capture algo que no sea un número.
   *
   * Se acepta la coma como separador decimal y se convierte a punto, ya que es
   * una costumbre común al capturar cantidades en español.
   *
   * @param mensaje Texto que se muestra antes de leer.
   * @return La cantidad capturada.
   */
  public static double leerDecimal(String mensaje) {
    while (true) {
      String entrada = leerLinea(mensaje).replace(',', '.');

      try {
        return Double.parseDouble(entrada);

      } catch (NumberFormatException e) {
        System.out.println("'" + entrada + "' no es una cantidad válida. Ejemplo: 150.50");
      }
    }
  }

  /**
   * Lee una cantidad monetaria y no la acepta hasta que sea mayor que cero.
   *
   * @param mensaje Texto que se muestra antes de leer.
   * @param campo   Nombre del campo, para construir el mensaje de error.
   * @return La cantidad capturada.
   */
  public static double leerMontoPositivo(String mensaje, String campo) {
    while (true) {
      double monto = leerDecimal(mensaje);

      try {
        return Validador.validarMontoPositivo(monto, campo);

      } catch (ValidacionException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Lee una fecha con formato DD/MM/AAAA y no la acepta hasta que corresponda a
   * un día que exista en el calendario y que no esté en el futuro.
   *
   * @param mensaje Texto que se muestra antes de leer.
   * @return La fecha capturada, normalizada a dos dígitos de día y de mes.
   */
  public static String leerFecha(String mensaje) {
    while (true) {
      String entrada = leerLinea(mensaje);

      try {
        return Validador.validarFechaPasada(entrada);

      } catch (ValidacionException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Lee uno o más valores para un campo multivaluado.
   *
   * El primer valor es obligatorio y después se pregunta si se quiere agregar
   * otro, hasta que el usuario diga que no o se alcance el límite.
   *
   * @param etiqueta Nombre del campo en singular, por ejemplo "correo".
   * @param lector   Función que lee y valida un solo valor.
   * @param maximo   Cantidad máxima de valores que se pueden capturar.
   * @return La lista con los valores capturados.
   */
  public static List<String> leerVarios(String etiqueta, java.util.function.Function<String, String> lector,
      int maximo) {
    List<String> valores = new ArrayList<>();

    while (valores.size() < maximo) {
      String valor = lector.apply(etiqueta.substring(0, 1).toUpperCase() + etiqueta.substring(1)
          + " " + (valores.size() + 1) + ": ");

      if (!valores.contains(valor)) {
        valores.add(valor);
      } else {
        System.out.println("Ese " + etiqueta + " ya está registrado para este cliente.");
      }

      if (valores.size() >= maximo) {
        break;
      }

      if (!confirmar("¿Deseas agregar otro " + etiqueta + "?")) {
        break;
      }
    }

    return valores;
  }

  /**
   * Lee el horario de una sucursal y no lo acepta hasta que cumpla con el
   * formato esperado.
   *
   * Sin este método el horario sería el único campo que se valida hasta el
   * momento de construir la sucursal, de modo que un error de captura obligaría
   * al usuario a volver a escribir todos los datos anteriores.
   *
   * @param mensaje Texto que se muestra antes de leer.
   * @return El horario capturado, ya validado.
   */
  public static String leerHorario(String mensaje) {
    while (true) {
      String entrada = leerTexto(mensaje);

      try {
        // Se construye un Horario solo para aprovechar sus validaciones. Si el
        // texto es correcto se regresa tal cual lo escribió el usuario.
        new Horario(entrada);
        return entrada;

      } catch (HorarioException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * Lee la opción de un menú, aceptando únicamente los números que el menú
   * ofrece.
   *
   * @param mensaje Texto que se muestra antes de leer.
   * @param min     Primera opción válida.
   * @param max     Última opción válida.
   * @return La opción elegida.
   */
  public static int leerOpcion(String mensaje, int min, int max) {
    while (true) {
      int opcion = leerEntero(mensaje);

      if (opcion >= min && opcion <= max) {
        return opcion;
      }

      System.out.println("Opción inválida. Elige un número entre " + min + " y " + max + ".");
    }
  }

  /**
   * Pregunta al usuario una confirmación de sí o no.
   *
   * @param mensaje Texto que se muestra antes de leer.
   * @return true si el usuario confirma.
   */
  public static boolean confirmar(String mensaje) {
    while (true) {
      String entrada = leerLinea(mensaje + " (s/n): ").toLowerCase();

      if (entrada.equals("s") || entrada.equals("si") || entrada.equals("sí")) {
        return true;
      }

      if (entrada.equals("n") || entrada.equals("no")) {
        return false;
      }

      System.out.println("Responde con 's' para sí o 'n' para no.");
    }
  }

  /**
   * Cierra el lector de la entrada estándar.
   *
   * Debe llamarse una sola vez, justo antes de que termine la aplicación.
   */
  public static void cerrar() {
    scanner.close();
  }
}
