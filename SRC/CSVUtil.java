/**
 * Utilidad que se encarga de armar y de separar las líneas de los archivos CSV.
 *
 * Existe porque separar una línea con {@code split(",")} falla en cuanto un
 * campo contiene una coma: el nombre de una sucursal como 'Sucursal Centro,
 * Norte' generaría una columna de más y el registro quedaría ilegible.
 *
 * La solución sigue la convención habitual de los archivos CSV: cuando un campo
 * contiene una coma, una comilla doble o un salto de línea, el campo se
 * encierra
 * entre comillas dobles, y las comillas que forman parte del texto se duplican.
 * Al leer, el separador solo se toma en cuenta cuando está fuera de comillas.
 *
 * Todos los métodos son estáticos, ya que la clase funciona como una utilidad y
 * no guarda estado propio.
 */
public class CSVUtil {

  /** Carácter que separa las columnas de un registro. */
  public static final char SEPARADOR = ',';

  /** Carácter que se utiliza para delimitar un campo que necesita escaparse. */
  public static final char COMILLA = '"';

  /**
   * Constructor privado para impedir que la clase se instancie, ya que solo
   * ofrece métodos estáticos.
   */
  private CSVUtil() {
  }

  /**
   * Prepara un campo para escribirse dentro de una línea CSV.
   *
   * Si el campo contiene una coma, una comilla doble o un salto de línea, se
   * encierra entre comillas y se duplican las comillas internas. En cualquier
   * otro caso el campo se escribe tal cual.
   *
   * @param campo Texto a escapar. Un valor nulo se trata como texto vacío.
   * @return El campo listo para escribirse en el archivo.
   */
  public static String escapar(String campo) {
    if (campo == null) {
      return "";
    }

    boolean necesitaComillas = campo.indexOf(SEPARADOR) >= 0
        || campo.indexOf(COMILLA) >= 0
        || campo.contains("\n")
        || campo.contains("\r");

    if (!necesitaComillas) {
      return campo;
    }

    // Cada comilla del texto original se escribe dos veces para distinguirla
    // de las comillas que delimitan el campo.
    String interno = campo.replace("\"", "\"\"");

    return COMILLA + interno + COMILLA;
  }

  /**
   * Arma una línea CSV completa a partir de los campos que la componen.
   *
   * Cada campo se escapa antes de unirse, de modo que los métodos que la usan
   * no tengan que preocuparse por el contenido de los datos.
   *
   * @param campos Valores de las columnas, en el orden del encabezado.
   * @return La línea lista para escribirse en el archivo.
   */
  public static String unir(String... campos) {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < campos.length; i++) {
      if (i > 0) {
        sb.append(SEPARADOR);
      }

      sb.append(escapar(campos[i]));
    }

    return sb.toString();
  }

  /**
   * Separa una línea CSV en las columnas que la componen.
   *
   * El separador solo divide cuando se encuentra fuera de comillas, y las
   * comillas duplicadas dentro de un campo se convierten de nuevo en una sola.
   *
   * @param linea Línea leída del archivo.
   * @return Arreglo con el contenido de cada columna, ya sin comillas ni
   *         espacios sobrantes.
   */
  public static String[] parsear(String linea) {
    if (linea == null || linea.isEmpty()) {
      return new String[0];
    }

    java.util.List<String> columnas = new java.util.ArrayList<>();
    StringBuilder campoActual = new StringBuilder();
    boolean dentroDeComillas = false;
    char caracterActual = ' ';

    for (int i = 0; i < linea.length(); i++) {
      caracterActual = linea.charAt(i);

      if (dentroDeComillas) {
        if (caracterActual == COMILLA) {
          // Dos comillas seguidas representan una comilla del texto.
          if (i + 1 < linea.length() && linea.charAt(i + 1) == COMILLA) {
            campoActual.append(COMILLA);
            i++;
          } else {
            dentroDeComillas = false;
          }
        } else {
          campoActual.append(caracterActual);
        }

      } else {
        if (caracterActual == COMILLA) {
          dentroDeComillas = true;
        } else if (caracterActual == SEPARADOR) {
          columnas.add(campoActual.toString().trim());
          campoActual.setLength(0);
        } else {
          campoActual.append(caracterActual);
        }
      }
    }

    // La última columna no termina en separador, se agrega al salir del bucle.
    columnas.add(campoActual.toString().trim());

    return columnas.toArray(new String[0]);
  }

  /**
   * Regresa la llave de un registro, es decir, el contenido de su primera
   * columna convertido a número.
   *
   * @param linea Línea leída del archivo.
   * @return La llave del registro.
   * @throws NumberFormatException Si la primera columna no es un número.
   */
  public static int obtenerLlave(String linea) {
    String[] columnas = parsear(linea);

    if (columnas.length == 0) {
      throw new NumberFormatException("El registro está vacío y no tiene llave.");
    }

    return Integer.parseInt(columnas[0].trim());
  }
}
