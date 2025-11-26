/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

import java.io.Serializable;

/**
 *
 * @author frank
 */
public class IORequests implements Serializable {
    
    // Identificador de versión para la serialización (opcional pero recomendado)
    private static final long serialVersionUID = 1L;
    
    private Process ownerProcess; // El proceso que hizo esta solicitud
    private TipoSolicitud type;   // CREAR, LEER, ESCRIBIR, ELIMINAR
    
    // --- Información del Archivo ---
    private String filePath;      // Path completo, ej. "/root/docs/file.txt"
    private int sizeInBlocks;     // Usado principalmente para CREAR
    
    // --- CRÍTICO para la planificación ---
    private int targetBlock;      // El bloque de disco físico al que se debe acceder
    
    // --- NUEVO: Puente entre GUI y Simulador ---
    // Guardamos aquí el objeto NodeFile o NodeDirectory que queremos manipular.
    // Esto permite que el HiloSimulador tenga acceso al objeto lógico
    // cuando llegue el momento de procesar la solicitud.
    private Node archivoPendiente; 

    /**
     * Constructor principal de la solicitud.
     * @param ownerProcess El proceso que solicita la E/S.
     * @param type El tipo de operación.
     * @param filePath La ruta del archivo.
     * @param sizeInBlocks El tamaño (solo relevante para CREAR).
     * @param targetBlock El bloque objetivo para el algoritmo de disco.
     */
    public IORequests(Process ownerProcess, TipoSolicitud type, String filePath, int sizeInBlocks, int targetBlock) {
        this.ownerProcess = ownerProcess;
        this.type = type;
        this.filePath = filePath;
        this.sizeInBlocks = sizeInBlocks;
        this.targetBlock = targetBlock;
        this.archivoPendiente = null; // Se setea manualmente después si es necesario
        
        // SIMULACIÓN: Cuando un proceso hace una solicitud de E/S, 
        // el SO lo bloquea inmediatamente hasta que el disco termine.
        if (this.ownerProcess != null) {
            this.ownerProcess.setState(EstadoProceso.BLOQUEADO);
        }
    }

    // --- Getters y Setters ---

    public Process getOwnerProcess() {
        return ownerProcess;
    }

    public void setOwnerProcess(Process ownerProcess) {
        this.ownerProcess = ownerProcess;
    }

    public TipoSolicitud getType() {
        return type;
    }

    public void setType(TipoSolicitud type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getSizeInBlocks() {
        return sizeInBlocks;
    }

    public void setSizeInBlocks(int sizeInBlocks) {
        this.sizeInBlocks = sizeInBlocks;
    }

    public int getTargetBlock() {
        return targetBlock;
    }

    public void setTargetBlock(int targetBlock) {
        this.targetBlock = targetBlock;
    }

    public Node getArchivoPendiente() {
        return archivoPendiente;
    }

    /**
     * Guarda la referencia al nodo (Archivo/Carpeta) que se va a procesar.
     * Fundamental para que el HiloSimulador pueda terminar la operación.
     */
    public void setArchivoPendiente(Node archivoPendiente) {
        this.archivoPendiente = archivoPendiente;
    }
    
    @Override
    public String toString() {
        return type + " -> " + filePath + " [Bloque: " + targetBlock + "]";
    }
    
}