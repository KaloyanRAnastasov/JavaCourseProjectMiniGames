package games.connect4;

import game.Game;
import game.GameEndListener;
import game.GameStartListener;

import javax.swing.*;

public class Connect4Game extends Game {
    private final Connect4 connect4;

    private final GameEndListener endListener = new GameEndListener() {
        @Override
        public void endGame() {
            System.out.println("endGame event");
            // Dispose game resources if necessary
        }
    };

    private final GameStartListener startListener = new GameStartListener() {
        @Override
        public void startGame() {
            System.out.println("startGame event");
            // Initialize game logic and resources
            connect4.resetGame();
        }
    };

    public Connect4Game() {
        connect4 = new Connect4();
        setStartListener(startListener);
        setEndListener(endListener);
    }

    @Override
    public JPanel getGamePanel() {
        return connect4.createGamePanel();
    }

    @Override
    public String getGameName() {
        return connect4.getTitle();
    }

    @Override
    public ImageIcon getGameIcon() {
        return connect4.getIcon();
    }

    @Override
    public int getPreferredWidth() {
        return 800;
    }

    @Override
    public int getPreferredHeight() {
        return 600;
    }
}
