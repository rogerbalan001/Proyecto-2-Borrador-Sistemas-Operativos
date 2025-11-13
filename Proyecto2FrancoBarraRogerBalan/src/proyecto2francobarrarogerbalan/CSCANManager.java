/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

/**
 *
 * @author frank
 */
public class CSCANManager implements DiscoManager {

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

        // 1. Buscar la solicitud más cercana "hacia el final" (targetBlock >= head)
        NodeList<IORequests> current = requestList.getHead();
        while (current != null) {
            IORequests req = current.getData();
            int seekTime = req.getTargetBlock() - currentHeadPosition;
            
            if (seekTime >= 0 && seekTime < minSeekTime) {
                minSeekTime = seekTime;
                bestRequest = req;
            }
            current = current.getNext();
        }

        // 2. Si no se encontró nada "hacia el final"...
        if (bestRequest == null) {
            // ...significa que debemos "saltar" al inicio (bloque 0)
            // y buscar la solicitud más cercana al inicio (el targetBlock más bajo).
            
            current = requestList.getHead();
            // Esta vez no buscamos "seekTime", sino el bloque más bajo (más cercano a 0)
            int lowestBlock = Integer.MAX_VALUE; 
            
            while (current != null) {
                IORequests req = current.getData();
                if (req.getTargetBlock() < lowestBlock) {
                    lowestBlock = req.getTargetBlock();
                    bestRequest = req;
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