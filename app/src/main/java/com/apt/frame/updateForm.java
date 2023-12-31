package com.apt.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

public class updateForm extends JPanel {

    public updateForm() {
        super();

        // Make updateForm a GridBagLayout As Well
        this.setLayout(new GridBagLayout());

        // Grid Bag Constraints To Reuse
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;

        // Text Panel On Left
        JPanel textNamePanel = new JPanel();
        textNamePanel.setLayout(new GridBagLayout());

        c.weightx = 2;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        JEditorPane productTextInfo = new JEditorPane();
        productTextInfo.setText("Product:");
        productTextInfo.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        productTextInfo.setBackground(Color.LIGHT_GRAY);
        productTextInfo.setEditable(false);
        textNamePanel.add(productTextInfo, c);

        c.gridy = 1;
        JEditorPane emailTextInfo = new JEditorPane();
        emailTextInfo.setText("Email:");
        emailTextInfo.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        emailTextInfo.setBackground(Color.LIGHT_GRAY);
        emailTextInfo.setEditable(false);
        textNamePanel.add(emailTextInfo, c);
        
        c.weighty = 2;
        c.gridy = 0;
        this.add(textNamePanel, c);
        
        // Text Enter Panel In Center
        JPanel textEnterPanel = new JPanel();
        textEnterPanel.setLayout(new GridBagLayout());

        c.weightx = 10;
        c.weighty = 1;
        c.gridx = 1;
        JEditorPane productTextEntry = new JEditorPane();
        productTextEntry.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        textEnterPanel.add(productTextEntry, c);

        c.gridy = 1;
        JEditorPane emailTextEntry = new JEditorPane();
        emailTextEntry.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        textEnterPanel.add(emailTextEntry, c);

        c.weighty = 2;
        c.gridy = 0;
        this.add(textEnterPanel, c);

        // Update Button Panel On Right
        JPanel updateButtonPanel = new JPanel();
        updateButtonPanel.setLayout(new GridBagLayout());

        c.weightx = 2;
        c.weighty = 1;
        c.gridx = 2;
        JButton productButton = new JButton();
        productButton.setText("Track");
        updateButtonPanel.add(productButton, c);

        c.gridy = 1;
        JButton emailButton = new JButton();
        emailButton.setText("Update");
        updateButtonPanel.add(emailButton, c);

        c.weighty = 2;
        c.gridy = 0;
        this.add(updateButtonPanel, c);
    }
}
