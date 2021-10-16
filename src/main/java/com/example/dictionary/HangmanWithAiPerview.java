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

public class HangmanWithAiPerview {
    @FXML
    TextField lengthOfWord;

    public void startWithAi(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("gameplay-hangman-withAI.fxml"));
        Parent mainParent = loader.load();
        GameplayHangmanWithAI controller = loader.getController();
        controller.init(Integer.parseInt(lengthOfWord.getText()));
        Scene scene = new Scene(mainParent);
        stage.setScene(scene);
    }
}
