package games.twentyfortyeight;

import game.Game;
import game.GameEndListener;
import game.GameStartListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TwentyFortyEightGame extends Game {
    private JPanel panel;
    private TwentyFortyEightPanel twentyFortyEightPanel;

    private GameEndListener endListener = new GameEndListener() {
        @Override
        public void endGame() {
            System.out.println("endGame event");
            // Code to dispose game resources
        }
    };

    private GameStartListener startListener = new GameStartListener() {
        @Override
        public void startGame() {
            System.out.println("startGame event");
            // Code to init game and game logic and to load resources
            twentyFortyEightPanel.requestFocusInWindow(); // Set focus to the game panel
        }
    };

    public TwentyFortyEightGame() {
        twentyFortyEightPanel = new TwentyFortyEightPanel();
        panel = new JPanel(new BorderLayout());
        panel.add(twentyFortyEightPanel, BorderLayout.CENTER);

        JButton returnButton = new JButton("Return to Main Menu");
        returnButton.addActionListener(this::onReturnClicked);
        panel.add(returnButton, BorderLayout.SOUTH);

        JButton resetButton = new JButton("Reset Game");
        resetButton.addActionListener(this::onResetClicked);
        panel.add(resetButton, BorderLayout.NORTH);

        setStartListener(startListener);
        setEndListener(endListener);
    }

    private void onReturnClicked(ActionEvent e) {
        System.out.println("Return to main menu.");
        notifyClose();
    }

    private void onResetClicked(ActionEvent e) {
        System.out.println("Reset game.");
        twentyFortyEightPanel.resetGame();
        twentyFortyEightPanel.requestFocusInWindow();
    }

    @Override
    public JPanel getGamePanel() {
        return panel;
    }

    @Override
    public String getGameName() {
        return "2048";
    }

    @Override
    public ImageIcon getGameIcon() {
        String imagePath = "/games/twentyfortyeight/resources/2048_icon.png";
        java.net.URL imgURL = getClass().getResource(imagePath);
        assert imgURL != null;
        return new ImageIcon(imgURL);
    }

    @Override
    public int getPreferredWidth() {
        return 600;
    }

    @Override
    public int getPreferredHeight() {
        return 600;
    }
}