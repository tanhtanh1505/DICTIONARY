package com.example.dictionary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayHangMan implements Initializable {
    @FXML
    private Label guessWord;
    @FXML
    private TextField guessedWord;
    @FXML
    private Label lives;
//    @FXML
//    private static Label timecounter;
    @FXML
    private Label score;

    private GameHangMan game = new GameHangMan();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game.init();
        guessWord.setText(game.getGuessedWord());
        lives.setText(String.valueOf(game.getNumberHeart()));
        score.setText("Score: " + String.valueOf(game.getScore()));
    }

    public void updateGame(){
        if(game.getStatus()){
            game.check(guessedWord.getText().charAt(0));
            guessWord.setText(game.getGuessedWord());
            guessedWord.clear();
            lives.setText(String.valueOf(game.getNumberHeart()));
            score.setText("Score: " + String.valueOf(game.getScore()));

            if(game.getWin()){
                Alert notice = new Alert(Alert.AlertType.INFORMATION);
                notice.setTitle("Congratulation!");
                notice.setContentText("Your Score: " + String.valueOf(game.getScore()));
                notice.setHeaderText("You Win!!!");
                notice.show();
            }
        }
        else {
            Alert alError = new Alert(Alert.AlertType.INFORMATION);
            alError.setContentText("Your Score: " + String.valueOf(game.getScore()));
            alError.setHeaderText("You Loss");
            alError.show();
        }
    }
    public void checkGuessedWord(ActionEvent event) {
        updateGame();
    }

    public void checkByEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            updateGame();
        }
    }

    public void getSuggestWord(ActionEvent event) {
        game.getSuggestWord();
        guessWord.setText(game.getGuessedWord());
        score.setText("Score: " + String.valueOf(game.getScore()));
    }
}
