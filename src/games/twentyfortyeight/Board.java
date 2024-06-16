package games.twentyfortyeight;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    public static final int SIZE = 4;
    private Tile[][] tiles;
    private Random random;

    public Board(boolean randomize) {
        tiles = new Tile[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tiles[i][j] = new Tile();
            }
        }
        random = new Random();
        if (randomize) {
            try {
                addRandomTile();
                addRandomTile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void addRandomTile() throws Exception {
        List<Tile> emptyTiles = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (tiles[i][j].isEmpty()) {
                    emptyTiles.add(tiles[i][j]);
                }
            }
        }
        if (!emptyTiles.isEmpty()) {
            Tile tile = emptyTiles.get(random.nextInt(emptyTiles.size()));
            tile.setValue(random.nextInt(10) == 0 ? 4 : 2);
        } else {
            throw new Exception("No empty tiles available to add a new random tile.");
        }
    }

    public boolean canMove() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (tiles[i][j].isEmpty() ||
                        (i > 0 && tiles[i][j].getValue() == tiles[i - 1][j].getValue()) ||
                        (j > 0 && tiles[i][j].getValue() == tiles[i][j - 1].getValue()) ||
                        (i < SIZE - 1 && tiles[i][j].getValue() == tiles[i + 1][j].getValue()) ||
                        (j < SIZE - 1 && tiles[i][j].getValue() == tiles[i][j + 1].getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean move(Direction direction) throws Exception {
        boolean moved = false;
        switch (direction) {
            case LEFT:
                moved = moveLeft();
                break;
            case RIGHT:
                moved = moveRight();
                break;
            case UP:
                moved = moveUp();
                break;
            case DOWN:
                moved = moveDown();
                break;
        }
        if (moved) {
            addRandomTile();
        }
        return moved;
    }

    private boolean moveLeft() {
        boolean moved = false;
        for (int i = 0; i < SIZE; i++) {
            Tile[] row = tiles[i];
            for (int j = 1; j < SIZE; j++) {
                if (!row[j].isEmpty()) {
                    int k = j;
                    while (k > 0 && row[k - 1].isEmpty()) {
                        row[k - 1].setValue(row[k].getValue());
                        row[k].setValue(0);
                        k--;
                        moved = true;
                    }
                    if (k > 0 && row[k - 1].getValue() == row[k].getValue() && !row[k - 1].isMerged()) {
                        row[k - 1].setValue(row[k - 1].getValue() * 2);
                        row[k - 1].setMerged(true);
                        row[k].setValue(0);
                        moved = true;
                    }
                }
            }
        }
        resetMergedStatus();
        return moved;
    }

    private boolean moveRight() {
        boolean moved = false;
        for (int i = 0; i < SIZE; i++) {
            Tile[] row = tiles[i];
            for (int j = SIZE - 2; j >= 0; j--) {
                if (!row[j].isEmpty()) {
                    int k = j;
                    while (k < SIZE - 1 && row[k + 1].isEmpty()) {
                        row[k + 1].setValue(row[k].getValue());
                        row[k].setValue(0);
                        k++;
                        moved = true;
                    }
                    if (k < SIZE - 1 && row[k + 1].getValue() == row[k].getValue() && !row[k + 1].isMerged()) {
                        row[k + 1].setValue(row[k + 1].getValue() * 2);
                        row[k + 1].setMerged(true);
                        row[k].setValue(0);
                        moved = true;
                    }
                }
            }
        }
        resetMergedStatus();
        return moved;
    }

    private boolean moveUp() {
        boolean moved = false;
        for (int j = 0; j < SIZE; j++) {
            for (int i = 1; i < SIZE; i++) {
                if (!tiles[i][j].isEmpty()) {
                    int k = i;
                    while (k > 0 && tiles[k - 1][j].isEmpty()) {
                        tiles[k - 1][j].setValue(tiles[k][j].getValue());
                        tiles[k][j].setValue(0);
                        k--;
                        moved = true;
                    }
                    if (k > 0 && tiles[k - 1][j].getValue() == tiles[k][j].getValue() && !tiles[k - 1][j].isMerged()) {
                        tiles[k - 1][j].setValue(tiles[k - 1][j].getValue() * 2);
                        tiles[k - 1][j].setMerged(true);
                        tiles[k][j].setValue(0);
                        moved = true;
                    }
                }
            }
        }
        resetMergedStatus();
        return moved;
    }

    private boolean moveDown() {
        boolean moved = false;
        for (int j = 0; j < SIZE; j++) {
            for (int i = SIZE - 2; i >= 0; i--) {
                if (!tiles[i][j].isEmpty()) {
                    int k = i;
                    while (k < SIZE - 1 && tiles[k + 1][j].isEmpty()) {
                        tiles[k + 1][j].setValue(tiles[k][j].getValue());
                        tiles[k][j].setValue(0);
                        k++;
                        moved = true;
                    }
                    if (k < SIZE - 1 && tiles[k + 1][j].getValue() == tiles[k][j].getValue() && !tiles[k + 1][j].isMerged()) {
                        tiles[k + 1][j].setValue(tiles[k + 1][j].getValue() * 2);
                        tiles[k + 1][j].setMerged(true);
                        tiles[k][j].setValue(0);
                        moved = true;
                    }
                }
            }
        }
        resetMergedStatus();
        return moved;
    }

    private void resetMergedStatus() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tiles[i][j].setMerged(false);
            }
        }
    }

    public void reset() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tiles[i][j].setValue(0);
                tiles[i][j].setMerged(false);
            }
        }
        try {
            addRandomTile();
            addRandomTile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum Direction {
        LEFT, RIGHT, UP, DOWN
    }
}