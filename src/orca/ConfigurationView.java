/*
 * ConfigurationView.java
 *
 * Created on June 30, 2008, 8:11 PM
 */

package orca;

/**
 *
 * @author  prajwalan
 */
public class ConfigurationView extends javax.swing.JFrame {
    
	// make compiler happy :)
	private static final long serialVersionUID = 666L;
	/** Creates new form ConfigurationView */
    public ConfigurationView(OrcaGUI parent) {
        this.parent = parent;
        initComponents();
        loadMessengerConfiguration();
        setReSubscribeInterval(100);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtUserID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtSIPProxyIP = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtMyPort = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtSIPProxyPort = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtReSubscribeInterval = new javax.swing.JSpinner();
        chkBoxEnablePresence = new javax.swing.JCheckBox();
        chkBoxShowMyPresence = new javax.swing.JCheckBox();
        chkBoxSendLocationWithPresence = new javax.swing.JCheckBox();
        chkBoxReceiveLocationWithPresence = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        txtLocationAdvertiseInterval = new javax.swing.JSpinner();
        jLabel15 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtLocationURL = new javax.swing.JTextField();
        btnTestLocationURL = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLocationOutput = new javax.swing.JTextArea();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setTitle("Orca Configuration");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Name: ");

        txtName.setText("java1");

        jLabel2.setText("User ID : ");

        txtUserID.setText("java1");

        jLabel3.setText("SIP Proxy IP: ");

        //txtSIPProxyIP.setText("129.241.209.181");
        /*
        txtSIPProxyIP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSIPProxyIPKeyTyped(evt);
            }
        });
        */

        jLabel4.setText("My Port:");

        txtMyPort.setText("5678");
        txtMyPort.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMyPortKeyTyped(evt);
            }
        });

        jLabel10.setText("SIP Proxy Port: ");

        txtSIPProxyPort.setText("5060");
        txtSIPProxyPort.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSIPProxyPortKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(136, 136, 136)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(txtUserID, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel2)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtSIPProxyIP, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(txtSIPProxyPort, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)))))
                        .addContainerGap(17, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(txtMyPort, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)))
                        .addGap(221, 221, 221))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSIPProxyPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3))
                            .addComponent(txtUserID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSIPProxyIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMyPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("General", jPanel1);

        jLabel12.setText("Re-Subscribe Interval:");
        jLabel12.setEnabled(false);

        jLabel13.setText("sec");

        txtReSubscribeInterval.setEnabled(false);

        chkBoxEnablePresence.setText("Enable Presence");
        chkBoxEnablePresence.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkBoxEnablePresenceItemStateChanged(evt);
            }
        });

        chkBoxShowMyPresence.setText("Show my Presence Status");
        chkBoxShowMyPresence.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkBoxShowMyPresenceItemStateChanged(evt);
            }
        });

        chkBoxSendLocationWithPresence.setText("Send Location Information with Presence Status");
        chkBoxSendLocationWithPresence.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkBoxSendLocationWithPresenceItemStateChanged(evt);
            }
        });

        chkBoxReceiveLocationWithPresence.setText("Receive Location Information with Presence Status");

        jLabel14.setText("Advertise location every ");

        txtLocationAdvertiseInterval.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                txtLocationAdvertiseIntervalStateChanged(evt);
            }
        });

        jLabel15.setText("minutes");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkBoxReceiveLocationWithPresence)
                    .addComponent(chkBoxSendLocationWithPresence)
                    .addComponent(chkBoxShowMyPresence)
                    .addComponent(chkBoxEnablePresence)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtReSubscribeInterval, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLocationAdvertiseInterval, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)))
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(txtReSubscribeInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkBoxEnablePresence)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkBoxShowMyPresence)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkBoxSendLocationWithPresence)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkBoxReceiveLocationWithPresence)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtLocationAdvertiseInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Presence", jPanel3);

        jLabel11.setText("Location URL:");

        txtLocationURL.setText("http://geoposen.item.ntnu.no:8080/geofinder/ws/getloc");

        btnTestLocationURL.setText("Test");
        btnTestLocationURL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestLocationURLActionPerformed(evt);
            }
        });

        txtLocationOutput.setColumns(20);
        txtLocationOutput.setEditable(false);
        txtLocationOutput.setLineWrap(true);
        txtLocationOutput.setRows(5);
        jScrollPane1.setViewportView(txtLocationOutput);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnTestLocationURL))
                            .addComponent(txtLocationURL, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLocationURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTestLocationURL)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Location", jPanel4);

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(241, Short.MAX_VALUE)
                .addComponent(btnSave)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancel)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnSave))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        
    }//GEN-LAST:event_formWindowClosed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        parent.saveMessengerConfiguration(true);
        parent.toggleConfiguration();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        parent.toggleConfiguration();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        parent.toggleConfiguration();
    }//GEN-LAST:event_formWindowClosing

    private void btnTestLocationURLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestLocationURLActionPerformed
        doLocationURLTest();
    }//GEN-LAST:event_btnTestLocationURLActionPerformed

    private void txtLocationAdvertiseIntervalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_txtLocationAdvertiseIntervalStateChanged
        // TODO add your handling code here:
        doLocationAdvertiseIntervalStateChanged();
    }//GEN-LAST:event_txtLocationAdvertiseIntervalStateChanged

    private void chkBoxSendLocationWithPresenceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkBoxSendLocationWithPresenceItemStateChanged
        // TODO add your handling code here:
        doUIThing();
    }//GEN-LAST:event_chkBoxSendLocationWithPresenceItemStateChanged

    private void chkBoxShowMyPresenceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkBoxShowMyPresenceItemStateChanged
        // TODO add your handling code here:
        doUIThing();
    }//GEN-LAST:event_chkBoxShowMyPresenceItemStateChanged

    private void chkBoxEnablePresenceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkBoxEnablePresenceItemStateChanged
        // TODO add your handling code here:
        doUIThing();
    }//GEN-LAST:event_chkBoxEnablePresenceItemStateChanged

    private void txtSIPProxyPortKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSIPProxyPortKeyTyped
        if( !(evt.getKeyChar() >= '0' && evt.getKeyChar() <= '9' ))
            evt.consume();
    }//GEN-LAST:event_txtSIPProxyPortKeyTyped

    private void txtMyPortKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMyPortKeyTyped
        if( !(evt.getKeyChar() >= '0' && evt.getKeyChar() <= '9' ))
            evt.consume();
    }//GEN-LAST:event_txtMyPortKeyTyped

    private void txtSIPProxyIPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSIPProxyIPKeyTyped
        // TODO add your handling code here:
        if( !((evt.getKeyChar() >= '0' && evt.getKeyChar() <= '9') || evt.getKeyChar() == '.' ))
            evt.consume();
    }//GEN-LAST:event_txtSIPProxyIPKeyTyped
    
    public void doLocationAdvertiseIntervalStateChanged()
    {
        int locationAdIntvl = 5;
        
        locationAdIntvl = ((Integer)txtLocationAdvertiseInterval.getValue()).intValue();
        
        if( locationAdIntvl < 5 )
        {
            locationAdIntvl = 5;
            txtLocationAdvertiseInterval.setValue(new Integer(5));
        }
        
    }
    
    public void doUIThing()
    {
        chkBoxReceiveLocationWithPresence.setEnabled(chkBoxShowMyPresence.isSelected()
                && chkBoxEnablePresence.isSelected());
        chkBoxSendLocationWithPresence.setEnabled(chkBoxShowMyPresence.isSelected()
                && chkBoxEnablePresence.isSelected());
        chkBoxShowMyPresence.setEnabled(chkBoxEnablePresence.isSelected());
        txtLocationAdvertiseInterval.setEnabled(chkBoxShowMyPresence.isSelected()
                && chkBoxEnablePresence.isSelected() 
                && chkBoxSendLocationWithPresence.isSelected());
    }
    
    public void loadMessengerConfiguration()
    {
        if( parent == null )
            return;
        if (parent.messengerConfiguration == null )
            return;
        
        this.txtName.setText(parent.messengerConfiguration.name);
        this.txtUserID.setText(parent.messengerConfiguration.id);
        this.txtSIPProxyIP.setText(parent.messengerConfiguration.sipProxyIP);
        this.txtSIPProxyPort.setText(String.valueOf(parent.messengerConfiguration.sipProxyPort));
        this.txtMyPort.setText(String.valueOf(parent.messengerConfiguration.myPort));
        
        this.chkBoxEnablePresence.setSelected
                (parent.messengerConfiguration.enablePresence);
        this.chkBoxShowMyPresence.setSelected
                (parent.messengerConfiguration.showMyPresence);
        this.chkBoxSendLocationWithPresence.setSelected
                (parent.messengerConfiguration.sendLocationWithPresence);
        this.chkBoxReceiveLocationWithPresence.setSelected
                (parent.messengerConfiguration.receiveLocationWithPresence);
        this.txtLocationAdvertiseInterval.setValue(
                new Integer(parent.messengerConfiguration.locationAdInterval));
        doUIThing();
        
        this.txtLocationURL.setText(parent.messengerConfiguration.locationURL);
        this.txtLocationOutput.setText("");
    }
    
    public int getLocationAdInterval()
    {
        return ((Integer)txtLocationAdvertiseInterval.getValue()).intValue();
    }
    
    public boolean getEnablePresence()
    {
        return chkBoxEnablePresence.isSelected();
    }
    
    public boolean getShowMyPresence()
    {
        return chkBoxShowMyPresence.isSelected();
    }
    
    public boolean getSendLocationWithPresence()
    {
        return chkBoxSendLocationWithPresence.isSelected();
    }
    
    public boolean getReceiveLocationWithPresence()
    {
        return chkBoxReceiveLocationWithPresence.isSelected();
    }
    
    public int getMyPort()
    {
        return Integer.parseInt(txtMyPort.getText());
    }
    
    public String getMyName()
    {
        return txtName.getText();
    }
    
    public String getUserID()
    {
        return txtUserID.getText();
    }
    
    public String getSIPProxyIP()
    {
        return txtSIPProxyIP.getText();
    }
            
    public String getSIPProxy()
    {
        return txtSIPProxyIP.getText()+":" +txtSIPProxyPort.getText();
    }

    public int getSIPProxyPort()
    {
        return Integer.parseInt(txtSIPProxyPort.getText());
    }
            
    public String getLocationURL()
    {
        return txtLocationURL.getText();
    }
    
    public int getReSubscribeInterval()
    {
        return ((Integer)txtReSubscribeInterval.getValue()).intValue();
    }
    
    public void setReSubscribeInterval(int intvl)
    {
        txtReSubscribeInterval.setValue(new Integer(intvl));
    }
    
    public void doLocationURLTest()
    {
    }
    

    private OrcaGUI parent;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnTestLocationURL;
    private javax.swing.JCheckBox chkBoxEnablePresence;
    private javax.swing.JCheckBox chkBoxReceiveLocationWithPresence;
    private javax.swing.JCheckBox chkBoxSendLocationWithPresence;
    private javax.swing.JCheckBox chkBoxShowMyPresence;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JSpinner txtLocationAdvertiseInterval;
    private javax.swing.JTextArea txtLocationOutput;
    private javax.swing.JTextField txtLocationURL;
    private javax.swing.JTextField txtMyPort;
    private javax.swing.JTextField txtName;
    private javax.swing.JSpinner txtReSubscribeInterval;
    private javax.swing.JTextField txtSIPProxyIP;
    private javax.swing.JTextField txtSIPProxyPort;
    private javax.swing.JTextField txtUserID;
    // End of variables declaration//GEN-END:variables
    
}
