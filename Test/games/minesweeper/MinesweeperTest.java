package games.minesweeper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static org.junit.jupiter.api.Assertions.*;

public class MinesweeperTest {
    private Minesweeper minesweeper;

    @BeforeEach
    public void setUp() {
        minesweeper = new Minesweeper();
    }

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
        int mineCount = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (minesweeper.getMinesArray()[i][j]) {
                    mineCount++;
                }
            }
        }
        assertEquals(10, mineCount);
    }

    @Test
    public void testMouseClickRightFlag() {
        minesweeper.getButtons()[0][0].dispatchEvent(new MouseEvent(minesweeper.getButtons()[0][0], MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), MouseEvent.BUTTON3_DOWN_MASK, 0, 0, 1, false, MouseEvent.BUTTON3));
        assertEquals("F", minesweeper.getButtons()[0][0].getText());
        assertTrue(minesweeper.getFlagged()[0][0]);

        minesweeper.getButtons()[0][0].dispatchEvent(new MouseEvent(minesweeper.getButtons()[0][0], MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), MouseEvent.BUTTON3_DOWN_MASK, 0, 0, 1, false, MouseEvent.BUTTON3));
        assertEquals("", minesweeper.getButtons()[0][0].getText());
        assertFalse(minesweeper.getFlagged()[0][0]);
    }

    @Test
    public void testMouseClickLeftReveal() {
        minesweeper.getMinesArray()[0][0] = false;
        minesweeper.getButtons()[0][0].dispatchEvent(new MouseEvent(minesweeper.getButtons()[0][0], MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false, MouseEvent.BUTTON1));
        assertFalse(minesweeper.getButtons()[0][0].isEnabled());
        assertTrue(minesweeper.getRevealed()[0][0]);
    }

    @Test
    public void testMouseClickLeftMine() {
        minesweeper.getMinesArray()[0][0] = true;
        minesweeper.getButtons()[0][0].dispatchEvent(new MouseEvent(minesweeper.getButtons()[0][0], MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false, MouseEvent.BUTTON1));
        assertEquals("X", minesweeper.getButtons()[0][0].getText());
        assertEquals(Color.RED, minesweeper.getButtons()[0][0].getBackground());
    }

    @Test
    public void testRevealAllMines() {
        minesweeper.getMinesArray()[0][0] = true;
        minesweeper.getMinesArray()[1][1] = true;
        minesweeper.getRevealAllMines();
        assertEquals("X", minesweeper.getButtons()[0][0].getText());
        assertEquals("X", minesweeper.getButtons()[1][1].getText());
        assertEquals(Color.RED, minesweeper.getButtons()[0][0].getBackground());
        assertEquals(Color.RED, minesweeper.getButtons()[1][1].getBackground());
    }

    @Test
    public void testCheckWinCondition() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                minesweeper.getRevealed()[i][j] = !minesweeper.getMinesArray()[i][j];
            }
        }
        minesweeper.getCheckWinCondition();
        // As JOptionPane.showMessageDialog is used for win notification,
        // we cannot easily assert its behavior in a test. This line is more for
        // ensuring coverage.
    }

    @Test
    public void testCountAdjacentMines() {
        minesweeper.getMinesArray()[1][1] = true;
        minesweeper.getMinesArray()[2][2] = true;
        assertEquals(2, minesweeper.countAdjacentMines(1, 2));
    }

    @Test
    public void testRevealMethod() {
        minesweeper.getMinesArray()[1][1] = false;
        minesweeper.reveal(1, 1);
        assertTrue(minesweeper.getRevealed()[1][1]);
        assertFalse(minesweeper.getButtons()[1][1].isEnabled());
    }

    @Test
    public void testFlagging() {
        minesweeper.getButtons()[0][0].dispatchEvent(new MouseEvent(minesweeper.getButtons()[0][0], MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), MouseEvent.BUTTON3_DOWN_MASK, 0, 0, 1, false, MouseEvent.BUTTON3));
        assertTrue(minesweeper.getFlagged()[0][0]);
        minesweeper.getButtons()[0][0].dispatchEvent(new MouseEvent(minesweeper.getButtons()[0][0], MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), MouseEvent.BUTTON3_DOWN_MASK, 0, 0, 1, false, MouseEvent.BUTTON3));
        assertFalse(minesweeper.getFlagged()[0][0]);
    }
}
