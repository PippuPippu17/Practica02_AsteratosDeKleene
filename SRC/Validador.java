import exceptions.ValidacionException;
import java.util.List;
import java.util.Locale;

/**
 * Concentra las reglas de dominio y de formato que debe cumplir la información
 * antes de escribirse en los archivos CSV.
 *
 * Cada método valida un aspecto puntual y lanza {@link ValidacionException} con
 * un mensaje que explica la regla incumplida. Las listas públicas describen los
 * dominios cerrados que se documentaron en el análisis de requerimientos, de
 * modo que exista un único lugar donde consultarlos y modificarlos.
 *
 * Todos los métodos son estáticos, ya que la clase funciona como una utilidad y
 * no guarda estado propio.
 */
public class Validador {

  /** Valores admitidos para el sexo de un cliente. */
  public static final List<String> SEXOS = List.of("Masculino", "Femenino", "No binario");

  /** Categorías admitidas para un premio, ordenadas de menor a mayor. */
  public static final List<String> CATEGORIAS_PREMIO = List.of("Bajo", "Medio", "Grande");

  /** Rangos de edad admitidos para un premio. */
  public static final List<String> RANGOS_EDAD = List.of("Infantil", "Juvenil", "Adulto");

  /** Puntos mínimos que debe pedir cualquier premio para poder canjearse. */
  public static final int PUNTOS_MINIMOS_CANJE = 20;

  /** Cantidad de dígitos que debe tener un número telefónico. */
  public static final int DIGITOS_TELEFONO = 10;

  /** Edad mínima de un cliente, de acuerdo al rango infantil del caso de uso. */
  public static final int EDAD_MINIMA = 3;

  /** Edad máxima aceptada para un cliente. */
  public static final int EDAD_MAXIMA = 120;

  /**
   * Constructor privado para impedir que la clase se instancie, ya que solo
   * ofrece métodos estáticos.
   */
  private Validador() {
  }

  // ------------------------------------------------------------ Texto ------

  /**
   * Verifica que un texto tenga contenido.
   *
   * @param valor Texto a revisar.
   * @param campo Nombre del campo, para construir el mensaje de error.
   * @return El texto sin espacios sobrantes al inicio ni al final.
   * @throws ValidacionException Si el texto es nulo o solo tiene espacios.
   */
  public static String validarTextoNoVacio(String valor, String campo) throws ValidacionException {
    if (valor == null || valor.trim().isEmpty()) {
      throw new ValidacionException("El campo '" + campo + "' no puede quedar vacío.");
    }

    return valor.trim();
  }

  /**
   * Verifica que un texto pertenezca a un dominio cerrado de valores.
   *
   * La comparación no distingue mayúsculas de minúsculas, y el valor que se
   * regresa es siempre el del catálogo, para que todos los registros del CSV
   * queden escritos de la misma manera.
   *
   * @param valor    Texto capturado por el usuario.
   * @param opciones Lista de valores permitidos.
   * @param campo    Nombre del campo, para construir el mensaje de error.
   * @return El valor tal como aparece en el catálogo.
   * @throws ValidacionException Si el valor no está dentro de las opciones.
   */
  public static String validarEnCatalogo(String valor, List<String> opciones, String campo)
      throws ValidacionException {
    String limpio = validarTextoNoVacio(valor, campo);

    for (String opcion : opciones) {
      if (opcion.equalsIgnoreCase(limpio)) {
        return opcion;
      }
    }

    throw new ValidacionException(campo, limpio, "los valores permitidos son " + String.join(", ", opciones) + ".");
  }

  /**
   * Verifica que un texto no contenga el separador de columnas del CSV.
   *
   * Es una validación preventiva pensada para los campos que el usuario captura
   * libremente.
   *
   * @param valor Texto a revisar.
   * @param campo Nombre del campo, para construir el mensaje de error.
   * @return El texto sin espacios sobrantes.
   * @throws ValidacionException Si el texto está vacío.
   */
  public static String validarTextoDeCampo(String valor, String campo) throws ValidacionException {
    return validarTextoNoVacio(valor, campo);
  }

