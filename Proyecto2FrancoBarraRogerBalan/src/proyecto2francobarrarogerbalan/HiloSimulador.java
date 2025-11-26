/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

/**
 *
 * @author noelb
 */
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * MOTOR DEL SISTEMA OPERATIVO (KERNEL)
 * * Esta clase corre en un Hilo (Thread) independiente al de la interfaz gráfica.
 * Su función es simular el paso del tiempo y procesar las solicitudes de E/S
 * una por una, tal como lo haría un CPU/Disco real.
 * * Cumple con el requerimiento de: CONCURRENCIA y SIMULACIÓN.
 * * @author frank
 */
public class HiloSimulador extends Thread {
    
    private FileManager fileManager;
    private GUI gui; // Referencia para actualizar la vista visualmente
    
    // Banderas de control del hilo
    private boolean corriendo = true;
    private boolean pausado = false;

    /**
     * Constructor del Kernel simulado.
     * @param fileManager Acceso a todo el backend (Disco, Procesos, Memoria).
     * @param gui Acceso al frontend para repintar el árbol y la tabla.
     */
    public HiloSimulador(FileManager fileManager, GUI gui) {
        this.fileManager = fileManager;
        this.gui = gui;
    }

    @Override
    public void run() {
        System.out.println("--- KERNEL (SIMULADOR) INICIADO ---");
        
        while (corriendo) {
            try {
                // 1. SIMULACIÓN DE TIEMPO (Slow Motion)
                // Hacemos que el sistema "duerma" 1 segundo entre revisiones
                // para que el profesor pueda ver cómo cambian los estados en la tabla.
                Thread.sleep(1000); 
                
                // Si el usuario pausó la simulación, saltamos esta iteración
                if (pausado) continue;

                // 2. OBTENER EL PLANIFICADOR ACTUAL
                // Puede ser FIFO, SCAN o SSTF (Patrón Strategy)
                DiscoManager planificador = fileManager.getPlanificador();
                
                // 3. VERIFICAR SI HAY TRABAJO PENDIENTE
                if (planificador.hasPendingRequests()) {
                    
                    // A. Preguntar al algoritmo cuál es la SIGUIENTE solicitud
                    // (Aquí es donde SCAN decide si sigue subiendo o baja, o SSTF busca el más cercano)
                    int headPos = fileManager.getCurrentHeadPosition();
                    IORequests solicitud = planificador.getNextRequest(headPos);
                    
                    if (solicitud != null) {
                        // B. Ejecutar la solicitud encontrada
                        procesarSolicitud(solicitud);
                    }
                }
                
            } catch (InterruptedException e) {
                System.err.println("Hilo Simulador interrumpido: " + e.getMessage());
            }
        }
    }
    
    /**
     * Ejecuta la lógica real de la solicitud (Mover cabeza, escribir disco, etc.)
     * y gestiona los cambios de estado del proceso (BLOQUEADO -> EJECUTANDO -> TERMINADO).
     */
    private void procesarSolicitud(IORequests req) {
        System.out.println(">>> PROCESANDO: " + req.getType() + " en bloque obj: " + req.getTargetBlock());

        // 1. CAMBIO DE ESTADO: El proceso pasa a EJECUTANDO (CPU/Disco activo)
        Process proc = req.getOwnerProcess();
        fileManager.getProcessManager().setProcessState(proc, EstadoProceso.EJECUTANDO);
        
        // Actualizamos la GUI inmediatamente para que se vea el cambio de estado
        actualizarGUI(); 
        
        // Simulación visual: Mover la cabeza lectora al bloque destino
        fileManager.setCurrentHeadPosition(req.getTargetBlock());
        
        // 2. EJECUTAR LA OPERACIÓN REAL EN EL DISCO (Backend)
        boolean exito = true;
        
        try {
            // Simulamos un pequeño retraso extra por operación de escritura/lectura
            Thread.sleep(500); 

            switch (req.getType()) {
                case CREAR:
                    // Recuperamos el archivo que estaba "en espera"
                    if (req.getArchivoPendiente() instanceof NodeFile) {
                        NodeFile archivo = (NodeFile) req.getArchivoPendiente();
                        
                        // Intentamos asignar los bloques físicos
                        exito = fileManager.getGestorAsignacion().allocateFile(archivo);
                        
                        if (!exito) {
                            // FALLO CRÍTICO: DISCO LLENO
                            System.err.println("FALLO: Disco lleno al intentar crear archivo.");
                            
                            // Revertir: Borramos el nodo del árbol porque no cupo en disco
                            if (archivo.getParent() != null) {
                                // Nota: Asumimos que List tiene remove, si no, hay que hacerlo manual
                                archivo.getParent().getChildren().remove(archivo);
                            }
                        }
                    }
                    break;
                    
                case ELIMINAR:
                    // Liberamos los bloques físicos
                    if (req.getArchivoPendiente() instanceof NodeFile) {
                         // Aquí borramos los bloques del disco
                        fileManager.getGestorAsignacion().deallocateFile((NodeFile) req.getArchivoPendiente());
                        
                        // Y aquí borramos el nodo del árbol lógico
                        Node nodoABorrar = req.getArchivoPendiente();
                        if (nodoABorrar.getParent() != null) {
                             nodoABorrar.getParent().getChildren().remove(nodoABorrar);
                        }
                    } else if (req.getArchivoPendiente() instanceof NodeDirectory) {
                        // Caso especial: Borrar carpeta (solo removemos del árbol si está vacía)
                        Node nodoABorrar = req.getArchivoPendiente();
                        if (nodoABorrar.getParent() != null) {
                             nodoABorrar.getParent().getChildren().remove(nodoABorrar);
                        }
                    }
                    break;
                    
                case LEER:
                case ESCRIBIR:
                    // Estas operaciones no cambian la estructura del disco en esta simulación,
                    // solo consumen tiempo y mueven la cabeza lectora.
                    break;
            }
        } catch (InterruptedException e) {
            exito = false;
        }

        // 3. CAMBIO DE ESTADO: El proceso TERMINA
        fileManager.getProcessManager().terminateProcess(proc);
        
        // 4. ACTUALIZAR LA GUI FINAL (Swing es delicado, debe usarse invokeLater)
        boolean finalExito = exito;
        SwingUtilities.invokeLater(() -> {
            gui.actualizarVistas(); // Tu método existente en GUI
            
            if (!finalExito) {
                JOptionPane.showMessageDialog(gui, 
                        "Error Crítico: No hay espacio suficiente en el disco.", 
                        "Error de E/S", 
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    // --- Métodos de Control ---
    
    /**
     * Helper para llamar a actualizarVistas de forma segura desde el hilo.
     */
    private void actualizarGUI() {
        SwingUtilities.invokeLater(() -> gui.actualizarVistas());
    }
    
    public void detener() {
        this.corriendo = false;
    }
    
    public void setPausado(boolean pausado) {
        this.pausado = pausado;
    }
    
    public boolean isPausado() {
        return pausado;
    }
}
