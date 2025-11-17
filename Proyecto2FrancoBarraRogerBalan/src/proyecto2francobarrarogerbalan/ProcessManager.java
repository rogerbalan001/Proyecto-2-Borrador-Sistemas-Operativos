/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package proyecto2francobarrarogerbalan;

/**
 *
 * @author frank
 */
public class ProcessManager {
    
    // Usamos nuestra lista personalizada para la tabla de procesos
    private List<Process> processTable;

    public ProcessManager() {
        this.processTable = new List<>();
    }
    
    /**
     * Crea un nuevo proceso, lo registra en la tabla y lo retorna.
     * @param name Un nombre descriptivo para el proceso (ej. "Crear archivo.txt")
     * @return El Proceso recién creado.
     */
    public Process createProcess(String name) {
        Process newProcess = new Process(name);
        this.processTable.add(newProcess);
        return newProcess;
    }
    
    /**
     * Marca un proceso como TERMINADO.
     * (Opcional: podrías removerlo de la lista, pero es bueno mantenerlo
     * para que la GUI lo muestre como "TERMINADO").
     * @param process El proceso que ha finalizado.
     */
    public void terminateProcess(Process process) {
        if (process != null) {
            process.setState(EstadoProceso.TERMINADO);
            // Si List.java tuviera un método remove(), podríamos hacer:
            // this.processTable.remove(process);
        }
    }
    
    /**
     * Cambia el estado de un proceso (ej. a EJECUTANDO).
     * @param process El proceso a modificar.
     * @param newState El nuevo estado.
     */
    public void setProcessState(Process process, EstadoProceso newState) {
        if (process != null) {
            process.setState(newState);
        }
    }
    
    /**
     * Devuelve la lista de todos los procesos para ser mostrada en la GUI.
     * @return La tabla de procesos.
     */
    public List<Process> getProcessTable() {
        return processTable;
    }
    // Agrega esto dentro de la clase ProcessManager.java

public List<Process> getProcessList() {
    return this.processTable;
}
}