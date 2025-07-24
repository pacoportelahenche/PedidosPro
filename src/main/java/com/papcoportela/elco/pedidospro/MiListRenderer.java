package com.papcoportela.elco.pedidospro;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Paco Portela Henche
 * @date 21 mar 2023
 */
/**
     * Esta clase nos permite manejar a nuestro antojo la forma en como se
     * visualizan los datos en la JList listadoPedidos. Una instancia de esta
     * clase le sera pasada a la JList a traves del metodo setCellRenderer(new
     * miListRenderer()). Lo que hacemos es reescribir el metodo
     * getListCellRendererComponent para adaptarlo a nuestras necesidades.
     */
    class MiListRenderer extends JLabel implements ListCellRenderer {

        /**
         * Constructor de la clase.
         */
        public MiListRenderer() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
            setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
        }

        @Override
        public Component getListCellRendererComponent(
                JList lista,
                Object valor,
                int indice,
                boolean estaSeleccionado,
                boolean celdaTieneFoco) {

            if (estaSeleccionado) {
                setBackground(Color.BLACK);
                setForeground(lista.getSelectionForeground());
            } else {
                setBackground(Color.BLACK);
                setForeground(lista.getForeground());
            }
            LineaPedido obj = (LineaPedido) valor;

            /* Dependiendo del estado y del tipo del pedido visualizamos los
            datos de un color u otro.
            */
            switch (obj.getEstadoPedido()) {
                /* Si el pedido esta pendiente de pedir visualizamos los 
                de alimentacion de color naranja y los de drogueria de color
                verde.
                */
                case LineaPedido.ESTADO_PENDIENTE -> {
                    if (obj.getTipoPedido() == LineaPedido.TIPO_ALIMENTACION) {
                        setForeground(Color.ORANGE);
                        setText(obj.toString());
                    } else if (obj.getTipoPedido() == LineaPedido.TIPO_DROGUERIA) {
                        setForeground(Color.GREEN);
                        setText(obj.toString());
                    }
                }
                /* Si el pedido esta ya pedido visualizamos de color rojo
                tanto si es de alimentacion como de drogueria.
                */
                case LineaPedido.ESTADO_PEDIDO -> {
                    setForeground(Color.RED);
                    setText(obj.toString());
                }
                /* Si el pedido esta seleccionado para enviar por correo
                electronico lo visualizamos de color azul claro.
                */
                case LineaPedido.ESTADO_SELECCIONADO -> {
                    setForeground(Color.CYAN);
                    setText(obj.toString());
                }
                /* Por ultimo si el pedido a sido recibido lo visualizamos de
                color gris claro.
                */
                default -> {
                    setForeground(Color.LIGHT_GRAY);
                    setText(obj.toString());
                }
            }
            return this;
        }
    }

