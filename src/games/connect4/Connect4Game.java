package games.connect4;

import game.Game;
import game.GameEndListener;
import game.GameStartListener;
import javax.swing.*;


public class Connect4Game extends Game {
    private Connect4 connect4;

    private final GameEndListener endListener = new GameEndListener() {
        @Override
        public void endGame() {
            System.out.println("endGame event");
            connect4 = null;
            // Dispose game resources if necessary
        }
    };

    private final GameStartListener startListener = new GameStartListener() {
        @Override
        public void startGame() {
            System.out.println("startGame event");
            connect4 = new Connect4();
            // Initialize game logic and resources
            //connect4.resetGame();
        }
    };

    public Connect4Game() {
        setStartListener(startListener);
        setEndListener(endListener);
    }

    @Override
    public JPanel getGamePanel() {
        return connect4.createGamePanel();
    }

    @Override
    public String getGameName() {
        return "connect4";
    }

    @Override
    public ImageIcon getGameIcon() {
        String imagePath = "/games/connect4/resources/Connect4Logo.png";
        java.net.URL imgURL = getClass().getResource(imagePath);
        assert imgURL != null;
        return new ImageIcon(imgURL);
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
