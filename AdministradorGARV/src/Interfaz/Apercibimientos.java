/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Cliente.ConectorApercibimientos;
import Util.ConfigurationFileException;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import vo.Apercibimiento;

/**
 *
 * @author miguelmbp
 */
public class Apercibimientos extends javax.swing.JFrame {

    /**
     * Creates new form Apercibimientos
     */
    private List<String> cookies;

    public Apercibimientos(List<String> cookies) {
        UIManager.getLookAndFeelDefaults()
                .put("defaultFont", new Font("Verdana", Font.PLAIN, 13));
        initComponents();
        setLocationRelativeTo(null);
        rellenarTabla();
        this.cookies = cookies;
        rellenarAño();
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
        jComboBoxCurso = new javax.swing.JComboBox<>();
        jComboBoxAlumno = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxAño = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Alumno", "Periodo", "Unidad", "Materia", "FechaInicio", "FechaFin", "Activo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(204, 204, 204));
        jTable1.setShowVerticalLines(false);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(0);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(2).setMinWidth(100);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(2).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(3).setMinWidth(100);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(3).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(5).setMinWidth(100);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(5).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(6).setMinWidth(100);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(6).setMaxWidth(100);
            jTable1.getColumnModel().getColumn(7).setMinWidth(100);
            jTable1.getColumnModel().getColumn(7).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(7).setMaxWidth(100);
        }

        jComboBoxCurso.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jComboBoxCurso.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        jComboBoxCurso.setEnabled(false);
        jComboBoxCurso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCursoActionPerformed(evt);
            }
        });

        jComboBoxAlumno.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jComboBoxAlumno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        jComboBoxAlumno.setEnabled(false);
        jComboBoxAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxAlumnoActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jLabel1.setText("Curso");

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jLabel2.setText("Alumno");

        jComboBoxAño.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jComboBoxAño.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        jComboBoxAño.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxAñoActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jLabel3.setText("Año");

        jMenu1.setText("Apercibimientos");
        jMenu1.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jMenuItem1.setText("Activar/Desactivar Apercibimiento");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem4.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jMenuItem4.setText("Informes");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jMenuItem5.setText("Subir Archivos");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem7.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jMenuItem7.setText("Asignaturas Especiales");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

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
        jMenuItem6.setText("Ver usuarios");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1139, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(56, 56, 56)
                        .addComponent(jComboBoxAño, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83)
                        .addComponent(jLabel1)
                        .addGap(69, 69, 69)
                        .addComponent(jComboBoxCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(83, 83, 83)
                        .addComponent(jLabel2)
                        .addGap(71, 71, 71)
                        .addComponent(jComboBoxAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBoxCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBoxAño, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        Visitas v = new Visitas(cookies);
        v.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        DefaultTableModel t = (DefaultTableModel) jTable1.getModel();
        int pos = jTable1.getSelectedRow();
        if (pos == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un registro", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                int id = Integer.parseInt(t.getValueAt(pos, 0).toString());
                String activoS = t.getValueAt(pos, 7).toString();
                boolean activo = (activoS.equals("Activo"));
                ConectorApercibimientos cs = new ConectorApercibimientos();
                List<Apercibimiento> apercibimientos = cs.desActivarApercibimiento(id, !activo);
                actualizarTabla();
            } catch (ConfigurationFileException ex) {
                JOptionPane.showMessageDialog(this, "Error en el archivo de configuración");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error en la conexión con el servidor");
            }
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void actualizarTabla() {
        int posAño = jComboBoxAño.getSelectedIndex();
        int posCurso = jComboBoxCurso.getSelectedIndex();
        int posAlumno = jComboBoxAlumno.getSelectedIndex();

        if (posAño != -1 && posCurso != -1 && posAlumno != -1) {
            String año = jComboBoxAño.getItemAt(posAño);
            String curso = jComboBoxCurso.getItemAt(posCurso);
            String alumno = jComboBoxAlumno.getItemAt(posAlumno);
            ConectorApercibimientos cs = new ConectorApercibimientos();
            if (año.equalsIgnoreCase("todos")) {
                try {
                    List<Apercibimiento> lista = cs.cargarApercibimientos();
                    rellenarTabla(lista);
                } catch (IOException ex) {
                    Logger.getLogger(Apercibimientos.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (curso.equalsIgnoreCase("todos")) {
                try {
                    List<Apercibimiento> lista = cs.apercibimientosPorAño(año);
                    rellenarTabla(lista);
                } catch (IOException ex) {
                    Logger.getLogger(Apercibimientos.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (alumno.equalsIgnoreCase("todos")) {
                try {
                    List<Apercibimiento> lista = cs.apercibimientosPorCurso(año, curso);
                    rellenarTabla(lista);
                } catch (IOException ex) {
                    Logger.getLogger(Apercibimientos.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    List<Apercibimiento> lista = cs.apercibimientosPorAlumno(año, curso, alumno);
                    rellenarTabla(lista);
                } catch (IOException ex) {
                    Logger.getLogger(Apercibimientos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            rellenarTabla();
        }
    }


    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        Usuarios usuarios = new Usuarios(cookies);
        usuarios.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        AsignaturasEspeciales ae = new AsignaturasEspeciales(this, true);
        ae.setVisible(true);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        InformesApercibimientos ia = new InformesApercibimientos(this, true);
        ia.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jComboBoxAñoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxAñoActionPerformed
        int posAño = jComboBoxAño.getSelectedIndex();
        if (posAño != -1) {
            String año = jComboBoxAño.getItemAt(posAño);
            ConectorApercibimientos cs = new ConectorApercibimientos();
            if (año.equalsIgnoreCase("todos")) {
                jComboBoxCurso.removeAllItems();
                jComboBoxCurso.addItem("Todos");
                jComboBoxCurso.setEnabled(false);

                jComboBoxAlumno.removeAllItems();
                jComboBoxAlumno.addItem("Todos");
                jComboBoxAlumno.setEnabled(false);

                rellenarTabla();
            } else {
                try {
                    jComboBoxCurso.setEnabled(true);
                    rellenarCurso();
                    List<Apercibimiento> lista = cs.apercibimientosPorAño(año);
                    rellenarTabla(lista);
                } catch (IOException ex) {
                    Logger.getLogger(Apercibimientos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }//GEN-LAST:event_jComboBoxAñoActionPerformed

    private void jComboBoxCursoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCursoActionPerformed
        int posAño = jComboBoxAño.getSelectedIndex();
        int posCurso = jComboBoxCurso.getSelectedIndex();

        if (posAño != -1 && posCurso != -1) {
            String año = jComboBoxAño.getItemAt(posAño);
            String curso = jComboBoxCurso.getItemAt(posCurso);
            ConectorApercibimientos cs = new ConectorApercibimientos();
            if (curso.equalsIgnoreCase("todos") && !año.equalsIgnoreCase("todos")) {
                try {
                    jComboBoxAlumno.removeAllItems();
                    jComboBoxAlumno.addItem("Todos");
                    jComboBoxAlumno.setEnabled(false);

                    List<Apercibimiento> lista = cs.apercibimientosPorAño(año);
                    rellenarTabla(lista);
                } catch (IOException ex) {
                    Logger.getLogger(Apercibimientos.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (!curso.equalsIgnoreCase("todos") && !año.equalsIgnoreCase("todos")) {
                try {
                    jComboBoxAlumno.setEnabled(true);
                    rellenarAlumno();
                    List<Apercibimiento> lista = cs.apercibimientosPorCurso(año, curso);
                    rellenarTabla(lista);
                } catch (IOException ex) {
                    Logger.getLogger(Apercibimientos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }


    }//GEN-LAST:event_jComboBoxCursoActionPerformed

    private void jComboBoxAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxAlumnoActionPerformed
        int posAño = jComboBoxAño.getSelectedIndex();
        int posCurso = jComboBoxCurso.getSelectedIndex();
        int posAlumno = jComboBoxAlumno.getSelectedIndex();

        if (posAño != -1 && posCurso != -1 && posAlumno != -1) {
            String año = jComboBoxAño.getItemAt(posAño);
            String curso = jComboBoxCurso.getItemAt(posCurso);
            String alumno = jComboBoxAlumno.getItemAt(posAlumno);
            ConectorApercibimientos cs = new ConectorApercibimientos();
            if (alumno.equalsIgnoreCase("todos") && !curso.equalsIgnoreCase("todos") && !año.equalsIgnoreCase("todos")) {
                try {
                    List<Apercibimiento> lista = cs.apercibimientosPorCurso(año, curso);
                    rellenarTabla(lista);
                } catch (IOException ex) {
                    Logger.getLogger(Apercibimientos.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (!alumno.equalsIgnoreCase("todos") && !curso.equalsIgnoreCase("todos") && !año.equalsIgnoreCase("todos")) {
                try {
                    List<Apercibimiento> lista = cs.apercibimientosPorAlumno(año, curso, alumno);
                    rellenarTabla(lista);
                } catch (IOException ex) {
                    Logger.getLogger(Apercibimientos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_jComboBoxAlumnoActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccione un archivo zip o pdf");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filterpdf = new FileNameExtensionFilter("Archivo pdf", "pdf");
        FileNameExtensionFilter filterzip = new FileNameExtensionFilter("Archivo zip", "zip");
        fileChooser.addChoosableFileFilter(filterpdf);
        fileChooser.addChoosableFileFilter(filterzip);

        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (filterpdf.accept(selectedFile) || filterzip.accept(selectedFile)) {
                enviarArchivo(selectedFile);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un archivo zip o pdf");
            }
        } else if (result == JFileChooser.CANCEL_OPTION) {
            fileChooser.setVisible(false);
        }


    }//GEN-LAST:event_jMenuItem5ActionPerformed

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
            java.util.logging.Logger.getLogger(Apercibimientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Apercibimientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Apercibimientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Apercibimientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Apercibimientos(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBoxAlumno;
    private javax.swing.JComboBox<String> jComboBoxAño;
    private javax.swing.JComboBox<String> jComboBoxCurso;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    private void rellenarTabla() {
        try {
            ConectorApercibimientos cs = new ConectorApercibimientos();
            List<Apercibimiento> apercibimientos = cs.cargarApercibimientos();

            DefaultTableModel t = (DefaultTableModel) jTable1.getModel();
            t.setRowCount(0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            for (int i = 0; i < apercibimientos.size(); i++) {
                Apercibimiento a = apercibimientos.get(i);
                String activo = "";
                if (a.isActivo()) {
                    activo = "Activo";
                } else {
                    activo = "Inactivo";
                }
                Object[] elementos = {a.getId(), a.getAlumno(), a.getPeriodoAcademico(), a.getUnidad(), a.getMateria(), sdf.format(a.getFechaInicio()), sdf.format(a.getFechaFin()), activo};
                t.addRow(elementos);
            }
        } catch (ConfigurationFileException ex) {
            JOptionPane.showMessageDialog(this, "Error en el archivo de configuración");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error en la conexión con el servidor");
        }
    }

    private void rellenarTabla(List<Apercibimiento> apercibimientos) {
        DefaultTableModel t = (DefaultTableModel) jTable1.getModel();
        t.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for (int i = 0; i < apercibimientos.size(); i++) {
            Apercibimiento a = apercibimientos.get(i);
            String activo = "";
            if (a.isActivo()) {
                activo = "Activo";
            } else {
                activo = "Inactivo";
            }
            Object[] elementos = {a.getId(), a.getAlumno(), a.getPeriodoAcademico(), a.getUnidad(), a.getMateria(), sdf.format(a.getFechaInicio()), sdf.format(a.getFechaFin()), activo};
            t.addRow(elementos);
        }

    }

    private void rellenarAño() {
        try {
            ConectorApercibimientos cs = new ConectorApercibimientos();
            List<String> año = cs.cargarAño();
            jComboBoxAño.removeAllItems();
            jComboBoxAño.addItem("Todos");
            for (int i = 0; i < año.size(); i++) {
                jComboBoxAño.addItem(año.get(i));
            }
            jComboBoxAño.setSelectedIndex(0);
        } catch (ConfigurationFileException ex) {
            JOptionPane.showMessageDialog(this, "Error en el archivo de configuración");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error en la conexión con el servidor");
        }

    }

    private void rellenarCurso() {
        try {
            int posAño = jComboBoxAño.getSelectedIndex();
            if (posAño != -1) {
                ConectorApercibimientos cs = new ConectorApercibimientos();
                List<String> cursos = cs.cargarCursosFiltro(jComboBoxAño.getItemAt(posAño));
                jComboBoxCurso.removeAllItems();
                jComboBoxCurso.addItem("Todos");
                for (int i = 0; i < cursos.size(); i++) {
                    jComboBoxCurso.addItem(cursos.get(i));
                }
                jComboBoxCurso.setSelectedIndex(0);
            }
        } catch (ConfigurationFileException ex) {
            JOptionPane.showMessageDialog(this, "Error en el archivo de configuración");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error en la conexión con el servidor");
        }

    }

    private void rellenarAlumno() {
        try {
            int posAño = jComboBoxAño.getSelectedIndex();
            int posCurso = jComboBoxCurso.getSelectedIndex();
            if (posAño != -1 && posCurso != -1) {
                String año = jComboBoxAño.getItemAt(posAño);
                String curso = jComboBoxCurso.getItemAt(posCurso);
                if (!año.equalsIgnoreCase("todos") && !curso.equalsIgnoreCase("todos")) {
                    ConectorApercibimientos cs = new ConectorApercibimientos();
                    List<String> alumnos = cs.cargarAlumnoFiltro(año, curso);
                    jComboBoxAlumno.removeAllItems();
                    jComboBoxAlumno.addItem("Todos");
                    for (int i = 0; i < alumnos.size(); i++) {
                        jComboBoxAlumno.addItem(alumnos.get(i));
                    }
                    jComboBoxAlumno.setSelectedIndex(0);
                }
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
            ConectorApercibimientos cs = new ConectorApercibimientos();
            cs.enviarArchivo(base64, FilenameUtils.getExtension(selectedFile.getAbsolutePath()), cookies.get(0), cookies.get(1));
        } catch (IOException ex) {
            Logger.getLogger(Apercibimientos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
