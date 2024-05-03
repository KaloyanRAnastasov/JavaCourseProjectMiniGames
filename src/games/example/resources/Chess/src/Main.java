import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] options = {"White", "Black"};
            int choice = JOptionPane.showOptionDialog(null, "Play", "Chess Game", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            boolean isWhitePlayer = (choice == 0);

            JFrame frame = new JFrame("Chess Game");
            Board board = new Board(isWhitePlayer);
            frame.add(board);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
