package games.twentyfortyeight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TwentyFortyEightPanel extends JPanel {
    private static final int TILE_SIZE = 100;
    private static final int TILE_MARGIN = 16;
    private Board board;

    public TwentyFortyEightPanel() {
        setFocusable(true);
        requestFocusInWindow();
        board = new Board(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                boolean moved = false;
                try {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            moved = board.move(Board.Direction.LEFT);
                            break;
                        case KeyEvent.VK_RIGHT:
                            moved = board.move(Board.Direction.RIGHT);
                            break;
                        case KeyEvent.VK_UP:
                            moved = board.move(Board.Direction.UP);
                            break;
                        case KeyEvent.VK_DOWN:
                            moved = board.move(Board.Direction.DOWN);
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (moved) {
                    repaint();
                    if (!board.canMove()) {
                        JOptionPane.showMessageDialog(null, "Game Over!");
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0xBBADA0));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        for (int y = 0; y < Board.SIZE; y++) {
            for (int x = 0; x < Board.SIZE; x++) {
                drawTile(g, board.getTiles()[y][x], x, y);
            }
        }
    }

    private void drawTile(Graphics g, Tile tile, int x, int y) {
        int value = tile.getValue();
        int xOffset = offsetCoors(x);
        int yOffset = offsetCoors(y);
        g.setColor(getBackground(value));
        g.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, 8, 8);
        g.setColor(getForeground(value));
        final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;
        final Font font = new Font(Font.SANS_SERIF, Font.BOLD, size);
        g.setFont(font);
        String s = String.valueOf(value);
        final FontMetrics fm = getFontMetrics(font);
        final int w = fm.stringWidth(s);
        final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];
        if (value != 0) {
            g.drawString(s, xOffset + (TILE_SIZE - w) / 2, yOffset + (TILE_SIZE + h) / 2);
        }
    }

    private static int offsetCoors(int arg) {
        return arg * (TILE_MARGIN + TILE_SIZE) + TILE_MARGIN;
    }

    private Color getBackground(int value) {
        switch (value) {
            case 2:    return new Color(0xEEE4DA);
            case 4:    return new Color(0xEDE0C8);
            case 8:    return new Color(0xF2B179);
            case 16:   return new Color(0xF59563);
            case 32:   return new Color(0xF67C5F);
            case 64:   return new Color(0xF65E3B);
            case 128:  return new Color(0xEDCF72);
            case 256:  return new Color(0xEDCC61);
            case 512:  return new Color(0xEDC850);
            case 1024: return new Color(0xEDC53F);
            case 2048: return new Color(0xEDC22E);
            default:   return new Color(0xCDC1B4);
        }
    }

    private Color getForeground(int value) {
        return value < 16 ? new Color(0x776E65) : new Color(0xF9F6F2);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(2 * TILE_MARGIN + 4 * TILE_SIZE, 2 * TILE_MARGIN + 4 * TILE_SIZE);
    }

    public void resetGame() {
        board.reset();
        repaint();
        requestFocusInWindow();
    }
}