package games.twentyfortyeight;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    public static final int SIZE = 4;
    private Tile[][] tiles;
    private Random random;

    public Board() {
        tiles = new Tile[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tiles[i][j] = new Tile();
            }
        }
        random = new Random();
        addRandomTile();
        addRandomTile();
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void addRandomTile() {
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
        }
    }

    public boolean canMove() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (tiles[i][j].isEmpty() ||
                        (i > 0 && tiles[i][j].getValue() == tiles[i - 1][j].getValue()) ||
                        (j > 0 && tiles[i][j].getValue() == tiles[i][j - 1].getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean move(Direction direction) {
        boolean moved = false;
        for (int i = 0; i < SIZE; i++) {
            Tile[] line = getLine(i, direction);
            Tile[] mergedLine = mergeLine(moveLine(line));
            if (!compare(line, mergedLine)) {
                moved = true;
                setLine(i, mergedLine, direction);
            }
        }
        if (moved) {
            addRandomTile();
        }
        return moved;
    }

    private Tile[] getLine(int index, Direction direction) {
        Tile[] line = new Tile[SIZE];
        for (int i = 0; i < SIZE; i++) {
            switch (direction) {
                case LEFT:
                    line[i] = tiles[index][i];
                    break;
                case RIGHT:
                    line[i] = tiles[index][SIZE - 1 - i];
                    break;
                case UP:
                    line[i] = tiles[i][index];
                    break;
                case DOWN:
                    line[i] = tiles[SIZE - 1 - i][index];
                    break;
            }
        }
        return line;
    }

    private void setLine(int index, Tile[] line, Direction direction) {
        for (int i = 0; i < SIZE; i++) {
            switch (direction) {
                case LEFT:
                    tiles[index][i] = line[i];
                    break;
                case RIGHT:
                    tiles[index][SIZE - 1 - i] = line[i];
                    break;
                case UP:
                    tiles[i][index] = line[i];
                    break;
                case DOWN:
                    tiles[SIZE - 1 - i][index] = line[i];
                    break;
            }
        }
    }

    private Tile[] moveLine(Tile[] oldLine) {
        List<Tile> list = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            if (!oldLine[i].isEmpty()) {
                list.add(oldLine[i]);
            }
        }
        while (list.size() != SIZE) {
            list.add(new Tile());
        }
        return list.toArray(new Tile[SIZE]);
    }

    private Tile[] mergeLine(Tile[] oldLine) {
        List<Tile> list = new ArrayList<>();
        for (int i = 0; i < SIZE - 1; i++) {
            if (!oldLine[i].isEmpty() && oldLine[i].getValue() == oldLine[i + 1].getValue()) {
                oldLine[i].merge(oldLine[i + 1]);
                list.add(oldLine[i]);
                oldLine[i + 1] = new Tile();  // Reset merged tile
                i++;
            } else {
                list.add(oldLine[i]);
            }
        }
        if (list.size() < SIZE && !oldLine[SIZE - 1].isEmpty()) {
            list.add(oldLine[SIZE - 1]);
        }
        while (list.size() != SIZE) {
            list.add(new Tile());
        }
        return list.toArray(new Tile[SIZE]);
    }

    private boolean compare(Tile[] line1, Tile[] line2) {
        if (line1 == line2) return true;
        if (line1.length != line2.length) return false;
        for (int i = 0; i < line1.length; i++) {
            if (line1[i].getValue() != line2[i].getValue()) {
                return false;
            }
        }
        return true;
    }

    public enum Direction {
        LEFT, RIGHT, UP, DOWN
    }
}