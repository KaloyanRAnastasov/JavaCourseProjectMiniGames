package games.chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainChess {
    private static Timer whiteTimer;
    private static Timer blackTimer;
    private static JLabel whiteTimerLabel;
    private static JLabel blackTimerLabel;
    private static int whiteTimeLeft;
    private static int blackTimeLeft;
    private static int increment;

    public JPanel Chess() {

            JFrame frame = new JFrame("Chess");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 650);
            frame.setLayout(new BorderLayout());

            JPanel startPanel = new JPanel();
            startPanel.setLayout(new GridLayout(5, 2));
            startPanel.setBackground(new Color(43, 43, 43)); // Set dark background color

            JLabel colorLabel = new JLabel("Choose your color:");
            colorLabel.setForeground(Color.WHITE); // Set text color to white
            JButton whiteButton = new JButton("White");
            JButton blackButton = new JButton("Black");
            JLabel timeLabel = new JLabel("Set game time (minutes):");
            timeLabel.setForeground(Color.WHITE); // Set text color to white
            JTextField timeField = new JTextField();
            JLabel incrementLabel = new JLabel("Set increment (seconds):");
            incrementLabel.setForeground(Color.WHITE); // Set text color to white
            JTextField incrementField = new JTextField();
            whiteTimerLabel = new JLabel("White Time: 00:00");
            whiteTimerLabel.setForeground(Color.WHITE); // Set text color to white
            blackTimerLabel = new JLabel("Black Time: 00:00");
            blackTimerLabel.setForeground(Color.WHITE); // Set text color to white

            startPanel.add(colorLabel);
            startPanel.add(new JLabel()); // Empty label for spacing
            startPanel.add(whiteButton);
            startPanel.add(blackButton);
            startPanel.add(timeLabel);
            startPanel.add(timeField);
            startPanel.add(incrementLabel);
            startPanel.add(incrementField);
            startPanel.add(whiteTimerLabel);
            startPanel.add(blackTimerLabel);

            frame.add(startPanel, BorderLayout.CENTER);

            whiteButton.addActionListener(e -> startGame(frame, true, timeField.getText(), incrementField.getText()));
            blackButton.addActionListener(e -> startGame(frame, false, timeField.getText(), incrementField.getText()));

            frame.setVisible(true);

            return startPanel;
    }

    private static void startGame(JFrame frame, boolean isWhitePlayer, String timeText, String incrementText) {
        int time;
        try {
            time = Integer.parseInt(timeText) * 60; // Convert to seconds
        } catch (NumberFormatException e) {
            time = 10 * 60; // Default to 10 minutes
        }
        try {
            increment = Integer.parseInt(incrementText);
        } catch (NumberFormatException e) {
            increment = 0; // Default to 0 increment
        }

        whiteTimeLeft = time;
        blackTimeLeft = time;

        whiteTimerLabel.setText("White Time: " + formatTime(whiteTimeLeft));
        blackTimerLabel.setText("Black Time: " + formatTime(blackTimeLeft));

        Board board = new Board(isWhitePlayer);

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.add(board, BorderLayout.CENTER);

        JPanel timerPanel = new JPanel(new GridLayout(1, 2));
        timerPanel.add(whiteTimerLabel);
        timerPanel.add(blackTimerLabel);
        frame.add(timerPanel, BorderLayout.NORTH);

        startTimers(board);

        frame.revalidate();
        frame.repaint();
    }

    private static void startTimers(Board board) {
        whiteTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (board.isWhiteTurn) {
                    whiteTimeLeft--;
                    if (whiteTimeLeft <= 0) {
                        whiteTimeLeft = 0;
                        whiteTimer.stop();
                        blackTimer.stop();
                        JOptionPane.showMessageDialog(null, "Black wins on time!");
                    }
                    whiteTimerLabel.setText("White Time: " + formatTime(whiteTimeLeft));
                }
            }
        });
        blackTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!board.isWhiteTurn) {
                    blackTimeLeft--;
                    if (blackTimeLeft <= 0) {
                        blackTimeLeft = 0;
                        whiteTimer.stop();
                        blackTimer.stop();
                        JOptionPane.showMessageDialog(null, "White wins on time!");
                    }
                    blackTimerLabel.setText("Black Time: " + formatTime(blackTimeLeft));
                }
            }
        });

        whiteTimer.start();
        blackTimer.start();

        board.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (board.isWhiteTurn) {
                    whiteTimeLeft += increment;
                } else {
                    blackTimeLeft += increment;
                }
                whiteTimerLabel.setText("White Time: " + formatTime(whiteTimeLeft));
                blackTimerLabel.setText("Black Time: " + formatTime(blackTimeLeft));
            }
        });
    }

    private static String formatTime(int time) {
        int minutes = time / 60;
        int seconds = time % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
