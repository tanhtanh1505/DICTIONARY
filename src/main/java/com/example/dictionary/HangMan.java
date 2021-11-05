package com.example.dictionary;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HangMan implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void switchScene(ActionEvent event, String s) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(s));
        Parent mainParent = loader.load();
        Scene scene = new Scene(mainParent);
        stage.setScene(scene);
    }

    public void btnStart(ActionEvent event) throws IOException {
        switchScene(event,"hangman-player-preview.fxml");
    }

    public void btnPlayWithAI(ActionEvent event) throws IOException {
        switchScene(event,"hangman-withAi-perview.fxml");
    }

    public void btnScore(ActionEvent event) throws IOException {
        switchScene(event, "hangman-score.fxml");
    }

    public void btnExit(ActionEvent event) throws IOException {
        switchScene(event, "hello-view.fxml");
    }
}