  // ----------------------------------------------------------- Números -----

  /**
   * Verifica que un número entero sea mayor o igual a cero.
   *
   * @param valor Número a revisar.
   * @param campo Nombre del campo, para construir el mensaje de error.
   * @return El mismo número si es válido.
   * @throws ValidacionException Si el número es negativo.
   */
  public static int validarEnteroNoNegativo(int valor, String campo) throws ValidacionException {
    if (valor < 0) {
      throw new ValidacionException(campo, String.valueOf(valor), "debe ser mayor o igual a cero.");
    }

    return valor;
  }

  /**
   * Verifica que un número entero sea estrictamente mayor que cero.
   *
   * @param valor Número a revisar.
   * @param campo Nombre del campo, para construir el mensaje de error.
   * @return El mismo número si es válido.
   * @throws ValidacionException Si el número es cero o negativo.
   */
  public static int validarEnteroPositivo(int valor, String campo) throws ValidacionException {
    if (valor <= 0) {
      throw new ValidacionException(campo, String.valueOf(valor), "debe ser mayor que cero.");
    }

    return valor;
  }

  /**
   * Verifica que una cantidad monetaria sea positiva.
   *
   * @param valor Cantidad a revisar.
   * @param campo Nombre del campo, para construir el mensaje de error.
   * @return La misma cantidad si es válida.
   * @throws ValidacionException Si la cantidad es cero o negativa.
   */
  public static double validarMontoPositivo(double valor, String campo) throws ValidacionException {
    if (valor <= 0) {
      throw new ValidacionException(campo, String.format(Locale.US, "%.2f", valor),
          "debe ser una cantidad mayor que cero.");
    }

    return valor;
  }

  /**
   * Verifica que un número esté dentro de un intervalo cerrado.
   *
   * @param valor Número a revisar.
   * @param min   Valor mínimo permitido.
   * @param max   Valor máximo permitido.
   * @param campo Nombre del campo, para construir el mensaje de error.
   * @return El mismo número si es válido.
   * @throws ValidacionException Si el número queda fuera del intervalo.
   */
  public static int validarEnRango(int valor, int min, int max, String campo) throws ValidacionException {
    if (valor < min || valor > max) {
      throw new ValidacionException(campo, String.valueOf(valor),
          "debe estar entre " + min + " y " + max + ".");
    }

    return valor;
  }

  /**
   * Verifica que un número sea múltiplo de diez.
   *
   * Aplica a los costos de los juegos y a los montos de recarga, según las
   * reglas de negocio del caso de uso.
   *
   * @param valor Número a revisar.
   * @param campo Nombre del campo, para construir el mensaje de error.
   * @return El mismo número si es válido.
   * @throws ValidacionException Si el número no es múltiplo de diez.
   */
  public static int validarMultiploDeDiez(int valor, String campo) throws ValidacionException {
    if (valor % 10 != 0) {
      throw new ValidacionException(campo, String.valueOf(valor), "debe ser un múltiplo de diez.");
    }

    return valor;
  }

  // ---------------------------------------------------------- Contacto -----

  /**
   * Verifica que un número telefónico tenga exactamente diez dígitos.
   *
   * @param telefono Número a revisar.
   * @return El mismo número si es válido.
   * @throws ValidacionException Si el número no tiene diez dígitos.
   */
  public static long validarTelefono(long telefono) throws ValidacionException {
    String digitos = String.valueOf(telefono);

    if (telefono <= 0 || digitos.length() != DIGITOS_TELEFONO) {
      throw new ValidacionException("teléfono", digitos,
          "debe tener exactamente " + DIGITOS_TELEFONO + " dígitos.");
    }

    return telefono;
  }

