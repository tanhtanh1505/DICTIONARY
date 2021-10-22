package com.example.dictionary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HangmanPlayerPreview {
    @FXML
    TextField namePlayer;

    public void letGo(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("gameplay-hangman-player-guess.fxml"));
        Parent mainParent = loader.load();
        Scene scene = new Scene(mainParent);
        GameplayHangManPlayerGuess controller = loader.getController();
        controller.initGame(namePlayer.getText());
        stage.setScene(scene);
    }
}
