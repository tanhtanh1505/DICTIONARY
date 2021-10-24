package com.example.dictionary;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FirstView implements Initializable {
    loadData task;

    @FXML
    ProgressBar progressBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        task = new loadData();
        progressBar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }

    public void start(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("hello-view.fxml"));
        Parent eWordParent = loader.load();
        Scene scene = new Scene(eWordParent);
        stage.setScene(scene);
    }
}

class loadData extends Task<Void>{
    @Override
    protected Void call(){
        try {
            //updateProgress(1,2);
            InitData.loadData();
            updateProgress(2,2);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
