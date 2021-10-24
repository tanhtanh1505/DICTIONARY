package com.example.dictionary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HangmanWithAiPerview {
    @FXML
    TextField lengthOfWord;

    public void startWithAi(ActionEvent event) throws IOException {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("gameplay-hangman-AI-guess.fxml"));
            Parent mainParent = loader.load();
            GameplayHangmanAIGuess controller = loader.getController();
            controller.init(Integer.parseInt(lengthOfWord.getText()));
            Scene scene = new Scene(mainParent);
            stage.setScene(scene);
        }
        catch (Exception e){
            Alert alError = new Alert(Alert.AlertType.ERROR);
            alError.setHeaderText("Please enter the length of word");
            alError.show();
        }
    }
}