  /**
   * Verifica que un número telefónico escrito como texto tenga exactamente diez
   * dígitos.
   *
   * Los teléfonos de un cliente se guardan como texto porque son varios dentro de
   * una misma columna del CSV.
   *
   * @param telefono Número a revisar.
   * @return El número sin espacios sobrantes.
   * @throws ValidacionException Si el número no tiene diez dígitos o trae algo
   *                             que
   *                             no sea un dígito.
   */
  public static String validarTelefono(String telefono) throws ValidacionException {
    String limpio = validarTextoNoVacio(telefono, "teléfono");

    if (!limpio.matches("\\d{" + DIGITOS_TELEFONO + "}")) {
      throw new ValidacionException("teléfono", limpio,
          "debe tener exactamente " + DIGITOS_TELEFONO + " dígitos y solo dígitos.");
    }

    return limpio;
  }

  /**
   * Verifica que un correo electrónico tenga una estructura mínima válida.
   *
   * Se revisa que exista una sola arroba, que haya texto antes y después de
   * ella, y que el dominio incluya un punto que no quede en los extremos.
   *
   * @param correo Correo a revisar.
   * @return El correo sin espacios sobrantes.
   * @throws ValidacionException Si el correo no cumple con la estructura.
   */
  public static String validarCorreo(String correo) throws ValidacionException {
    String limpio = validarTextoNoVacio(correo, "correo electrónico");

    int posArroba = limpio.indexOf('@');
    int ultimaArroba = limpio.lastIndexOf('@');

    if (posArroba <= 0 || posArroba != ultimaArroba || posArroba == limpio.length() - 1) {
      throw new ValidacionException("correo electrónico", limpio,
          "debe tener una sola arroba con texto antes y después. Ejemplo: 'nombre@dominio.com'.");
    }

    String dominio = limpio.substring(posArroba + 1);
    int posPunto = dominio.indexOf('.');

    if (posPunto <= 0 || posPunto == dominio.length() - 1) {
      throw new ValidacionException("correo electrónico", limpio,
          "el dominio debe incluir un punto. Ejemplo: 'nombre@dominio.com'.");
    }

    if (limpio.contains(" ")) {
      throw new ValidacionException("correo electrónico", limpio, "no puede contener espacios.");
    }

    return limpio;
  }

  // ------------------------------------------------------------ Fechas ----

  /**
   * Verifica que una fecha escrita como DD/MM/AAAA corresponda a un día que
   * existe en el calendario.
   *
   * Se guarda como texto y no como número para no perder el cero inicial de los
   * días y los meses menores a diez.
   *
   * @param fecha Fecha con formato DD/MM/AAAA.
   * @return La fecha normalizada, siempre con dos dígitos de día y de mes.
   * @throws ValidacionException Si la fecha no existe o está fuera de rango.
   */
  public static String validarFecha(String fecha) throws ValidacionException {
    String limpia = validarTextoNoVacio(fecha, "fecha");
    String[] partes = limpia.split("/");

    if (partes.length != 3) {
      throw new ValidacionException("fecha", limpia, "debe escribirse con el formato DD/MM/AAAA.");
    }

    int dia;
    int mes;
    int anio;

    try {
      dia = Integer.parseInt(partes[0].trim());
      mes = Integer.parseInt(partes[1].trim());
      anio = Integer.parseInt(partes[2].trim());

    } catch (NumberFormatException e) {
      throw new ValidacionException("fecha", limpia, "el día, el mes y el año deben ser números.");
    }

    if (mes < 1 || mes > 12) {
      throw new ValidacionException("fecha", limpia, "el mes '" + mes + "' no existe.");
    }

    if (anio < 1900 || anio > 2100) {
      throw new ValidacionException("fecha", limpia, "el año '" + anio + "' está fuera de rango.");
    }

    int diasDelMes = diasDelMes(mes, anio);

    if (dia < 1 || dia > diasDelMes) {
      throw new ValidacionException("fecha", limpia,
          "el mes '" + mes + "' del año '" + anio + "' tiene " + diasDelMes + " días.");
    }

    return String.format("%02d/%02d/%04d", dia, mes, anio);
  }

