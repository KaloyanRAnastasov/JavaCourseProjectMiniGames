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
        assertFalse(ticTacToe.player_turn);
    }

    @Test
    public void testCheckForDraw() {
        assertFalse(ticTacToe.checkForDraw());
    }

    @Test
    public void testPauseGame() {
        ticTacToe.pauseGame();
        assertTrue(ticTacToe.gamePaused);
    }

    @Test
    public void testResumeGame() {
        ticTacToe.pauseGame();
        ticTacToe.resumeGame();
        assertFalse(ticTacToe.gamePaused);
    }

    @Test
    public void testXWins() {
        ticTacToe.buttons[0].setText("X");
        ticTacToe.buttons[1].setText("X");
        ticTacToe.buttons[2].setText("X");
        ticTacToe.check();
        assertEquals("X wins", ticTacToe.textfield.getText());
        assertEquals(Color.GREEN, ticTacToe.buttons[0].getBackground());
    }

    @Test
    public void testOWins() {
        ticTacToe.buttons[0].setText("O");
        ticTacToe.buttons[3].setText("O");
        ticTacToe.buttons[6].setText("O");
        ticTacToe.check();
        assertEquals("O wins", ticTacToe.textfield.getText());
        assertEquals(Color.GREEN, ticTacToe.buttons[0].getBackground());
    }
}