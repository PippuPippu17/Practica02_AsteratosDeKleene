import exceptions.ArchivoCSVException;
import exceptions.RegistroNoEncontradoException;

/**
 * Clase base de los menús que administran una entidad del prototipo.
 *
 * Los tres menús del sistema hacen exactamente lo mismo: muestran cinco
 * opciones, agregan un registro, lo consultan por su llave, lo editan y lo
 * eliminan. Lo único que cambia entre ellos es qué datos se capturan y cómo se
 * reconstruye el objeto a partir de una línea del archivo.
 *
 * Por eso aquí vive todo lo que se repite, incluyendo el manejo de excepciones,
 * y cada menú concreto solo implementa las tres operaciones que dependen de su
 * entidad. Antes de esta separación, las mismas veinte líneas de captura de
 * errores estaban escritas nueve veces dentro de la clase principal.
 *
 * @param <T> Entidad que administra el menú.
 */
public abstract class MenuEntidad<T extends Registrable> {

  /** Ruta del archivo CSV donde se guardan los registros de la entidad. */
  protected final String rutaArchivo;

  /** Nombre de la entidad en singular, por ejemplo "sucursal". */
  protected final String entidad;

  /** Nombre de la entidad en plural y con mayúscula, por ejemplo "Sucursales". */
  protected final String entidadPlural;

  /**
   * Construye el menú de una entidad.
   *
   * @param rutaArchivo   Ruta del archivo CSV de la entidad.
   * @param entidad       Nombre en singular y minúscula, usado en los mensajes.
   * @param entidadPlural Nombre en plural y con mayúscula, usado en el título.
   */
  protected MenuEntidad(String rutaArchivo, String entidad, String entidadPlural) {
    this.rutaArchivo = rutaArchivo;
    this.entidad = entidad;
    this.entidadPlural = entidadPlural;
  }

  // ------------------------------------------------- Operaciones propias ----

  /**
   * Pide al usuario los datos de un registro nuevo y construye la entidad.
   *
   * @return El registro capturado.
   * @throws Exception Si alguno de los datos capturados no es válido.
   */
  protected abstract T capturar() throws Exception;

  /**
   * Pide al usuario los datos con los que se sustituirá un registro existente.
   *
   * @param llave    Llave del registro que se está editando, que no cambia.
   * @param original Registro tal como está guardado, por si se necesita.
   * @return El registro con los datos nuevos.
   * @throws Exception Si alguno de los datos capturados no es válido.
   */
  protected abstract T capturarEdicion(int llave, T original) throws Exception;

  /**
   * Reconstruye un registro a partir de una línea del archivo CSV.
   *
   * @param lineaCSV Línea leída del archivo.
   * @return El registro reconstruido.
   * @throws Exception Si la línea no tiene el formato esperado.
   */
  protected abstract T desdeCSV(String lineaCSV) throws Exception;

  // ------------------------------------------------------------- Menú ------

  /**
   * Muestra el menú de la entidad y atiende las operaciones que elija el usuario
   * hasta que pida regresar al menú principal.
   */
  public void mostrar() {
    boolean volver = false;

    while (!volver) {
      System.out.println("\n" + entidadPlural);
      System.out.println("1. Agregar " + entidad);
      System.out.println("2. Consultar " + entidad + " por llave");
      System.out.println("3. Editar " + entidad);
      System.out.println("4. Eliminar " + entidad);
      System.out.println("5. Volver al menú principal");

      int opcion = EntradaConsola.leerOpcion("Selecciona una operación: ", 1, 5);
      System.out.println();

      switch (opcion) {
        case 1:
          agregar();
          break;
        case 2:
          consultar();
          break;
        case 3:
          editar();
          break;
        case 4:
          eliminar();
          break;
        case 5:
          volver = true;
          break;
      }
    }
  }

  // -------------------------------------------------------------- CRUD -----

