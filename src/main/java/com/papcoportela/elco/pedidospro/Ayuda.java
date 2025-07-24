
package com.papcoportela.elco.pedidospro;

import javax.swing.JOptionPane;

/**
 *
 * @author Paco Portela Henche
 * @date 21 mar 2023
 * Esta clase presenta una ayuda de como utilizar la aplicacion al usuario.
 */
public class Ayuda {
    public Ayuda(){
        String textoAyuda = "<html><head></head><body>"
                + "<div align=center><h1><b><u>FUNCIONAMIENTO DEL PROGRAMA"
                + "</u></b></h1></div>"
                + "<div align=center>"
                + "<h2>Selección del tipo de pedido</h2></div>"
                + "<font size=4><ol><li>Seleccione el tipo de pedido que "
                + "quiere introducir (alimentación o droguería).</li>"
                + "<li>Introduzca el texto en el campo de texto.</li>"
                + "<li>Pulse <b>ENTER.</b>"
                + "</li></ol></font>"
                + "<div align=center><h2>Visualización "
                + "de pedidos por tipo</h2></div>"
                + "<font size=4><ol><li>Seleccione el tipo en el selector.</b>"
                + "</li></ol></font>"
                + "<div align=center><h2>Mecanica de trabajo</h2></div>"
                + "<font size=4>Para utilizar correctamente la funcionalidad "
                + "del envío del pedido por correo electrónico la forma de "
                + "escribir<br>el pedido es importante. Primero tecleamos la "
                + "cantidad de cajas que queremos del producto "
                + "y a continuacion (sin espacios) el producto a pedir."
                + "<br>El programa añade un "
                + "guión para separar la cantidad del texto. "
                + "Cuando enviemos el pedido por correo el programa separa "
                + "<br>por el guión la cantidad y el nombre del producto y lo "
                + "envía en una tabla al proveedor."
                + "<br> Si no introducimos ninguna "
                + "cantidad el programa incluye 1 caja por defecto."
                + "<br>Cuando haya introducido las lineas de pedido "
                + "verá que cada tipo de pedido tiene un color predefinido; "
                + "<br><b>NARANJA</b> los de alimentación y <b>VERDE</b>"
                + " los de droguería.<br> Si pulsa en alguna de"
                + " las lineas el color"
                + " del pedido cambiará a <b>ROJO</b>,"
                + " lo que indica que ya ha "
                + "sido pedido.<br> Si volvemos a pulsar "
                + "otra vez el color pasará a"
                + " ser <b>GRIS</b> lo que significa que ya hemos recibido la "
                + "mercancia.<br> Cuando salgamos del "
                + "programa las lineas en color "
                + "<b>GRIS</b> no se grabarán.</font>"
                + "<div align=center><h2>Envío por correo electrónico</h2></div>"
                + "<font size=4>Seleccione los articulos que quiera enviar por"
                + " correo electronico pinchando primero con el boton<br>"
                + "izquierdo del raton y luego con el derecho. El articulo"
                + " cambiará su color a azul claro.<br> Una vez que haya "
                + "seleccionado todos los artículos, pulse en el menú "
                + "<b>Utilidades</b> y luego en <b>Correo</b>.<br> Escriba la "
                + "direccion a quien lo quiere enviar y pulse <b>Aceptar.</b>"
                + "</body></html>";
        
        JOptionPane.showMessageDialog(null, textoAyuda, "AYUDA",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
