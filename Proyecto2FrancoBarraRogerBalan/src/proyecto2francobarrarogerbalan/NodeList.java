/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

import java.io.Serializable; // <--- AGREGAR IMPORT

public class NodeList<T> implements Serializable { // <--- AGREGAR IMPLEMENTS
    
    private static final long serialVersionUID = 1L;
    
    private T data; // El dato que guarda (ej. un NodeFile, un Process, etc.)
    private NodeList<T> next; // El puntero al siguiente nodo

    public NodeList(T data) {
        this.data = data;
        this.next = null;
    }

    // --- Getters y Setters ---
    
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public NodeList<T> getNext() {
        return next;
    }

    public void setNext(NodeList<T> next) {
        this.next = next;
    }
}
