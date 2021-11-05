package com.example.dictionary;

import com.example.GoogleAPI.Language;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
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

    //Setting
    @FXML
    private ProgressBar progressReset;
    @FXML
    private Label statusReset;

    //Khoi tao du lieu
    @Override
    public void initialize(URL location, ResourceBundle resources){
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

    public void speakWord(ActionEvent event) throws IOException, JavaLayerException {
        if(selectLangFrom.getValue().equals(Language.ENGLISH)){
            TranslateByGoogle.speak(word.getText(), Language.VIETNAMESE);
        }
        else {
            TranslateByGoogle.speak(word.getText(), Language.ENGLISH);
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
        if(!listBookMark.getItems().contains(word.getText())) {
            BookMark.addWord(word.getText(), selectLangFrom.getValue());
            listBookMark.getItems().add(word.getText());
            Notifications.create()
                    .title("Mark success")
                    .text(word.getText() + " is marked!").darkStyle()
                    .showInformation();
        }
    }

    public void selectWordInBookMark(MouseEvent mouseEvent) {
        String s = BookMark.getMean((String)listBookMark.getSelectionModel().getSelectedItem());
        infoWordInBookMark.getEngine().loadContent(s, "text/html");
    }

    public void deleteAWordInBookMark(ActionEvent event) {
        BookMark.deleteWord((String) listBookMark.getSelectionModel().getSelectedItem());
        listBookMark.getItems().remove(listBookMark.getSelectionModel().getSelectedIndex());
        infoWordInBookMark.getEngine().loadContent("");
    }

    public void deleteBookMark(ActionEvent event) {
        BookMark.deleteAll();
        listBookMark.getItems().clear();
        infoWordInBookMark.getEngine().loadContent("");
    }

    //History
    public void deleteHistory(ActionEvent event) {
        listHistory.getItems().clear();
        History.deleteAll();
    }

    //Edit Word
    public void addNewWord(ActionEvent event) {
        if(InitData.addAWord(newWord.getText(), meanOfNewWord.getText(), selectLangNewWord.getValue())) {
            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setHeaderText("Add word successfully!");
            al.setContentText("You added new word: '" + newWord.getText().toUpperCase() + "' into " + selectLangNewWord.getValue() + " database");
            al.show();
        }
        newWord.clear();
        meanOfNewWord.clear();
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure want to delete this word?");
        alert.setContentText("Click OK to confirm");

        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            InitData.removeWord(listWord.getSelectionModel().getSelectedItem(), selectLangFrom.getValue());
            listWord.getItems().remove(listWord.getSelectionModel().getSelectedIndex());
            Notifications.create()
                    .title("Delete success")
                    .text(word.getText() + " is deleted!").darkStyle()
                    .showInformation();
        }
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

    //Setting
    loadData taskReset;

    public void restoreDatabase(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Data");
        alert.setHeaderText("Are you sure want to reset database?");
        alert.setContentText("Click OK to confirm");

        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            progressReset.setVisible(true);
            taskReset = new loadData();
            progressReset.progressProperty().bind(taskReset.progressProperty());
            statusReset.textProperty().bind(taskReset.messageProperty());
            new Thread(taskReset).start();
        }
    }

    public void reset(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Data");
        alert.setHeaderText("Are you sure want to reset all data?");
        alert.setContentText("Click OK to confirm");

        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            progressReset.setVisible(true);
            taskReset = new loadData();
            progressReset.progressProperty().bind(taskReset.progressProperty());
            statusReset.textProperty().bind(taskReset.messageProperty());
            new Thread(taskReset).start();

            History.deleteAll();
            BookMark.deleteAll();
            RankHangMan.deleteAll();
        }
    }

    class loadData extends Task<Void> {
        @Override
        protected Void call(){
            updateMessage("Please wait, it will take some minutes");
            InitData.reset(false);
            updateProgress(1,1);
            updateMessage("Done!");
            return null;
        }

    }
}