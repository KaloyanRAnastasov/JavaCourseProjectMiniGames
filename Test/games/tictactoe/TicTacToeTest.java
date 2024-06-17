package games.tictactoe;

import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

import static org.junit.Assert.*;

public class TicTacToeTest  {
    private TicTacToe ticTacToe;

    @Before
    public void setUp() {
        ticTacToe = new TicTacToe();
    }

    @Test
    public void testFirstTurn() {
        assertTrue(ticTacToe.isPlayer_turn());
    }

    @Test
    public void testCheckForDraw() {
        assertFalse(ticTacToe.checkForDraw());
    }

    @Test
    public void testPauseGame() {
        ticTacToe.pauseGame();
        assertTrue(ticTacToe.isGamePaused());
    }

    @Test
    public void testResumeGame() {
        ticTacToe.pauseGame();
        ticTacToe.resumeGame();
        assertFalse(ticTacToe.isGamePaused());
    }

    @Test
    public void testXWins() {
        ticTacToe.getButtons()[0].setText("X");
        ticTacToe.getButtons()[1].setText("X");
        ticTacToe.getButtons()[2].setText("X");
        ticTacToe.check();
        assertEquals("X wins", ticTacToe.getTextfield().getText());
        assertEquals(Color.GREEN, ticTacToe.getButtons()[0].getBackground());
    }

    @Test
    public void testOWins() {
        ticTacToe.getButtons()[0].setText("O");
        ticTacToe.getButtons()[3].setText("O");
        ticTacToe.getButtons()[6].setText("O");
        ticTacToe.check();
        assertEquals("O wins", ticTacToe.getTextfield().getText());
        assertEquals(Color.GREEN, ticTacToe.getButtons()[0].getBackground());
    }
}