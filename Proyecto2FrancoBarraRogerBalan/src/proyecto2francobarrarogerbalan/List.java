/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

import java.io.Serializable;

/**
 * Lista Enlazada Genérica Personalizada.
 * Reemplaza a java.util.LinkedList o ArrayList.
 * * CAMBIOS REALIZADOS:
 * 1. Implementa Serializable (Vital para guardar el estado).
 * 2. Agregado método remove() (Vital para el HiloSimulador).
 * * @author frank
 * @param <T> El tipo de dato que guardará la lista.
 */
public class List<T> implements Serializable {
    
    // Identificador de versión para serialización (recomendado)
    private static final long serialVersionUID = 1L;
    
    private NodeList<T> head; // El primer nodo de la lista
    private int size;

    public List() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Agrega un elemento al final de la lista.
     * @param data El dato a guardar.
     */
    public void add(T data) {
        NodeList<T> newNode = new NodeList<>(data);
        if (isEmpty()) {
            head = newNode;
        } else {
            NodeList<T> current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
        size++;
    }

    /**
     * Elimina la primera ocurrencia del dato especificado.
     * --- ESTE ES EL MÉTODO QUE TE FALTABA ---
     * @param data El objeto a eliminar.
     * @return true si se eliminó, false si no se encontró.
     */
    public boolean remove(T data) {
        if (isEmpty()) {
            return false;
        }

        // Caso 1: El nodo a eliminar es la cabeza (head)
        if (head.getData().equals(data)) {
            head = head.getNext();
            size--;
            return true;
        }

        // Caso 2: Buscar en el resto de la lista
        NodeList<T> current = head;
        NodeList<T> prev = null;

        while (current != null && !current.getData().equals(data)) {
            prev = current;
            current = current.getNext();
        }

        // Si lo encontramos (current no es null)
        if (current != null) {
            prev.setNext(current.getNext());
            size--;
            return true;
        }

        return false; // No estaba en la lista
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return size;
    }
    
    /**
     * Permite recorrer la lista desde fuera.
     * @return El primer nodo de la lista.
     */
    public NodeList<T> getHead() {
        return head;
    }
}