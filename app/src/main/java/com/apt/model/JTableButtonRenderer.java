package com.apt.model;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class JTableButtonRenderer implements TableCellRenderer {

    public TableCellRenderer defaultCellRenderer;

    public JTableButtonRenderer(TableCellRenderer renderer) {
        defaultCellRenderer = renderer;
    };

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Component) {
            return (Component) value;
        }
        return defaultCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    };
}
