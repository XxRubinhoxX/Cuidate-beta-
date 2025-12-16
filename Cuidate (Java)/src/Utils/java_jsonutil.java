package utils;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;

/**
 * Utilidad para manejar operaciones JSON
 * 
 * NOTA: Este proyecto usa Gson para JSON.
 * Para compilar necesitas descargar gson-2.10.1.jar desde:
 * https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar
 * 
 * Compilar con:
 * javac -cp ".;gson-2.10.1.jar" -d bin src/**/*.java
 * 
 * Ejecutar con:
 * java -cp ".;bin;gson-2.10.1.jar" Main
 */
public class JsonUtil {
    private static final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .create();
    
    /**
     * Lee un archivo JSON y lo convierte en JsonObject
     */
    public static JsonObject leerJson(String rutaArchivo) {
        try {
            File archivo = new File(rutaArchivo);
            if (!archivo.exists()) {
                return new JsonObject();
            }
            
            String contenido = new String(Files.readAllBytes(Paths.get(rutaArchivo)));
            if (contenido.trim().isEmpty()) {
                return new JsonObject();
            }
            
            return gson.fromJson(contenido, JsonObject.class);
        } catch (Exception e) {
            System.err.println("Error al leer JSON: " + e.getMessage());
            return new JsonObject();
        }
    }
    
    /**
     * Guarda un JsonObject en un archivo
     */
    public static void guardarJson(String rutaArchivo, JsonObject datos) {
        try {
            // Crear directorio si no existe
            File archivo = new File(rutaArchivo);
            File directorio = archivo.getParentFile();
            if (directorio != null && !directorio.exists()) {
                directorio.mkdirs();
            }
            
            String json = gson.toJson(datos);
            Files.write(Paths.get(rutaArchivo), json.getBytes());
        } catch (Exception e) {
            System.err.println("Error al guardar JSON: " + e.getMessage());
        }
    }
    
    /**
     * Convierte un objeto a JsonElement
     */
    public static JsonElement toJsonElement(Object obj) {
        return gson.toJsonTree(obj);
    }
    
    /**
     * Convierte un JsonElement a un objeto de tipo específico
     */
    public static <T> T fromJsonElement(JsonElement element, Class<T> clase) {
        return gson.fromJson(element, clase);
    }
}
