package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

// This class is responsible only for rendering cells, following SRP
public class BoardCellRenderer extends DefaultTableCellRenderer {
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color GRID_COLOR = new Color(255, 255, 0, 80); // Semi-transparent yellow

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        // Set background color of cells
        label.setBackground(BACKGROUND_COLOR);
        label.setBorder(BorderFactory.createLineBorder(GRID_COLOR, 1)); // Thin border
        label.setText(""); // Empty text

        // Center the content
        label.setHorizontalAlignment(SwingConstants.CENTER);

        return label;
    }
}