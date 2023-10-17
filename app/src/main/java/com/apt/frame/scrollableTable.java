package com.apt.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class scrollableTable extends JPanel {
    
    JTable table;

    public scrollableTable() {
        super();

        // Create JTable
        DefaultTableModel model = new DefaultTableModel();      
        table = new JTable(model);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        // Add Columns
        model.addColumn("#");
        model.addColumn("Product Name");
        model.addColumn("Original Price");
        model.addColumn("Current Price");
        model.addColumn("Historic Low");
        model.addColumn("Delete");

        // Set Column Width
        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(420);
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setPreferredWidth(140);
        table.getColumnModel().getColumn(4).setPreferredWidth(140);
        table.getColumnModel().getColumn(5).setPreferredWidth(70);
        table.getColumnModel().setColumnMargin(0);

        // Set Row Height
        table.setRowHeight(70);
        table.setRowMargin(0);

        // Add JTable to ScrollablePane
        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setPreferredSize(new Dimension(980, 770));
        this.add(scrollTable, BorderLayout.CENTER);
    }

    public void updateData() {
        // The Singleton will handle all data operations
        System.out.println("Updated Data!");
    }
}
