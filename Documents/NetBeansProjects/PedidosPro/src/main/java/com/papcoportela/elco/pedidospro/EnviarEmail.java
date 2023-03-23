
package com.papcoportela.elco.pedidospro;

import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

/**
 *
 * @author Paco Portela Henche
 */
public class EnviarEmail extends javax.swing.JFrame {

    /**
     * Creates new form EnviarEmail
     * @param pedidos
     */
    public EnviarEmail(List pedidos) {
        initComponents();
        this.pedidos = pedidos;
        this.setLocationRelativeTo(null);
        this.getRootPane().setDefaultButton(botonAceptar);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        textEnviarA = new javax.swing.JTextField();
        botonAceptar = new javax.swing.JButton();
        botonCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Enviar Correo");

        jLabel1.setText("Enviar a");

        botonAceptar.setText("Aceptar");
        botonAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAceptarActionPerformed(evt);
            }
        });

        botonCancelar.setText("Cancelar");
        botonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textEnviarA, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(botonAceptar)
                        .addGap(51, 51, 51)
                        .addComponent(botonCancelar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textEnviarA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonAceptar)
                    .addComponent(botonCancelar))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAceptarActionPerformed
        StringBuilder sb = new StringBuilder();
        String direccion = this.textEnviarA.getText();
        if(direccion.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Escriba un direccion de correo", "AVISO",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!direccion.contains("@")){
            JOptionPane.showMessageDialog(this,
                    "Introduzca una direccion de correo valida",
                    "AVISO", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for(int i = 0; i < this.pedidos.size(); i++){
            LineaPedido lineaActual = (LineaPedido)this.pedidos.get(i);
            if(lineaActual.getEstadoPedido() == LineaPedido.ESTADO_SELECCIONADO){
                sb.append(lineaActual.getTextoPedido()).append("\n");
                lineaActual.setEstadoPedido(LineaPedido.ESTADO_PEDIDO);
            }
        }
        String datos = sb.toString();
        if(datos.isEmpty()){
            JOptionPane.showMessageDialog(this,
                "No hay ningun articulo seleccionado para enviar",
                "Sin articulos seleccionados", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(false);
            return;
        }
        String datosPedidoHtml = convertirTextoHTML(sb.toString());
        enviarCorreo(datosPedidoHtml, direccion);
    }//GEN-LAST:event_botonAceptarActionPerformed

    private String convertirTextoHTML(String datos){
        String[] arrayDatos = datos.split("\n");
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body><table border=1><tr><th>CTD.</th>"
                + "<th>ARTICULO</th>");
        for (String arrayDato : arrayDatos) {
            String[] d = arrayDato.split("-");
            sb.append("<tr><td>").append(d[0]).append("</td>").append("<td>")
                    .append(d[1]).append("</td></tr>");
        }
        sb.append("</table></body></html>").append("<p>Muchas gracias.</p>");
        return sb.toString();
    }
    
    private void botonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_botonCancelarActionPerformed

    private void enviarCorreo(String datos, String direccion){
        try{
            Properties props = new Properties();
            // Nombre del host de correo, Yahoo en este caso
            props.setProperty("mail.smtp.host", "smtp.mail.yahoo.com");
            // usar el protocolo de seguridad TLS si esta disponible
            props.setProperty("mail.smtp.starttls.enable", "true");
            // Puerto de Yahoo para envio de correos
            props.setProperty("mail.smtp.port", "587");
            // Nombre del usuario que envia el mensaje
            props.setProperty("mail.smtp.user", "elcocoruna@yahoo.com");
            // Si se requiere o no usuario y password para conectarse
            props.setProperty("mail.smtp.auth", "true");
            // Creamos la sesion con las propiedades que hemos creado
            Session sesion = Session.getDefaultInstance(props);
            // Creamos el menssaje
            MimeMessage mensaje = new MimeMessage(sesion);
            // El mensaje se envia desde la direccion
            mensaje.setFrom(new InternetAddress("elcocoruna@yahoo.com"));
            // El mensaje se envia a la direccion
            mensaje.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(direccion));
            // Asunto del mensaje
            mensaje.setSubject("Pedido economato ELCO.");
            // Texto del mensaje
            mensaje.setText(datos, "ISO-8859-1", "html");
            // Creamos el objeto Transport
            Transport t = sesion.getTransport("smtp");
            // Conectamos con el usuario y la contraseña
            t.connect("elcocoruna@yahoo.com", "gvlwdrewkjjfkoav");
            // Enviamos el mensaje
            t.sendMessage(mensaje, mensaje.getAllRecipients());
            // Cerramos el transporte
            t.close();
            JOptionPane.showMessageDialog(null, "Correo enviado");
            this.setVisible(false);
            this.dispose();
        }
        catch(MessagingException ex){
            ex.toString();
        }
    }
    
    private List pedidos;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonAceptar;
    private javax.swing.JButton botonCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField textEnviarA;
    // End of variables declaration//GEN-END:variables
}
