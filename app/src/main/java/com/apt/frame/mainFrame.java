package com.apt.frame;

import java.awt.AWTEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.apt.data.APTDataSingleton;

public class mainFrame extends JFrame implements AWTEventListener {
    String user;

    scrollableTable tablePanel;
    updateForm formPanel;

    public mainFrame() {
        super();

        // Get Input
        APTDataSingleton dataCore = APTDataSingleton.getInstance();
        this.user = dataCore.getUser();

        // Set Up Main Settings
        this.setTitle("Amazon Price Tracker  -  " + user);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());
        this.setSize(980, 910);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        // Set Up GridBagConstraints
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;

        // Add content panes
        c.weighty = 11;
        c.weightx = .5;
        c.gridy = 0;
        tablePanel = new scrollableTable();
        this.getContentPane().add(tablePanel, c);

        c.weighty = 2;
        c.weightx = .5;
        c.gridy = 1;
        formPanel = new updateForm();
        this.getContentPane().add(formPanel, c);

        // Add Listener to buttons located in updateFrame
        addButtonListeners();
        
        // Add KeyListener
        this.getToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);

        // Update Table And Make Visible
        this.tablePanel.updateTable(true);
        this.setVisible(true);
    }

    @Override
    public void eventDispatched(AWTEvent event) {
        if (event instanceof KeyEvent) {
            KeyEvent key = (KeyEvent) event;
            if (key.getKeyCode() == 82 && key.isControlDown() && key.getID() == KeyEvent.KEY_PRESSED) {
                this.tablePanel.updateTable(true);
                key.consume();
            }
        }
    }

    public void addButtonListeners() {
        // Add Listener to buttons located in updateFrame
        JPanel updateButtonPanel = (JPanel) this.formPanel.getComponent(2);
        JButton productButton = (JButton) updateButtonPanel.getComponent(0);
        productButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                // Get Data
                APTDataSingleton dataCore = APTDataSingleton.getInstance();

                // Get Text Enter Boxes located in updateFrame
                JPanel textEnterPanel = (JPanel) formPanel.getComponent(1);
                JEditorPane productTextEntry = (JEditorPane) textEnterPanel.getComponent(0);

                // Update add_track and delete productTextEntry text
                String productText = productTextEntry.getText();
                productTextEntry.setText("");
                if (productText.matches(".+/dp/.{10}.+")) {
                    dataCore.addEntry(productText);
                    tablePanel.updateTable(false);
                }
            } 
        });
        JButton emailButton = (JButton) updateButtonPanel.getComponent(1);
        emailButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                // Get Data
                APTDataSingleton dataCore = APTDataSingleton.getInstance();

                // Get Text Enter Boxes located in updateFrame
                JPanel textEnterPanel = (JPanel) formPanel.getComponent(1);
                JEditorPane emailTextEntry = (JEditorPane) textEnterPanel.getComponent(1);

                // Update Email
                dataCore.setEmail(emailTextEntry.getText());
                tablePanel.updateTable(false);
            } 
        });
    }
}
