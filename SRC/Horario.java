import exceptions.HorarioException;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa el horario de atención de una sucursal.
 *
 * El horario se captura como una sola cadena con el formato
 * 'Dia-Dia HoraInicio-HoraFin', y pueden encadenarse varios rangos separados
 * con el carácter '|', por ejemplo
 * 'Lun-Vie 11:00-21:00|Sab-Dom 10:00-22:00'.
 *
 * La clase valida que los días existan, que se capturen en orden cronológico,
 * que ningún día reciba dos horarios distintos y que las horas estén escritas
 * en formato de veinticuatro horas.
 */
public class Horario {

  /** Abreviaturas válidas de los días, en el orden en que corren en la semana. */
  public static final List<String> DIAS_VALIDOS = List.of("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom");

  /** Texto del horario tal como lo capturó el usuario. */
  private String horario = "";

  /** Días a los que todavía no se les ha asignado un rango de horas. */
  private List<String> diasDisponibles = new ArrayList<>(DIAS_VALIDOS);

  /**
   * Construye un horario a partir de su representación en texto y lo valida.
   *
   * @param horario Horario con el formato 'Dia-Dia HH:MM-HH:MM', que puede
   *                encadenar varios rangos separados con el carácter '|'.
   * @throws HorarioException Si el horario está vacío, si algún día no existe, si
   *                          los días no van en orden, si un día recibe dos
   *                          rangos o si alguna hora está fuera de rango.
   */
  public Horario(String horario) throws HorarioException {
    if (horario == null || horario.trim().isEmpty()) {
      throw new HorarioException("No puedes ingresar un horario vacío.");
    }

    try {
      // Cadena copia.
      StringBuilder sb = new StringBuilder(horario);
      int posDeCorte = 0;
      String rangoDeDias = "";
      int[] rangoDeDiasValidado = new int[0];
      String rangoDeHoras = "";

      // Bucle que verifica todos los horarios de atención.
      while (true) {
        posDeCorte = sb.indexOf(" ");

        // Este fragmento de código nos regresa el rango de días ya válidado, el rango
        // es de acuerdo a DIAS_VALIDOS .
        rangoDeDias = sb.substring(0, posDeCorte);
        rangoDeDiasValidado = getRangoDeDiasValido(rangoDeDias);

        // Actualizamos la cadena copia para quedarnos con el rango de horas.
        sb.delete(0, posDeCorte + 1);

        // Verficamos que esten bien delimitados los horarios.
        if (sb.indexOf("|") == -1 && sb.length() != 11) {
          throw new HorarioException("Error: '" + horario
              + "'. Para añadir multiples horarios de atención tienes que delimitar con '|'.");
        }

        // Dependiendo de si estamos en la última iteración cambia la posición de corte.
        posDeCorte = (sb.indexOf("|") == -1 ? sb.length() : sb.indexOf("|"));
        rangoDeHoras = sb.substring(0, posDeCorte);

        // Verificamos el rango de horas, tiene que estar en formato militar.
        verificarRangoDeHoras(rangoDeHoras);

        // Una vez que verificamos el rango de horas eliminamos los días ya que ahora
        // tienen un horario válido y este deja de estar disponible.
        for (int i = 0; i < rangoDeDiasValidado[1] + 1; i++) {
          diasDisponibles.remove(DIAS_VALIDOS.get(i));
        }

        // En caso de no encontrar más "|", quiere decir que hemos acabado, por lo tanto
        // finalizamos el bucle
        if (posDeCorte == sb.length()) {
          sb.delete(0, posDeCorte);
          // System.out.println(diasDisponibles); // depuración del código
          break;
        }

        // En caso de encontrar un "|" eliminamos el rango de horas que estabamos
        // checando para seguir con el siguiente
        // rango de días y horas.
        sb.delete(0, posDeCorte + 1);
        // System.out.println(diasDisponibles); // depuración del código
      }

    } catch (IndexOutOfBoundsException e) {
      // Atrapa cualquier error de corte de índices inválidos
      throw new HorarioException(
          "Error: '" + horario
              + "'. Formato de horario mal estructurado. Formato esperado: 'Dia-Dia HoraApertura-HoraCierre|Dia-Dia HoraApertura-HoraCierre' ",
          e);
    } catch (NullPointerException e) {
      throw new HorarioException("Se encontró un valor nulo encontrado al procesar el horario.", e);
    }

    this.horario = horario;
  }

