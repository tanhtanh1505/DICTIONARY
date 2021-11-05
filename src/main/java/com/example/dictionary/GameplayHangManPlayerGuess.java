package com.example.dictionary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class GameplayHangManPlayerGuess{
    @FXML
    private Label guessWord;
    @FXML
    private TextField guessedWord;
    @FXML
    private Label lives;

    @FXML
    private Label score;

    private GameHangManPlayerGuess game = new GameHangManPlayerGuess();
    private String namePlayer = "";

    public void initGame(String name) {
        namePlayer = name;
        game.init();
        guessWord.setText(game.getGuessedWord());
        lives.setText(String.valueOf(game.getNumberHeart()));
        score.setText("Score: " + String.valueOf(game.getScore()));
    }

    public void updateGame(){
        if(game.getStatus()){
            game.check(guessedWord.getText().charAt(0));
            guessWord.setText(game.getGuessedWord());
            guessedWord.clear();
            lives.setText(String.valueOf(game.getNumberHeart()));
            score.setText("Score: " + String.valueOf(game.getScore()));

            if(game.getWin()){
                Alert notice = new Alert(Alert.AlertType.INFORMATION);
                notice.setTitle("Congratulation!");
                notice.setContentText("Your Score: " + String.valueOf(game.getScore()));
                notice.setHeaderText("You Win!!!");
                notice.show();
            }
        }
        else {
            Alert alError = new Alert(Alert.AlertType.INFORMATION);
            alError.setContentText("Your Score: " + String.valueOf(game.getScore()));
            alError.setHeaderText("You Loss");
            alError.show();
        }
    }
    public void checkGuessedWord(ActionEvent event) {
        if(guessedWord.getText().length() > 0)
            updateGame();
    }

    public void checkByEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            if(guessedWord.getText().length() > 0)
                updateGame();
        }
    }

    public void getSuggestWord(ActionEvent event) {
        guessedWord.setText(game.getSuggestWord());
        if(guessWord.getText() != null)
            updateGame();
    }

    public void exit(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure want to exit game?");
        alert.setContentText("Click OK to confirm");

        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            //Luu diem
            RankHangMan.add(namePlayer, game.getScore());

            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("hang-man.fxml"));
            Parent mainParent = loader.load();
            Scene scene = new Scene(mainParent);
            stage.setScene(scene);
        }
    }
}
