package com.apt.model;

import javax.swing.table.AbstractTableModel;

public class JTableButtonModel extends AbstractTableModel {
    private Object[][] rows = {};
    private String[] columns = {"#", "Product Name", "Original Price", "Current Price", "Historic Low", "Delete"};

    @Override
    public int getRowCount() {
        return rows.length;
    };

    @Override
    public int getColumnCount() {
        return columns.length;
    };

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rows[rowIndex][columnIndex];
    };

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 0 || column == 5;
    };

    @Override
    public Class<? extends Object> getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    };

    @Override
    public String getColumnName(int column) {
        return columns[column];
    };

    public void setData(Object[][] data) {
        this.rows = data;
    }
        
}
