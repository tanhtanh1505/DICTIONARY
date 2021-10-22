package com.example.dictionary;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
          FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        final int width = 700, height = 500;
        Scene scene = new Scene(fxmlLoader.load(), height, width);
        stage.setMinHeight(height);
        stage.setMinWidth(width);

        stage.setMaxHeight(height);
        stage.setMaxWidth(width);

        stage.setTitle("Dictionary");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        BookMark.Save();
        History.Save();
        InitData.Save();
        System.out.println("Stopped");
    }
    public static void main(String[] args) {
        launch();
    }
}