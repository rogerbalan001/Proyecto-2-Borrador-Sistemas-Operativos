    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyecto2francobarrarogerbalan;

import javax.swing.UIManager;

/**
 * Punto de entrada del Simulador de Sistema Operativo.
 * Inicializa el Backend (FileManager), el Frontend (GUI) y el Kernel (HiloSimulador).
 */
public class Proyecto2FrancoBarraRogerBalan {

    public static void main(String[] args) {
        // 1. Estética: Intentar poner el estilo nativo del sistema operativo (Windows/Mac/Linux)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo cargar el LookAndFeel del sistema, usando default.");
        }

        // 2. Inicializar el sistema de archivos (Backend)
        // Esto crea el Disco, la Tabla de Procesos y el Directorio Raíz.
        FileManager fileManager = new FileManager();
        
        // (Opcional) Si tu FileManager no crea el root por defecto, hazlo aquí.
        // Pero vi en tu código que sí lo hace en el constructor, así que todo bien.

        // 3. Iniciar la Interfaz Gráfica y el Hilo del Kernel
        java.awt.EventQueue.invokeLater(() -> {
            
            // A. Crear y mostrar la ventana
            GUI gui = new GUI(fileManager);
            gui.setVisible(true);
            
            // B. ¡ARRANCAR EL MOTOR DEL SIMULADOR!
            // Esto inicia el hilo que procesa las solicitudes en segundo plano.
            HiloSimulador kernel = new HiloSimulador(fileManager, gui);
            kernel.start();
            
            System.out.println("Sistema Operativo Simulado iniciado correctamente.");
        });
    }
}