package games.minesweeper;

import game.Game;
import game.GameEndListener;
import game.GameStartListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import games.minesweeper.Minesweeper;

public class MineSweeperGame extends Game {
   // private JPanel panel;
    private Minesweeper minesweeper;

    private GameEndListener endListener = new GameEndListener() {
        @Override
        public void endGame() {
            System.out.println("endGame event");
            minesweeper = null;
            // here is a code to dispose game resources
        }
    };

    private GameStartListener startListener = new GameStartListener() {
        @Override
        public void startGame(){
            System.out.println("startGame event");
            minesweeper = new Minesweeper();
            // here is a code to init game and game logic and to load resources
        }

    };

    public MineSweeperGame() {

        //panel = new JPanel();
        //panel.add(new JLabel("Welcome to Game 1!"));
        JButton returnButton = new JButton("Return to Main Menu");
        returnButton.addActionListener(this::onReturnClicked);
        //panel.add(returnButton);
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
        return minesweeper.getPanel();
    }

    @Override
    public String getGameName() {
        return "minesweeper";
    }

    @Override
    public ImageIcon getGameIcon() {
        String imagePath = "/games/minesweeper/resources/Minesweeper_flag.png";
        java.net.URL imgURL = getClass().getResource(imagePath);
        assert imgURL != null;
        return new ImageIcon(imgURL);
    }

    @Override
    public int getPreferredWidth() {
        return 1024;
    }

    @Override
    public int getPreferredHeight() {
        return 1024;
    }
}