package game;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    public static List<Game> loadGames() {
        List<Game> games = new ArrayList<>();
        games.add(new games.example.ExampleGame());
        return games;
    }
}