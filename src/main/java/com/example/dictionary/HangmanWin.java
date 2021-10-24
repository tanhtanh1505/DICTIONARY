package com.example.dictionary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HangmanWin {
    @FXML
    Label score;

    private String namePlayer = "";
    private int scoreGet = 0;

    public void initialize(String nameP, int sc) {
        score.setText(String.valueOf(sc));
        namePlayer = nameP;
        scoreGet = sc;
    }

    public void exit(ActionEvent event) throws IOException {
        //Luu diem
        RankHangMan.add(namePlayer, scoreGet);
        RankHangMan.Save();

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("hang-man.fxml"));
        Parent mainParent = loader.load();
        Scene scene = new Scene(mainParent);
        stage.setScene(scene);
    }
}
