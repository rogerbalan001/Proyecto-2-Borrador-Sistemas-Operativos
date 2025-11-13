/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package proyecto2francobarrarogerbalan;

/**
 *
 * @author frank
 */
public interface DiscoManager {
    
    /**
     * Añade una nueva solicitud de E/S a la cola de espera del planificador.
     * @param request La solicitud generada por un proceso.
     */
    void addRequest(IORequests request);
    
    /**
     * Decide cuál es la siguiente solicitud a procesar, la extrae de la cola y la retorna.
     * Aquí es donde vive la "lógica" de cada algoritmo.
     * * @param currentHeadPosition La posición actual de la cabeza lectora del disco.
     * (FIFO la ignorará, pero SSTF y SCAN la usarán).
     * @return La siguiente solicitud de E/S a ser ejecutada.
     */
    IORequests getNextRequest(int currentHeadPosition);
    
    /**
     * Verifica si hay solicitudes pendientes en la cola.
     * @return true si la cola no está vacía, false en caso contrario.
     */
    boolean hasPendingRequests();
    
    /**
     * (Opcional, pero útil para la GUI)
     * Devuelve una vista de la cola de solicitudes pendientes.
     * NOTA: Esto podría requerir modificar tu 'Cola.java' para que sea iterable
     * o tenga un método 'getContenido()'.
     * * @return Una lista (tu List.java) de las solicitudes en espera.
     */
    // List<IORequests> getQueueSnapshot();
    
}
