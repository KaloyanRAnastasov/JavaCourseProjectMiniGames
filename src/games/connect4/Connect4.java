package games.connect4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Connect4 {
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final String EMPTY_SLOT = " ";
    private final String[][] board;
    private final JPanel boardPanel;
    private String currentPlayer;

    public Connect4() {
        board = new String[ROWS][COLUMNS];
        boardPanel = new JPanel(new GridLayout(ROWS, COLUMNS));
        currentPlayer = "Red";

        initializeBoard();
        updateBoard();
    }

    public JPanel createGamePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel topPanel = createTopPanel();
        panel.add(topPanel, BorderLayout.NORTH);

        panel.add(boardPanel, BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel();
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new GridLayout(1, COLUMNS));
        for (int i = 0; i < COLUMNS; i++) {
            JButton columnButton = createButton("Drop", i);
            topPanel.add(columnButton);
        }
        return topPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton resetButton = createButton("Reset", -1);
        resetButton.addActionListener(this::resetGame);
        bottomPanel.add(resetButton);
        return bottomPanel;
    }

    private JButton createButton(String text, int column) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 50));
        button.setFont(new Font("Times New Roman", Font.BOLD, 16));
        button.setBackground(Color.LIGHT_GRAY);
        if (column >= 0) {
            button.addActionListener(new ColumnButtonListener(column));
        }
        return button;
    }

    void initializeBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j] = EMPTY_SLOT;
            }
        }
    }

    void updateBoard() {
        boardPanel.removeAll();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                boardPanel.add(createCellButton(row, col));
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private JButton createCellButton(int row, int col) {
        JButton cellButton = new JButton();
        cellButton.setEnabled(false);
        cellButton.setBackground(switch (board[row][col]) {
            case "Red" -> Color.RED;
            case "Blue" -> Color.BLUE;
            default -> Color.WHITE;
        });

        return cellButton;
    }

    public boolean placePiece(int column) {
        for (int i = ROWS - 1; i >= 0; i--) {
            if (board[i][column].equals(EMPTY_SLOT)) {
                board[i][column] = currentPlayer;
                return true;
            }
        }
        return false;
    }

    public boolean checkWin(String player) {
        return checkWinDirection(player, 0, 1) || checkWinDirection(player, 1, 0) || checkWinDirection(player, 1, 1) || checkWinDirection(player, 1, -1);
    }

    private boolean checkWinDirection(String player, int rowIncrement, int colIncrement) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (checkDirection(player, i, j, rowIncrement, colIncrement)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDirection(String player, int row, int col, int rowIncrement, int colIncrement) {
        for (int k = 0; k < 4; k++) {
            int newRow = row + k * rowIncrement;
            int newCol = col + k * colIncrement;
            if (newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= COLUMNS || !board[newRow][newCol].equals(player)) {
                return false;
            }
        }
        return true;
    }

    private class ColumnButtonListener implements ActionListener {
        private final int column;

        public ColumnButtonListener(int column) {
            this.column = column;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (placePiece(column)) {
                updateBoard();
                if (checkWin(currentPlayer)) {
                    JOptionPane.showMessageDialog(null, "Player " + currentPlayer + " wins!");
                    resetGame();
                } else {
                    currentPlayer = currentPlayer.equals("Red") ? "Blue" : "Red";
                }
            } else {
                JOptionPane.showMessageDialog(null, "Column is full. Try another column.");
            }
        }
    }

    private void resetGame(ActionEvent e) {
        resetGame();
    }

    public void resetGame() {
        initializeBoard();
        updateBoard();
        currentPlayer = "Red";
    }

    public JPanel getPanel() {
        return boardPanel;
    }

    public String getTitle() {
        return "Connect 4";
    }

    public ImageIcon getIcon() {
        String imagePath = "/games/connect4/resources/Connect4Logo.png";
        java.net.URL imgURL = getClass().getResource(imagePath);
        assert imgURL != null;
        return new ImageIcon(imgURL);
    }
}
