package com.example.dictionary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class GameplayHangmanAIGuess {
    private GameHangManAIGuess game;
    private String defaultMask = "";

    @FXML
    Label secretWord, guess, remainingG, listWordGuessed;
    @FXML
    TextField mask;

    public void init(int len) {
        game = new GameHangManAIGuess(len);
        for(int i = 0; i < len; i++)
            defaultMask += "-";
        render(game.getNextGuess());
    }

    public void render(char gw){
        remainingG.setText("Remaining Guess: " + String.valueOf(game.getIncorrectGuess()));
        secretWord.setText(game.getSecretWord());
        guess.setText("I guess " + gw);
        listWordGuessed.setText(game.getPreviousGuesses());
        mask.setText(defaultMask);

        if(game.getStatus() == 1){
            Alert alError = new Alert(Alert.AlertType.INFORMATION);
            alError.setContentText("I can't guess continue, hang me :(");
            alError.setHeaderText("Ohhh!!");
            alError.show();
        }
        else if(game.getStatus() == 2){
            Alert alError = new Alert(Alert.AlertType.INFORMATION);
            alError.setContentText("Your word is " + game.getSecretWord().toUpperCase());
            alError.setHeaderText("I WIN!!");
            alError.show();
        }
        else if(game.getStatus() == 3){
            Alert alError = new Alert(Alert.AlertType.INFORMATION);
            alError.setContentText("I am stupid :(");
            alError.setHeaderText("I LOSS!!");
            alError.show();
        }
    }

    public void solve(){
        char guessa = game.getNextGuess();
        if (guessa == 0) {
            game.setStatus(1);
        }
        String answerMask = mask.getText();
        game.update(guessa, answerMask);
        render(guessa);
    }

    public void agree(ActionEvent event) {
        solve();
    }

    public void disagree(ActionEvent event) {
        solve();
    }

    public void exit(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("hang-man.fxml"));
        Parent mainParent = loader.load();
        Scene scene = new Scene(mainParent);
        stage.setScene(scene);
    }
}
