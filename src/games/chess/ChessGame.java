package games.chess;

import game.Game;
import game.GameEndListener;
import game.GameStartListener;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ChessGame extends Game {
    private JPanel panel;
    private Board chessBoard;

    private GameEndListener endListener = new GameEndListener() {
        @Override
        public void endGame() {
            System.out.println("endGame event");
            // Add code here to dispose game resources
        }
    };

    private GameStartListener startListener = new GameStartListener() {
        @Override
        public void startGame() {
            System.out.println("startGame event");
            // Add code here to initialize game logic and load resources
            initializeGame();
        }
    };

    public ChessGame() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Welcome to Chess Game!"));

        chessBoard = new Board(true);
        panel.add(chessBoard);

        JButton returnButton = new JButton("Return to Main Menu");
        returnButton.addActionListener(this::onReturnClicked);
        panel.add(returnButton);

        setStartListener(startListener);
        setEndListener(endListener);
    }

    private void initializeGame() {
        // Initialize the chess board or any other game setup logic here
        chessBoard.setupNewGame();
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
        return "Chess";
    }

    @Override
    public ImageIcon getGameIcon() {
        String imagePath = "/games/chess/resources/logo/chessLogo.jpg";
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
