/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

import java.io.Serializable; // <--- AGREGAR IMPORT

public class FileManager implements Serializable { // <--- AGREGAR IMPLEMENTS
    
    // Identificador de versión (opcional, pero buena práctica)
    private static final long serialVersionUID = 1L;

    // --- Componentes del Sistema ---
    private DiscoDuro disco;
    private GestorAsignacion gestorAsignacion;
    private ProcessManager processManager;
    private DiscoManager planificadorActual; // El Strategy (FIFO, SSTF, SCAN, etc.)
    
    // --- Estado del Sistema ---
    private NodeDirectory root; // El directorio raíz
    private int currentHeadPosition; // Posición de la cabeza lectora

    public FileManager() {
        // 1. Inicializar el "Hardware"
        this.disco = new DiscoDuro();
        this.currentHeadPosition = 0; // La cabeza lectora empieza en el bloque 0

        // 2. Inicializar los Gestores
        this.gestorAsignacion = new GestorAsignacion(this.disco);
        this.processManager = new ProcessManager();
        
        // 3. Establecer la política de planificación por defecto (FIFO)
        this.planificadorActual = new FIFOManager();
        
        // 4. Inicializar el Sistema de Archivos con el directorio Root
        this.root = new NodeDirectory("root", null);
    }

    // --- Métodos de Lógica (Helpers) ---

    /**
     * Busca un nodo dado un path (ej. "/root/carpeta/archivo")
     */
    private Node findNode(String path) {
        if (path.equals("/")) {
            return root;
        }
        // Eliminar el primer slash si existe y dividir
        if (path.startsWith("/")) path = path.substring(1);
        
        String[] parts = path.split("/"); 
        Node currentNode = root;

        for (String part : parts) {
            if (currentNode instanceof NodeDirectory) {
                currentNode = ((NodeDirectory) currentNode).findChild(part);
                if (currentNode == null) {
                    return null; // No encontrado
                }
            } else {
                return null; // Se intentó buscar dentro de un archivo (no es directorio)
            }
        }
        return currentNode;
    }
    
    // --- Getters para conectar con la GUI ---
    
    public NodeDirectory getRoot() {
        return root;
    }
    
    public DiscoDuro getDisco() {
        return disco;
    }
    
    public ProcessManager getProcessManager() {
        return processManager;
    }

    public GestorAsignacion getGestorAsignacion() {
        return gestorAsignacion;
    }

    public DiscoManager getPlanificador() {
        return planificadorActual;
    }
    
    /**
     * NUEVO: Este método permite a la GUI obtener la lista de procesos
     * para pintarla en la tabla.
     */
    public List<Process> getTablaProcesos() {
        // Llama al método del ProcessManager (ver nota abajo si da error)
        return processManager.getProcessList(); 
    }

    public int getCurrentHeadPosition() {
        return currentHeadPosition;
    }

    public void setCurrentHeadPosition(int currentHeadPosition) {
        this.currentHeadPosition = currentHeadPosition;
    }
}