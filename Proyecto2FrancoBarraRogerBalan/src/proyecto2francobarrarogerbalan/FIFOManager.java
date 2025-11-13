/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

/**
 *
 * @author frank
 */
public class FIFOManager implements DiscoManager {
    
    // ¡Aquí usamos tu clase Cola!
    private Cola<IORequests> requestQueue;

    public FIFOManager() {
        this.requestQueue = new Cola<>();
    }

    /**
     * Añade la solicitud al final de la cola.
     */
    @Override
    public void addRequest(IORequests request) {
        // Asumimos que tu Cola.java tiene un método 'enqueue' o 'add'
        this.requestQueue.enqueue(request); 
    }

    /**
     * Devuelve la solicitud del frente de la cola (la primera que entró),
     * sin importar la posición de la cabeza lectora.
     */
    @Override
    public IORequests getNextRequest(int currentHeadPosition) {
        if (!hasPendingRequests()) {
            return null;
        }
        // Asumimos que tu Cola.java tiene un método 'dequeue' o 'remove'
        return this.requestQueue.dequeue();
    }

    /**
     * Verifica si la cola tiene elementos.
     */
    @Override
    public boolean hasPendingRequests() {
        // Asumimos que tu Cola.java tiene un método 'isEmpty'
        return !this.requestQueue.isEmpty();
    }
    
    /*
    @Override
    public List<IORequests> getQueueSnapshot() {
        // TODO: Implementar si se necesita para la GUI.
        // Esto requeriría que 'Cola' sea iterable.
        return null;
    }
    */
}
