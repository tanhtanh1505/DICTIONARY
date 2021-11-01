package com.example.dictionary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("first-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setTitle("Dictionary");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        BookMark.Save();
        History.Save();
        InitData.Save();
        RankHangMan.Save();
        System.out.println("Stopped!");
    }

    public static void main(String[] args) {
        launch();
    }
}