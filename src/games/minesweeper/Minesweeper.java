package games.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Minesweeper extends JFrame {
    private static final int SIZE = 10;
    private static final int MINES = 10;
    private JPanel panel;
    private JButton[][] buttons;
    private boolean[][] mines;
    private boolean[][] revealed;
    private boolean[][] flagged;

    public Minesweeper() {
        setTitle("Minesweeper");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new GridLayout(SIZE, SIZE));
        buttons = new JButton[SIZE][SIZE];
        mines = new boolean[SIZE][SIZE];
        revealed = new boolean[SIZE][SIZE];
        flagged = new boolean[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].addMouseListener(new ButtonMouseListener(i, j));
                panel.add(buttons[i][j]);
            }
        }

        add(panel, BorderLayout.CENTER);
        placeMines();
    }

    private void placeMines() {
        Random random = new Random();
        int placedMines = 0;

        while (placedMines < MINES) {
            int x = random.nextInt(SIZE);
            int y = random.nextInt(SIZE);

            if (!mines[x][y]) {
                mines[x][y] = true;
                placedMines++;
            }
        }
    }


    public static int getCurrentSize() {
        return SIZE;
    }

    public static int getMines() {
        return MINES;
    }
    public JButton[][] getButtons() {
        return buttons;
    }

    public boolean[][] getMinesArray() {
        return mines;
    }

    public boolean[][] getRevealed() {
        return revealed;
    }

    public boolean[][] getFlagged() {
        return flagged;
    }


    private class ButtonMouseListener extends MouseAdapter {
        private int x;
        private int y;

        public ButtonMouseListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                if (!revealed[x][y]) {
                    flagged[x][y] = !flagged[x][y];
                    buttons[x][y].setText(flagged[x][y] ? "F" : "");
                }
            } else if (SwingUtilities.isLeftMouseButton(e)) {
                if (flagged[x][y]) {
                    return;
                }
                if (mines[x][y]) {
                    buttons[x][y].setText("X");
                    buttons[x][y].setBackground(Color.RED);
                    revealAllMines();
                    JOptionPane.showMessageDialog(null, "Game Over!");
                } else {
                    reveal(x, y);
                    checkWinCondition();
                }
            }
        }
    }

    public void reveal(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE || revealed[x][y]) {
            return;
        }

        revealed[x][y] = true;
        buttons[x][y].setEnabled(false);
        int mineCount = countAdjacentMines(x, y);

        if (mineCount > 0) {
            buttons[x][y].setText(String.valueOf(mineCount));
        } else {
            buttons[x][y].setText("");
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        reveal(x + i, y + j);
                    }
                }
            }
        }
    }


    public int countAdjacentMines(int x, int y) {
        int count = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nx = x + i;
                int ny = y + j;

                if (nx >= 0 && nx < SIZE && ny >= 0 && ny < SIZE && mines[nx][ny]) {
                    count++;
                }
            }
        }

        return count;
    }


    private void revealAllMines() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (mines[i][j]) {
                    buttons[i][j].setText("X");
                    buttons[i][j].setBackground(Color.RED);
                }
            }
        }
    }
    public void getRevealAllMines() {
        revealAllMines();
    }

    private void checkWinCondition() {
        int revealedCells = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (revealed[i][j]) {
                    revealedCells++;
                }
            }
        }


        if (revealedCells == SIZE * SIZE - MINES) {
            JOptionPane.showMessageDialog(null, "You Win!");
        }
    }
    public void getCheckWinCondition() {
        checkWinCondition();
    }

    public JPanel getPanel()
    {
        return panel;
    }

}