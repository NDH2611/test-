package com.example.arkanoid;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class UsernameLog {
    private UsernameLogController controller;
    private Stage logStage;

    public UsernameLog(Stage owner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UsernameLog.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            logStage = new Stage();
            logStage.initModality(Modality.APPLICATION_MODAL);
            logStage.initOwner(owner);
            logStage.setTitle("Login");
            logStage.setResizable(false);

            Scene scene = new Scene(root, 400, 300);
            logStage.setScene(scene);

            controller.setLogStage(logStage);
        } catch (IOException e) {
            System.err.println("Error loading FXML");
            e.printStackTrace();
        }
    }

    public String showAndWait() {
        if (logStage != null) {
            logStage.showAndWait();
        }
        if (controller != null) {
            return controller.getUsername();
        }
        return "player";
    }

    public String getUsername() {
        return controller != null ? controller.getUsername() : "player";
    }

    public boolean isConfirmed() {
        return controller != null && controller.isConfirmed();
    }
}
