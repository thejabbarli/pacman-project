package model;

import javax.swing.table.AbstractTableModel;

public class BoardModel extends AbstractTableModel {
    private Object[][] data;
    private int rows;
    private int columns;
    private GameMap gameMap;

    public BoardModel(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new Object[rows][columns];
        this.gameMap = new GameMap(rows, columns);

        // Initialize empty board
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                data[i][j] = null; // Empty cell
            }
        }
    }

    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public int getColumnCount() {
        return columns;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        data[rowIndex][columnIndex] = value;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false; // Cells are not editable
    }

    public GameMap getGameMap() {
        return gameMap;
    }
}