package game;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    public static List<Game> loadGames() {
        List<Game> games = new ArrayList<>();
        games.add(new games.example.ExampleGame());
        games.add(new games.tictactoe.TicTacToeGame());
        games.add(new games.the_snake.SnakeGame());
        games.add(new games.minesweeper.MineSweeperGame());
        games.add(new games.twentyfortyeight.TwentyFortyEightGame());
        return games;
    }
}