package com.example.dictionary;

import com.example.GoogleAPI.Language;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    //Dich 1 tu
    @FXML
    private ComboBox<String> selectLangFrom;
    @FXML
    private TextField wordFind;
    @FXML
    private Label word;
    @FXML
    ListView<String> listWord;
    @FXML
    private WebView infoWord;
    @FXML
    private Button editWordBtn;

    //Dich 1 doan van
    @FXML
    private TextArea EasayStart, EasayResult;
    @FXML
    private ComboBox<String> langFrom, langTo;

    //Book Mark
    @FXML
    private ListView<String> listBookMark;
    @FXML
    private WebView infoWordInBookMark;

    //History
    @FXML
    private ListView<String> listHistory;

    //Add word
    @FXML
    private ComboBox<String> selectLangNewWord;
    @FXML
    private TextField newWord;
    @FXML
    private TextArea meanOfNewWord;

    //Khoi tao du lieu
    @Override
    public void initialize(URL location, ResourceBundle resources){
        InitData.loadData();
        BookMark.load();
        History.load();

        HashMap<String, String> dataLanguage = Language.getInstance().getListLanguage();
        ObservableList<String> data = FXCollections.observableArrayList(dataLanguage.values());
        ObservableList<String> selectLang = FXCollections.observableArrayList("ENGLISH", "VIETNAMESE");

        selectLangFrom.setItems(selectLang);
        selectLangFrom.getSelectionModel().select(0);

        Collections.sort(data);
        langFrom.setItems(data);
        langFrom.getSelectionModel().select("ENGLISH");
        langTo.setItems(data);
        langTo.getSelectionModel().select("VIETNAMESE");

        listBookMark.setItems(FXCollections.observableArrayList(BookMark.getBookMark()));

        listHistory.setItems(FXCollections.observableArrayList(History.getListHistory()));

        selectLangNewWord.setItems(selectLang);
    }

    //Xu li dich 1 tu
    public void keyHandle(KeyEvent ke) {
        if (ke.getCode().equals(KeyCode.ENTER)){
            if(wordFind.getText().length() > 0) {
                String s = TranslateOffline.textTranslate(selectLangFrom.getValue(), wordFind.getText());
                word.setText(wordFind.getText().toUpperCase());
                infoWord.getEngine().loadContent(s, "text/html");
                History.add(word.getText());
                listHistory.getItems().add(word.getText());
            }
        }
    }

    public void keyType(){
        ObservableList<String> listSuggest = FXCollections.observableArrayList(
                TranslateOffline.listWordSuggestE(wordFind.getText(), selectLangFrom.getValue()));
        listWord.setItems(listSuggest);

    }

    public void selectWordSuggest(MouseEvent mouseEvent) {
        String s = TranslateOffline.textTranslate(selectLangFrom.getValue(), listWord.getSelectionModel().getSelectedItem());
        word.setText(listWord.getSelectionModel().getSelectedItem().toUpperCase());
        infoWord.getEngine().loadContent(s, "text/html");
        History.add(word.getText());
        listHistory.getItems().add(word.getText());
    }

    public void speakWord(MouseEvent mouseEvent) throws IOException, JavaLayerException {
        if(mouseEvent.getClickCount() == 1){
            if(selectLangFrom.getValue().equals(Language.ENGLISH)){
                TranslateByGoogle.speak(word.getText(), Language.VIETNAMESE);
            }
            else {
                TranslateByGoogle.speak(word.getText(), Language.ENGLISH);
            }
        }
    }

    //Xu li dich 1 doan van
    public void essayTranslate(ActionEvent event) {
        try{
            String s = TranslateByGoogle.essayTranslate(langFrom.getValue(), langTo.getValue(), EasayStart.getText());
            EasayResult.setText(s);
        }
        catch (Exception e){
            Alert alError = new Alert(Alert.AlertType.ERROR);
            alError.setContentText(e.getMessage());
            alError.show();
        }
    }

    public void speakEssay(MouseEvent mouseEvent) throws IOException, JavaLayerException {
        if(mouseEvent.getClickCount() == 1){
            TranslateByGoogle.speakEssay(EasayResult.getText(), Language.ENGLISH);
        }
    }

    //BookMark
    public void mark(ActionEvent event) {
        BookMark.addWord(word.getText());
        if(!listBookMark.getItems().contains(word.getText())) {
            listBookMark.getItems().add(word.getText());
        }
    }

    public void selectWordInBookMark(MouseEvent mouseEvent) {
        String s = TranslateOffline.textTranslate(Language.ENGLISH, listBookMark.getSelectionModel().getSelectedItem());
        infoWordInBookMark.getEngine().loadContent(s, "text/html");
    }

    public void deleteAWordInBookMark(ActionEvent event) {
        BookMark.deleteWord((String) listBookMark.getSelectionModel().getSelectedItem());
        listBookMark.getItems().remove(listBookMark.getSelectionModel().getSelectedIndex());
    }

    public void deleteBookMark(ActionEvent event) {
        BookMark.deleteAll();
        listBookMark.getItems().clear();
    }

    //History
    public void deleteHistory(ActionEvent event) {
        listHistory.getItems().clear();
        History.deleteAll();
    }

    //Edit Word
    public void addNewWord(ActionEvent event) {
        InitData.addAWord(newWord.getText(), meanOfNewWord.getText(), selectLangNewWord.getValue());
    }

    public void editWord(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("edit-word-window.fxml"));
        Parent eWordParent = loader.load();
        Scene scene = new Scene(eWordParent);
        EditWordController controller = loader.getController();
        controller.setInfoWord(word.getText(), selectLangFrom.getValue());
        stage.setScene(scene);
    }

    public void deleteAWordInData(ActionEvent event) {
        InitData.removeWord(listWord.getSelectionModel().getSelectedItem(), selectLangFrom.getValue());
        listWord.getItems().remove(listWord.getSelectionModel().getSelectedIndex());
    }

    //Game
    public void switchToHangMan(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("hang-man.fxml"));
        Parent eWordParent = loader.load();
        Scene scene = new Scene(eWordParent);
        stage.setScene(scene);
    }
}