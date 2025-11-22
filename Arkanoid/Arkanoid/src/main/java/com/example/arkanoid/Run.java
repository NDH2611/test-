package com.example.arkanoid;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class Run extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        GameEngine engine = new GameEngine(stage);
        stage.setScene(engine.getScene());

        GameStateController troller = new GameStateController(stage, engine);
        troller.setState(GameState.MENU);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
