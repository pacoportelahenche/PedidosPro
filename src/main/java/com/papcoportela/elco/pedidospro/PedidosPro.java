package com.papcoportela.elco.pedidospro;

/**
 *
 * @author Paco Portela Henche
 * 18/03/23
 * Esta clase sirve de punto de entrada a la aplicacion. Crea un objeto
 * Interfaz (un JFrame) que presenta la interfaz para el usuario y la hace
 * visible.
 */
public class PedidosPro {

    /**
     * Metodo de entrada a la aplicacion.
     * @param args los parametros pasados cuando arrancamos el programa desde
     * la consola.
     */
    public static void main(String[] args) {
        Interfaz interfaz = new Interfaz();
        interfaz.setVisible(true);
    }
}
