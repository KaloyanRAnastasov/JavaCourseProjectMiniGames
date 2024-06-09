package games.tictactoe;

import  java.awt.*;
import  java.awt.event.*;
import  java.util.*;
import  javax.swing.*;

//frame, buttons and title
public class TicTacToe implements  ActionListener{

    private  Random random = new Random();
    private JPanel upperPanel = new JPanel();
    private  JPanel title = new JPanel();
    private  JPanel button = new JPanel();
    private  JLabel textfield = new JLabel();
    private  JButton[] buttons = new JButton[9];
    private  boolean player_turn;
    private   boolean gamePaused=false;
    private  JPanel pauseScr= new JPanel();


    //Frame,buttons and title for the game
    TicTacToe() {

        upperPanel.setSize(800, 800);
        upperPanel.setBackground(Color.WHITE);
        upperPanel.setLayout(new BorderLayout());
        upperPanel.setVisible(true);
        upperPanel.add(pauseScr);

        textfield.setBackground(Color.BLACK);
        textfield.setForeground(Color.CYAN);
        textfield.setFont(new Font("Ink Free", Font.BOLD, 75));
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setText("Tic-Tac-Toe");
        textfield.setOpaque(true);

        title.setLayout(new BorderLayout());
        title.setBounds(0, 0, 800, 100);
        title.add(textfield);
        upperPanel.add(title, BorderLayout.NORTH);
        upperPanel.add(button);

        button.setLayout(new GridLayout(3, 3));
        button.setBackground(new Color(150, 150, 150));

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            button.add(buttons[i]);
            buttons[i].setFont(new Font("Ink Tree", Font.BOLD, 100));
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
        }



            // Add key listener to the frame
            upperPanel.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        if (gamePaused) {
                            resumeGame();
                        } else {
                            pauseGame();
                        }
                    }
                }
            });

            // focus the  frame to receive key events
            upperPanel.setFocusable(true);
            upperPanel.requestFocusInWindow();

            firstTurn();
        }

    //check each of the buttons if there is text inside(X or O ) in a button
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gamePaused){
            return;
        }
        for (int i = 0; i < 9; i++) {
            if (e.getSource() == buttons[i]) {
                if (player_turn) {
                    if (buttons[i].getText().equals("")) {
                        buttons[i].setForeground(new Color(255, 0, 0));
                        buttons[i].setText("X");
                        player_turn = false;
                        textfield.setText("O turn");
                        check();
                        if (!textfield.getText().equals("X wins")) {
                            if (checkForDraw()) {
                                for (JButton button : buttons) {
                                    button.setEnabled(false);
                                }
                                textfield.setText("Draw");
                            }
                        }
                    }
                } else {
                    if (buttons[i].getText().equals("")) {
                        buttons[i].setForeground(new Color(0, 0, 255));
                        buttons[i].setText("O");
                        player_turn = true;
                        textfield.setText("X turn");
                        check();
                        if (!textfield.getText().equals("O wins")) {
                            if (checkForDraw()) {
                                for (JButton button : buttons) {
                                    button.setEnabled(false);
                                }
                                textfield.setText("Draw");
                            }
                        }
                    }
                }
            }
        }
    }

    //used for the test in TicTacToeTest
