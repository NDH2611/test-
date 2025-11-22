package com.example.arkanoid;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class LeaderboardController {
    @FXML
    private TableView<ScoreRecord> leaderboardTable;
    @FXML
    private TableColumn<ScoreRecord, Integer> rankColumn;
    @FXML
    private TableColumn<ScoreRecord, String> playerColumn;
    @FXML
    private TableColumn<ScoreRecord, Integer> levelColumn;
    @FXML
    private TableColumn<ScoreRecord, Integer> scoreColumn;
    @FXML
    private ComboBox<String> modeSelector;
    @FXML
    private Button backButton;
    @FXML
    private VBox leaderboardContainer;

    private DatabaseManager dbManager;
    private Stage stage;
    private String currentMode = "Solo";

    public void initialize() {
        dbManager = DatabaseManager.getInstance();
        ObservableList<String> modes = FXCollections.observableArrayList("Solo");
        modeSelector.setItems(modes);
        modeSelector.setValue(currentMode);
        loadLeaderboard(currentMode);
        modeSelector.setOnAction(event -> {
            currentMode = modeSelector.getValue();
            loadLeaderboard(currentMode);
        });
    }

    private void loadLeaderboard(String mode) {
        leaderboardContainer.getChildren().clear();

        List<ScoreRecord> scores = dbManager.getTopScore(mode, 10);

        for (int i = 0; i < scores.size(); i++) {
            ScoreRecord record = scores.get(i);
            HBox row = createLeaderboardRow(i + 1, record);
            leaderboardContainer.getChildren().add(row);
        }
    }

    private HBox createLeaderboardRow(int rank, ScoreRecord record) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 20, 12, 20));
        row.setSpacing(15);
        row.setPrefHeight(60);

        String backgroundColor;
        String medalIcon = "";

        if (rank == 1) {
            backgroundColor = "linear-gradient(from 0% 0% to 100% 0%, #efbf04 43%, #ffd7a0 100%)";
            medalIcon = "\uD83E\uDD47";
        } else if (rank == 2) {
            backgroundColor = "linear-gradient(from 0% 0% to 100% 0%, #c4c4c4 65%, #e7fdd733 100%)";
            medalIcon = "\uD83E\uDD48";
        } else if (rank == 3) {
            backgroundColor = "linear-gradient(from 0% 0% to 100% 0%, #ce8946 65%, #f1d18e33 100%)";
            medalIcon = "\uD83E\uDD49";
        } else {
            backgroundColor = "linear-gradient(from 0% 0% to 100% 0%, #d3cce3 65%, #e9e4f033 100%)";
        }
        row.setStyle(String.format(
                "-fx-background-color: %s; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: #f2e2d24d; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-width: 1; " +
                        "-fx-effect: dropshadow(gaussian, #0000004d, 5, 0.3, 0, 2);",
                backgroundColor
        ));
        Label rankLabel = new Label(rank <= 3 ? medalIcon + " " : String.valueOf(rank));
        rankLabel.setMinWidth(60);
        rankLabel.setStyle("-fx-text-fill: #313e50; -fx-font-size: 22px; -fx-font-weight: bold;");

        Label playerLabel = new Label(record.getUserName());
        playerLabel.setMinWidth(200);
        playerLabel.setStyle("-fx-text-fill: #313e50; -fx-font-size: 16px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        VBox levelBox = new VBox(2);
        levelBox.setAlignment(Pos.CENTER);
        Label levelTitle = new Label("Level");
        levelTitle.setStyle("-fx-text-fill: #313e50; -fx-font-size: 10px;");
        Label levelValue = new Label(String.valueOf(record.getLevelNum()));
        levelValue.setStyle("-fx-text-fill: #313e50; -fx-font-size: 16px; -fx-font-weight: bold;");
        levelBox.getChildren().addAll(levelTitle, levelValue);
        levelBox.setMinWidth(60);

        VBox scoreBox = new VBox(2);
        scoreBox.setAlignment(Pos.CENTER);
        Label scoreTitle = new Label("Score");
        scoreTitle.setStyle("-fx-text-fill: #313e50; -fx-font-size: 10px;");
        Label scoreValue = new Label(String.valueOf(record.getScore()));
        scoreValue.setStyle("-fx-text-fill: #313e50; -fx-font-size: 16px; -fx-font-weight: bold;");
        scoreBox.getChildren().addAll(scoreTitle, scoreValue);
        scoreBox.setMinWidth(80);

        row.getChildren().addAll(rankLabel, playerLabel, levelBox, scoreBox);
        return row;
    }

    @FXML
    private void onBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Arkanoid");
        MenuController controller = (MenuController) loader.getController();

    }

    @FXML
    private void onButtonHover() {
        backButton.setStyle(backButton.getStyle() + "-fx-background-color: #ff5a75;");
    }

    @FXML
    private void onButtonExit() {
        backButton.setStyle(backButton.getStyle().replace("-fx-background-color: #ff5a75;", "-fx-background-color: #e94560;"));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMode(String mode) {
        this.currentMode = mode;
        if (modeSelector != null) {
            modeSelector.setValue(mode);
            loadLeaderboard(mode);
        }
    }
}
