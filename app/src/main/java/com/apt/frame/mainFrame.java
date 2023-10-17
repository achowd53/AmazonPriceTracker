package com.apt.frame;

import java.awt.AWTEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

public class mainFrame extends JFrame implements AWTEventListener {
    String user;
    String pwd;

    scrollableTable tablePanel;
    updateForm formPanel;

    public mainFrame(String user, String pwd) {
        super();

        // Get Input
        this.user = user;
        this.pwd = pwd;

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

        // Add KeyListener
        this.getToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);

        // Update Table And Make Visible
        this.tablePanel.updateData();
        this.setVisible(true);
    }

    @Override
    public void eventDispatched(AWTEvent event) {
        if (event instanceof KeyEvent) {
            KeyEvent key = (KeyEvent) event;
            if (key.getKeyCode() == 82 && key.isControlDown() && key.getID() == KeyEvent.KEY_PRESSED) {
                this.tablePanel.updateData();
                key.consume();
            }
        }
    }
}
