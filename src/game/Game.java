package game;

import javax.swing.*;

public abstract class Game {
    protected GameCloseListener closeListener;
    public abstract JPanel getGamePanel();
    public abstract String getGameName();
    public abstract ImageIcon getGameIcon();
    public abstract int getPreferredWidth();
    public abstract int getPreferredHeight();

    public void setCloseListener(GameCloseListener listener) {
        this.closeListener = listener;
    }
    protected void notifyClose() {
        if (closeListener != null) {
            closeListener.closeGame();
        }
    }
}