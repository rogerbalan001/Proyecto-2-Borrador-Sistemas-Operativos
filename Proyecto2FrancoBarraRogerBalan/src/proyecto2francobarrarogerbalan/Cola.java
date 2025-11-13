/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

/**
 *
 * @author frank
 */
public class Cola<T> {
    
    private NodeList<T> front; // El frente de la cola (por donde se saca)
    private NodeList<T> rear;  // El final de la cola (por donde se mete)
    private int size;

    public Cola() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    /**
     * Añade un elemento al final de la cola (encolar).
     * @param data El dato a encolar.
     */
    public void enqueue(T data) {
        NodeList<T> newNode = new NodeList<>(data);
        
        if (isEmpty()) {
            // Si está vacía, el nuevo nodo es tanto el frente como el final
            front = newNode;
            rear = newNode;
        } else {
            // Si no, se añade después del final actual
            rear.setNext(newNode);
            rear = newNode; // El nuevo nodo es ahora el final
        }
        size++;
    }

    /**
     * Saca y retorna el elemento del frente de la cola (desencolar).
     * @return El dato del frente, o null si la cola está vacía.
     */
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }

        // Obtener el dato del frente
        T data = front.getData();
        
        // Mover el puntero 'front' al siguiente
        front = front.getNext();

        // Si 'front' se vuelve null, la cola quedó vacía,
        // así que 'rear' también debe ser null.
        if (front == null) {
            rear = null;
        }
        
        size--;
        return data;
    }

    /**
     * Devuelve el elemento del frente sin sacarlo de la cola.
     * @return El dato del frente, o null si la cola está vacía.
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return front.getData();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}