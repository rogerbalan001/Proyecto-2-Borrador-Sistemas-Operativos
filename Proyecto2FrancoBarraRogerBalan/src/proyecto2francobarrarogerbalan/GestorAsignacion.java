/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

import java.io.Serializable; // <--- AGREGAR IMPORT

public class GestorAsignacion implements Serializable { // <--- AGREGAR IMPLEMENTS
    
    private static final long serialVersionUID = 1L;
    
    private DiscoDuro disco;

    /**
     * Constructor. Requiere una instancia del DiscoDuro para poder operar.
     * @param disco El disco simulado (SD).
     */
    public GestorAsignacion(DiscoDuro disco) {
        this.disco = disco;
    }

    /**
     * Asigna el espacio necesario en disco para un archivo.
     * Busca los bloques libres y los encadena.
     * * @param file El NodeFile que necesita asignación de bloques.
     * @return true si la asignación fue exitosa, false si no hay espacio.
     */
    public boolean allocateFile(NodeFile file) {
        int blocksNeeded = file.getSizeInBlocks();

        // 1. Comprobar si hay espacio suficiente en el disco
        if (blocksNeeded > disco.getFreeBlocksCount()) {
            System.err.println("Error de asignación: No hay suficientes bloques libres.");
            return false;
        }

        // 2. Manejar caso de archivo de 0 bloques (archivo vacío)
        if (blocksNeeded == 0) {
            file.setFirstBlock(0); // 0 = EOF, no ocupa bloques
            return true;
        }

        // 3. Encontrar el primer bloque y asignarlo al archivo
        int firstBlock = disco.findFreeBlock();
        if (firstBlock == -1) {
            // Esto no debería pasar si la comprobación de freeBlocksCount es correcta
            return false; 
        }
        
        file.setFirstBlock(firstBlock);
        
        int prevBlockIndex = firstBlock;
        int currentBlockIndex;

        // 4. Buscar y encadenar los bloques restantes
        for (int i = 1; i < blocksNeeded; i++) {
            currentBlockIndex = disco.findFreeBlock();
            
            // Asigna el bloque ANTERIOR para que APUNTE al bloque ACTUAL
            disco.allocateBlock(prevBlockIndex, currentBlockIndex);
            
            // El bloque actual se convierte en el "anterior" para la siguiente iteración
            prevBlockIndex = currentBlockIndex;
        }

        // 5. Asignar el ÚLTIMO bloque de la cadena
        // Lo hacemos apuntar a 0 (nuestro indicador de Fin de Archivo - EOF)
        disco.allocateBlock(prevBlockIndex, 0); 
        
        return true;
    }

    /**
     * Libera todos los bloques asociados a un archivo.
     * Sigue la cadena encadenada y libera cada bloque.
     * * @param file El NodeFile cuyos bloques serán liberados.
     */
    public void deallocateFile(NodeFile file) {
        int firstBlock = file.getFirstBlock();
        
        if (firstBlock <= 0) { // 0 es EOF, -1 es no asignado
            // El archivo no tiene bloques asignados, no hay nada que hacer.
            return; 
        }

        // Usamos el método helper que ya creamos en DiscoDuro
        disco.freeChainedBlocks(firstBlock);
        
        // Marcamos el archivo como "no asignado"
        file.setFirstBlock(-1);
    }
    
}