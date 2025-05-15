package view;

import model.BoardModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class GameView extends JPanel {
    private JTable boardTable;
    private BoardModel boardModel;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JLabel livesLabel;

    public GameView(int rows, int columns) {
        setLayout(new BorderLayout());

        // Initialize the board model
        boardModel = new BoardModel(rows, columns);

        // Create the game board table
        boardTable = new JTable(boardModel);
        boardTable.setDefaultRenderer(Object.class, new BoardCellRenderer());

        // Configure grid appearance
        boardTable.setShowGrid(true);
        boardTable.setGridColor(new Color(255, 255, 0, 80)); // Semi-transparent yellow for thinner appearance
        boardTable.setIntercellSpacing(new Dimension(0, 0)); // Minimal space between cells

        // Disable cell selection
        boardTable.setCellSelectionEnabled(false);
        boardTable.setRowSelectionAllowed(false);
        boardTable.setColumnSelectionAllowed(false);
        boardTable.setFocusable(false); // Remove focus border

        // Remove table header
        boardTable.setTableHeader(null);

        // Create a panel for game stats
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setBackground(Color.BLACK);

        // Add score, time, and lives labels with larger font
        Font statsFont = new Font("Arial", Font.BOLD, 16);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(statsFont);

        timeLabel = new JLabel("Time: 0");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(statsFont);

        livesLabel = new JLabel("Lives: 3");
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setFont(statsFont);

        statsPanel.add(scoreLabel);
        statsPanel.add(Box.createHorizontalStrut(20)); // Add spacing
        statsPanel.add(timeLabel);
        statsPanel.add(Box.createHorizontalStrut(20)); // Add spacing
        statsPanel.add(livesLabel);

        // Add components to the panel
        add(statsPanel, BorderLayout.NORTH);

        // Add table in a panel that will handle resizing
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.BLACK);
        tablePanel.add(boardTable, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Set the background color
        setBackground(Color.BLACK);

        // Add component listener to handle resizing
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resizeTable();
            }
        });
    }

    // Resize table cells when the container is resized
    private void resizeTable() {
        int width = getWidth();
        int height = getHeight() - 40; // Account for stats panel

        // Determine cell size
        int cellWidth = width / boardModel.getColumnCount();
        int cellHeight = height / boardModel.getRowCount();

        // Set row heights
        for (int i = 0; i < boardTable.getRowCount(); i++) {
            boardTable.setRowHeight(i, cellHeight);
        }

        // Set column widths
        TableColumnModel columnModel = boardTable.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(cellWidth);
        }

        // Update UI
        boardTable.revalidate();
        boardTable.repaint();
    }

    // Cell renderer for the game board
    private class BoardCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            // Set background color of cells
            label.setBackground(Color.BLACK);
            label.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 0, 80), 1)); // Thinner border
            label.setText(""); // Empty text

            // Center the content
            label.setHorizontalAlignment(SwingConstants.CENTER);

            return label;
        }
    }

    public BoardModel getBoardModel() {
        return boardModel;
    }

    @Override
    public Dimension getPreferredSize() {
        // Set a default size twice as large
        return new Dimension(800, 600);
    }
}