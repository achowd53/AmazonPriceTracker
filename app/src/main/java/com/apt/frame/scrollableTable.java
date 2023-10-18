package com.apt.frame;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.apt.data.APTDataSingleton;
import com.apt.model.JTableButtonEditor;
import com.apt.model.JTableButtonModel;
import com.apt.model.JTableButtonRenderer;

public class scrollableTable extends JPanel {
    
    JTable table;
    JTableButtonModel model;

    public scrollableTable() {
        super();

        // Create String Cell Renderer
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Create JTable and Add Renderer
        model = new JTableButtonModel();
        table = new JTable(model);
        table.setDefaultRenderer(JButton.class, new JTableButtonRenderer(table.getDefaultRenderer(JButton.class)));
        table.setDefaultRenderer(String.class, centerRenderer);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(false);

        // Add Editor
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setCellEditor(new JTableButtonEditor());
        columnModel.getColumn(5).setCellEditor(new JTableButtonEditor());

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

    public void updateTable(boolean refreshData) {
        // Use Singleton to get AWS and Table Data
        APTDataSingleton dataCore = APTDataSingleton.getInstance();
        if (refreshData) {
            dataCore.refreshData();
        }
        Object[][] data = dataCore.getTableData();
        // Add Button Listeners to delete and link buttons
        for (int i = 0; i < data.length; i++) {
            final int j = i;
            ((JButton) data[i][0]).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { 
                    APTDataSingleton dataCore = APTDataSingleton.getInstance();
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            URI uri = new URI(dataCore.links[j]);
                            desktop.browse(uri);
                        } catch (Exception excp) {
                            excp.printStackTrace();
                        }
                    }
                } 
            });
            ((JButton) data[i][5]).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { 
                    APTDataSingleton dataCore = APTDataSingleton.getInstance();
                    dataCore.deleteEntry(dataCore.links[j]);
                    updateTable(false);
                } 
            });
        }
        // Put data into table
        JTableButtonModel model = (JTableButtonModel) table.getModel();
        model.setData(data);
        model.fireTableDataChanged();
    }
}