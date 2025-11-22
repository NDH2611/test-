package com.example.arkanoid;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TwoPlayerGameEngine {
    private Stage stage;
    private Scene scene;
    private HBox container;
    private StackPane root;

    private AnimationTimer gameLoop;
    private long startTime;

    private PlayerArea player1Area;
    private PlayerArea player2Area;

    private boolean pPressed = false;
    private GameState currentState = GameState.READY;
    private Parent pauseMenu;
    private Parent endMenu;
    private MenuController menuController;
    private GameStateController troller;

    public TwoPlayerGameEngine(Stage stage) {
        this.stage = stage;

        player1Area = new PlayerArea();
        player2Area = new PlayerArea();

        container = new HBox();

        Rectangle separator = new Rectangle(3, GameConfig.HEIGHT);
        separator.setFill(GameConfig.SEPARATOR_COLOR);

        container.getChildren().addAll(
                player1Area.getCanvas(),
                separator,
                player2Area.getCanvas());

        root=new StackPane();
        root.getChildren().add(container);

        root=new StackPane();
        root.getChildren().add(container);
        scene = new Scene(root, GameConfig.WIDTH * 2 + 3, GameConfig.HEIGHT);
        scene.setOnKeyPressed(event -> handleKeyInput(event));
        scene.setOnKeyReleased(event -> handleKeyInput(event));
    }

    public void startGameLoop() {
        if (gameLoop != null) {
            return;
        }
        startTime = System.nanoTime();
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double deltaTime = (now - startTime) / 1000000000.0;
                startTime = now;
                updateGame(deltaTime);
                renderGame();
            }
        };
        gameLoop.start();
        System.out.println("game started");
    }

    public void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
            startTime = 0;
            System.out.println("game stopped.");
        }
    }

    private void updateGame(double deltaTime) {
        if (currentState != GameState.RUNNING) {
            return;
        }
        if(player1Area.isGameOver() && player2Area.isGameOver()) {
            showEndMenu();
            return;
        }
        boolean player1Restart=player1Area.needRestart();
        boolean player2Restart=player2Area.needRestart();
        if (player1Restart || player2Restart) {
            currentState = GameState.READY;
            stopGameLoop();
            return;
        }
        player1Area.update(deltaTime);
        player2Area.update(deltaTime);
    }

    public void handleKeyInput(KeyEvent event) {
        if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.D) {
            player1Area.getPaddles().get(0).handleInput(event, true);
        }

        if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
            player2Area.getPaddles().get(0).handleInput(event, false);
        }

        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            switch (event.getCode()) {
                case P:
                    if (!pPressed) {
                        pPressed = true;
                        if (currentState == GameState.RUNNING) {
                            pauseGame();
                        } else if (currentState == GameState.PAUSE) {
                            resumeGame();
                        }
                    }
                    break;

                case SPACE:
                    if (currentState == GameState.READY) {
                        player1Area.startBall();
                        player2Area.startBall();
                        currentState = GameState.RUNNING;
                        startGameLoop();
                    }
                    break;
            }
        } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            if (event.getCode() == KeyCode.P) {
                pPressed = false;
            }
        }
    }

    private void pauseGame() {
        currentState = GameState.PAUSE;
        stopGameLoop();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("state.fxml"));
            loader.setController(this);
            pauseMenu = loader.load();
            root.getChildren().add(pauseMenu);
            pauseMenu.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderGame() {
        player1Area.render();
        player2Area.render();
    }
    private void resumeGame() {
        currentState = GameState.RUNNING;
        if (pauseMenu != null) {
            root.getChildren().remove(pauseMenu);
            pauseMenu = null;
        }
        startGameLoop();
    }

    private void showEndMenu() {
        currentState = GameState.GAME_OVER;
        stopGameLoop();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("endgame.fxml"));
            loader.setController(this);
            endMenu = loader.load();

            String winnerMessage = "";
            if (player1Area.isGameOver() && !player2Area.isGameOver()) {
                winnerMessage = "Player 2 Wins!";
            } else if (player2Area.isGameOver() && !player1Area.isGameOver()) {
                winnerMessage = "Player 1 Wins!";
            } else {
                winnerMessage = "Draw!";
            }

            System.out.println("Game Over: " + winnerMessage);
            System.out.println("Player 1 Score: " + player1Area.getTotalScores());
            System.out.println("Player 2 Score: " + player2Area.getTotalScores());

            root.getChildren().add(endMenu);
            endMenu.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onPlayContinue() {
        resumeGame();
    }

    @FXML
    private void onRestartGame() {
        if (endMenu != null) {
            root.getChildren().remove(endMenu);
            endMenu = null;
        }

        Stage currentStage = (Stage) root.getScene().getWindow();
        TwoPlayerGameEngine newEngine = new TwoPlayerGameEngine(currentStage);
        newEngine.startGameLoop();
        currentStage.setScene(newEngine.getScene());


    }

    @FXML
    private void onReturnMenu() {
        if (pauseMenu != null) {
            root.getChildren().remove(pauseMenu);
            pauseMenu = null;
        }
        if (endMenu != null) {
            root.getChildren().remove(endMenu);
            endMenu = null;
        }
        Stage stage = (Stage) root.getScene().getWindow();
        GameStateController.showMenu(stage);
    }
    @FXML
    private void onViewLeaderboard () {
        try {
            menuController.onLeaderboard();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Scene getScene() {
        return scene;
    }
    public StackPane getRoot() {
        return root;
    }
}
