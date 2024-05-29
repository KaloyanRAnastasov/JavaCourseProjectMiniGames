package games.twentyfortyeight;

public class Tile {
    private int value;

    public Tile() {
        this.value = 0;
    }

    public boolean isEmpty() {
        return value == 0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void merge(Tile other) {
        this.value += other.value;
        other.value = 0;
    }
}