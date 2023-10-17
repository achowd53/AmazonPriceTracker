package com.apt.frame;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class mainFrame extends JFrame {
    //scrollableTable tablePanel;
    updateForm formPanel;

    public mainFrame() {
        super();

        // Set Up Main Settings
        this.setTitle("Amazon Price Tracker  -  ");
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
        //tablePanel = new scrollableTable();
        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(Color.RED);
        this.getContentPane().add(tablePanel, c);

        c.weighty = 2;
        c.weightx = .5;
        c.gridy = 1;
        formPanel = new updateForm();
        this.getContentPane().add(formPanel, c);

        // Make Visible
        this.setVisible(true);
    }

    public void updateTitle(String user) {
        this.setTitle("Amazon Price Tracker  -  "+user);
    }
}
