import exceptions.ArchivoCSVException;
import exceptions.RegistroNoEncontradoException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que se encarga de manejar la lectura, escritura y manipulación
 * de datos en archivos CSV.
 *
 * Todos los métodos son estáticos, ya que la clase funciona como una utilidad
 * y no guarda estado propio.
 */
public class HandlerCSV {

  /**
   * Crea el archivo CSV con el encabezado indicado en caso de que aún no exista.
   *
   * El método es idempotente: si el archivo ya existe no hace nada y no lanza
   * ninguna excepción, de modo que puede invocarse con seguridad cada vez que
   * arranca la aplicación.
   *
   * @param rutaArchivo Ruta donde se creará el archivo.
   * @param encabezado  Primera línea con las columnas del CSV.
   * @return true si el archivo se creó en esta llamada, false si ya existía.
   * @throws ArchivoCSVException Si el encabezado es inválido o si ocurre un fallo
   *                             al escribir.
   */
  public static boolean inicializarArchivo(String rutaArchivo, String encabezado) throws ArchivoCSVException {
    if (encabezado == null || encabezado.trim().isEmpty()) {
      throw new ArchivoCSVException("El encabezado no puede estar vacío.");
    }

    File archivo = new File(rutaArchivo);

    // Si ya existe no se toca, para no perder los registros que tenga dentro.
    if (archivo.exists()) {
      return false;
    }

    // Nos aseguramos de que exista el directorio que va a contener al archivo.
    File directorio = archivo.getParentFile();
    if (directorio != null && !directorio.exists()) {
      directorio.mkdirs();
    }

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
      bw.write(encabezado);
      bw.newLine();
      return true;

    } catch (IOException e) {
      throw new ArchivoCSVException("No se pudo crear el archivo con la ruta: '" + rutaArchivo + "'.", e);
    }
  }

  /**
   * Agrega un nuevo registro al final del archivo CSV.
   * Valida que no exista previamente un registro con la misma llave.
   *
   * @param rutaArchivo Ruta del archivo CSV donde se insertará el registro.
   * @param id          Identificador numérico del nuevo registro.
   * @param lineaCSV    Cadena con los datos formateados en CSV.
   * @throws ArchivoCSVException Si ya existe la llave o si ocurre un fallo al
   *                             escribir.
   */
  public static void addRegistro(String rutaArchivo, int id, String lineaCSV) throws ArchivoCSVException {
    if (lineaCSV == null || lineaCSV.trim().isEmpty()) {
      throw new ArchivoCSVException("No se puede agregar un registro vacío.");
    }

    if (existeId(rutaArchivo, id)) {
      throw new ArchivoCSVException("Ya existe un registro con la llave '" + id + "'.");
    }

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
      bw.write(lineaCSV);
      bw.newLine();

    } catch (IOException e) {
      throw new ArchivoCSVException("Hubo un error al intentar escribir en '" + rutaArchivo + "'.", e);
    }
  }

  /**
   * Indica si en el archivo existe un registro con la llave proporcionada.
   *
   * Este método es la manera segura de preguntar por una llave, ya que no lanza
   * excepción cuando el registro no está.
   *
   * @param rutaArchivo Ruta del archivo CSV donde se realizará la búsqueda.
   * @param id          Identificador numérico del registro a buscar.
   * @return true si la llave existe, false en caso contrario.
   * @throws ArchivoCSVException Si el archivo no existe o hay errores de lectura
   *                             y formato.
   */
  public static boolean existeId(String rutaArchivo, int id) throws ArchivoCSVException {
    return buscarLinea(rutaArchivo, id) != null;
  }

  /**
   * Regresa la línea completa del CSV que tenga la llave ingresada.
   *
   * A diferencia de {@link #existeId(String, int)}, este método nunca regresa
   * null: si la llave no está, lanza una excepción. Así ningún método que lo
   * utilice puede continuar por accidente con un valor nulo.
   *
   * @param rutaArchivo Ruta del archivo CSV donde se realizará la búsqueda.
   * @param id          Identificador numérico del registro a buscar.
   * @return La línea completa encontrada.
   * @throws ArchivoCSVException           Si el archivo no existe o hay errores
   *                                       de lectura y formato.
   * @throws RegistroNoEncontradoException Si no hay ningún registro con esa
   *                                       llave.
   */
  public static String buscarPorId(String rutaArchivo, int id)
      throws ArchivoCSVException, RegistroNoEncontradoException {
    String linea = buscarLinea(rutaArchivo, id);

    if (linea == null) {
      throw new RegistroNoEncontradoException(id, rutaArchivo);
    }

    return linea;
  }

  /**
   * Recorre el archivo en busca de la llave indicada.
   * Omite el encabezado y las líneas en blanco.
   *
   * Es el único punto del manejador donde una búsqueda puede resultar nula, y
   * por eso se mantiene privado.
   *
   * @param rutaArchivo Ruta del archivo CSV donde se realizará la búsqueda.
   * @param id          Identificador numérico del registro a buscar.
   * @return La línea encontrada, o null si la llave no está en el archivo.
   * @throws ArchivoCSVException Si el archivo no existe o hay errores de lectura
   *                             y formato.
   */
  private static String buscarLinea(String rutaArchivo, int id) throws ArchivoCSVException {
    File archivo = new File(rutaArchivo);
    if (!archivo.exists()) {
      throw new ArchivoCSVException("No hay ningún archivo en la ruta ingresada: '" + rutaArchivo + "'.");
    }

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
      int idActual = 0;

      // Leemos la primera línea que es el encabezado.
      String lineaLeida = br.readLine();

      while ((lineaLeida = br.readLine()) != null) {
        if (lineaLeida.trim().isEmpty()) {
          continue;
        }

        idActual = CSVUtil.obtenerLlave(lineaLeida);

        if (idActual == id) {
          return lineaLeida;
        }
      }

    } catch (IOException | NumberFormatException e) {
      throw new ArchivoCSVException("Hubo un error al leer y procesar el archivo '" + rutaArchivo + "'.", e);
    }

    return null;
  }

  /**
   * Elimina un registro de acuerdo a la llave proporcionada.
   *
   * @param rutaArchivo Ruta del archivo CSV donde se eliminará el registro.
   * @param id          Identificador numérico del registro a borrar.
   * @throws ArchivoCSVException           Si hubo problemas de lectura o
   *                                       escritura.
   * @throws RegistroNoEncontradoException Si la llave no existe en el archivo.
   */
  public static void removeRegistro(String rutaArchivo, int id)
      throws ArchivoCSVException, RegistroNoEncontradoException {
    if (!existeId(rutaArchivo, id)) {
      throw new RegistroNoEncontradoException(id, rutaArchivo);
    }

    List<String> registros = getAll(rutaArchivo);
    String registroActual = "";
    int idActual = 0;

    // Bucle que para hasta que se haya eliminado el registro con la llave deseada.
    for (int i = 0; i < registros.size(); i++) {
      registroActual = registros.get(i);
      idActual = CSVUtil.obtenerLlave(registroActual);

      if (idActual == id) {
        registros.remove(i);
        break;
      }
    }

    // Reescribimos el archivo con la lista de registros correcta.
    setAll(rutaArchivo, getEncabezado(rutaArchivo), registros);
  }

  /**
   * Actualiza un registro existente reemplazándolo con la nueva línea ingresada.
   *
   * La sustitución se hace en memoria y el archivo se reescribe una sola vez, de
   * manera que si algo falla no se pierde el registro original.
   *
   * @param rutaArchivo Ruta del archivo CSV donde se encuentra el registro.
   * @param id          Identificador numérico del registro a actualizar.
   * @param lineaCSV    Cadena con los nuevos datos formateados en CSV.
   * @throws ArchivoCSVException           Si hay un error al sobrescribir el
   *                                       archivo.
   * @throws RegistroNoEncontradoException Si la llave no existe en el archivo.
   */
  public static void setRegistro(String rutaArchivo, int id, String lineaCSV)
      throws ArchivoCSVException, RegistroNoEncontradoException {
    if (lineaCSV == null || lineaCSV.trim().isEmpty()) {
      throw new ArchivoCSVException("No se puede guardar un registro vacío.");
    }

    if (!existeId(rutaArchivo, id)) {
      throw new RegistroNoEncontradoException(id, rutaArchivo);
    }

    List<String> registros = getAll(rutaArchivo);
    int idActual = 0;

    // Sustituimos la línea correspondiente conservando su posición original.
    for (int i = 0; i < registros.size(); i++) {
      idActual = CSVUtil.obtenerLlave(registros.get(i));

      if (idActual == id) {
        registros.set(i, lineaCSV);
        break;
      }
    }

    setAll(rutaArchivo, getEncabezado(rutaArchivo), registros);
  }

  /**
   * Regresa todos los registros que se encuentren en el archivo ingresado.
   * Ignora el encabezado y las líneas vacías.
   *
   * @param rutaArchivo Ruta del archivo CSV a leer.
   * @return Lista de Strings con cada uno de los registros.
   * @throws ArchivoCSVException Si el archivo no existe o hay un error al leerlo.
   */
  public static List<String> getAll(String rutaArchivo) throws ArchivoCSVException {
    File archivo = new File(rutaArchivo);
    if (!archivo.exists()) {
      throw new ArchivoCSVException("No se encontró un archivo en la ruta: '" + rutaArchivo + "'.");
    }

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
      List<String> registros = new ArrayList<>();
      String lineaLeida = br.readLine();

      while ((lineaLeida = br.readLine()) != null) {
        if (!lineaLeida.trim().isEmpty()) {
          registros.add(lineaLeida);
        }
      }

      return registros;

    } catch (IOException e) {
      throw new ArchivoCSVException("Error al cargar la lista completa desde '" + rutaArchivo + "'.", e);
    }
  }

  /**
   * Sobrescribe un archivo entero a partir de una lista de Strings con formato
   * correcto.
   *
   * @param rutaArchivo Ruta del archivo a sobrescribir.
   * @param encabezado  Primera línea con las columnas del CSV.
   * @param lineas      Lista de cadenas que conformarán el nuevo contenido.
   * @throws ArchivoCSVException Si el encabezado es inválido o si ocurre un error
   *                             de escritura.
   */
  private static void setAll(String rutaArchivo, String encabezado, List<String> lineas) throws ArchivoCSVException {
    if (encabezado == null || encabezado.trim().isEmpty()) {
      throw new ArchivoCSVException("El encabezado no puede estar vacío.");
    }

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, false))) {
      // Escribe al inicio el encabezado.
      bw.write(encabezado);
      bw.newLine();

      // Bucle que se encarga de escribir toda la lista en el archivo.
      for (String l : lineas) {
        bw.write(l);
        bw.newLine();
      }
    } catch (IOException e) {
      throw new ArchivoCSVException("Hubo un error al intentar sobrescribir '" + rutaArchivo + "'.", e);
    }
  }

  /**
   * Regresa el encabezado del archivo CSV ingresado.
   *
   * @param rutaArchivo Ruta del archivo a consultar.
   * @return Un String que contiene el encabezado del archivo.
   * @throws ArchivoCSVException Si el archivo no existe, está vacío o hay un
   *                             error al leerlo.
   */
  public static String getEncabezado(String rutaArchivo) throws ArchivoCSVException {
    File archivo = new File(rutaArchivo);
    if (!archivo.exists()) {
      throw new ArchivoCSVException("No se encontró el archivo: '" + rutaArchivo + "'.");
    }

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
      String encabezado = br.readLine();

      if (encabezado == null || encabezado.trim().isEmpty()) {
        throw new ArchivoCSVException("El archivo '" + rutaArchivo + "' no tiene encabezado.");
      }

      return encabezado;

    } catch (IOException e) {
      throw new ArchivoCSVException("Error al leer el encabezado de '" + rutaArchivo + "'.", e);
    }
  }
}
