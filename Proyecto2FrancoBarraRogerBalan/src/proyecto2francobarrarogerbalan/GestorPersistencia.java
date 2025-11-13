/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
/**
 *
 * @author frank
 */
public class GestorPersistencia {
    
    // Hacemos los métodos estáticos para no necesitar instanciar el gestor.

    /**
     * Guarda el estado actual del FileManager en un archivo binario.
     * @param fileManager El objeto FileManager que contiene todo el estado.
     * @param filePath La ruta del archivo donde se guardará (ej. "estado.sim")
     * @return true si se guardó con éxito, false si falló.
     */
    public static boolean guardarEstado(FileManager fileManager, String filePath) {
        
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            
            oos.writeObject(fileManager); // ¡La magia ocurre aquí!
            System.out.println("Estado del simulador guardado en: " + filePath);
            return true;
            
        } catch (IOException e) {
            System.err.println("Error al guardar el estado: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Carga un estado guardado desde un archivo y devuelve un nuevo FileManager.
     * @param filePath La ruta del archivo de donde se cargará (ej. "estado.sim")
     * @return Un objeto FileManager restaurado, o null si falló.
     */
    public static FileManager cargarEstado(String filePath) {
        
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            
            FileManager fileManager = (FileManager) ois.readObject(); // ¡Magia!
            System.out.println("Estado del simulador cargado desde: " + filePath);
            return fileManager;
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar el estado: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
