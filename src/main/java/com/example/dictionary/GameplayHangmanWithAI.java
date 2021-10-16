package com.example.dictionary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class GameplayHangmanWithAI {
    @FXML
    Label guess, guessedWord;

    private ArrayList<String> listGuessedWord = new ArrayList<>();
    private String gWord = "";
    private List<Character> listGuessChar = List.of('e', 'a', 'o', 'i', 'u');
    private char c;
    private int pos = 0;
    private int autoIncrease = 98;

    public void init(int len) {
        for(String s : InitData.getKeyEV()){
            if(s.length() == len){
                listGuessedWord.add(s);
            }
        }

        for(int i = 0; i < len; i++){
            gWord += "_ ";
        }

        guessedWord.setText(gWord);
        guessChar();
    }

    public void guessChar(){
        if(pos < listGuessChar.size()){
            c = listGuessChar.get(pos);
            pos += 1;
        }
        else {
//            Random rd = new Random();
//            c = (char) (rd.nextInt(25) + 97);
            c = (char) autoIncrease;
            autoIncrease++;
        }

        guess.setText("Have character " + c + "?");

        for(int i = 0; i < listGuessedWord.size(); i++){
            System.out.println(listGuessedWord.get(i));
        }
    }

    public void agree(ActionEvent event) {
        for(int i = 0; i < listGuessedWord.size(); i++) {
            if (!listGuessedWord.get(i).contains("" + c)) {
                listGuessedWord.remove(i);
                i--;
                if(listGuessedWord.size() == 1){
                    guessedWord.setText(listGuessedWord.get(0));
                    break;
                }
            }
        }
        guessChar();
    }

    public void disagree(ActionEvent event) {
        guessChar();
    }
}
