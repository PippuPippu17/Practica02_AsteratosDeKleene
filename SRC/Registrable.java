/**
 * Contrato que cumplen las entidades que se guardan en un archivo CSV.
 *
 * Las tres entidades del prototipo comparten dos características: cada una se
 * identifica con una llave numérica y cada una sabe convertirse en una línea de
 * texto. Declararlas aquí permite que los menús trabajen con cualquiera de
 * ellas sin conocer sus detalles.
 *
 * La operación inversa, reconstruir un objeto a partir de una línea, no forma
 * parte de la interfaz porque en Java los métodos estáticos no se heredan. Cada
 * menú concreto se encarga de esa conversión.
 */
public interface Registrable {

  /**
   * Regresa la llave que identifica al registro dentro de su archivo.
   *
   * @return La llave del registro.
   */
  int getLlave();

  /**
   * Convierte el registro a una línea con formato CSV.
   *
   * @return Una línea lista para escribirse en el archivo.
   */
  String toCSV();
}
