package games.twentyfortyeight;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TwentyFortyEightTest {
    private Board board;

    @Before
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testInitialBoardSetup() {
        int nonEmptyTiles = 0;
        for (int y = 0; y < Board.SIZE; y++) {
            for (int x = 0; x < Board.SIZE; x++) {
                if (!board.getTiles()[y][x].isEmpty()) {
                    nonEmptyTiles++;
                }
            }
        }
        assertEquals(2, nonEmptyTiles);
    }

    @Test
    public void testMoveLeft() {
        board.getTiles()[0][0].setValue(2);
        board.getTiles()[0][1].setValue(2);
        assertTrue(board.move(Board.Direction.LEFT));
        assertEquals(4, board.getTiles()[0][0].getValue());
        assertTrue(board.getTiles()[0][1].isEmpty());
    }

    @Test
    public void testMoveRight() {
        board.getTiles()[0][2].setValue(2);
        board.getTiles()[0][3].setValue(2);
        assertTrue(board.move(Board.Direction.RIGHT));
        assertEquals(4, board.getTiles()[0][3].getValue());
        assertTrue(board.getTiles()[0][2].isEmpty());
    }

    @Test
    public void testMoveUp() {
        board.getTiles()[0][0].setValue(2);
        board.getTiles()[1][0].setValue(2);
        assertTrue(board.move(Board.Direction.UP));
        assertEquals(4, board.getTiles()[0][0].getValue());
        assertTrue(board.getTiles()[1][0].isEmpty());
    }

    @Test
    public void testMoveDown() {
        board.getTiles()[2][0].setValue(2);
        board.getTiles()[3][0].setValue(2);
        assertTrue(board.move(Board.Direction.DOWN));
        assertEquals(4, board.getTiles()[3][0].getValue());
        assertTrue(board.getTiles()[2][0].isEmpty());
    }

    @Test
    public void testCannotMove() {
        for (int y = 0; y < Board.SIZE; y++) {
            for (int x = 0; x < Board.SIZE; x++) {
                board.getTiles()[y][x].setValue((x + y) % 2 == 0 ? 2 : 4);
            }
        }
        assertFalse(board.canMove());
    }

    @Test
    public void testCanMove() {
        board.getTiles()[0][0].setValue(2);
        board.getTiles()[0][1].setValue(2);
        assertTrue(board.canMove());
    }

    @Test
    public void testMergeTiles() {
        board.getTiles()[0][0].setValue(2);
        board.getTiles()[0][1].setValue(2);
        board.getTiles()[0][2].setValue(4);
        board.getTiles()[0][3].setValue(4);
        assertTrue(board.move(Board.Direction.LEFT));
        assertEquals(4, board.getTiles()[0][0].getValue());
        assertEquals(8, board.getTiles()[0][1].getValue());
        assertTrue(board.getTiles()[0][2].isEmpty());
        assertTrue(board.getTiles()[0][3].isEmpty());
    }

    @Test
    public void testAddRandomTile() {
        board = new Board();
        int nonEmptyTiles = 0;
        for (int y = 0; y < Board.SIZE; y++) {
            for (int x = 0; x < Board.SIZE; x++) {
                if (!board.getTiles()[y][x].isEmpty()) {
                    nonEmptyTiles++;
                }
            }
        }
        assertEquals(2, nonEmptyTiles);
        board.addRandomTile();
        nonEmptyTiles = 0;
        for (int y = 0; y < Board.SIZE; y++) {
            for (int x = 0; x < Board.SIZE; x++) {
                if (!board.getTiles()[y][x].isEmpty()) {
                    nonEmptyTiles++;
                }
            }
        }
        assertEquals(3, nonEmptyTiles);
    }
}