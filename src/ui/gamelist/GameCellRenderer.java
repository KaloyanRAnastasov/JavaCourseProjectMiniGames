package ui.gamelist;

import game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameCellRenderer extends JLabel implements ListCellRenderer<Game> {
    private Image resizeImage(Image originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();

        // Apply quality rendering settings
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the resized image
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        return resizedImage;
    }
    @Override
    public Component getListCellRendererComponent(JList<? extends Game> list, Game value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        setText(value.getGameName());
        ImageIcon icon = new ImageIcon( resizeImage(value.getGameIcon().getImage(),64,64));
        setIcon(icon);
        setOpaque(true);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setHorizontalTextPosition(JLabel.RIGHT);
        setHorizontalAlignment(JLabel.LEFT);
        return this;
    }
}