package games.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Minesweeper extends JFrame {
    private static final int sizeOfSide = 10;
    private static final int minesNumber = 25;
    private final JPanel panel;
    private final JButton[][] buttons;
    private final boolean[][] mines;
    private final boolean[][] revealed;
    private final boolean[][] flagged;

    int sizeSquare = sizeOfSide * sizeOfSide;



    public Minesweeper() {
        setTitle("Minesweeper");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new GridLayout(sizeOfSide, sizeOfSide));
        buttons = new JButton[sizeOfSide][sizeOfSide];
        mines = new boolean[sizeOfSide][sizeOfSide];
        revealed = new boolean[sizeOfSide][sizeOfSide];
        flagged = new boolean[sizeOfSide][sizeOfSide];

        for (int i = 0; i < sizeOfSide; i++) {
            for (int j = 0; j < sizeOfSide; j++) {
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
        if(minesNumber >= sizeSquare) {
            throw new IllegalArgumentException("Number of mines is greater than the number of cells");
        }


        while (placedMines < minesNumber) {
            int x = random.nextInt(sizeOfSide);
            int y = random.nextInt(sizeOfSide);

            if (!mines[x][y]) {
                mines[x][y] = true;
                placedMines++;
            }
        }
    }


    public static int getCurrentSize() {
        return sizeOfSide;
    }

    public static int getMines() {
        return minesNumber;
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
        private final int x;
        private final int y;

        public ButtonMouseListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                if (!revealed[x][y]) {
                    flagged[x][y] = !flagged[x][y];
                    buttons[x][y].setBackground(flagged[x][y] ? Color.yellow : null);
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
        if (x < 0 || x >= sizeOfSide || y < 0 || y >= sizeOfSide || revealed[x][y]) {
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

                if (nx >= 0 && nx < sizeOfSide && ny >= 0 && ny < sizeOfSide && mines[nx][ny]) {
                    count++;
                }
            }
        }

        return count;
    }


    private void revealAllMines() {
        for (int i = 0; i < sizeOfSide; i++) {
            for (int j = 0; j < sizeOfSide; j++) {
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
        for (int i = 0; i < sizeOfSide; i++) {
            for (int j = 0; j < sizeOfSide; j++) {
                if (revealed[i][j]) {
                    revealedCells++;
                }
            }
        }


        if (revealedCells == sizeOfSide * sizeOfSide - minesNumber) {
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