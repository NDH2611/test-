package com.example.arkanoid;

import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.Interpolator;

import java.io.IOException;

public class MenuController {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private ImageView Start;
    @FXML
    private ImageView Mode;
    @FXML
    private ImageView Guide;
    @FXML
    private ImageView Exit;
    @FXML
    private Text text;
    @FXML
    private ImageView Leaderboard;
    @FXML
    private ImageView About;
    @FXML
    private Button backButton;
    @FXML
    private Button nextButton;

    private Stage stage;
    private GameEngine gameEngine;
    private MusicManager musicManager = MusicManager.getInstance();


    @FXML
    public void initialize() {
        if (Start == null) return;

        addHoverEffect(Mode);
        addHoverEffect(Guide);
        addHoverEffect(Exit);
        addHoverEffect(Leaderboard);
        addHoverEffect(About);
        addHoverEffect(Start);
    }

    private void addHoverEffect(javafx.scene.Node node) {
        if (node == null) return;
        node.setOnMouseEntered(e -> {
            node.setScaleX(1.15);
            node.setScaleY(1.15);
        });
        node.setOnMouseExited(e -> {
            node.setScaleX(1.0);
            node.setScaleY(1.0);
        });
    }

    @FXML
    private void onStart() {
        try {
            musicManager.playSoundEffect("button_toggle");

            playFadeOut(() -> {
                try {
                    System.out.println(">>> Starting GameEngine...");
                    Stage stage = (Stage) Start.getScene().getWindow();
                    GameEngine game = new GameEngine(stage);
                    System.out.println(">>> GameEngine created");
                    javafx.application.Platform.runLater(() -> {
                        game.inputUsername();
                        System.out.println(">>> Username done");
                        stage.setScene(game.getScene());
                        stage.centerOnScreen();
                        stage.show();

                        GameStateController controller = game.getTroller();
                        musicManager.playMusic("gameplay");
                        controller.setState(GameState.READY);
                        System.out.println(">>> Game displayed");
                    });

                    GameStateController controller = game.getTroller();
                    musicManager.playMusic("gameplay");
                    controller.setState(GameState.READY);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAbout() {

        switchSceneWithEffect("About.fxml");
    }

    @FXML
    private void onMode() {

        switchSceneWithEffect("mode.fxml");

    }

    @FXML
    private void comeBack() throws IOException {
        String currentFXML = getCurrentFXML();

        if (currentFXML.endsWith("hdsd.fxml") || currentFXML.endsWith("About.fxml")) {
            switchSceneWithEffect("menu.fxml");
        } else if (currentFXML.endsWith("hdsd2.fxml")) {
            switchSceneWithEffect("hdsd.fxml");
        } else if (currentFXML.endsWith("hdsd3.fxml")) {
            switchSceneWithEffect("hdsd.fxml");
        } else {
            System.out.println("Không có scene trước cho: " + currentFXML);
        }
    }

    @FXML
    private void toNext() {
        String currentFXML = getCurrentFXML();

        if (currentFXML.endsWith("hdsd.fxml")) {
            switchSceneWithEffect("hdsd2.fxml");
        } else if (currentFXML.endsWith("hdsd2.fxml")) {
            switchSceneWithEffect("hdsd3.fxml");
        } else {
            System.out.println("Không có scene tiếp theo cho: " + currentFXML);
        }
    }

    private String getCurrentFXML() {
        try {
            String url = rootPane.getScene().getRoot().getClass().getResource("").toString();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = stage.getScene();
            Object data = scene.getRoot().getUserData();
            return data == null ? "" : data.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    @FXML
    public void onLeaderboard() throws IOException {

        switchSceneWithEffect("leaderboard.fxml");
    }

    @FXML
    private void onGuide() {

        switchSceneWithEffect("hdsd.fxml");
    }

    @FXML
    private void onExit(javafx.scene.input.MouseEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void switchSceneWithEffect(String fxmlName) {
        musicManager.playSoundEffect("button_toggle");
        playFadeOut(() -> {
            try {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
                Parent newRoot = loader.load();

                newRoot.setUserData(fxmlName);

                Scene scene = new Scene(newRoot);
                stage.setScene(scene);
                stage.show();

                playFadeIn(newRoot);
                playSlideAnimation(newRoot);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void playFadeOut(Runnable afterFade) {
        System.out.println(">>> playFadeOut start, rootPane = " + rootPane);
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), rootPane);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> {
            System.out.println(">>> Fade done!");
            afterFade.run();
        });
        fadeOut.play();
    }

    private void playFadeIn(Parent newRoot) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), newRoot);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private void playSlideAnimation(Parent newRoot) {
        Node start = newRoot.lookup("#Start");
        Node mode = newRoot.lookup("#Mode");
        Node guide = newRoot.lookup("#Guide");
        Node about = newRoot.lookup("#About");
        Node exit = newRoot.lookup("#Exit");
        Node leaderboard = newRoot.lookup("#Leaderboard");

        slideIn(start, -300);
        slideIn(mode, - 450);
        slideIn(guide, -600);
        slideIn(leaderboard, -750);
        slideIn(about, -900);
        slideIn(exit, -1050);
    }

    private void slideIn(Node node, double fromX) {
        if (node == null) return;
        node.setTranslateX(fromX);
        TranslateTransition slide = new TranslateTransition(Duration.millis(800), node);
        slide.setToX(0);
        slide.setInterpolator(Interpolator.EASE_OUT);
        slide.play();
    }

}

