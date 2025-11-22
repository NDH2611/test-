package com.example.arkanoid;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;
import java.io.IOException;

public class GameStateController {
    @FXML
    public Button ViewLeaderboard;
    @FXML
    private ImageView Leaderboard;
    @FXML
    private ImageView ReturnMenu;
    @FXML
    private ImageView PlayContinue;

    private GameState currentState = GameState.MENU;
    private GameEngine gameEngine;
    private static Stage stage;
    private Parent pauseMenu;
    private Parent endMenu;

    private static MusicManager musicManager=MusicManager.getInstance();

    @FXML
    private void onReturnMenu() {
        if(pauseMenu != null) {
            gameEngine.getRoot().getChildren().remove(pauseMenu);
        }
        showMenu(this.getStage());
    }

    @FXML
    private void onPlayContinue() {
        if(pauseMenu != null) {
            gameEngine.getRoot().getChildren().remove(pauseMenu);
        }
        for (PowerUp powerUp : gameEngine.getActivePowerUps()) {
            if (powerUp instanceof ShrinkPaddlePowerUp) {
                ((ShrinkPaddlePowerUp) powerUp).resumeEffect();
            } else if (powerUp instanceof ExpandPaddlePowerUp) {
                ((ExpandPaddlePowerUp) powerUp).resumeEffect();
            }
        }
        setState(GameState.RUNNING);
    }

    public GameStateController() {
    }

    public GameStateController(Stage stage, GameEngine gameEngine) {
        this.stage = stage;
        this.gameEngine = gameEngine;
    }

    public void setState(GameState newState) {
        this.currentState = newState;
        System.out.println("state changed");
        switch (newState) {
            case MENU:
                showMenu(this.getStage());
                break;
            case RUNNING:
                startGame();
                break;
            case PAUSE:
                pauseGame();
                break;
            case GAME_OVER:
                gameOverRestart();
                break;
            case READY:
                gameEngine.startGameLoop();
                break;
            case LEADERBOARD:
                onViewLeaderboard();
                break;
        }
    }

    private Stage getStage() {
        return stage;
    }

    public GameState getState() {
        return currentState;
    }

    public void onViewLeaderboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("leaderboard.fxml"));
            Parent root = loader.load();
            LeaderboardController controller = loader.getController();

            Stage leaderboardStage = (Stage) getLeaderboard().getScene().getWindow();
            leaderboardStage.setTitle("Leaderboard");
            leaderboardStage.setScene(new Scene(root));
            controller.setStage(leaderboardStage);
            controller.setMode("Solo");
            leaderboardStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showMenu(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(GameStateController.class.getResource("menu.fxml"));
            Scene menuScene = new Scene(loader.load());
            stage.setScene(menuScene);
            stage.centerOnScreen();
            musicManager.playMusic("menu");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startGame() {
        if(pauseMenu != null) {
            gameEngine.getRoot().getChildren().remove(pauseMenu);
            pauseMenu = null;
        }
        stage.setScene(gameEngine.getScene());
        stage.show();
        gameEngine.getScene().getRoot().requestFocus();
        gameEngine.startGameLoop();
        for (PowerUp powerUp : gameEngine.getActivePowerUps()) {
            if (powerUp instanceof ShrinkPaddlePowerUp) {
                ShrinkPaddlePowerUp shr = (ShrinkPaddlePowerUp) powerUp;
                shr.resumeEffect();

            } else if (powerUp instanceof ExpandPaddlePowerUp) {
                ExpandPaddlePowerUp exp = (ExpandPaddlePowerUp) powerUp;
                exp.resumeEffect();
            }
        }
    }

    private void pauseGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("state.fxml"));
            loader.setController(this);
            pauseMenu = loader.load();

            gameEngine.getRoot().getChildren().add(pauseMenu);
            pauseMenu.toFront();
            pauseMenu.requestFocus();
            pauseMenu.setPickOnBounds(true);

            gameEngine.stopGameLoop();

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (PowerUp powerUp : gameEngine.getActivePowerUps()) {
            if (powerUp instanceof ShrinkPaddlePowerUp) {
                ShrinkPaddlePowerUp shr = (ShrinkPaddlePowerUp) powerUp;
                shr.pauseEffect();
            } else if (powerUp instanceof ExpandPaddlePowerUp) {
                ExpandPaddlePowerUp exp = (ExpandPaddlePowerUp) powerUp;
                exp.pauseEffect();
            }
        }
    }

    private void gameOverRestart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("endgame.fxml"));
            loader.setController(this);
            endMenu = loader.load();

            ImageView playBtn = (ImageView) endMenu.lookup("#PlayContinue");
            playBtn.setOnMouseClicked(e -> onRestartGame());

            gameEngine.getRoot().getChildren().add(endMenu);
            endMenu.toFront();
            endMenu.requestFocus();
            endMenu.setPickOnBounds(true);

            gameEngine.stopGameLoop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void onRestartGame() {
        System.out.println("Restart clicked");
        if (gameEngine == null) {
            System.out.println("gameEngine is null!");
            return;
        }
        if(endMenu != null) {
            gameEngine.getRoot().getChildren().remove(endMenu);
            endMenu = null;
        }
        gameEngine.restartGame();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    public ImageView getLeaderboard() {
        return Leaderboard;
    }
}
