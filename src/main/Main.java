package main;

import game.Game;
import game.GameManager;
import ui.gamelist.GameCellRenderer;
import ui.menu.SetupDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class Main {
    private static JFrame frame;
    private static List<Game> games;

    private static void showSetupDialog() {
        SetupDialog dialog = new SetupDialog(frame);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            frame.setTitle("Game Hub - " + dialog.getNickname() + "@" + dialog.getServerAddress());
        }
    }

    public static void main(String[] args) {
        frame = new JFrame("Game Hub");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem setupMenuItem = new JMenuItem("Setup");
        setupMenuItem.addActionListener(_ -> showSetupDialog());
        menu.add(setupMenuItem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        try {
            games = GameManager.loadGames();
            JList<Game> gameList = new JList<>(new DefaultListModel<>());
            for (Game game : games) {
                ((DefaultListModel<Game>) gameList.getModel()).addElement(game);
            }
            gameList.setCellRenderer(new GameCellRenderer());
            gameList.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        int index = gameList.locationToIndex(evt.getPoint());
                        if (index != -1) {
                            openGameInModalFrame(games.get(index));
                        }
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(gameList);
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to load games", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

    }

    private static void openGameInModalFrame(Game game) {
        JDialog dialog = new JDialog(frame, game.getGameName(), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e){
                game.notifyOpen();

            }

            @Override
            public void windowClosed(WindowEvent e) {
                game.notifyEnd();
                System.out.println("Dialog has closed");
                // Actions to perform after the dialog is fully closed
            }
        });

        game.setCloseListener(dialog::dispose);

        System.out.println("Dialog is opened");
        game.notifyStart();

        dialog.setSize(game.getPreferredWidth(), game.getPreferredHeight());
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().add(game.getGamePanel(), BorderLayout.CENTER);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);

    }
}
