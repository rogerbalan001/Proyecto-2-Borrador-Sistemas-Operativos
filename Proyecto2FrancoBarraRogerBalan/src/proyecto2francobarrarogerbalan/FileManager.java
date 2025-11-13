/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

/**
 *
 * @author frank
 */
public class FileManager {

    // --- Componentes del Sistema ---
    private DiscoDuro disco;
    private GestorAsignacion gestorAsignacion;
    private ProcessManager processManager;
    private DiscoManager planificadorActual; // ¡El Strategy!
    // private GestorPersistencia gestorPersistencia;
    // private GestorSesion gestorSesion;
    
    // --- Estado del Sistema ---
    private NodeDirectory root; // El directorio raíz
    private int currentHeadPosition;

    public FileManager() {
        // 1. Inicializar el "Hardware"
        this.disco = new DiscoDuro();
        this.currentHeadPosition = 0; // La cabeza lectora empieza en el bloque 0

        // 2. Inicializar los Gestores
        this.gestorAsignacion = new GestorAsignacion(this.disco);
        this.processManager = new ProcessManager();
        // this.gestorPersistencia = new GestorPersistencia();
        // this.gestorSesion = new GestorSesion();
        
        // 3. Establecer la política de planificación por defecto
        this.planificadorActual = new FIFOManager();
        
        // 4. Crear el directorio raíz
        // El raíz no tiene padre (null)
        this.root = new NodeDirectory("/", null); 
        // Asignamos su propio bloque al directorio raíz (bloque 0)
        // (Esto es una simplificación, pero necesaria)
        this.disco.allocateBlock(0, 0); // Ocupa el bloque 0 y es EOF
    }
    
    // --- API Pública (para la GUI) ---

    /**
     * Método principal para crear un archivo.
     * Crea el Proceso, genera la Solicitud de E/S y la encola.
     * @param pathCompleto Ej. "/docs/miArchivo.txt"
     * @param sizeInBlocks El tamaño deseado
     * @return true si la solicitud fue creada, false si hay un error (ej. path inválido)
     */
    public boolean crearArchivo(String pathCompleto, int sizeInBlocks) {
        // (Aquí iría la lógica de GestorSesion para permisos)

        // 1. Validar el path y encontrar el padre
        String nombreArchivo = getNombreDePath(pathCompleto);
        String pathPadre = getPadreDePath(pathCompleto);
        NodeDirectory padre = (NodeDirectory) findNode(pathPadre);

        if (padre == null || !(padre instanceof NodeDirectory)) {
            System.err.println("Error: Directorio padre no encontrado en: " + pathPadre);
            return false;
        }
        
        if (padre.findChild(nombreArchivo) != null) {
             System.err.println("Error: Ya existe un archivo/directorio con ese nombre.");
            return false;
        }

        // 2. Crear el Proceso
        Process p = processManager.createProcess("Crear " + nombreArchivo);

        // 3. Determinar el "target block"
        // Para CREAR, la solicitud es ir al primer bloque libre disponible.
        int targetBlock = disco.findFreeBlock();
        if (targetBlock == -1) {
            System.err.println("Error: Disco lleno. No se puede crear el proceso.");
            processManager.terminateProcess(p); // Se cancela el proceso
            return false;
        }
        
        // 4. Crear la Solicitud de E/S
        IORequests request = new IORequests(p, TipoSolicitud.CREAR, pathCompleto, sizeInBlocks, targetBlock);
        
        // 5. Encolar la solicitud
        this.planificadorActual.addRequest(request);
        
        return true;
    }
    
    /**
     * Método principal para eliminar un archivo.
     * @param pathCompleto Ej. "/docs/miArchivo.txt"
     * @return true si la solicitud fue creada, false si no se encontró el archivo.
     */
    public boolean eliminarArchivo(String pathCompleto) {
        // 1. Encontrar el nodo a eliminar
        Node nodo = findNode(pathCompleto);
        
        if (nodo == null || !(nodo instanceof NodeFile)) {
            System.err.println("Error: Archivo no encontrado en " + pathCompleto);
            return false;
        }
        
        NodeFile archivo = (NodeFile) nodo;

        // 2. Crear el Proceso
        Process p = processManager.createProcess("Eliminar " + archivo.getName());

        // 3. Determinar el "target block"
        // Para ELIMINAR, la solicitud es ir al *primer bloque* del archivo.
        int targetBlock = archivo.getFirstBlock();
        if(targetBlock == -1) targetBlock = 0; // Archivo existe pero está vacío
        
        // 4. Crear la Solicitud de E/S
        IORequests request = new IORequests(p, TipoSolicitud.ELIMINAR, pathCompleto, 0, targetBlock);
        
        // 5. Encolar la solicitud
        this.planificadorActual.addRequest(request);
        return true;
    }
    
