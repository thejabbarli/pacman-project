package view;

import model.BoardModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.logging.Logger;

public class GameView extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(GameView.class.getName());

    private JTable boardTable;
    private BoardModel boardModel;
    private JPanel statsPanel;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JLabel livesLabel;

    public GameView(int rows, int columns) {
        // Use BorderLayout with zero gaps
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.BLACK);
        setBorder(null); // Explicitly remove any border

        // Initialize the board model
        boardModel = new BoardModel(rows, columns);

        // Create stats panel
        createStatsPanel();

        // Create game board
        createGameBoard(rows, columns);

        // Add resizing listener
        setupResizeListener();
    }

    private void createStatsPanel() {
        statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        statsPanel.setBackground(Color.BLACK);
        statsPanel.setBorder(null); // Explicitly remove any border

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
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(timeLabel);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(livesLabel);

        add(statsPanel, BorderLayout.NORTH);
    }

    private void createGameBoard(int rows, int columns) {
        // Create the game board table
        boardTable = new JTable(boardModel);
        boardTable.setDefaultRenderer(Object.class, new BoardCellRenderer());

        // Configure grid appearance
        boardTable.setShowGrid(true);
        boardTable.setGridColor(new Color(255, 255, 0, 80));
        boardTable.setIntercellSpacing(new Dimension(0, 0));

        // Remove all borders and margins
        boardTable.setBorder(null);
        boardTable.setBackground(Color.BLACK);

        // Disable cell selection
        boardTable.setCellSelectionEnabled(false);
        boardTable.setRowSelectionAllowed(false);
        boardTable.setColumnSelectionAllowed(false);
        boardTable.setFocusable(false);

        // Remove table header
        boardTable.setTableHeader(null);

        // Set initial row heights
        int initialCellSize = GameDimensions.calculateCellSize(
                getPreferredSize().width,
                getPreferredSize().height - GameDimensions.getStatsHeight(),
                rows,
                columns
        );

        for (int i = 0; i < rows; i++) {
            boardTable.setRowHeight(i, initialCellSize);
        }

        // Set initial column widths
        TableColumnModel columnModel = boardTable.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(initialCellSize);
            columnModel.getColumn(i).setMinWidth(5);
        }

        // Create a panel to hold the table with specific layout
        JPanel boardPanel = new JPanel(new GridLayout(1, 1));
        boardPanel.setBackground(Color.BLACK);
        boardPanel.setBorder(null); // Explicitly remove any border
        boardPanel.add(boardTable);

        add(boardPanel, BorderLayout.CENTER);
    }

    private void setupResizeListener() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                safelyResizeTable();
            }
        });
    }

    private void safelyResizeTable() {
        try {
            int width = getWidth();
            int height = getHeight() - statsPanel.getHeight();
            int rows = boardModel.getRowCount();
            int columns = boardModel.getColumnCount();

            int cellSize = Math.max(Math.min(width / columns, height / rows), 5);

            // Update row heights
            for (int i = 0; i < boardTable.getRowCount(); i++) {
                boardTable.setRowHeight(i, cellSize);
            }

            // Update column widths
            TableColumnModel columnModel = boardTable.getColumnModel();
            for (int i = 0; i < columnModel.getColumnCount(); i++) {
                columnModel.getColumn(i).setPreferredWidth(cellSize);
            }

            // Force repaint
            revalidate();
            repaint();
        } catch (Exception ex) {
            LOGGER.warning("Error resizing table: " + ex.getMessage());
        }
    }

    public BoardModel getBoardModel() {
        return boardModel;
    }

    @Override
    public Dimension getPreferredSize() {
        return GameDimensions.getInitialWindowSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return GameDimensions.getMinimumWindowSize();
    }

    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
    }

    public void updateTime(int seconds) {
        timeLabel.setText("Time: " + seconds);
    }

    public void updateLives(int lives) {
        livesLabel.setText("Lives: " + lives);
    }
}