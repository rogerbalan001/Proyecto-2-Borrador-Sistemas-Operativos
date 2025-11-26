/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto2francobarrarogerbalan;

import java.io.Serializable; // <--- AGREGAR IMPORT

public class GestorSesion implements Serializable { // <--- AGREGAR IMPLEMENTS
    
    private static final long serialVersionUID = 1L;
    
    private ModoUsuario modoActual;

    public GestorSesion() {
        // Por defecto, el sistema arranca en modo Usuario
        this.modoActual = ModoUsuario.USUARIO;
    }

    /**
     * Cambia el modo de la sesión actual.
     * @param nuevoModo El modo al que se desea cambiar.
     */
    public void setModoActual(ModoUsuario nuevoModo) {
        this.modoActual = nuevoModo;
        System.out.println("Modo cambiado a: " + nuevoModo);
    }

    /**
     * Devuelve el modo de la sesión actual.
     * @return El ModoUsuario actual.
     */
    public ModoUsuario getModoActual() {
        return modoActual;
    }

    /**
     * Helper rápido para verificar si el modo actual es Administrador.
     * @return true si el modo es ADMIN, false en caso contrario.
     */
    public boolean isAdmin() {
        return this.modoActual == ModoUsuario.ADMINISTRADOR;
    }
}
