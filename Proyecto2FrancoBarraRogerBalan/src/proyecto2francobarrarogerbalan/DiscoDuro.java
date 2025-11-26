/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

import java.io.Serializable; // <--- AGREGAR IMPORT

public class DiscoDuro implements Serializable { // <--- AGREGAR IMPLEMENTS
    
    private static final long serialVersionUID = 1L;
    
    // Definimos un tamaño máximo de bloques [cite: 34, 35]
    // Vamos a usar 256 bloques como ejemplo.
    public static final int TOTAL_BLOCKS = 256; 
    
    private Block[] blocks;
    private int freeBlocksCount;

    public DiscoDuro() {
        this.blocks = new Block[TOTAL_BLOCKS];
        this.freeBlocksCount = TOTAL_BLOCKS;
        
        // Inicializar todos los bloques como libres
        for (int i = 0; i < TOTAL_BLOCKS; i++) {
            this.blocks[i] = new Block();
        }
    }

    /**
     * Busca y devuelve el índice del primer bloque libre que encuentra.
     * @return El índice del bloque libre, o -1 si el disco está lleno.
     */
    public int findFreeBlock() {
        if (freeBlocksCount == 0) {
            return -1; // No hay espacio
        }
        
        for (int i = 0; i < TOTAL_BLOCKS; i++) {
            if (blocks[i].isFree()) {
                return i;
            }
        }
        return -1; // No debería llegar aquí si freeBlocksCount es correcto
    }

    /**
     * "Ocupa" un bloque en una posición específica.
     * @param blockIndex El índice del bloque a ocupar.
     * @param nextBlockIndex El índice del siguiente bloque al que debe apuntar 
     * (usar 0 para fin de archivo).
     */
    public void allocateBlock(int blockIndex, int nextBlockIndex) {
        if (blockIndex < 0 || blockIndex >= TOTAL_BLOCKS) {
            return; // Índice fuera de rango
        }
        
        Block block = blocks[blockIndex];
        if (block.isFree()) {
            block.setFree(false);
            block.setNextBlock(nextBlockIndex);
            this.freeBlocksCount--;
        }
    }

    /**
     * Libera un bloque específico y resetea sus punteros[cite: 33].
     * @param blockIndex El índice del bloque a liberar.
     */
    public void freeBlock(int blockIndex) {
        if (blockIndex < 0 || blockIndex >= TOTAL_BLOCKS) {
            return; // Índice fuera de rango
        }
        
        if (!blocks[blockIndex].isFree()) {
            blocks[blockIndex].freeBlock();
            this.freeBlocksCount++;
        }
    }
    
    /**
     * Libera una cadena completa de bloques (asignación encadenada).
     * @param firstBlockIndex El índice del primer bloque del archivo.
     */
    public void freeChainedBlocks(int firstBlockIndex) {
        int currentIndex = firstBlockIndex;
        
        while (currentIndex != -1 && currentIndex != 0) { // 0 es EOF
            if (currentIndex >= TOTAL_BLOCKS) break; // Seguridad
            
            int nextIndex = blocks[currentIndex].getNextBlock();
            freeBlock(currentIndex);
            currentIndex = nextIndex;
        }
    }

    // --- Getters ---

    public int getFreeBlocksCount() {
        return freeBlocksCount;
    }

    public int getTotalBlocks() {
        return TOTAL_BLOCKS;
    }
    
    /**
     * Permite a la GUI "leer" el estado del disco para dibujarlo.
     * @return El array completo de bloques.
     */
    public Block[] getBlocks() {
        return blocks;
    }
    
    public Block getBlock(int index) {
        if (index < 0 || index >= TOTAL_BLOCKS) {
            return null;
        }
        return blocks[index];
    }
}
