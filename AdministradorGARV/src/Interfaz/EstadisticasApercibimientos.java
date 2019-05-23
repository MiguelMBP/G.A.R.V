/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaz;

import Cliente.ConectorApercibimientos;
import Util.ConfigurationFileException;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author miguelmbp
 */
public class EstadisticasApercibimientos extends javax.swing.JDialog {

    /**
     * Creates new form EstadisticasApercibimientos
     */
    public EstadisticasApercibimientos(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        rellenarAño();
        setLocationRelativeTo(parent);
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxAño = new javax.swing.JComboBox<>();
        jComboBoxMes = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxMes1 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 20)); // NOI18N
        jLabel1.setText("Estadísticas");

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jLabel2.setText("Periodo Academico");

        jComboBoxAño.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jComboBoxAño.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxAñoActionPerformed(evt);
            }
        });

        jComboBoxMes.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jComboBoxMes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxMesActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jLabel3.setText("Mes de inicio");

        jComboBoxMes1.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jComboBoxMes1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxMes1ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jLabel4.setText("Mes final");

        jButton1.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jButton1.setText("Generar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2)
                            .addComponent(jComboBoxAño, 0, 271, Short.MAX_VALUE)
                            .addComponent(jLabel3)
                            .addComponent(jComboBoxMes, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxMes1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(193, 193, 193)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(177, 177, 177)
                        .addComponent(jLabel1)))
                .addContainerGap(123, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addGap(56, 56, 56)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jComboBoxAño, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jComboBoxMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jComboBoxMes1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(jButton1)
                .addContainerGap(60, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxAñoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxAñoActionPerformed
        rellenarMesInicio();
        rellenarMesFin();
    }//GEN-LAST:event_jComboBoxAñoActionPerformed

    private void jComboBoxMesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxMesActionPerformed

    }//GEN-LAST:event_jComboBoxMesActionPerformed

    private void jComboBoxMes1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxMes1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxMes1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        int posAño = jComboBoxAño.getSelectedIndex();
        int posInicio = jComboBoxMes.getSelectedIndex();
        int posFin = jComboBoxMes1.getSelectedIndex();

        String[] parametros = leerConfiguración();

        if (posAño == -1 || posInicio == -1 || posFin == -1) {

        } else {
            try {
                String url = "http://" + parametros[0] + ":" + parametros[1] + "/apercibimientos/informe/estadisticasApercibimiento/";
                String año = jComboBoxAño.getItemAt(posAño);
                String inicio = mesANumero(jComboBoxMes.getItemAt(posInicio));
                String fin = mesANumero(jComboBoxMes1.getItemAt(posFin));
                
                url += año + "/" + inicio + "/" + fin;
                

                Desktop.getDesktop().browse(new URL(url).toURI());
            } catch (MalformedURLException ex) {
                Logger.getLogger(Apercibimientos.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(Apercibimientos.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Apercibimientos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(EstadisticasApercibimientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EstadisticasApercibimientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EstadisticasApercibimientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EstadisticasApercibimientos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EstadisticasApercibimientos dialog = new EstadisticasApercibimientos(new javax.swing.JFrame(), true);
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
    
    private void rellenarMesInicio() {
        try {
            jComboBoxMes.removeAllItems();
            int pos = jComboBoxAño.getSelectedIndex();
            ConectorApercibimientos cs = new ConectorApercibimientos();
            List<String> meses = cs.cargarMeses(jComboBoxAño.getItemAt(pos));
            meses = numeroAMes(meses);
            
            for (int i = 0; i < meses.size(); i++) {
                jComboBoxMes.addItem(meses.get(i));
            }
        } catch (ConfigurationFileException ex) {
            JOptionPane.showMessageDialog(this, "Error en el archivo de configuración");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error en la conexión con el servidor");
        }

    }
    
    private void rellenarMesFin() {
        try {
            jComboBoxMes1.removeAllItems();
            int pos = jComboBoxAño.getSelectedIndex();
            ConectorApercibimientos cs = new ConectorApercibimientos();
            List<String> meses = cs.cargarMeses(jComboBoxAño.getItemAt(pos));
            meses = numeroAMes(meses);
            
            for (int i = 0; i < meses.size(); i++) {
                jComboBoxMes1.addItem(meses.get(i));
            }
        } catch (ConfigurationFileException ex) {
            JOptionPane.showMessageDialog(this, "Error en el archivo de configuración");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error en la conexión con el servidor");
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBoxAño;
    private javax.swing.JComboBox<String> jComboBoxMes;
    private javax.swing.JComboBox<String> jComboBoxMes1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables

    
    private List<String> numeroAMes(List<String> meses) {
        List<String> lista = new ArrayList<>();
        for (String num : meses) {
            switch (num) {
                case "1":
                    lista.add("Enero");
                    break;
                case "2":
                    lista.add("Febrero");
                    break;
                case "3":
                    lista.add("Marzo");
                    break;
                case "4":
                    lista.add("Abril");
                    break;
                case "5":
                    lista.add("Mayo");
                    break;
                case "6":
                    lista.add("Junio");
                    break;
                case "7":
                    lista.add("Julio");
                    break;
                case "8":
                    lista.add("Agosto");
                    break;
                case "9":
                    lista.add("Septiembre");
                    break;
                case "10":
                    lista.add("Octubre");
                    break;
                case "11":
                    lista.add("Noviembre");
                    break;
                case "12":
                    lista.add("Diciembre");
                    break;
            }
        }
        return lista;
    }

    private String mesANumero(String mes) {
        switch (mes) {
            case "Enero":
                return "1";
            case "Febrero":
                return "2";
            case "Marzo":
                return "3";
            case "Abril":
                return "4";
            case "Mayo":
                return "5";
            case "Junio":
                return "6";
            case "Julio":
                return "7";
            case "Agosto":
                return "8";
            case "Septiembre":
                return "9";
            case "Octubre":
                return "10";
            case "Noviembre":
                return "11";
            case "Diciembre":
                return "12";
        }
        return "0";
    }
    
    private String[] leerConfiguración(){
        String[] parametros = new String[2];

        try (BufferedReader br = new BufferedReader(new FileReader("config.txt"));) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parametro = line.split(":");
                if (parametro[0].equalsIgnoreCase("django_address")) {
                    parametros[0] = parametro[1];
                } else if (parametro[0].equalsIgnoreCase("django_port")) {
                    parametros[1] = parametro[1];
                }
            }
        } catch (ConfigurationFileException ex) {
            JOptionPane.showMessageDialog(this, "Error en el archivo de configuración");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error en la conexión con el servidor");
        }
        return parametros;
    }
    
    private void rellenarAño() {
        try {
            ConectorApercibimientos cs = new ConectorApercibimientos();
            List<String> año = cs.cargarAño();
            
            for (int i = 0; i < año.size(); i++) {
                jComboBoxAño.addItem(año.get(i));
            }
        } catch (ConfigurationFileException ex) {
            JOptionPane.showMessageDialog(this, "Error en el archivo de configuración");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error en la conexión con el servidor");
        }

    }
}
