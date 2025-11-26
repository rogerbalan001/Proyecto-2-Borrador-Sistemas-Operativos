/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

import java.io.Serializable; // <--- IMPORTANTE

/**
 * Algoritmo SSTF (Shortest Seek Time First).
 * Selecciona la solicitud que requiere el menor movimiento de la cabeza lectora
 * desde la posición actual.
 * @author frank
 */
public class SSTFManager implements DiscoManager, Serializable { // <--- IMPLEMENTS
    
    private static final long serialVersionUID = 1L;

    private List<IORequests> requestList; // Usamos List para poder buscar en medio

    public SSTFManager() {
        this.requestList = new List<>();
    }

    @Override
    public void addRequest(IORequests request) {
        this.requestList.add(request);
    }

    @Override
    public boolean hasPendingRequests() {
        return !this.requestList.isEmpty();
    }

    @Override
    public IORequests getNextRequest(int currentHeadPosition) {
        if (!hasPendingRequests()) {
            return null;
        }

        // Iterador para recorrer tu lista personalizada
        NodeList<IORequests> current = requestList.getHead();
        
        IORequests bestRequest = null;
        int minSeekTime = Integer.MAX_VALUE;

        // 1. Barrer toda la lista buscando el que tenga menor distancia absoluta
        while (current != null) {
            IORequests req = current.getData();
            
            // Calculamos distancia absoluta (Math.abs)
            int seekTime = Math.abs(req.getTargetBlock() - currentHeadPosition);

            if (seekTime < minSeekTime) {
                minSeekTime = seekTime;
                bestRequest = req;
            }
            current = current.getNext();
        }

        // 2. Eliminar la solicitud seleccionada de la lista y retornarla
        if (bestRequest != null) {
            this.requestList.remove(bestRequest);
        }
        
        return bestRequest;
    }
}