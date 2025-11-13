/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

/**
 *
 * @author frank
 */
public class IORequests {
    
    private Process ownerProcess; // El proceso que hizo esta solicitud
    private TipoSolicitud type;
    
    // --- Información del Archivo ---
    private String filePath;      // Path completo, ej. "/root/docs/file.txt"
    private int sizeInBlocks;   // Usado principalmente para CREAR
    
    // --- ¡CRÍTICO para la planificación! ---
    private int targetBlock;    // El bloque de disco al que se debe acceder
                                // Para LEER/ELIMINAR: es el file.getFirstBlock()
                                // Para CREAR: puede ser el primer bloque libre encontrado
                                // Para ESCRIBIR: el bloque específico a modificar

    public IORequests(Process ownerProcess, TipoSolicitud type, String filePath, int sizeInBlocks, int targetBlock) {
        this.ownerProcess = ownerProcess;
        this.type = type;
        this.filePath = filePath;
        this.sizeInBlocks = sizeInBlocks;
        this.targetBlock = targetBlock;
        
        // Cuando se crea una solicitud, el proceso que la emite se bloquea
        this.ownerProcess.setState(EstadoProceso.BLOQUEADO);
    }

    // --- Getters ---

    public Process getOwnerProcess() {
        return ownerProcess;
    }

    public TipoSolicitud getType() {
        return type;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getSizeInBlocks() {
        return sizeInBlocks;
    }

    public int getTargetBlock() {
        return targetBlock;
    }
}