    /**
     * Cambia la estrategia de planificación de disco en caliente.
     * @param politica El nombre de la política ("FIFO", "SSTF", "SCAN", "CSCAN")
     */
    public void setPlanificador(String politica) {
        switch (politica) {
            case "FIFO":
                this.planificadorActual = new FIFOManager();
                break;
            case "SSTF":
                this.planificadorActual = new SSTFManager();
                break;
            case "SCAN":
                this.planificadorActual = new SCANManager();
                break;
            case "CSCAN":
                this.planificadorActual = new CSCANManager();
                break;
            default:
                this.planificadorActual = new FIFOManager(); // Default
        }
        // (Opcional: transferir las solicitudes de la cola vieja a la nueva)
    }

    /**
     * El "motor" del simulador. La GUI llamaría a este método
     * repetidamente (ej. con un Timer) para procesar una solicitud.
     */
    public void procesarSiguienteSolicitud() {
        if (!planificadorActual.hasPendingRequests()) {
            return; // No hay nada que hacer
        }

        // 1. Pedir al planificador la SIGUIENTE solicitud
        IORequests req = planificadorActual.getNextRequest(this.currentHeadPosition);
        
        if (req == null) return;
        
        // 2. "Mover" la cabeza lectora
        System.out.println("Moviendo cabeza de " + this.currentHeadPosition + " a " + req.getTargetBlock());
        this.currentHeadPosition = req.getTargetBlock();
        
        // 3. Actualizar estado del proceso
        processManager.setProcessState(req.getOwnerProcess(), EstadoProceso.EJECUTANDO);

        // 4. Ejecutar la acción
        switch (req.getType()) {
            
            case CREAR:
                ejecutarCreacion(req);
                break;
                
            case ELIMINAR:
                ejecutarEliminacion(req);
                break;
                
            // case LEER: ...
            // case ESCRIBIR: ...
        }
        
        // 5. Terminar el proceso
        processManager.terminateProcess(req.getOwnerProcess());
        System.out.println("Proceso " + req.getOwnerProcess().getPid() + " terminado.");
    }
    
    // --- Métodos de Ejecución (Privados) ---

    private void ejecutarCreacion(IORequests req) {
        // Re-localizamos al padre (necesario)
        String nombreArchivo = getNombreDePath(req.getFilePath());
        String pathPadre = getPadreDePath(req.getFilePath());
        NodeDirectory padre = (NodeDirectory) findNode(pathPadre);

        if (padre == null) {
            System.err.println("Error CRÍTICO: El padre desapareció durante la creación.");
            return;
        }

        // 1. Crear el Nodo lógico
        NodeFile newFile = new NodeFile(nombreArchivo, padre, req.getSizeInBlocks());
        
        // 2. Asignar los bloques físicos
        boolean exito = gestorAsignacion.allocateFile(newFile);
        
        if (exito) {
            // 3. Añadir el nodo al árbol
            padre.addChild(newFile);
            System.out.println("Archivo creado: " + newFile.getPath());
        } else {
            System.err.println("Error de ejecución: No se pudo asignar espacio para " + newFile.getName());
            // El proceso se marcará como terminado (fallido)
        }
    }
    
    private void ejecutarEliminacion(IORequests req) {
        Node nodo = findNode(req.getFilePath());
        if (nodo == null || !(nodo instanceof NodeFile)) {
             System.err.println("Error CRÍTICO: El archivo desapareció durante la eliminación.");
            return;
        }
        
        NodeFile archivo = (NodeFile) nodo;
        NodeDirectory padre = archivo.getParent();
        
        // 1. Liberar los bloques físicos
        gestorAsignacion.deallocateFile(archivo);
        
        // 2. Quitar el nodo del árbol (requiere List.remove())
        // padre.removeChild(archivo.getName()); // <-- Necesitarás implementar esto
        System.out.println("Archivo eliminado: " + archivo.getPath());
    }

    // --- Métodos de Utilidad (Privados) ---
    
    /**
     * Navega el árbol de nodos usando un path.
     * @param path Ej. "/docs/miArchivo.txt"
     * @return El Nodo (File o Directory) o null si no se encuentra.
     */
    private Node findNode(String path) {
        if (path.equals("/")) {
            return root;
        }

        String[] parts = path.substring(1).split("/"); // Quita el 1er "/" y divide
        Node currentNode = root;

        for (String part : parts) {
            if (currentNode instanceof NodeDirectory) {
                currentNode = ((NodeDirectory) currentNode).findChild(part);
                if (currentNode == null) {
                    return null; // No encontrado
                }
            } else {
                return null; // Se intentó buscar dentro de un archivo
            }
        }
        return currentNode;
    }
    
    private String getNombreDePath(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
    }

    private String getPadreDePath(String path) {
        String parentPath = path.substring(0, path.lastIndexOf('/'));
        if (parentPath.isEmpty()) {
            return "/"; // El padre es el root
        }
        return parentPath;
    }

    // --- Getters (para que la GUI pueda "ver" el estado) ---
    
    public NodeDirectory getRoot() {
        return root;
    }
    
    public DiscoDuro getDisco() {
        return disco;
    }
    
    public List<Process> getTablaProcesos() {
        return processManager.getProcessTable();
    }
    
    public int getCurrentHeadPosition() {
        return currentHeadPosition;
    }
}
