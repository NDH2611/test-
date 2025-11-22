package com.example.arkanoid;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ModeController extends MenuController {
    @FXML
    private Button Solo;
    @FXML
    private Button Duel;

    @FXML
    public void onSolo() {
        try {
            Stage stage = (Stage) Solo.getScene().getWindow();
            GameEngine game = new GameEngine(stage);
            game.inputUsername();

            stage.setScene(game.getScene());
            game.getRoot().requestFocus();
            stage.show();

            game.getTroller().setState(GameState.READY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onDuel() {
        try {
            Stage stage = (Stage) Duel.getScene().getWindow();
            TwoPlayerGameEngine game = new TwoPlayerGameEngine(stage);
            stage.setScene(game.getScene());
            stage.show();
            game.startGameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
