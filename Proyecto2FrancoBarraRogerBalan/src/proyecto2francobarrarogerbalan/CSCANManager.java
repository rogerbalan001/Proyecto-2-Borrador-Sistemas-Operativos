/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

import java.io.Serializable; // <--- IMPORTANTE

/**
 * Algoritmo C-SCAN (Circular SCAN).
 * La cabeza se mueve solo hacia el final (bloques altos).
 * Al llegar al extremo, salta inmediatamente al inicio (bloque 0) y sigue subiendo.
 * @author frank
 */
public class CSCANManager implements DiscoManager, Serializable { // <--- IMPLEMENTS

    private static final long serialVersionUID = 1L;
    
    private List<IORequests> requestList;

    public CSCANManager() {
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

        IORequests bestRequest = null;
        int minSeekTime = Integer.MAX_VALUE;

        // 1. Buscar la solicitud más cercana PERO QUE SEA MAYOR O IGUAL a la posición actual
        // (Es decir, "subiendo" hacia el final del disco)
        NodeList<IORequests> current = requestList.getHead();
        
        while (current != null) {
            IORequests req = current.getData();
            int seekTime = req.getTargetBlock() - currentHeadPosition;
            
            // Si seekTime es positivo, el bloque está adelante
            if (seekTime >= 0 && seekTime < minSeekTime) {
                minSeekTime = seekTime;
                bestRequest = req;
            }
            current = current.getNext();
        }

        // 2. Si no encontramos nada "adelante", significa que dimos la vuelta.
        // Buscamos la solicitud con el bloque MÁS BAJO de toda la lista (cercano al 0).
        if (bestRequest == null) {
            
            current = requestList.getHead();
            int lowestBlock = Integer.MAX_VALUE; 
            
            while (current != null) {
                IORequests req = current.getData();
                
                // Buscamos simplemente el número de bloque más pequeño
                if (req.getTargetBlock() < lowestBlock) {
                    lowestBlock = req.getTargetBlock();
                    bestRequest = req;
                }
                current = current.getNext();
            }
        }
        
        // 3. Eliminar y retornar
        if (bestRequest != null) {
            this.requestList.remove(bestRequest);
        }
        return bestRequest;
    }
}