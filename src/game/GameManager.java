package game;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    public static List<Game> loadGames() throws GameException {
       List<Game> games = new ArrayList<>();
        try {
            games.add(new games.example.ExampleGame());
            games.add(new games.tictactoe.TicTacToeGame());
            games.add(new games.the_snake.SnakeGame());
            games.add(new games.minesweeper.MineSweeperGame());
            games.add(new games.twentyfortyeight.TwentyFortyEightGame());
            games.add(new games.connect4.Connect4Game());

        } catch (Exception e) {
            throw new GameException("Error loading games");
        }
        return games;

    }
}