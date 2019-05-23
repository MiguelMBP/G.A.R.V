/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Cliente.ConectorApercibimientos;
import Util.ConfigurationFileException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mmbernal
 */
public class AsignaturasEspeciales extends javax.swing.JDialog {

    /**
     * Creates new form AsignaturasEspeciales
     */
    public AsignaturasEspeciales(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
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
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Nombre"
            }
        ));
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(0);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(35);
        }

        jMenu1.setText("Opciones");
        jMenu1.setFont(new java.awt.Font("Verdana", 0, 15)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jMenuItem1.setText("Añadir");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jMenuItem2.setText("Modificar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jMenuItem3.setText("Eliminar");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            String materia = JOptionPane.showInputDialog("Nombre de la asignatura");
            if ((materia != null) && (materia.length() > 0)) {
                ConectorApercibimientos cs = new ConectorApercibimientos();
                List<String> lista = cs.crearAsignatura(materia);
                rellenarTabla(lista);
            }
        } catch (ConfigurationFileException ex) {
            JOptionPane.showMessageDialog(this, "Error en el archivo de configuración");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error en la conexión con el servidor");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        DefaultTableModel t = (DefaultTableModel) jTable1.getModel();
        int pos = jTable1.getSelectedRow();
        if (pos == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un registro", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                String materia = JOptionPane.showInputDialog(this, "Nombre de la asignatura", jTable1.getValueAt(pos, 1));
                if ((materia != null) && (materia.length() > 0)) {
                    ConectorApercibimientos cs = new ConectorApercibimientos();
                    List<String> lista = cs.modificarAsignatura(Integer.parseInt(jTable1.getValueAt(pos, 0).toString()), materia);
                    rellenarTabla(lista);
                }
            } catch (ConfigurationFileException ex) {
                JOptionPane.showMessageDialog(this, "Error en el archivo de configuración");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error en la conexión con el servidor");
            }
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        DefaultTableModel t = (DefaultTableModel) jTable1.getModel();
        int pos = jTable1.getSelectedRow();
        if (pos == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un registro", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                ConectorApercibimientos cs = new ConectorApercibimientos();
                List<String> lista = cs.eliminarAsignatura(Integer.parseInt(jTable1.getValueAt(pos, 0).toString()));
                rellenarTabla(lista);
            } catch (ConfigurationFileException ex) {
                JOptionPane.showMessageDialog(this, "Error en el archivo de configuración");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error en la conexión con el servidor");
            }
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

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
            java.util.logging.Logger.getLogger(AsignaturasEspeciales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AsignaturasEspeciales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AsignaturasEspeciales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AsignaturasEspeciales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AsignaturasEspeciales dialog = new AsignaturasEspeciales(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    private void rellenarTabla() {
        try {
            ConectorApercibimientos cs = new ConectorApercibimientos();
            List<String> asignaturas = cs.cargarAsignaturas();

            DefaultTableModel t = (DefaultTableModel) jTable1.getModel();
            t.setRowCount(0);
            for (int i = 0; i < asignaturas.size(); i++) {
                String[] ae = asignaturas.get(i).split("--");
                Object[] elementos = {ae[0], ae[1]};
                t.addRow(elementos);
            }
        } catch (ConfigurationFileException ex) {
            JOptionPane.showMessageDialog(this, "Error en el archivo de configuración");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error en la conexión con el servidor");
        }
    }

    private void rellenarTabla(List<String> asignaturas) {

        DefaultTableModel t = (DefaultTableModel) jTable1.getModel();
        t.setRowCount(0);
        for (int i = 0; i < asignaturas.size(); i++) {
            String[] ae = asignaturas.get(i).split("--");
            Object[] elementos = {ae[0], ae[1]};
            t.addRow(elementos);
        }

    }
}
