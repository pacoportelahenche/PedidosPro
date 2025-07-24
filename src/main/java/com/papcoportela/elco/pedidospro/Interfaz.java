package com.papcoportela.elco.pedidospro;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.Collections;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Paco Portela Henche
 * @date 18 mar 2023 Esta clase crea la interfaz que va a utilizar el usuario.
 */
public class Interfaz extends javax.swing.JFrame {

    private Pedidos pedidos;

    /**
     * Constructor de la clase.
     */
    public Interfaz() {
        initComponents();
        initOtros();
        situarVentana();
        ponerIcono();
        this.pedidos = new Pedidos();
        this.comprobarArchivoDatos();
        this.cargarDatos();
    }

    /**
     * Inicializamos algunas propiedades de la ventana y de la lista de pedidos.
     */
    private void initOtros() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent ev) {
                textIntroducirPedido.requestFocusInWindow();
            }

            @Override
            public void windowClosing(WindowEvent ev) {
                salir();
            }
        });

        this.listadoPedidos.setCellRenderer(new MiListRenderer());
        /*TextPrompt tp = new TextPrompt
            ("Escriba aquí los pedidos", this.textIntroducirPedido);
        tp.changeStyle(Font.ITALIC + Font.BOLD);
        tp.setForeground(Color.BLUE);*/
        this.textIntroducirPedido.setToolTipText(null);
    }

    /**
     * Situamos la ventana en la parte inferior izquierda de la pantalla.
     */
    private void situarVentana() {
        int altoPantalla
                = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        Dimension d = this.getPreferredSize();
        int altoPedido = d.height;
        // la instruccion de abajo coloca la pantalla abajo derecha.
        this.setLocation(0, altoPantalla - altoPedido);
    }

    /**
     * Cambiamos el icono de la ventana.
     */
    private void ponerIcono() {
        URL url = this.getClass().getResource("/recursos/elco.png");
        if (url != null) {
            ImageIcon imagen = new ImageIcon(url);
            this.setIconImage(imagen.getImage());
        }
    }

    /**
     * Guardamos los datos y salimos del programa.
     *
     * @return un cero que es el codigo de todo correcto.
     */
    public int salir() {
        this.guardarDatos();
        System.exit(0);
        return 0;
    }

    /**
     * Comprobamos que exista el fichero que contiene los datos y si no existe
     * lo creamos junto con todas las carpetas necesarias.
     */
    private void comprobarArchivoDatos() {
        File fichero = new File("datos/pedidos.xml");
        if (!fichero.exists()) {
            try {
                File f = new File("datos");
                f.mkdirs();
                fichero.createNewFile();
            } catch (java.io.IOException ex) {
                JOptionPane.
                        showMessageDialog(this, ex.toString());
            }
        }
    }

    /**
     * Cargamos los datos que estan en formato XML utilizando la API de JAXB.
     */
    private void cargarDatos() {
        try {
            File ficheroDatos = new File("datos/pedidos.xml");
            if (ficheroDatos.length() == 0) {
                return;
            }
            JAXBContext jaxbContext = JAXBContext.newInstance(Pedidos.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            pedidos = (Pedidos) jaxbUnmarshaller.unmarshal(ficheroDatos);
            if (this.pedidos.getPedidos() != null) {
                this.listadoPedidos.
                        setListData(this.pedidos.getPedidos()
                                .toArray(new LineaPedido[0]));
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        actualizarTextoTotales(comprobarCantidadArticulos());
        this.verPedidosDeTipo(LineaPedido.TIPO_ALIMENTACION);
    }
    
    /**
     * Este metodo comprueba la cantidad de articulos de cada tipo,
     * alimentacion y drogueria.
     * @return Una matriz de dos Strings que almacenan la cantidad de cada
     * tipo de pedido.
     */
    private String[] comprobarCantidadArticulos(){
        String[] cantidadPorTipo = new String[2];
        int alimentacion = 0;
        int drogueria = 0;
        for(int i = 0; i < this.listadoPedidos.getModel().getSize(); i++){
            LineaPedido lineaActual =
                    this.listadoPedidos.getModel().getElementAt(i);
            if(lineaActual.getTipoPedido() == LineaPedido.TIPO_ALIMENTACION){
                alimentacion++;
            }
            else{
                drogueria++;
            }
            cantidadPorTipo[0] = String.valueOf(alimentacion);
            cantidadPorTipo[1] = String.valueOf(drogueria);
        }
        return cantidadPorTipo;
    }
    
    /**
     * Este metodo actualiza el texto que informa de la cantidad de pedidos
     * de cada tipo.
     * @param cantidades Una matriz de dos Strings que contiene la cantidad
     * de cada tipo de pedido.
     */
    private void actualizarTextoTotales(String[] cantidades){
        String texto = "Total lineas: " + 
                this.listadoPedidos.getModel().getSize() + "; Alimentación: "
                + cantidades[0] + "; Droguería: " + cantidades[1];
        this.textInformacion.setText(texto);
        
    }

    /**
     * Guardamos los datos en formato XML utilizando la API JAXB.
     */
    private void guardarDatos() {
        if (this.pedidos.getPedidos() == null) {
            return;
        }
        try {
            /* init jaxb marshaler */
            JAXBContext jaxbContext = JAXBContext.newInstance(Pedidos.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            /* set this flag to true to format the output */
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            this.eliminarPedidosRecibidos();
            /* marshaling of java objects in xml (output to file and standard output) */
            jaxbMarshaller.marshal(pedidos, new File("datos/pedidos.xml"));
            //jaxbMarshaller.marshal(pedidos, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * Eliminamos los pedidos que ya han sido recibidos antes de guardar.
     */
    private void eliminarPedidosRecibidos() {
        for (int i = 0; i < this.pedidos.getPedidos().size(); i++) {
            LineaPedido linea = this.pedidos.getPedidos().get(i);
            if (linea.getEstadoPedido() == LineaPedido.ESTADO_RECIBIDO) {
                this.pedidos.getPedidos().remove(i);
                i--;
            }
        }
    }

    /**
     * Visualizamos en la JList los pedidos de tipo que pasamos como argumento.
     *
     * @param tipo el tipo de pedido que queremos ver.
     */
    private void verPedidosDeTipo(int tipo) {
        LineaPedido linea;
        //si queremos ver todos los pedidos pasamos un argumento -1.
        if (tipo == -1) {
            for (int i = 0; i < this.pedidos.getPedidos().size(); i++) {
                linea = this.pedidos.getPedidos().get(i);
                linea.setVisible(true);
            }

        } else {
            /* hacemos visibles las lineas de pedido que corresponden con el
            tipo que hemos seleccionado (alimentacion o drogueri­a) y el 
            resto de las lineas invisibles. Esto influira en la forma en la
            que las lineas de pedido se visualizaran en la lista de pedidos.
            Ver el metodo 'toString()' de la clase 'LineaPedido'*/
            for (int i = 0; i < this.pedidos.getPedidos().size(); i++) {
                linea = this.pedidos.getPedidos().get(i);
                if (linea.getTipoPedido() == tipo) {
                    linea.setVisible(true);
                } else {
                    linea.setVisible(false);
                }
            }
        }
        this.actualizarDatos();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        listadoPedidos = new javax.swing.JList<>();
        comboTipoPedido = new javax.swing.JComboBox<>();
        textIntroducirPedido = new javax.swing.JTextField();
        textInformacion = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuFichero = new javax.swing.JMenu();
        menuItemSalir = new javax.swing.JMenuItem();
        menuUtilidades = new javax.swing.JMenu();
        menuItemCorreo = new javax.swing.JMenuItem();
        menuAyuda = new javax.swing.JMenu();
        menuItemAyuda = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("PEDIDOS");
        setAlwaysOnTop(true);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Listado de pedidos"));
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setViewportView(listadoPedidos);

        listadoPedidos.setBackground(new java.awt.Color(0, 0, 0));
        listadoPedidos.setForeground(new java.awt.Color(0, 0, 0));
        listadoPedidos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listadoPedidosMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(listadoPedidos);

        comboTipoPedido.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Alimentación", "Droguería", "Todos" }));
        comboTipoPedido.setToolTipText("Tipo de pedido");
        comboTipoPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTipoPedidoActionPerformed(evt);
            }
        });

        textIntroducirPedido.setToolTipText("");
        textIntroducirPedido.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                textIntroducirPedidoCaretUpdate(evt);
            }
        });
        textIntroducirPedido.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textIntroducirPedidoFocusGained(evt);
            }
        });
        textIntroducirPedido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textIntroducirPedidoKeyPressed(evt);
            }
        });

        textInformacion.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        textInformacion.setText("Alimentación:     || Droguería:     || Total: ");

        menuFichero.setText("Fichero");

        menuItemSalir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuItemSalir.setText("Salir");
        menuItemSalir.setToolTipText("Sale del programa y graba los pedidos");
        menuItemSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSalirActionPerformed(evt);
            }
        });
        menuFichero.add(menuItemSalir);

        jMenuBar1.add(menuFichero);

        menuUtilidades.setText("Utilidades");

        menuItemCorreo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuItemCorreo.setText("Correo");
        menuItemCorreo.setToolTipText("Envia los articulos seleccionados por correo electronico");
        menuItemCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemCorreoActionPerformed(evt);
            }
        });
        menuUtilidades.add(menuItemCorreo);

        jMenuBar1.add(menuUtilidades);

        menuAyuda.setText("Ayuda");

        menuItemAyuda.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuItemAyuda.setText("Ayuda");
        menuItemAyuda.setToolTipText("Muestra un texto de ayuda");
        menuItemAyuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAyudaActionPerformed(evt);
            }
        });
        menuAyuda.add(menuItemAyuda);

        jMenuBar1.add(menuAyuda);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textInformacion)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(comboTipoPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textIntroducirPedido, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboTipoPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textIntroducirPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textInformacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Cuando pulsamos el menuItem Salir.
     *
     * @param evt el objeto que contiene los datos del evento generado.
     */
    private void menuItemSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSalirActionPerformed
        this.salir();
    }//GEN-LAST:event_menuItemSalirActionPerformed

    /**
     * Cuando cambiamos en el comboBox del tipo de pedido.
     *
     * @param evt el objeto que contiene los datos del evento generado.
     */
    private void comboTipoPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTipoPedidoActionPerformed
        int indice = this.comboTipoPedido.getSelectedIndex();
        switch (indice) {
            case 0 ->
                this.verPedidosDeTipo(LineaPedido.TIPO_ALIMENTACION);
            case 1 ->
                this.verPedidosDeTipo(LineaPedido.TIPO_DROGUERIA);
            default ->
                this.verPedidosDeTipo(-1);
        }
        this.textIntroducirPedido.requestFocusInWindow();
    }//GEN-LAST:event_comboTipoPedidoActionPerformed

    /**
     * Cuando el textField de introducir los pedidos gana el foco.
     *
     * @param evt el objeto que contiene los datos del evento generado.
     */
    private void textIntroducirPedidoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textIntroducirPedidoFocusGained
        this.textIntroducirPedido.selectAll();
    }//GEN-LAST:event_textIntroducirPedidoFocusGained

    /**
     * Cuando pulsamos ENTER en el textField de introducir los pedidos.
     *
     * @param evt el objeto que contiene los datos del evento generado.
     */
    private void textIntroducirPedidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textIntroducirPedidoKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            this.textIntroducirPedido.selectAll();
            this.addArticulo();
        }
    }//GEN-LAST:event_textIntroducirPedidoKeyPressed

    /**
     * Cuando pulsamos con el mouse sobre la JList que contiene los pedidos.
     *
     * @param evt el objeto que contiene los datos del evento generado.
     */
    private void listadoPedidosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listadoPedidosMousePressed
        int ind = this.listadoPedidos.getSelectedIndex();
        if (ind != -1) {
            LineaPedido lineaSeleccionada = this.listadoPedidos.getSelectedValue();
            int botonPulsado = evt.getButton();
            if (botonPulsado == MouseEvent.BUTTON1) {
                lineaSeleccionada.cambiarEstadoPedido();
            } else if (botonPulsado == MouseEvent.BUTTON3) {
                lineaSeleccionada.setEstadoPedido(LineaPedido.ESTADO_SELECCIONADO);
            }
            this.listadoPedidos.repaint();
            this.textIntroducirPedido.requestFocusInWindow();
        }
    }//GEN-LAST:event_listadoPedidosMousePressed

    /**
     * Cuando pulsamos el menuItem Ayuda.
     *
     * @param evt el objeto que contiene los datos del evento generado.
     */
    private void menuItemAyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAyudaActionPerformed
        new Ayuda();
    }//GEN-LAST:event_menuItemAyudaActionPerformed

    /**
     * Cuando pulsamos en el menuItem Correo.
     *
     * @param evt el objeto que contiene los datos del evento generado.
     */
    private void menuItemCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemCorreoActionPerformed
        if (this.pedidos.getPedidos() == null) {
            JOptionPane.showMessageDialog(this, "No hay articulos para pedir",
                    "Sin pedidos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        enviarCorreo();
    }//GEN-LAST:event_menuItemCorreoActionPerformed

    /**
     * Cuando cambia la posicion del cursor en el textField de los pedidos.
     *
     * @param evt
     */
    private void textIntroducirPedidoCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_textIntroducirPedidoCaretUpdate
        this.seleccionarLineaListaPedidos();
    }//GEN-LAST:event_textIntroducirPedidoCaretUpdate

    /**
     * Este metodo selecciona una linea en la lista de pedidos dependiendo del
     * texto que estemos escribiendo en el campo jtfTextoPedido.
     */
    private void seleccionarLineaListaPedidos() {
        // si la lista de pedidos no existe o esta vacia salimos del metodo
        if (this.pedidos.getPedidos() == null
                || this.pedidos.getPedidos().isEmpty()) {
            return;
        }
        // obtenemos el texto que estamos escribiendo actualmente
        String textoPedido = this.textIntroducirPedido.getText();
        // recorremos la lista de pedidos
        for (int i = 0; i < this.pedidos.getPedidos().size(); i++) {
            // obtenemos el texto de la linea correspondiente
            String textoLinea
                    = this.pedidos.getPedidos().get(i).getTextoPedido();
            int contNumeros = 0;
            boolean numerosQuitados = false;
            // recorremos el texto que estamos escribiendo y contamos los 
            //numeros que tiene al principio que son los de la cantidad que 
            // queremos pedir del articulo
            for(int j = 0; j < textoPedido.length(); j++){
                char c = textoPedido.charAt(j);
                if(!numerosQuitados){
                    if(c >= '0' && c <= '9'){
                        contNumeros++;
                    }
                    else{
                        numerosQuitados = true;
                    }
                }
            }
            // le quitamos el numero y el guion al texto de la linea
            String[] textoSinNumero = textoLinea.split("-");
            // comparamos ambos textos y seleccionamos la linea de pedido mas
            // cercana al texto que estamos escribiendo
            if (textoPedido.substring(contNumeros)
                    .compareToIgnoreCase(textoSinNumero[1]) < 0) {
                this.listadoPedidos.ensureIndexIsVisible(i);
                contNumeros = 0;
                break;
            }
        }
    }

    /**
     * Creamos un nuevo objeto EnviarEMail (un JFrame) y lo hacemos visible.
     */
    private void enviarCorreo() {
        new EnviarEmail(this.pedidos.getPedidos(), this).setVisible(true);
    }

    /**
     * Añadimos un articulo a la lista de pedidos.
     */
    private void addArticulo() {
        String texto = this.textIntroducirPedido.getText();
        LineaPedido nuevaLinea = new LineaPedido();
        // Comprobamos que contenga texto.
        if (texto.length() > 0) {
            /* Comprobamos que el texto no sean solo numeros. Si es asi
               salimos del metodo sin añadir la linea.
            */
            boolean todoNumeros = comprobarTodoNumeros(texto);
            if(todoNumeros) return;
            texto = comprobarCantidad(texto);
            nuevaLinea.setTextoPedido(texto.toUpperCase());
            /* Dependiendo de la seleccion del comboBox creamos un articulo de
            alimentacion o de drogueria. En caso de que ninguno este
            seleccionado sacamos un aviso al usuario.
             */
            int indiceCombo = this.comboTipoPedido.getSelectedIndex();
            switch (indiceCombo) {
                case 0 -> {
                    nuevaLinea.setTipoPedido(LineaPedido.TIPO_ALIMENTACION);
                    this.pedidos.add(nuevaLinea);
                }
                case 1 -> {
                    nuevaLinea.setTipoPedido(LineaPedido.TIPO_DROGUERIA);
                    this.pedidos.add(nuevaLinea);
                }
                default ->
                    JOptionPane.showMessageDialog(this,
                            "Seleccione el tipo de pedido en el combo box",
                            "AVISO", JOptionPane.INFORMATION_MESSAGE);
            }
            // Ordenamos la lista por orden alfabetico.
            Collections.sort(this.pedidos.getPedidos());
            // Actualizamos la lista.
            actualizarDatos();
            actualizarTextoTotales(comprobarCantidadArticulos());
        }
    }
    
    /**
     * Comprobamos que el texto introducido por el usuario no contenga solamente
     * números. Esto causaría un error al intentar cargar los datos.
     * @param texto el texto introducido por el usuario.
     * @return un boolean que contiene 'true' en caso de que el texto 
     * contenga solo numeros.
     */
    private boolean comprobarTodoNumeros(String texto){
        boolean todoNumeros = true;
        for (int i = 0; i < texto.length(); i++){
            if(texto.charAt(i) < '0' || texto.charAt(i) > '9'){
                todoNumeros = false;
                break;
            }
        }
        return todoNumeros;
    }

    /**
     * Actualiza la lista de datos de la JList de los pedidos.
     */
    private void actualizarDatos() {
        Collections.sort(this.pedidos.getPedidos());
        this.listadoPedidos.
                setListData(this.pedidos.getPedidos()
                        .toArray(new LineaPedido[0]));
        this.listadoPedidos.repaint();
        this.textIntroducirPedido.selectAll();
    }

    /**
     * Comprobamos que el usuario haya añadido una cantidad a pedir a la linea
     * del pedido. Si no lo ha hecho se añade 1 por defecto.
     *
     * @param texto el pedido que vamos a comprobar.
     * @return el pedido con la cantidad.
     */
    private String comprobarCantidad(String texto) {
        StringBuilder sb = new StringBuilder();
        boolean esCero = true;
        for (int i = 0; i < texto.length(); i++) {
            // si no tiene un numero delante añadimos un 1 y un guion
            if (texto.charAt(0) < '0' || texto.charAt(0) > '9') {
                texto = "1-" + texto;
                break;
            } // si tiene numeros delante los separamos del texto y añadimos un 
            // guion despues
            else {
                if (texto.charAt(i) >= '0' && texto.charAt(i) <= '9') {
                    if (texto.charAt(i) == '0' && esCero) {
                        continue;
                    }
                    sb.append(texto.charAt(i));
                    esCero = false;
                } else {
                    if(sb.length() == 0) sb.append("1");
                    sb.append("-");
                    sb.append(texto.substring(i));
                    texto = sb.toString();
                    break;
                }
            }
        }
        return texto;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboTipoPedido;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<LineaPedido> listadoPedidos;
    private javax.swing.JMenu menuAyuda;
    private javax.swing.JMenu menuFichero;
    private javax.swing.JMenuItem menuItemAyuda;
    private javax.swing.JMenuItem menuItemCorreo;
    private javax.swing.JMenuItem menuItemSalir;
    private javax.swing.JMenu menuUtilidades;
    private javax.swing.JTextField textInformacion;
    private javax.swing.JTextField textIntroducirPedido;
    // End of variables declaration//GEN-END:variables

}
