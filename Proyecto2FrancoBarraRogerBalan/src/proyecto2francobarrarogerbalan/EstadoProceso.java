/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package proyecto2francobarrarogerbalan;

/**
 *
 * @author frank
 */
public enum EstadoProceso {
    NUEVO,     // El proceso acaba de ser creado
    BLOQUEADO, // El proceso está en la cola de E/S, esperando por el disco
    EJECUTANDO,// La solicitud de E/S del proceso está siendo atendida por el disco
    TERMINADO  // El proceso ha completado su operación
}