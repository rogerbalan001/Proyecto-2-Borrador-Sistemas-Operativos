/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

import java.io.Serializable; // <--- IMPORTANTE

/**
 * Algoritmo FIFO (First In, First Out).
 * Atiende las solicitudes en el orden exacto en que llegaron.
 * @author frank
 */
public class FIFOManager implements DiscoManager, Serializable { // <--- IMPLEMENTS
    
    private static final long serialVersionUID = 1L; // Versión para serialización
    
    // Usamos tu clase Cola, que ya hicimos Serializable en pasos anteriores
    private Cola<IORequests> requestQueue;

    public FIFOManager() {
        this.requestQueue = new Cola<>();
    }

    /**
     * Añade la solicitud al final de la cola.
     */
    @Override
    public void addRequest(IORequests request) {
        this.requestQueue.enqueue(request); 
    }

    /**
     * Devuelve la solicitud del frente de la cola (la primera que entró).
     * FIFO ignora la posición de la cabeza lectora.
     */
    @Override
    public IORequests getNextRequest(int currentHeadPosition) {
        if (!hasPendingRequests()) {
            return null;
        }
        // Saca el primero de la fila
        return this.requestQueue.dequeue();
    }

    @Override
    public boolean hasPendingRequests() {
        return !this.requestQueue.isEmpty();
    }
}