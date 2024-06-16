package game;

import javax.swing.*;

public abstract class Game {


    protected GameCloseListener closeListener;
    protected GameStartListener startListener;
    protected GameEndListener endListener;

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

    protected void setStartListener (GameStartListener listener)  {this.startListener = listener;}
    public void notifyStart() {
        if (startListener != null) {
            startListener.startGame();
        }
    }

    protected void setEndListener (GameEndListener listener)  {this.endListener = listener;}
    public void notifyEnd() {
        if (endListener != null) {
            endListener.endGame();
        }
    }


}