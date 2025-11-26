/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

import java.io.Serializable; // <--- IMPORTANTE

/**
 * Algoritmo SCAN (Elevador).
 * La cabeza se mueve en una dirección atendiendo todo a su paso, 
 * rebota al final y atiende en sentido contrario.
 * @author frank
 */
public class SCANManager implements DiscoManager, Serializable { // <--- IMPLEMENTS
    
    private static final long serialVersionUID = 1L;

    private List<IORequests> requestList;
    private boolean movingTowardsEnd; // true = subiendo (0->255), false = bajando

    public SCANManager() {
        this.requestList = new List<>();
        this.movingTowardsEnd = true; // Empezamos subiendo por defecto
    }

    @Override
    public void addRequest(IORequests request) {
        this.requestList.add(request);
    }

    @Override
    public boolean hasPendingRequests() {
        return !this.requestList.isEmpty();
    }

    /**
     * Lógica Matemática del SCAN.
     */
    @Override
    public IORequests getNextRequest(int currentHeadPosition) {
        if (!hasPendingRequests()) {
            return null;
        }

        IORequests bestRequest = null;
        int minSeekTime = Integer.MAX_VALUE;

        // 1. Recorrer la lista buscando la mejor opción en la dirección actual
        NodeList<IORequests> current = requestList.getHead();
        
        while (current != null) {
            IORequests req = current.getData();
            int distance = req.getTargetBlock() - currentHeadPosition;

            // Si vamos subiendo (movingTowardsEnd es true)
            if (movingTowardsEnd) {
                // Solo nos interesan los que están POR DELANTE (distance >= 0)
                if (distance >= 0 && distance < minSeekTime) {
                    minSeekTime = distance;
                    bestRequest = req;
                }
            } 
            // Si vamos bajando (movingTowardsEnd es false)
            else {
                // Solo nos interesan los que están POR DETRÁS (distance <= 0)
                // Usamos Math.abs porque la distancia será negativa
                if (distance <= 0 && Math.abs(distance) < minSeekTime) {
                    minSeekTime = Math.abs(distance);
                    bestRequest = req;
                }
            }
            current = current.getNext();
        }

        // 2. Si no encontramos nada en la dirección actual... ¡REBOTAMOS!
        if (bestRequest == null) {
            movingTowardsEnd = !movingTowardsEnd; // Cambiamos dirección
            
            // Y volvemos a buscar (Recursividad simple o repetir lógica)
            // Para no complicar, repetimos la búsqueda con la nueva dirección:
            current = requestList.getHead();
            minSeekTime = Integer.MAX_VALUE;
            
            while (current != null) {
                IORequests req = current.getData();
                int distance = req.getTargetBlock() - currentHeadPosition;

                if (movingTowardsEnd) {
                    if (distance >= 0 && distance < minSeekTime) {
                        minSeekTime = distance;
                        bestRequest = req;
                    }
                } else {
                    if (distance <= 0 && Math.abs(distance) < minSeekTime) {
                        minSeekTime = Math.abs(distance);
                        bestRequest = req;
                    }
                }
                current = current.getNext();
            }
        }
        
        // 3. Eliminar de la lista y retornar
        if (bestRequest != null) {
            this.requestList.remove(bestRequest);
        }
        return bestRequest;
    }
}