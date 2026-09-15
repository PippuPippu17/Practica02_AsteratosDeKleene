import exceptions.ArchivoCSVException;
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
 */
public class HandlerCSV {
    /**
     * Inicializa un nuevo archivo CSV con el encabezado indicado.
     * Valida que el archivo no exista, ya que no se puede inicializar un archvio
     * con información dentro.
     * 
     * @param rutaArchivo Ruta donde se creará el archivo.
     * @param encabezado  Primera línea con las columnas del CSV.
     * @throws ArchivoCSVException Si el archivo ya existe o si ocurre un fallo al
     *                             escribir.
     */
    public static void inicializarArchivo(String rutaArchivo, String encabezado) throws ArchivoCSVException {
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
                bw.write(encabezado);
                bw.newLine();

            } catch (IOException e) {
                throw new ArchivoCSVException("No se pudo crear el archivo con la ruta: '" + rutaArchivo + "'.", e);
            }
        } else {
            throw new ArchivoCSVException("El archivo: '" + rutaArchivo + "' ya ha sido inicializado antes");
        }
    }

    /**
     * Agrega un nuevo registro al final del archivo CSV.
     * Valida que no exista previamente un registro con el mismo id.
     * 
     * @param rutaArchivo Ruta del archivo CSV donde se insertará el registro.
     * @param id          Identificador numérico del nuevo registro.
     * @param lineaCSV    Cadena con los datos formateados en CSV.
     * @throws ArchivoCSVException Si ya existe el ID o si ocurre un fallo al
     *                             escribir.
     */
    public static void addRegistro(String rutaArchivo, int id, String lineaCSV) throws ArchivoCSVException {
        if (buscarPorId(rutaArchivo, id) != null) {
            throw new ArchivoCSVException("Ya existe un registro con el ID '" + id + "''.");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            bw.write(lineaCSV);
            bw.newLine();

        } catch (IOException e) {
            throw new ArchivoCSVException("Hubo un error al intentar escribir en '" + rutaArchivo + "'.", e);
        }
    }

    /**
     * Regresa una línea completa del CSV que tenga el id ingresado.
     * Omite el encabezado y líneas en blanco.
     * 
     * @param rutaArchivo Ruta del archivo CSV donde se realizará la búsqueda.
     * @param id          Identificador numérico del registro a buscar.
     * @return La línea completa encontrada, o null si no está.
     * @throws ArchivoCSVException Si el archivo no existe o hay errores de lectura
     *                             y formato.
     */
    public static String buscarPorId(String rutaArchivo, int id) throws ArchivoCSVException {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            throw new ArchivoCSVException("No hay ningun archivo en la ruta ingresada: '" + rutaArchivo + "'.");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            int idActual = 0;

            // Leemos la primera línea que es el encabezado.
            String lineaLeida = br.readLine();

            while ((lineaLeida = br.readLine()) != null) {
                if (lineaLeida.trim().isEmpty()) {
                    continue;
                }

                String[] columnas = lineaLeida.split(",");
                idActual = Integer.parseInt(columnas[0].trim());

                if (idActual == id) {
                    return lineaLeida;
                }
            }

        } catch (IOException | NumberFormatException e) {
            throw new ArchivoCSVException("Hubo un error al leer y procesar el archivo " + rutaArchivo, e);
        }

        return null;
    }

    /**
     * Elimina un registro de acuerdo al id proporcionado.
     * 
     * @param rutaArchivo Ruta del archivo CSV donde se eliminará el registro.
     * @param id          Identificador numérico del registro a borrar.
     * @throws ArchivoCSVException Si el id no existe en el archivo o si hubo
     *                             problemas de escritura.
     */
    public static void removeRegistro(String rutaArchivo, int id) throws ArchivoCSVException {
        if (buscarPorId(rutaArchivo, id) == null) {
            throw new ArchivoCSVException(
                    "No hay ningun registro con el id '" + id + "' en el archivo: '" + rutaArchivo + "'.");
        }

        List<String> registros = getAll(rutaArchivo);
        String registroActual = "";
        int idActual = 0;

        // Bucle que para hasta que se haya eliminado el registro con el id deseado.
        for (int i = 0; i < registros.size(); i++) {
            registroActual = registros.get(i);
            idActual = Integer.parseInt(registroActual.trim().split(",")[0]);

            if (idActual == id) {
                registros.remove(i);
                break;
            }
        }

        // Rescribimos el archivo con la lista de resgistros correcta.
        setAll(rutaArchivo, getEncabezado(rutaArchivo), registros);
    }

    /**
     * Actualiza un registro existente reemplazándolo con la nueva línea ingresada.
     * 
     * @param rutaArchivo Ruta del archivo CSV donde se encuentra el registro.
     * @param id          Identificador numérico del registro a actualizar.
     * @param lineaCSV    Cadena con los nuevos datos formateados en CSV.
     * @throws ArchivoCSVException Si el id no existe o si hay un error al
     *                             sobrescribirlo.
     */
    public static void setRegistro(String rutaArchivo, int id, String lineaCSV) throws ArchivoCSVException {
        if (buscarPorId(rutaArchivo, id) == null) {
            throw new ArchivoCSVException(
                    "No hay ningun registro con el id: '" + id + "'. En el archivo: '" + rutaArchivo + "'.");
        }

        try {
            removeRegistro(rutaArchivo, id);
            addRegistro(rutaArchivo, id, lineaCSV);
        } catch (ArchivoCSVException e) {
            throw new ArchivoCSVException("Hubo un error al intentar sobrescribir el resgistro con el id: '" + id
                    + "'. En el archivo: '" + rutaArchivo + "'.");
        }
    }

    /**
     * Regresa todos los registros que se encuentren en el archivo ingresado.
     * Ignora el encabezado y líneas vacías.
     * 
     * @param rutaArchivo Ruta del archivo CSV a leer.
     * @return Lista de Strings con cada uno de los registros.
     * @throws ArchivoCSVException Si el archivo no existe o hay un error al leerlo.
     */
    public static List<String> getAll(String rutaArchivo) throws ArchivoCSVException {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            throw new ArchivoCSVException("No se encontro un archivo en la ruta: '" + rutaArchivo + "'.");
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
            throw new ArchivoCSVException("Error al cargar la lista completa desde " + rutaArchivo, e);
        }
    }

    /**
     * Sobrescribe un archivo entero a partir de una lista de Strings con formato
     * correcto.
     * 
     * @param rutaArchivo Ruta del archivo a sobrescribir.
     * @param lineas      Lista de cadenas que conformarán el nuevo contenido.
     * @throws ArchivoCSVException Si ocurre un error de escritura.
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
            throw new ArchivoCSVException("Hubo un error al intentar sobreescribir '" + rutaArchivo + "'.", e);
        }
    }

    /**
     * Regresa el encabezado de el archivo CSV ingresado.
     * 
     * @param rutaArchivo Ruta del archivo a sobrescribir.
     * @return Un String que contiene el encabezado del archivo.
     * @throws ArchivoCSVException Si el archivo no existe o hay un error al leerlo.
     */
    public static String getEncabezado(String rutaArchivo) throws ArchivoCSVException {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            throw new ArchivoCSVException("No se encontró el archivo: '" + rutaArchivo + "'.");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            return br.readLine();
        } catch (IOException e) {
            throw new ArchivoCSVException("Error al leer el encabezado de '" + rutaArchivo + "'.", e);
        }
    }
}

class Prueba1 {
    public static void main(String[] args) {
        try {
            //HandlerCSV.inicializarArchivo("./SRC/prueba.csv", "id,nombre,celular");
            //HandlerCSV.addRegistro("./SRC/prueba.csv", 2, "2,Leonardo,5548665420");
            List<String> registros = HandlerCSV.getAll("./SRC/prueba.csv");
            for (String registro : registros) {
                System.out.println(registro);
            }
            //HandlerCSV.removeRegistro("./SRC/prueba.csv", 2);

            System.out.println("Paso");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
