package com.papcoportela.elco.pedidospro;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"textoPedido", "estadoPedido", "tipoPedido"})
@XmlRootElement(name = "Linea")



/**
 *
 * @author Paco Portela Henche
 * @date 18 mar 2023
 * Esta clase encapsula el contenido y funcionamiento de una linea de un pedido.
 * Utiliza las anotaciones de la API JABX para poder trabajar con datos
 * en formato XML de forma sencilla.
 */
public class LineaPedido implements Comparable{
    // variables miembro de la clase
    private String textoPedido; // el texto del pedido.
    private int tipoPedido; // el tipo del pedido.
    private int estadoPedido; // el estado del pedido
    private boolean visible; // indica si la linea se va a visualizar o no.
    
    // posibles estados del pedido
    /*
     * Si el arti­culo esta sin pedir aun.
     */
    public static final int ESTADO_PENDIENTE = 0;
    /*
     * Si el arti­culo ya ha sido pedido.
     */
    public static final int ESTADO_PEDIDO = 1;
    /*
     * Si el arti­culo ya ha sido recibido.
     */
    public static final int ESTADO_RECIBIDO = 2;
    /*
    * Si el articulo esta seleccionado para enviar por correo electronico
    */
    public static final int ESTADO_SELECCIONADO = 3;
    
    // posibles tipos del pedido
    /**
     * El arti­culo es de alimentacion.
     */
    public static final int TIPO_ALIMENTACION = 0;
    /**
     * El arti­culo es de drogueri­a.
     */
    public static final int TIPO_DROGUERIA = 1;
    
    /**
     * Constructor sin argumentos
     */
    public LineaPedido(){
        this.textoPedido = "";
        this.estadoPedido = ESTADO_PENDIENTE;
        this.tipoPedido = TIPO_ALIMENTACION;
        this.visible = true;
    }
    
    /**
     * Constructor con dos argumentos
     * @param texto el texto del pedido
     * @param tipo el tipo del pedido (alimentacion o drogueri­a)
     */
    public LineaPedido(String texto, int tipo){
        this.textoPedido = texto;
        this.estadoPedido = ESTADO_PENDIENTE;
        this.tipoPedido = tipo;
        this.visible = true;
    }
    
    /**
     * Constructor con tres argumentos
     * @param texto el texto del pedido
     * @param estado el estado del pedido (pedido, pendiente o recibido)
     * @param tipo el tipo del pedido (alimentacion o drogueri­a)
     */
    public LineaPedido(String texto, int estado, int tipo){
        this.textoPedido = texto;
        this.estadoPedido = estado;
        this.tipoPedido = tipo;
        this.visible = true;
    }

    /**
     * Obtiene el texto del pedido
     * @return un String con el texto del pedido.
     */
    @XmlElement(name = "Texto")
    public String getTextoPedido(){
        return this.textoPedido;
    }
    
    /**
     * Pone el texto del pedido
     * @param texto el texto que queremos poner en el pedido.
     */

    
    public void setTextoPedido(String texto){
        this.textoPedido = texto;
    }

    /**
     * Obtiene el estado del pedido
     * @return un entero que representa el estado del pedido
     */
    @XmlElement(name = "Estado")
    public int getEstadoPedido(){
        return this.estadoPedido;
    }

    /**
     * Cambia el estado del pedido
     * @param estado un entero que representa el estado en el que queremos 
     * poner el pedido.
     */
    
    public void setEstadoPedido(int estado){
        this.estadoPedido = estado;
    }

    /**
     * Este metodo permite cambiar el estado del pedido. Va cambiando el estado
     * de forma ci­clica: de ESTADO_PENDIENTE a ESTADO_PEDIDO. De ESTADO_PEDIDO
     * a ESTADO_RECIBIDO. De ESTADO_RECIBIDO a ESTADO_PENDIENTE y asi
     * sucesivamente.
     */
    public void cambiarEstadoPedido(){
        switch (this.getEstadoPedido()) {
            case LineaPedido.ESTADO_PENDIENTE ->
                this.setEstadoPedido(LineaPedido.ESTADO_PEDIDO);
            case LineaPedido.ESTADO_PEDIDO -> 
                this.setEstadoPedido(LineaPedido.ESTADO_RECIBIDO);
            default -> this.setEstadoPedido(LineaPedido.ESTADO_PENDIENTE);
        }
    }

    /**
     * Obtiene el tipo de pedido
     * @return un entero que representa el tipo del pedido.
     */
    @XmlElement(name = "Tipo")
    public int getTipoPedido(){
        return this.tipoPedido;
    }

    /**
     * Pone el tipo de pedido
     * @param tipo un entero que representa el tipo del pedido.
     */
    
    public void setTipoPedido(int tipo){
        this.tipoPedido = tipo;
    }

    /**
     * Obtiene si el pedido es visible o no lo es.
     * @return un boolean indicando si la linea es visible o no.
     */
    protected boolean isVisible(){
        return this.visible;
    }

    /**
     * Pone el pedido visible o invisible.
     * @param opcion  true = visible, false = invisible.
     */
    protected void setVisible(boolean opcion){
        this.visible = opcion;
    }

    /**
     * Devuelve el texto del pedido solo si el pedido esta en estado VISIBLE.
     * En el caso de que no este VISIBLE devuelve una cadena vaci­a.
     * @return el texto del pedido o nada si la linea no es visible.
     */
    @Override
    public String toString(){
        if(this.isVisible()){
            return this.textoPedido;
        }
        else{
            return "";
        }
    }
    
    /**
     * Este metodo sobreescribe el metodo 'compareTo' del interface 'Comparable'
     * Esto nos permite comparar dos objetos del tipo 'LineaPedido' lo que nos
     * permitira ordenarlos facilmente usando los metodos de la clase
     * 'java.util.Collections'
     * @param obj el objeto a comparar
     * @return un entero. Si es negativo el objeto es menor al objeto que se
     * pasa como argumento al metodo. Si es cero los objetos son iguales. Si es
     * positivo el objeto es mayor al objeto pasado como argumento.
     * En este caso los objetos se comparan alfabeticamente por su texto.
     */
    @Override
    public int compareTo(Object obj){
        LineaPedido otraLinea = (LineaPedido)obj;
        String textoOtra = otraLinea.getTextoPedido();
        String[] datosOtra = textoOtra.split("-");
        String textoEsta = this.getTextoPedido();
        String[] datosEsta = textoEsta.split("-");
        return datosEsta[1].compareToIgnoreCase(datosOtra[1]);
    }
}

