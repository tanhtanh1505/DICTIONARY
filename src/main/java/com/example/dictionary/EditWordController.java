package com.example.dictionary;

import com.example.GoogleAPI.Language;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;


public class EditWordController {
    private String language = "invalid";

    @FXML
    public TextArea infoWord;
    @FXML
    public Label word;
    @FXML
    private WebView previewChange;

    public void setInfoWord(String w, String lang){
        word.setText(w);
        if(lang.equals("ENGLISH") || lang.equals(Language.ENGLISH))
            infoWord.setText(InitData.getHashMapEV().get(w.toLowerCase()));
        else
            infoWord.setText(InitData.getHashMapVE().get(w.toLowerCase()));
        language = lang;
        previewChange.getEngine().loadContent(infoWord.getText(), "text/html");
    }

    public void checkKeyType(KeyEvent keyEvent) {
        previewChange.getEngine().loadContent(infoWord.getText(), "text/html");
    }

    public void submit(ActionEvent event) throws IOException {
        InitData.editWord(word.getText().toLowerCase(), infoWord.getText().toLowerCase(), language);
        Notifications.create()
                .title("Edit successfully")
                .text(word.getText() + " is edited!").darkStyle()
                .showInformation();
        cancel(event);
    }

    public void cancel(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("hello-view.fxml"));
        Parent mainParent = loader.load();
        Scene scene = new Scene(mainParent);
        stage.setScene(scene);
    }
}