  /**
   * Valida la sección de los días de un rango y regresa sus posiciones.
   *
   * Comprueba que los dos días existan, que el inicial sea anterior al final y
   * que ninguno de ellos tenga ya un horario asignado.
   *
   * @param dias Sección de días con el formato 'Dia-Dia'.
   * @return Arreglo con la posición del día inicial y la del día final.
   * @throws HorarioException Si los días no son válidos o ya fueron usados.
   */
  private int[] getRangoDeDiasValido(String dias) throws HorarioException {
    if (dias == null || dias.trim().isEmpty()) {
      throw new HorarioException("La sección de los días no puede estar vacía.");
    }

    // Cadena copia.
    StringBuilder sb = new StringBuilder(dias);

    // Posición delimitadora.
    int posGuion = sb.indexOf("-");

    // Verificamos el delimitador que pedimos se encuentre en la cadena.
    if (posGuion == -1) {
      throw new HorarioException(
          "El formato del rango de días es inválido. Formato esperado: 'Dia-Dia'");
    }

    // Variables que nos van a ayudar a determinar el rango de días que se ocuparon
    // si es que resultan válidos.
    int primerDia = 0;
    int segundoDia = 0;

    // Bucle que se encarga de verificar que el rango de días sea correcto.
    int posInicial = 0;
    int posFinal = 0;
    String dia = "";
    int estado = 0;
    while (estado < 2) {

      // El substring que tomaremos depende del estado en el que estemos.
      posInicial = (estado == 0 ? 0 : (posGuion + 1));
      posFinal = (estado == 0 ? posGuion : sb.length());

      dia = sb.substring(posInicial, posFinal).trim();

      // Verificamos que el dia que obtuvimos sea válido.
      if (!DIAS_VALIDOS.contains(dia)) {
        throw new HorarioException("El día '" + dia + "' no es un día válido.");
      }

      if (DIAS_VALIDOS.indexOf(diasDisponibles.getFirst()) > DIAS_VALIDOS.indexOf(dia)) {
        throw new HorarioException("Error: '" + dia
            + "'. Tienes que definir los rangos de los días en forma cronológica. Ejemplo correcto: 'Lun-Mar 12:00-14:00|Mie-Jue 13:00-15:00'.");
      }

      // Verificamos que el dia que obtuvimos este disponible.
      if (!diasDisponibles.contains(dia)) {
        throw new HorarioException("El día '" + dia + "' ya tiene un horario definido.");
      }

      // En caso de que el formato haya sido válido guardamos el día en su variable
      // correspondiente.
      if (estado == 0) {
        primerDia = DIAS_VALIDOS.indexOf(dia);
      } else {
        segundoDia = DIAS_VALIDOS.indexOf(dia);
      }

      // Cambiamos de estado.
      estado++;
    }

    // Verificamos que el rango de días este un orden cronológico.
    if (primerDia > segundoDia) {
      throw new HorarioException(
          "Rango de días inválido: '" + DIAS_VALIDOS.get(primerDia) + "-" + DIAS_VALIDOS.get(segundoDia) +
              "'. El día inicial debe de ser anterior al final. Ejemplo: 'Lun-Vie' o 'Mier-Sab').");
    }

    int[] rangoValidoDeDias = { primerDia, segundoDia };

    return rangoValidoDeDias;
  }

