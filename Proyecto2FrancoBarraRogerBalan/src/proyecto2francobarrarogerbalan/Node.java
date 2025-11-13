/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

/**
 *
 * @author frank
 */
public abstract class Node {
    
    protected String name;
    protected NodeDirectory parent; // Referencia a su directorio padre (null si es el root)
    // protected Date creationDate; // Podrías añadir esto

    public Node(String name, NodeDirectory parent) {
        this.name = name;
        this.parent = parent;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NodeDirectory getParent() {
        return parent;
    }

    public void setParent(NodeDirectory parent) {
        this.parent = parent;
    }
    
    // Método para obtener el path completo (ej. /root/docs/file.txt)
    public String getPath() {
        if (parent == null) { // Es el directorio raíz
            return "/"; 
        }
        String parentPath = parent.getPath();
        if (parentPath.equals("/")) {
            return "/" + name;
        }
        return parent.getPath() + "/" + name;
    }
    
    // Método abstracto que las subclases deberán implementar
    public abstract int getSizeInBlocks();
}
