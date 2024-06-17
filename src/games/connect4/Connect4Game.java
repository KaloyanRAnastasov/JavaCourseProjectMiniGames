package games.connect4;

import game.Game;
import javax.swing.*;

public class Connect4Game extends Game {
    private Connect4 connect4;

    public Connect4Game() {
        setStartListener(this::startGame);
        setEndListener(this::endGame);
    }

    private void startGame() {
        System.out.println("startGame event");
        connect4 = new Connect4();
    }

    private void endGame() {
        System.out.println("endGame event");
        connect4 = null;
    }

    @Override
    public JPanel getGamePanel() {
        return connect4 != null ? connect4.createGamePanel() : new JPanel();
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