  /**
   * Valida la sección de las horas de un rango.
   *
   * Comprueba el formato, que las horas y los minutos estén dentro de su rango y
   * que la hora de cierre sea posterior a la de apertura.
   *
   * @param horas Sección de horas con el formato 'HH:MM-HH:MM'.
   * @throws HorarioException Si el formato o los valores no son válidos.
   */
  private void verificarRangoDeHoras(String horas) throws HorarioException {
    if (horas == null || horas.trim().isEmpty()) {
      throw new HorarioException("La sección de la hora no puede estar vacía.");
    }

    // Cadena copia.
    StringBuilder sb = new StringBuilder(horas);

    // Posición delimitadora.
    int posGuion = sb.indexOf("-");

    // Verificamos el delimitador que pedimos se encuentre en la cadena.
    if (posGuion == -1) {
      throw new HorarioException(
          "El formato del rango de horas es inválido. Formato esperado: '00:01-23:59'");
    }

    // Variables que van a guardar las horas en caso de que sean válidas.
    int[] hora1 = new int[0];
    int[] hora2 = new int[0];

    // Bucle que se encarga de verificar que el rango de horas sea correcto.
    int estado = 0;
    int posInicial = 0;
    int posFinal = 0;
    int[] horaAux = new int[0];
    while (estado < 2) {
      posInicial = (estado == 0 ? 0 : (posGuion + 1));
      posFinal = (estado == 0 ? posGuion : sb.length());

      horaAux = obtenerHoraYMinuto(sb.substring(posInicial, posFinal));
      hora1 = (estado == 0 ? horaAux : hora1);
      hora2 = (estado == 0 ? hora1 : horaAux);

      estado++;
    }

    if (hora1[0] > hora2[0]) {
      throw new HorarioException(
          "Error: '" + sb + "'. El rango de horas debe de ir en órden cronológico. Ejemplo: '01:00-02:00'");
    }

    if (hora1[0] == hora2[0]) {
      if (hora1[1] >= hora2[1]) {
        throw new HorarioException(
            "Error: '" + sb
                + "'. Al ser la misma hora los minutos deben de ir en orden cronológico y debe de haber al menos un  minuto de diferencia. Ejemplo: '00:01-00:02'");
      }
    }
  }

  /**
   * Separa una hora en sus dos componentes numéricas y las valida.
   *
   * @param hora Hora con el formato 'HH:MM'.
   * @return Arreglo con la hora y el minuto.
   * @throws HorarioException Si el formato no es válido o los valores están fuera
   *                          de rango.
   */
  private int[] obtenerHoraYMinuto(String hora) throws HorarioException {
    if (hora == null || hora.trim().isEmpty()) {
      throw new HorarioException("La sección de la hora no puede tener una hora vacía.");
    }

    // Cadena copia.
    StringBuilder sb = new StringBuilder(hora);

    // Posición delimitadora.
    int posDosPuntos = sb.indexOf(":");

    // Verificamos el delimitador que pedimos se encuentre en la cadena.
    if (posDosPuntos == -1) {
      throw new HorarioException(
          "El formato de la hora es inválido. Formato esperado: 'hora:minutos'");
    }

    // Variables que van a guardar las horas y minutos en caso de que sean válidas.
    int horaValida = 0;
    int minutoValido = 0;

    // Bucle que se encarga de verificar que el rango de horas sea correcto.
    int estado = 0;
    int posInicial = 0;
    int posFinal = 0;
    int tiempoActual = 0;
    while (estado < 2) {
      posInicial = (estado == 0 ? 0 : (posDosPuntos + 1));
      posFinal = (estado == 0 ? posDosPuntos : sb.length());

      // Pasamos ya sea la hora o minuto de cadena a enteros.
      // Si el texto no es numérico lo convertimos a HorarioException para que
      // el usuario reciba un mensaje claro en lugar de un error crudo.
      String textoTiempo = sb.substring(posInicial, posFinal).trim();
      try {
        tiempoActual = Integer.parseInt(textoTiempo);
      } catch (NumberFormatException e) {
        throw new HorarioException(
            "El valor '" + textoTiempo + "' no es un número válido. Formato esperado: 'hora:minutos'.", e);
      }
      horaValida = (estado == 0 ? tiempoActual : horaValida);
      minutoValido = (estado == 0 ? minutoValido : tiempoActual);

      // De acuerdo al estado verificamos de acuerdo a el rango de horas o minutos.
      if (estado == 0) {
        if (horaValida < 0 || horaValida >= 24) {
          throw new HorarioException(
              "La hora '" + horaValida + "' no es válida, tiene que estar en el rango [0 , 24).");
        }
      } else {
        if (minutoValido < 0 || minutoValido >= 60) {
          throw new HorarioException(
              "El minuto '" + minutoValido + "' no es válida, tiene que estar en el rango [0 , 60).");
        }
      }

      estado++;
    }

    int[] horaYMinuto = { horaValida, minutoValido };

    return horaYMinuto;
  }

  @Override
  public String toString() {
    return horario;
  }
}
