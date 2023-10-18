package com.apt.model;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

public class JTableButtonEditor implements TableCellEditor {

    private Object editorValue;

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean hasFocus, int row, int column) {
		editorValue = value;
        return (Component) value;
    }

    @Override
    public Object getCellEditorValue() {
        return editorValue;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        return true;
    }

    @Override
    public void cancelCellEditing() {}

    @Override
    public void addCellEditorListener(CellEditorListener l) {}

    @Override
    public void removeCellEditorListener(CellEditorListener l) {}
}
