package com.example.arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HealthPowerUp extends PowerUp {
    public HealthPowerUp(double x, double y) {
        super(x, y, PowerUpType.HEALTH, 5000, false);
    }
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.PINK);
        gc.fillRect(x,y,width,height);
        gc.setFill(Color.WHITE);
        gc.fillText("H", x+6, y+14);
    }
}
