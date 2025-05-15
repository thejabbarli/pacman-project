package view;

import javax.swing.*;
import java.awt.*;

/**
 * Renders the grid background
 */
public class GridRenderer extends JComponent {
    private int rows;
    private int columns;
    private int cellSize;

    public GridRenderer(int rows, int columns, int cellSize) {
        this.rows = rows;
        this.columns = columns;
        this.cellSize = cellSize;
        setPreferredSize(new Dimension(columns * cellSize, rows * cellSize));
    }

    public void updateCellSize(int newCellSize) {
        this.cellSize = newCellSize;
        setPreferredSize(new Dimension(columns * cellSize, rows * cellSize));
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw grid lines
        g2d.setColor(new Color(255, 255, 0, 50)); // Semi-transparent yellow

        // Draw vertical lines
        for (int i = 0; i <= columns; i++) {
            int x = i * cellSize;
            g2d.drawLine(x, 0, x, rows * cellSize);
        }

        // Draw horizontal lines
        for (int i = 0; i <= rows; i++) {
            int y = i * cellSize;
            g2d.drawLine(0, y, columns * cellSize, y);
        }

        g2d.dispose();
    }
}