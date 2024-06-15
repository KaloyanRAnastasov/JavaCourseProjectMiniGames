package games.connect4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Connect4 {
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final char EMPTY_SLOT = ' ';
    private final char[][] board;
    private final JPanel boardPanel;
    private char currentPlayer;

    public Connect4() {
        board = new char[ROWS][COLUMNS];
        boardPanel = new JPanel(new GridLayout(ROWS, COLUMNS));
        currentPlayer = 'R';

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
            JButton columnButton = createStyledButton("Drop", i);
            topPanel.add(columnButton);
        }
        return topPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton returnButton = createStyledButton("Return", -1);
        returnButton.addActionListener(this::onReturnClicked);
        bottomPanel.add(returnButton);

        JButton resetButton = createStyledButton("Reset", -1);
        resetButton.addActionListener(this::onResetClicked);
        bottomPanel.add(resetButton);
        return bottomPanel;
    }

    private JButton createStyledButton(String text, int column) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 50));
        button.setFont(new Font("Times New Roman", Font.BOLD, 16));
        button.setBackground(Color.LIGHT_GRAY);
        button.setOpaque(true);
        button.setBorderPainted(false);
        if (column >= 0) {
            button.addActionListener(new ColumnButtonListener(column));
        }
        return button;
    }

    public void initializeBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j] = EMPTY_SLOT;
            }
        }
    }

    public void updateBoard() {
        boardPanel.removeAll();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                JPanel cell = createCell(row, col);
                boardPanel.add(cell);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private JPanel createCell(int row, int col) {
        JPanel cell = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (board[row][col] == 'R') {
                    g.setColor(Color.RED);
                } else if (board[row][col] == 'Y') {
                    g.setColor(Color.BLUE);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillOval(10, 10, getWidth() - 20, getHeight() - 20);
            }
        };
        cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return cell;
    }

    public boolean placePiece(int column) {
        for (int i = ROWS - 1; i >= 0; i--) {
            if (board[i][column] == EMPTY_SLOT) {
                board[i][column] = currentPlayer;
                return true;
            }
        }
        return false;
    }

    public boolean checkWin(char player) {
        return checkHorizontalWin(player) || checkVerticalWin(player) || checkDiagonalWin(player);
    }

    private boolean checkHorizontalWin(char player) {
        return checkWin(player, 0, 1);
    }

    private boolean checkVerticalWin(char player) {
        return checkWin(player, 1, 0);
    }

    private boolean checkDiagonalWin(char player) {
        return checkWin(player, 1, 1) || checkWin(player, 1, -1);
    }

    private boolean checkWin(char player, int rowIncrement, int colIncrement) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (checkDirection(player, i, j, rowIncrement, colIncrement)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDirection(char player, int row, int col, int rowIncrement, int colIncrement) {
        int count = 0;
        for (int k = 0; k < 4; k++) {
            int newRow = row + k * rowIncrement;
            int newCol = col + k * colIncrement;
            if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLUMNS && board[newRow][newCol] == player) {
                count++;
            } else {
                break;
            }
        }
        return count == 4;
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
                    currentPlayer = (currentPlayer == 'R') ? 'Y' : 'R';
                }
            } else {
                JOptionPane.showMessageDialog(null, "Column is full. Try another column.");
            }
        }
    }

    void resetGame() {
        initializeBoard();
        updateBoard();
        currentPlayer = 'R';
    }

    private void onResetClicked(ActionEvent e) {
        resetGame();
        System.out.println("Game reset.");
    }

    private void onReturnClicked(ActionEvent e) {
        // Logic to switch back to the main menu
        System.out.println("Return to main menu.");
        // Add logic to close or navigate
    }

    public JPanel getPanel() {
        return boardPanel;
    }

    public String getTitle() {
        return "Connect 4";
    }

    public ImageIcon getIcon() {
        String imagePath = "/games/connect4/logo/Connect4Logo.png";
        java.net.URL imgURL = getClass().getResource(imagePath);
        assert imgURL != null;
        return new ImageIcon(imgURL);
    }
}
