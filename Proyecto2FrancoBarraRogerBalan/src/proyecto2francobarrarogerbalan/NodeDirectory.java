/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

/**
 *
 * @author frank
 */
public class NodeDirectory extends Node {
    
    // ¡Aquí usamos nuestra lista personalizada!
    private List<Node> children; 

    public NodeDirectory(String name, NodeDirectory parent) {
        super(name, parent);
        this.children = new List<>();
    }

    // Método para agregar un hijo (archivo o subdirectorio)
    public void addChild(Node child) {
        child.setParent(this); // Importante: establecer el padre
        this.children.add(child);
    }
    
    // Método para buscar un hijo por nombre
    public Node findChild(String name) {
        NodeList<Node> current = children.getHead();
        while (current != null) {
            if (current.getData().getName().equals(name)) {
                return current.getData();
            }
            current = current.getNext();
        }
        return null; // No encontrado
    }
    
    // Método para eliminar un hijo (necesitarás implementarlo en tu List.java)
    // public boolean removeChild(String name) { ... }

    public List<Node> getChildren() {
        return children;
    }

    @Override
    public int getSizeInBlocks() {
        // Un directorio usualmente ocupa 1 bloque para su metadata (lista de hijos)
        // O podrías calcularlo recursivamente, pero para este simular, 1 es razonable.
        return 1; 
    }
}
