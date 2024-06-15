package games.connect4;

import org.junit.jupiter.api.Test;
import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class Connect4Test {

    @Test
    void testCreateGamePanel() {
        Connect4 game = new Connect4();
        JPanel panel = game.createGamePanel();
        assertNotNull(panel, "Game panel should not be null");
    }

    @Test
    void testInitializeBoard() {
        Connect4 game = new Connect4();
        game.initializeBoard();
        char[][] board = getBoard(game);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                assertEquals(' ', board[i][j], "Board cell should be empty");
            }
        }
    }

    @Test
    void testUpdateBoard() {
        Connect4 game = new Connect4();
        game.placePiece(0);
        game.updateBoard();
        JPanel panel = game.getPanel();
        assertNotNull(panel, "Board panel should not be null");
        // Additional checks for graphical elements could be added
    }

    @Test
    void testPlacePiece() {
        Connect4 game = new Connect4();
        assertTrue(game.placePiece(0), "Piece should be placed successfully");
        assertFalse(game.placePiece(7), "Piece should not be placed out of bounds");
    }

    @Test
    void testCheckWin() {
        Connect4 game = new Connect4();
        // Set up a winning condition manually
        char[][] board = getBoard(game);
        for (int i = 0; i < 4; i++) {
            board[0][i] = 'R';
        }
        assertTrue(game.checkWin('R'), "Player R should win");
        assertFalse(game.checkWin('Y'), "Player Y should not win");
    }

    @Test
    void testResetGame() {
        Connect4 game = new Connect4();
        game.placePiece(0);
        game.resetGame();
        char[][] board = getBoard(game);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                assertEquals(' ', board[i][j], "Board cell should be empty after reset");
            }
        }
    }

    @Test
    void testGetPanel() {
        Connect4 game = new Connect4();
        JPanel panel = game.getPanel();
        assertNotNull(panel, "Panel should not be null");
    }

    @Test
    void testGetTitle() {
        Connect4 game = new Connect4();
        String title = game.getTitle();
        assertEquals("Connect 4", title, "Title should be 'Connect 4'");
    }

    @Test
    void testGetIcon() {
        Connect4 game = new Connect4();
        ImageIcon icon = game.getIcon();
        assertNotNull(icon, "Icon should not be null");
    }

    private char[][] getBoard(Connect4 game) {
        // Use reflection to access the private board field
        try {
            java.lang.reflect.Field field = Connect4.class.getDeclaredField("board");
            field.setAccessible(true);
            return (char[][]) field.get(game);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail("Unable to access board field");
            return null;
        }
    }
}
