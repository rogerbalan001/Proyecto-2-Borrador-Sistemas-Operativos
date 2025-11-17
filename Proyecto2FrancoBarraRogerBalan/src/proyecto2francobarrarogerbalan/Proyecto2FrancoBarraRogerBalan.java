    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyecto2francobarrarogerbalan;

import javax.swing.UIManager;

public class Proyecto2FrancoBarraRogerBalan {

    public static void main(String[] args) {
        // Intentar poner el estilo del sistema operativo para que se vea mejor
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Inicializar el sistema de archivos (Backend)
        FileManager fileManager = new FileManager();
        
        // Crear carpeta root manualmente si no existe en el constructor de FileManager
        // (Asumo que FileManager crea el root internamente, si no, descomenta abajo)
        // if (fileManager.getRoot() == null) { /* lógica init root */ }

        // Iniciar la GUI (Frontend)
        java.awt.EventQueue.invokeLater(() -> {
            GUI gui = new GUI(fileManager);
            gui.setVisible(true);
        });
    }
}
