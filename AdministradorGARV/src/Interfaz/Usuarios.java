/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Cliente.ConectorUsuarios;
import Util.ConfigurationFileException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.codec.binary.Base64;
import vo.Usuario;

/**
 * Interfaz principal del módulo de usuarios
 * @author miguelmbp
 */
public class Usuarios extends javax.swing.JFrame {

    /**
     * Creates new form Usuarios
     */
    private List<String> cookies;

    public Usuarios(List<String> cookies) {
        initComponents();
        this.cookies = cookies;
        setLocationRelativeTo(null);
        rellenarTabla();
        this.setTitle("G.A.R.V.");
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
        jTable1 = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Usuario", "Nombre", "Correo", "Curso"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jMenu1.setText("Apercibimientos");
        jMenu1.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jMenuItem1.setText("Ver Apercibimientos");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Visitas");
        jMenu2.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N

        jMenuItem3.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jMenuItem3.setText("Ver visitas");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Usuarios");
        jMenu3.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N

        jMenuItem6.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jMenuItem6.setText("Crear ususario");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuItem2.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jMenuItem2.setText("Cambiar contraseña");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItem4.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jMenuItem4.setText("Editar usuario");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuItem5.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jMenuItem5.setText("Importar usuarios");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem7.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jMenuItem7.setText("Eliminar usuario");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem7);

        jMenuItem8.setText("Actualizar vista");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem8);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 918, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        Visitas v = new Visitas(cookies);
        v.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        CrearUsuario usuario = new CrearUsuario(this, true, cookies);
        usuario.setVisible(true);
        rellenarTabla();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Apercibimientos a = new Apercibimientos(cookies);
        a.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        DefaultTableModel t = (DefaultTableModel) jTable1.getModel();
        int pos = jTable1.getSelectedRow();
        if (pos == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un registro", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            String usuario = t.getValueAt(pos, 0).toString();
            CambiarContrasena cc = new CambiarContrasena(this, true, usuario, cookies);
            cc.setVisible(true);
        }

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        DefaultTableModel t = (DefaultTableModel) jTable1.getModel();
        int pos = jTable1.getSelectedRow();
        if (pos == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un registro", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            String usuario = t.getValueAt(pos, 0).toString();
            EditarUsuario eu = new EditarUsuario(this, true, cookies, usuario);
            eu.setVisible(true);
            rellenarTabla();
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccione un archivo csv");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filtercsv = new FileNameExtensionFilter("Archivo csv", "csv");
        fileChooser.addChoosableFileFilter(filtercsv);

        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (filtercsv.accept(selectedFile)) {
                enviarArchivo(selectedFile);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un archivo csv");
            }
        } else if (result == JFileChooser.CANCEL_OPTION) {
            fileChooser.setVisible(false);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        DefaultTableModel t = (DefaultTableModel) jTable1.getModel();
        int pos = jTable1.getSelectedRow();
        if (pos == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un registro", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            String usuario = t.getValueAt(pos, 0).toString();
            int dialogResult = JOptionPane.showConfirmDialog(this, "¿Desea eliminar este usuario?");
            if (dialogResult == JOptionPane.YES_OPTION) {
                try {
                    ConectorUsuarios cs = new ConectorUsuarios();
                    boolean eliminado = cs.eliminarUsuario(cookies, usuario);
                    if (eliminado) {
                        rellenarTabla();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al eliminar el usuario", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (ConfigurationFileException ex) {
                    JOptionPane.showMessageDialog(this, "Error en el archivo de configuración");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error en la conexión con el servidor");
                }
            }

        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        rellenarTabla();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Usuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Usuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Usuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Usuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Usuarios(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    private void rellenarTabla() {
        try {
            ConectorUsuarios cs = new ConectorUsuarios();
            List<Usuario> usuarios = cs.cargarUsuarios();

            DefaultTableModel t = (DefaultTableModel) jTable1.getModel();
            t.setRowCount(0);
            for (int i = 0; i < usuarios.size(); i++) {
                Usuario u = usuarios.get(i);
                Object[] elementos = {u.getUsuario(), u.getNombre() + " " + u.getApellidos(), u.getCorreo(), u.getCursoTutor()};
                t.addRow(elementos);
            }
        } catch (ConfigurationFileException ex) {
            JOptionPane.showMessageDialog(this, "Error en el archivo de configuración");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error en la conexión con el servidor");
        }
    }

    private void enviarArchivo(File selectedFile) {
        try {
            byte[] archivo = Files.readAllBytes(selectedFile.toPath());
            String base64 = Base64.encodeBase64String(archivo);
            ConectorUsuarios cs = new ConectorUsuarios();
            cs.enviarArchivo(base64, cookies.get(0), cookies.get(1));
        } catch (IOException ex) {
            Logger.getLogger(Apercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
