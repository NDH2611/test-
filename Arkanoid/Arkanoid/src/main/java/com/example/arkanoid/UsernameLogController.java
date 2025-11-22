package com.example.arkanoid;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UsernameLogController {
    @FXML
    private TextField usernameField;
    @FXML
    private Button continueButton;
    private boolean confirmed = false;
    private String username = "player";
    private Stage logStage;

    @FXML
    private void initialize() {
        usernameField.requestFocus();
        usernameField.setOnAction(event -> {
            handleConfirm();
        });
    }

    @FXML
    private void handleConfirm() {
        String input = usernameField.getText();
        if (usernameValidate(input)) {
            this.setUsername(input);
        } else {
            this.setUsername("player");
        }
        confirmed = true;
        if (logStage != null) {
            logStage.close();
        }
    }

    private boolean usernameValidate(String input) {
        if (input.isEmpty() || input.length() >= 20) return false;
        for (char c : input.toCharArray()) {
            if (Character.isLetterOrDigit(c) || c == '_') return true;
        }
        return false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Stage getLogStage() {
        return logStage;
    }

    public void setLogStage(Stage logStage) {
        this.logStage = logStage;
    }
}
