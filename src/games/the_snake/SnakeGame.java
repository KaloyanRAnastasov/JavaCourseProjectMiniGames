package games.the_snake;

import game.Game;
import game.GameEndListener;
import game.GameStartListener;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SnakeGame extends Game {
    private  Snake SnakeGame = null;


    private GameEndListener endListener = new GameEndListener() {
        @Override
        public void endGame() {
            System.out.println("endGame event");
            SnakeGame = null;
            // here is a code to dispose game resources
        }
    };

    private GameStartListener startListener = new GameStartListener() {
        @Override
        public void startGame(){
            System.out.println("startGame event");
            SnakeGame = new Snake();
            // here is a code to init game and game logic and to load resources
        }

    };

    public SnakeGame() {

        setStartListener(startListener);
        setEndListener(endListener);
    }

    private void onReturnClicked(ActionEvent e) {
        // Logic to switch back to the main menu
        System.out.println("Return to main menu.");
        notifyClose();
    }

    @Override
    public JPanel getGamePanel() {
        return SnakeGame;
    }

    @Override
    public String getGameName() {
        return "SnakeGame";
    }

    @Override
    public ImageIcon getGameIcon() {
        String imagePath = "/games/the_snake/snake_resources/snake_pic.png";
        java.net.URL imgURL = getClass().getResource(imagePath);
        assert imgURL != null;
        return new ImageIcon(imgURL);
    }

    @Override
    public int getPreferredWidth() {
        return 616;
    }

    @Override
    public int getPreferredHeight() {
        return 640;
    }
}