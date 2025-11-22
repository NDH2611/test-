package com.example.arkanoid;

import javafx.animation.PauseTransition;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ShrinkPaddlePowerUp extends PowerUp {
    private PauseTransition timer;

    public ShrinkPaddlePowerUp(double x, double y) {
        super(x, y, PowerUpType.SHRINK_PADDLE, 5000, true);
        timer = new PauseTransition(Duration.millis(duration));
    }

    public void applyEffect(Paddle paddle) {
        paddle.setWidth(GameConfig.PADDLE_WIDTH * 0.75);
        paddle.getRectangle().setWidth(paddle.getWidth());
        timer.setOnFinished(event -> {
            System.out.println("Shrink Paddle PowerUp Finished");
            paddle.setWidth(GameConfig.PADDLE_WIDTH);
            paddle.getRectangle().setWidth(paddle.getWidth());
            active = false;
        });
        timer.play();
    }

    public void pauseEffect() {
        if (timer != null) timer.pause();
    }

    public void resumeEffect() {
        if (timer != null) timer.play();
    }

    public void removeEffect(Paddle paddle) {
        paddle.setWidth(GameConfig.PADDLE_WIDTH);
        paddle.getRectangle().setWidth(paddle.getWidth());
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(x, y, rectangle.getWidth(), rectangle.getHeight());
        gc.setFill(Color.BLACK);
        gc.fillText("S", x + 7, y + 14);
    }

}
