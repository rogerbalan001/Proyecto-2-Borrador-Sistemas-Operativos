/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

/**
 *
 * @author frank
 */
public class Block {

    // -1 podría significar "libre"
    // 0 podría significar "ocupado, pero es el último bloque del archivo (EOF)"
    // N > 0 podría significar "ocupado, y el siguiente bloque es N"
    private int nextBlock; 
    
    // Podríamos añadir a qué archivo/proceso pertenece para la visualización [cite: 21]
    // private String ownerFileID; 
    
    private boolean isFree;

    public Block() {
        this.isFree = true;
        this.nextBlock = -1; // -1 significa que no apunta a nada y está libre
        // this.ownerFileID = null;
    }

    // --- Getters y Setters ---

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean isFree) {
        this.isFree = isFree;
    }

    public int getNextBlock() {
        return nextBlock;
    }

    /**
     * Asigna el puntero al siguiente bloque.
     * @param nextBlock El índice del siguiente bloque. Usar 0 para marcar fin de archivo (EOF).
     */
    public void setNextBlock(int nextBlock) {
        this.nextBlock = nextBlock;
    }
    
    /**
     * Limpia el bloque para liberarlo.
     */
    public void freeBlock() {
        this.isFree = true;
        this.nextBlock = -1;
        // this.ownerFileID = null;
    }
}
