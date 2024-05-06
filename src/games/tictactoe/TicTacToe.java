package games.tictactoe;

import  java.awt.*;
import  java.awt.event.*;
import  java.util.*;
import  javax.swing.*;

public class TicTacToe implements  ActionListener{

    Random random = new Random();
    JPanel frame = new JPanel();
    JPanel title = new JPanel();
    JPanel button = new JPanel();
    JLabel textfield = new JLabel();
    JButton[] buttons = new JButton[9];
    boolean player_turn;


    //Frame,buttons and title for the game
    TicTacToe() {

        frame.setSize(800, 800);
        frame.setBackground(Color.WHITE);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        textfield.setBackground(Color.BLACK);
        textfield.setForeground(Color.CYAN);
        textfield.setFont(new Font("Ink Free", Font.BOLD, 75));
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setText("Tic-Tac-Toe");
        textfield.setOpaque(true);

        title.setLayout(new BorderLayout());
        title.setBounds(0, 0, 800, 100);
        title.add(textfield);
        frame.add(title, BorderLayout.NORTH);
        frame.add(button);

        button.setLayout(new GridLayout(3, 3));
        button.setBackground(new Color(150, 150, 150));

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            button.add(buttons[i]);
            buttons[i].setFont(new Font("Ink Tree", Font.BOLD, 100));
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
        }

        firstTurn();
    }

    //check each of the buttons if there is text inside(X or O ) in a button
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 9; i++) {
            if (e.getSource() == buttons[i]) {
                if (player_turn) {
                    if (buttons[i].getText().equals("")) {
                        buttons[i].setForeground(new Color(255, 0, 0));
                        buttons[i].setText("X");
                        player_turn = false;
                        textfield.setText("O turn");
                        check();
                        if (checkForDraw()) {
                            for (JButton button : buttons) {
                                button.setEnabled(false);
                            }
                            textfield.setText("Draw");
                        }
                    }
                } else {
                    if (buttons[i].getText().equals("")) {
                        buttons[i].setForeground(new Color(0, 0, 255));
                        buttons[i].setText("O");
                        player_turn = true;
                        textfield.setText("X turn");
                        check();
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
}