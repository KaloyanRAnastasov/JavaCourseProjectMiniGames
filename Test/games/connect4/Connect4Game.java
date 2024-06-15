package games.connect4;

import game.Game;
import game.GameEndListener;
import game.GameStartListener;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Connect4Game extends Game {
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final char EMPTY_SLOT = ' ';
    private char[][] board = new char[ROWS][COLUMNS];
    private JButton[] columnButtons = new JButton[COLUMNS];
    private JPanel boardPanel = new JPanel(new GridLayout(ROWS, COLUMNS));
    private JPanel panel;
    private char currentPlayer = 'R';

    private GameEndListener endListener = new GameEndListener() {
        @Test
        @Override
        public void endGame() {
            System.out.println("endGame event");
            // Here is a code to dispose game resources
        }
    };

    private GameStartListener startListener = new GameStartListener() {
        @Test
        @Override
        public void startGame() {
            System.out.println("startGame event");
            // Here is a code to init game and game logic and to load resources
            initializeBoard();
            updateBoard();
        }
    };

    public Connect4Game() {
        panel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(1, COLUMNS));
        for (int i = 0; i < COLUMNS; i++) {
            JButton button = createStyledButton(String.valueOf(i), i);
            topPanel.add(button);
        }

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(boardPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton returnButton = createStyledButton("Return", -1);
        returnButton.addActionListener(this::onReturnClicked);
        bottomPanel.add(returnButton);

        JButton resetButton = createStyledButton("Reset", -1);
        resetButton.addActionListener(this::onResetClicked);
        bottomPanel.add(resetButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        setStartListener(startListener);
        setEndListener(endListener);
    }

    @Test
    private JButton createStyledButton(String text, int column) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 50));
        button.setFont(new Font("Times New Roman", Font.BOLD, 16)); // Reduced font size to 16
        button.setBackground(Color.LIGHT_GRAY);
        button.setOpaque(true);
        button.setBorderPainted(false);
        if (column >= 0) {
            button.addActionListener(new ColumnButtonListener(column));
        }
        return button;
    }

    @Test
    private void initializeBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j] = EMPTY_SLOT;
            }
        }
    }

    @Test
    private void updateBoard() {
        boardPanel.removeAll();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                final int currentRow = row;
                final int currentCol = col;
                JPanel cell = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        if (board[currentRow][currentCol] == 'R') {
                            g.setColor(Color.RED);
                        } else if (board[currentRow][currentCol] == 'Y') {
                            g.setColor(Color.BLUE);
                        } else {
                            g.setColor(Color.WHITE);
                        }
                        g.fillOval(10, 10, getWidth() - 20, getHeight() - 20);
                    }
                };
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                boardPanel.add(cell);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    @Test
    private boolean placePiece(int column) {
        for (int i = ROWS - 1; i >= 0; i--) {
            if (board[i][column] == EMPTY_SLOT) {
                board[i][column] = currentPlayer;
                return true;
            }
        }
        return false;
    }

    @Test
    private boolean checkWin(char player) {
        // Check horizontal win
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS - 3; j++) {
                if (board[i][j] == player && board[i][j + 1] == player && board[i][j + 2] == player && board[i][j + 3] == player) {
                    return true;
                }
            }
        }

        // Check vertical win
        for (int i = 0; i < ROWS - 3; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (board[i][j] == player && board[i + 1][j] == player && board[i + 2][j] == player && board[i + 3][j] == player) {
                    return true;
                }
            }
        }

        // Check diagonal win (bottom-left to top-right)
        for (int i = 3; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS - 3; j++) {
                if (board[i][j] == player && board[i - 1][j + 1] == player && board[i - 2][j + 2] == player && board[i - 3][j + 3] == player) {
                    return true;
                }
            }
        }

        // Check diagonal win (top-left to bottom-right)
        for (int i = 0; i < ROWS - 3; i++) {
            for (int j = 0; j < COLUMNS - 3; j++) {
                if (board[i][j] == player && board[i + 1][j + 1] == player && board[i + 2][j + 2] == player && board[i + 3][j + 3] == player) {
                    return true;
                }
            }
        }

        return false;
    }

    private class ColumnButtonListener implements ActionListener {
        private int column;

        public ColumnButtonListener(int column) {
            this.column = column;
        }

        @Test
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

    @Test
    private void resetGame() {
        initializeBoard();
        updateBoard();
        currentPlayer = 'R';
    }

    @Test
    private void onResetClicked(ActionEvent e) {
        resetGame();
        System.out.println("Game reset.");
    }

    @Test
    private void onReturnClicked(ActionEvent e) {
        // Logic to switch back to the main menu
        System.out.println("Return to main menu.");
        notifyClose();
    }

    @Test
    @Override
    public JPanel getGamePanel() {
        return panel;
    }

    @Test
    @Override
    public String getGameName() {
        return "Connect 4";
    }

    @Test
    @Override
    public ImageIcon getGameIcon() {
        String imagePath = "/games/connect4/logo/Connect4Logo.png";
        java.net.URL imgURL = getClass().getResource(imagePath);
        assert imgURL != null;
        return new ImageIcon(imgURL);
    }

    @Test
    @Override
    public int getPreferredWidth() {
        return 1024;
    }

    @Test
    @Override
    public int getPreferredHeight() {
        return 768;
    }
}
