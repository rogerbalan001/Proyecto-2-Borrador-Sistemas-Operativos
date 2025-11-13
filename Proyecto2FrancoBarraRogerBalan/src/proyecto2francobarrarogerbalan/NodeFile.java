/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

/**
 *
 * @author frank
 */
public class NodeFile extends Node {
    
    private int sizeInBlocks; // Tamaño en bloques [cite: 14]
    private int firstBlock;   // Dirección del primer bloque [cite: 62] (para asignación encadenada) [cite: 14]

    public NodeFile(String name, NodeDirectory parent, int sizeInBlocks) {
        super(name, parent);
        this.sizeInBlocks = sizeInBlocks;
        this.firstBlock = -1; // -1 indica que aún no ha sido asignado en disco
    }

    @Override
    public int getSizeInBlocks() {
        return this.sizeInBlocks;
    }

    public int getFirstBlock() {
        return firstBlock;
    }

    public void setFirstBlock(int firstBlock) {
        this.firstBlock = firstBlock;
    }
    
    // Sobrescribimos 'setName' si queremos lógica extra (ej. validar extensión)
    @Override
    public void setName(String name) {
        // Aquí podrías validar que el nombre no contenga caracteres inválidos, etc.
        this.name = name;
    }
}