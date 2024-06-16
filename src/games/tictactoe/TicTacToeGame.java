package games.tictactoe;
import game.Game;
import game.GameEndListener;
import game.GameStartListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import games.tictactoe.TicTacToe;

public class TicTacToeGame extends  Game {
    private final TicTacToe ticTacToe;

    private final GameEndListener endListener = new GameEndListener() {
        @Override
        public void endGame() {
            System.out.println("endGame event");
            // here is a code to dispose game resources
        }
    };

    private final GameStartListener startListener = new GameStartListener() {
        @Override
        public void startGame() {
            System.out.println("startGame event");
            // here is a code to init game and game logic and to load resources

        }

    };

    public TicTacToeGame() {
        ticTacToe = new TicTacToe();
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
    return ticTacToe.getUpperPanel();
    }

    @Override
    public String getGameName() {
        return "TicTacToe";
    }

    @Override
    public ImageIcon getGameIcon() {
        String imagePath = "/games/tictactoe/resources/tic-tac-toe_39453.png";
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