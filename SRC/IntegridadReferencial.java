import exceptions.ArchivoCSVException;
import exceptions.ValidacionException;
import java.util.ArrayList;
import java.util.List;

/**
 * Comprueba que las referencias entre las entidades apunten a registros que
 * realmente existen.
 *
 * En una base de datos este trabajo lo haría el manejador mediante llaves
 * foráneas. Como el prototipo guarda la información en archivos CSV, las
 * comprobaciones se hacen aquí antes de escribir o de borrar, para que el
 * conjunto de archivos no quede con referencias rotas.
 *
 * Todos los métodos son estáticos, ya que la clase funciona como una utilidad y
 * no guarda estado propio.
 */
public class IntegridadReferencial {

  /**
   * Constructor privado para impedir que la clase se instancie, ya que solo
   * ofrece métodos estáticos.
   */
  private IntegridadReferencial() {
  }

  /**
   * Verifica que exista un registro con la llave indicada en un archivo.
   *
   * @param rutaArchivo Archivo donde debe existir el registro.
   * @param llave       Llave que se está referenciando.
   * @param entidad     Nombre de la entidad, para construir el mensaje de error.
   * @return La misma llave si el registro existe.
   * @throws ValidacionException Si el registro no existe o no se pudo revisar.
   */
  public static int validarExiste(String rutaArchivo, int llave, String entidad) throws ValidacionException {
    try {
      if (!HandlerCSV.existeId(rutaArchivo, llave)) {
        throw new ValidacionException("No existe el registro de " + entidad + " con la llave '" + llave
            + "'. Da de alta ese registro antes de continuar.");
      }

      return llave;

    } catch (ArchivoCSVException e) {
      throw new ValidacionException("No se pudo verificar el registro de " + entidad + ": " + e.getMessage(), e);
    }
  }

  /**
   * Regresa los renglones del inventario que hacen referencia a una sucursal.
   *
   * @param rutaInventario Archivo del inventario.
   * @param idSucursal     Llave de la sucursal.
   * @return Los renglones que dependen de esa sucursal.
   * @throws ArchivoCSVException Si el archivo no se puede leer.
   */
  public static List<InventarioPremio> inventarioDeSucursal(String rutaInventario, int idSucursal)
      throws ArchivoCSVException {
    List<InventarioPremio> encontrados = new ArrayList<>();

    for (String linea : HandlerCSV.getAll(rutaInventario)) {
      try {
        InventarioPremio renglon = InventarioPremio.fromCSV(linea);

        if (renglon.getIdSucursal() == idSucursal) {
          encontrados.add(renglon);
        }

        // Una línea corrupta no debe impedir revisar las demás.
      } catch (RuntimeException e) {
        continue;
      }
    }

    return encontrados;
  }

  /**
   * Regresa los renglones del inventario que hacen referencia a un premio.
   *
   * @param rutaInventario Archivo del inventario.
   * @param idPremio       Llave del premio.
   * @return Los renglones que dependen de ese premio.
   * @throws ArchivoCSVException Si el archivo no se puede leer.
   */
  public static List<InventarioPremio> inventarioDePremio(String rutaInventario, int idPremio)
      throws ArchivoCSVException {
    List<InventarioPremio> encontrados = new ArrayList<>();

    for (String linea : HandlerCSV.getAll(rutaInventario)) {
      try {
        InventarioPremio renglon = InventarioPremio.fromCSV(linea);

        if (renglon.getIdPremio() == idPremio) {
          encontrados.add(renglon);
        }

      } catch (RuntimeException e) {
        continue;
      }
    }

    return encontrados;
  }

  /**
   * Verifica que una sucursal no tenga ya registrado un premio en su inventario.
   *
   * La pareja formada por la sucursal y el premio es lo que identifica de manera
   * natural a un renglón, así que no debe repetirse.
   *
   * @param rutaInventario Archivo del inventario.
   * @param idSucursal     Llave de la sucursal.
   * @param idPremio       Llave del premio.
   * @throws ValidacionException Si la pareja ya está registrada.
   */
  public static void validarParejaLibre(String rutaInventario, int idSucursal, int idPremio)
      throws ValidacionException {
    try {
      for (InventarioPremio renglon : inventarioDeSucursal(rutaInventario, idSucursal)) {
        if (renglon.getIdPremio() == idPremio) {
          throw new ValidacionException("La sucursal '" + idSucursal + "' ya tiene registrado el premio '"
              + idPremio + "' con la llave de inventario '" + renglon.getIdInventario()
              + "'. Edita ese renglón en lugar de crear uno nuevo.");
        }
      }

    } catch (ArchivoCSVException e) {
      throw new ValidacionException("No se pudo revisar el inventario: " + e.getMessage(), e);
    }
  }
}