  /**
   * Verifica que una fecha no sea posterior al día de hoy.
   *
   * @param fecha Fecha con formato DD/MM/AAAA, ya validada.
   * @return La misma fecha si es válida.
   * @throws ValidacionException Si la fecha está en el futuro.
   */
  public static String validarFechaPasada(String fecha) throws ValidacionException {
    String normalizada = validarFecha(fecha);
    String[] partes = normalizada.split("/");

    java.time.LocalDate capturada = java.time.LocalDate.of(
        Integer.parseInt(partes[2]), Integer.parseInt(partes[1]), Integer.parseInt(partes[0]));

    if (capturada.isAfter(java.time.LocalDate.now())) {
      throw new ValidacionException("fecha", normalizada, "no puede ser una fecha futura.");
    }

    return normalizada;
  }

  /**
   * Regresa cuántos días tiene un mes, tomando en cuenta los años bisiestos.
   *
   * @param mes  Número de mes, del 1 al 12.
   * @param anio Año al que pertenece el mes.
   * @return Cantidad de días del mes.
   */
  private static int diasDelMes(int mes, int anio) {
    switch (mes) {
      case 2:
        return esBisiesto(anio) ? 29 : 28;
      case 4:
      case 6:
      case 9:
      case 11:
        return 30;
      default:
        return 31;
    }
  }

  /**
   * Indica si un año es bisiesto.
   *
   * @param anio Año a revisar.
   * @return true si el año es bisiesto.
   */
  public static boolean esBisiesto(int anio) {
    return (anio % 4 == 0 && anio % 100 != 0) || anio % 400 == 0;
  }

  // ------------------------------------------- Reglas propias del caso -----

  /**
   * Verifica que la edad derivada de una fecha de nacimiento corresponda a una
   * persona que el centro puede atender.
   *
   * La edad ya no se captura, se calcula a partir de la fecha, así que esta
   * validación se aplica sobre el resultado del cálculo.
   *
   * @param edad Edad a revisar.
   * @return La misma edad si es válida.
   * @throws ValidacionException Si la edad queda fuera del rango permitido.
   */
  public static int validarEdad(int edad) throws ValidacionException {
    return validarEnRango(edad, EDAD_MINIMA, EDAD_MAXIMA, "edad");
  }

  /**
   * Verifica que los puntos que pide un premio correspondan a su categoría.
   *
   * Los rangos provienen del caso de uso: los premios bajos van de 20 a 1,000
   * puntos, los medios de 1,001 a 3,999 y los grandes de 4,000 en adelante.
   *
   * @param categoria Categoría del premio, ya validada contra su catálogo.
   * @param puntos    Puntos necesarios para canjear el premio.
   * @return Los mismos puntos si son válidos.
   * @throws ValidacionException Si los puntos no corresponden a la categoría.
   */
  public static int validarPuntosSegunCategoria(String categoria, int puntos) throws ValidacionException {
    if (puntos < PUNTOS_MINIMOS_CANJE) {
      throw new ValidacionException("puntos requeridos", String.valueOf(puntos),
          "ningún premio puede pedir menos de " + PUNTOS_MINIMOS_CANJE + " puntos.");
    }

    switch (categoria) {
      case "Bajo":
        if (puntos > 1000) {
          throw new ValidacionException("puntos requeridos", String.valueOf(puntos),
              "un premio bajo va de " + PUNTOS_MINIMOS_CANJE + " a 1,000 puntos.");
        }
        break;

      case "Medio":
        if (puntos < 1001 || puntos > 3999) {
          throw new ValidacionException("puntos requeridos", String.valueOf(puntos),
              "un premio medio va de 1,001 a 3,999 puntos.");
        }
        break;

      case "Grande":
        if (puntos < 4000) {
          throw new ValidacionException("puntos requeridos", String.valueOf(puntos),
              "un premio grande pide 4,000 puntos o más.");
        }
        break;

      default:
        throw new ValidacionException("categoría", categoria,
            "los valores permitidos son " + String.join(", ", CATEGORIAS_PREMIO) + ".");
    }

    return puntos;
  }
}
