/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

/**
 *
 * @author frank
 */
public class SCANManager implements DiscoManager {

    private List<IORequests> requestList;
    private boolean movingTowardsEnd; // true = moviéndose a bloques altos (ej. 255)
                                      // false = moviéndose a bloque 0

    public SCANManager() {
        this.requestList = new List<>();
        this.movingTowardsEnd = true; // Empezamos moviéndonos hacia el final
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

        // 1. Buscar la solicitud más cercana EN LA DIRECCIÓN ACTUAL
        NodeList<IORequests> current = requestList.getHead();
        while (current != null) {
            IORequests req = current.getData();
            int seekTime = req.getTargetBlock() - currentHeadPosition; // Distancia (con signo)

            if (movingTowardsEnd) {
                // Buscamos solicitudes ADELANTE (seekTime >= 0)
                if (seekTime >= 0 && seekTime < minSeekTime) {
                    minSeekTime = seekTime;
                    bestRequest = req;
                }
            } else {
                // Buscamos solicitudes ATRÁS (seekTime <= 0)
                // Usamos Math.abs para encontrar la más cercana
                if (seekTime <= 0 && Math.abs(seekTime) < minSeekTime) {
                    minSeekTime = Math.abs(seekTime);
                    bestRequest = req;
                }
            }
            current = current.getNext();
        }

        // 2. Si no se encontró nada en la dirección actual...
        if (bestRequest == null) {
            // ...revertimos la dirección y volvemos a buscar
            movingTowardsEnd = !movingTowardsEnd;
            
            // Volvemos a buscar con la dirección invertida
            // (Este código es casi igual al de arriba)
            current = requestList.getHead();
            minSeekTime = Integer.MAX_VALUE; // Resetear
            
            while (current != null) {
                IORequests req = current.getData();
                int seekTime = req.getTargetBlock() - currentHeadPosition;

                if (movingTowardsEnd) {
                    if (seekTime >= 0 && seekTime < minSeekTime) {
                        minSeekTime = seekTime;
                        bestRequest = req;
                    }
                } else {
                    if (seekTime <= 0 && Math.abs(seekTime) < minSeekTime) {
                        minSeekTime = Math.abs(seekTime);
                        bestRequest = req;
                    }
                }
                current = current.getNext();
            }
        }
        
        // 3. Eliminar y retornar la solicitud encontrada
        if (bestRequest != null) {
            this.requestList.remove(bestRequest);
        }
        return bestRequest;
    }
}
