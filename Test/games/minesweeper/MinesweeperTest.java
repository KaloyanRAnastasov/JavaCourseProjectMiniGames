package games.minesweeper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MinesweeperTest {
    private Minesweeper minesweeper;

    @BeforeEach
    public void setUp() {
        minesweeper = new Minesweeper();

    }
    //Setters
    JButton[][] buttons = minesweeper.getButtons();
    boolean[][] flagged = minesweeper.getFlagged();
    boolean[][] mines = minesweeper.getMinesArray();
    boolean[][] revealed = minesweeper.getRevealed();

    @Test
    public void testPanelNotNull() {
        assertNotNull(minesweeper.getPanel());
    }

    @Test
    public void testInitialButtonState() {
        JPanel panel = minesweeper.getPanel();
        assertEquals(100, panel.getComponentCount());
        for (Component component : panel.getComponents()) {
            assertTrue(component instanceof JButton);
            JButton button = (JButton) component;
            assertTrue(button.isEnabled());
            assertEquals("", button.getText());
        }
    }

    @Test
    public void testMinesPlaced() {
        boolean[][] mines = new boolean[10][10];
        int mineCount = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (mines[i][j]) {
                    mineCount++;
                }
            }
        }
        assertEquals(10, mineCount);
    }

    @Test
    public void testMouseClickRightFlag() {

        buttons[0][0].doClick(MouseEvent.BUTTON3_MASK);
        assertEquals("F", buttons[0][0].getText());
        assertTrue(flagged[0][0]);

        buttons[0][0].doClick(MouseEvent.BUTTON3_MASK);
        assertEquals("", buttons[0][0].getText());
        assertFalse(flagged[0][0]);
    }

    @Test
    public void testMouseClickLeftReveal() {
        mines[0][0] = false;
        buttons[0][0].doClick();
        assertFalse(buttons[0][0].isEnabled());
        assertTrue(revealed[0][0]);
    }

    @Test
    public void testMouseClickLeftMine() {
        mines[0][0] = true;
        buttons[0][0].doClick();
        assertEquals("X", buttons[0][0].getText());
        assertEquals(Color.RED, buttons[0][0].getBackground());
    }

    @Test
    public void testRevealAllMines() {
        mines[0][0] = true;
        mines[1][1] = true;
        minesweeper.getRevealAllMines();
        assertEquals("X", buttons[0][0].getText());
        assertEquals("X", buttons[1][1].getText());
        assertEquals(Color.RED, buttons[0][0].getBackground());
        assertEquals(Color.RED, buttons[1][1].getBackground());
    }

    @Test
    public void testCheckWinCondition() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                revealed[i][j] = !mines[i][j];
            }
        }
        minesweeper.getCheckWinCondition();
        // As JOptionPane.showMessageDialog is used for win notification,
        // we cannot easily assert its behavior in a test. This line is more for
        // ensuring coverage.
    }

    @Test
    public void testCountAdjacentMines() {
        mines[1][1] = true;
        mines[2][2] = true;
        assertEquals(2, minesweeper.getCountAdjacentMines(1, 2));
    }

    @Test
    public void testRevealMethod() {
        mines[1][1] = false;
        minesweeper.getReveal(1, 1);
        assertTrue(revealed[1][1]);
        assertFalse(buttons[1][1].isEnabled());
    }
}
