/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

/**
 *
 * @author frank
 */
public class List<T> {
    
    private NodeList<T> head; // El primer elemento de la lista
    private int size;

    public List() {
        this.head = null;
        this.size = 0;
    }

    // Método para agregar al final (el más común)
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

    // Método para eliminar (requerirá más lógica, ej. por índice o por objeto)
    // public boolean remove(T data) { ... }
    
    // Método para obtener por índice
    // public T get(int index) { ... }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return size;
    }
    
    // Podrías necesitar un método para obtener el 'head' 
    // y así poder iterar la lista desde fuera
    public NodeList<T> getHead() {
        return head;
    }
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

    // Caso 2: El nodo a eliminar está en medio o al final
    NodeList<T> current = head;
    NodeList<T> prev = null;

    while (current != null && !current.getData().equals(data)) {
        prev = current;
        current = current.getNext();
    }

    // Si current es null, el dato no se encontró
    if (current == null) {
        return false;
    }

    // Si se encontró, "saltamos" el nodo actual
    prev.setNext(current.getNext());
    size--;
    return true;
}
}
