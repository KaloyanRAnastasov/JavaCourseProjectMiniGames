package games.chess;

import game.Game;
import game.GameEndListener;
import game.GameStartListener;
import games.chess.MainChess;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ChessGame extends Game {
    private JPanel panel;

    private GameEndListener endListener = new GameEndListener() {
        @Override
        public void endGame() {
            System.out.println("endGame event");
            // here is a code to dispose game resources
        }
    };

    private GameStartListener startListener = new GameStartListener() {

        @Override
        public void startGame(){
            System.out.println("startGame event");
            // here is a code to init game and game logic and to load resources
        }

    };

    public ChessGame() {
        MainChess chess = new MainChess();
        panel = chess.Chess();
        panel.add(new JLabel("Welcome to Game 1!"));
        JButton returnButton = new JButton("Return to Main Menu");
        returnButton.addActionListener(this::onReturnClicked);
        panel.add(returnButton);
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
        return panel;
    }

    @Override
    public String getGameName() {
        return "Game 1";
    }

    @Override
    public ImageIcon getGameIcon() {
        String imagePath = "/games/example/resources/sweeper.png";
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
        return 768;
    }
}