public boolean isPlayerTurn(){
        return player_turn;
}
    // label for each turn
    public void firstTurn(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (random.nextInt(2) == 0){
            player_turn = true;
            textfield.setText("X turn");
        } else {
            player_turn = false;
            textfield.setText("O turn");
        }
    }


    // combinations to win
    public void check(){
        if((buttons[0].getText().equals("X"))&&buttons[1].getText().equals("X")&&buttons[2].getText().equals("X")){
            xWins(0,1,2);
        }
        if((buttons[3].getText().equals("X"))&&buttons[4].getText().equals("X")&&buttons[5].getText().equals("X")){
            xWins(3,4,5);
        }
        if((buttons[6].getText().equals("X"))&&buttons[7].getText().equals("X")&&buttons[8].getText().equals("X")){
            xWins(6,7,8);
        }
        if((buttons[0].getText().equals("X"))&&buttons[3].getText().equals("X")&&buttons[6].getText().equals("X")){
            xWins(0,3,6);
        }
        if((buttons[1].getText().equals("X"))&&buttons[4].getText().equals("X")&&buttons[7].getText().equals("X")){
            xWins(1,4,7);
        }
        if((buttons[2].getText().equals("X"))&&buttons[5].getText().equals("X")&&buttons[8].getText().equals("X")){
            xWins(2,5,8);
        }
        if((buttons[0].getText().equals("X"))&&buttons[4].getText().equals("X")&&buttons[8].getText().equals("X")){
            xWins(0,4,8);
        }
        if((buttons[2].getText().equals("X"))&&buttons[4].getText().equals("X")&&buttons[6].getText().equals("X")){
            xWins(2,4,6);
        }


        if((buttons[0].getText().equals("O"))&&buttons[1].getText().equals("O")&&buttons[2].getText().equals("O")){
            oWins(0,1,2);
        }
        if((buttons[3].getText().equals("O"))&&buttons[4].getText().equals("O")&&buttons[5].getText().equals("O")){
            oWins(3,4,5);
        }
        if((buttons[6].getText().equals("O"))&&buttons[7].getText().equals("O")&&buttons[8].getText().equals("O")){
            oWins(6,7,8);
        }
        if((buttons[0].getText().equals("O"))&&buttons[3].getText().equals("O")&&buttons[6].getText().equals("O")){
            oWins(0,3,6);
        }
        if((buttons[1].getText().equals("O"))&&buttons[4].getText().equals("O")&&buttons[7].getText().equals("O")){
            oWins(1,4,7);
        }
        if((buttons[2].getText().equals("O"))&&buttons[5].getText().equals("O")&&buttons[8].getText().equals("O")){
            oWins(2,5,8);
        }
        if((buttons[0].getText().equals("O"))&&buttons[4].getText().equals("O")&&buttons[8].getText().equals("O")){
            oWins(0,4,8);
        }
        if((buttons[2].getText().equals("O"))&&buttons[4].getText().equals("O")&&buttons[6].getText().equals("O")){
            oWins(2,4,6);
        }
    }

    //X win finish
    public void xWins(int a, int b, int c){
        buttons[a].setBackground(Color.GREEN);
        buttons[b].setBackground(Color.GREEN);
        buttons[c].setBackground(Color.GREEN);

        for (int i = 0; i < 9; i++){
            buttons[i].setEnabled(false);
        }
        textfield.setText("X wins");
    }


    //O win finish
    public void oWins(int a, int b, int c){
        buttons[a].setBackground(Color.GREEN);
        buttons[b].setBackground(Color.GREEN);
        buttons[c].setBackground(Color.GREEN);

        for (int i = 0; i < 9; i++){
            buttons[i].setEnabled(false);
        }
        textfield.setText("O wins");
    }
    public boolean checkForDraw() {
        for (JButton button : buttons) {
            if (button.getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    // Pause the game
    public void pauseGame() {
        gamePaused = true;
        pauseScr.setVisible(true);
        textfield.setText("Game Paused");
    }

    // Resume the game
    public void resumeGame() {
        gamePaused = false;
        pauseScr.setVisible(false);
        if (player_turn) {
            textfield.setText("X turn");
        } else {
            textfield.setText("O turn");
        }
    }

    public Random getRandom() {
        return random;
    }

    public JPanel getPauseScr() {
        return pauseScr;
    }

    public JPanel getUpperPanel() {
        return upperPanel;
    }

    public JPanel getTitle() {
        return title;
    }

    public JLabel getTextfield() {
        return textfield;
    }

    public JPanel getButton() {
        return button;
    }

    public JButton[] getButtons() {
        return buttons;
    }

    public boolean isPlayer_turn() {
        return player_turn;
    }

    public boolean isGamePaused() {
        return gamePaused;
    }
}