  /**
   * Captura un registro nuevo y lo escribe en el archivo CSV.
   *
   * La llave se valida antes de pedir el resto de los datos, para no hacer
   * escribir al usuario información que no se va a poder guardar.
   */
  public void agregar() {
    System.out.println("Ingresa los datos de la nueva " + entidad + ":");

    try {
      T registro = capturar();
      HandlerCSV.addRegistro(rutaArchivo, registro.getLlave(), registro.toCSV());

      System.out.println("\nSe agregó con éxito la " + entidad + " con los siguientes datos:");
      System.out.println(registro);

    } catch (ArchivoCSVException e) {
      System.out.println(e.getMessage());
    } catch (Exception e) {
      reportar(e);
    }
  }

  /**
   * Pide una llave, busca el registro y muestra sus datos completos.
   */
  public void consultar() {
    int llave = EntradaConsola.leerEntero("Ingresa la llave de la " + entidad + " a consultar: ");

    try {
      T registro = desdeCSV(HandlerCSV.buscarPorId(rutaArchivo, llave));

      System.out.println("\nDatos de la " + entidad + " con llave '" + llave + "':");
      System.out.println(registro);

    } catch (RegistroNoEncontradoException e) {
      System.out.println(e.getMessage());
    } catch (ArchivoCSVException e) {
      System.out.println("No se pudo leer el archivo: " + e.getMessage());
    } catch (Exception e) {
      reportar(e);
    }
  }

  /**
   * Pide una llave y, si el registro existe, captura los datos nuevos y
   * sobrescribe la línea correspondiente del archivo.
   */
  public void editar() {
    int llave = EntradaConsola.leerEntero("Ingresa la llave de la " + entidad + " a editar: ");
    T original = null;

    // Se confirma que el registro existe antes de pedir nada más.
    try {
      original = desdeCSV(HandlerCSV.buscarPorId(rutaArchivo, llave));

    } catch (RegistroNoEncontradoException e) {
      System.out.println(e.getMessage());
      return;
    } catch (ArchivoCSVException e) {
      System.out.println("No se pudo leer el archivo: " + e.getMessage());
      return;
    } catch (Exception e) {
      reportar(e);
      return;
    }

    System.out.println("\nDatos actuales:");
    System.out.println(original);
    System.out.println("Ingresa los datos nuevos de la " + entidad + ":");

    try {
      T editado = capturarEdicion(llave, original);
      HandlerCSV.setRegistro(rutaArchivo, llave, editado.toCSV());

      System.out.println("\nLos nuevos datos de la " + entidad + " con llave '" + llave + "' son:");
      System.out.println(editado);

    } catch (RegistroNoEncontradoException e) {
      System.out.println(e.getMessage());
    } catch (ArchivoCSVException e) {
      System.out.println("No se pudo guardar el registro: " + e.getMessage());
    } catch (Exception e) {
      reportar(e);
    }
  }

  /**
   * Pide una llave, muestra el registro y lo elimina previa confirmación.
   */
  public void eliminar() {
    int llave = EntradaConsola.leerEntero("Ingresa la llave de la " + entidad + " a borrar: ");

    try {
      T registro = desdeCSV(HandlerCSV.buscarPorId(rutaArchivo, llave));

      System.out.println("\nSe va a eliminar el siguiente registro:");
      System.out.println(registro);

      if (!EntradaConsola.confirmar("¿Confirmas la eliminación?")) {
        System.out.println("No se eliminó nada.");
        return;
      }

      HandlerCSV.removeRegistro(rutaArchivo, llave);
      System.out.println("Se eliminó la " + entidad + " con llave '" + llave + "'.");

    } catch (RegistroNoEncontradoException e) {
      System.out.println(e.getMessage());
    } catch (ArchivoCSVException e) {
      System.out.println("No se pudo modificar el archivo: " + e.getMessage());
    } catch (Exception e) {
      reportar(e);
    }
  }

  // ----------------------------------------------------------- Auxiliar ----

  /**
   * Informa al usuario de un error que no corresponde a los casos previstos.
   *
   * Separa los errores de formato de los verdaderamente inesperados, para que el
   * mensaje sea útil sin exponer detalles internos.
   *
   * @param e Excepción que se va a reportar.
   */
  protected void reportar(Exception e) {
    if (e instanceof IllegalArgumentException) {
      System.out.println("El registro está en un formato incorrecto: " + e.getMessage());
    } else {
      System.out.println("Ocurrió un error inesperado: " + e.getMessage());
    }
  }
}
