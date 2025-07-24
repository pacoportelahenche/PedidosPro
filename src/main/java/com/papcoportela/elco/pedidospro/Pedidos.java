package com.papcoportela.elco.pedidospro;


/**
 *
 * @author Paco Portela Henche
 * @date 18 mar 2023
 * Esta clase representa la lista de pedidos que usaremos en la aplicacion.
 * Utilizamos las anotaciones de la API JABX para poder trabajar con datos
 * en formato XML de forma sencilla.
 */
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Pedidos")
public class Pedidos {
    private List<LineaPedido> listaPedidos;
    
    /**
     * Obtenemos la lista de pedidos.
     * @return la List que contiene los pedidos.
     */
    public List<LineaPedido> getPedidos(){
        return this.listaPedidos;
    }
    
    /**
     * Ponemos la lista de pedidos.
     * @param lista la List que contiene los pedidos.
     */
    @XmlElement(name = "Linea")
    public void setPedidos(List<LineaPedido> lista){
        this.listaPedidos = lista;
    }
    
    /**
     * Añadimos un linea de pedido a la lista.
     * @param linea el objeto LineaPedido a añadir.
     */
    public void add(LineaPedido linea){
        if(this.listaPedidos == null){
            this.listaPedidos = new ArrayList<>();
        }
        this.listaPedidos.add(linea);
    }
    
    /**
     * Imprime toda la lista de pedidos.
     * @return un <code>String</code> que contiene la lista de pedidos.
     */
    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        for( LineaPedido linea : this.listaPedidos )
        {
            str.append( linea.toString() );
        }
        return str.toString();
    }

}
