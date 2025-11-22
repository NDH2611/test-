package com.example.arkanoid;

import com.example.arkanoid.GameConfig;
import com.example.arkanoid.Level;
import com.example.arkanoid.Ball;
import com.example.arkanoid.Paddle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

public class GameRenderer {
    private GraphicsContext gc;
    private Font uiFont;

    public GameRenderer(GraphicsContext gc, Font uiFont) {
        this.gc = gc;
        this.uiFont = uiFont;
    }

    public void render(List<Ball> balls, List<Paddle> paddles, List<Level> levels, List<PowerUp> powerUps) {
        clearScreen();
        renderLevel(levels);
        renderBall(balls);
        renderPaddle(paddles);
        renderPowerUps(powerUps);
    }

    private void clearScreen() {
        gc.setFill(GameConfig.BACKGROUND_COLOR);
        gc.fillRect(0, 0, GameConfig.CANVAS_WIDTH, GameConfig.CANVAS_HEIGHT);
    }

    private void renderBall(List<Ball> balls) {
        if (balls.isEmpty()) {
            return;
        }
        for (int i = 0; i < balls.size(); i++) {
            balls.get(i).render(gc);
        }
    }

    private void renderPaddle(List<Paddle> paddles) {
        if (paddles.isEmpty()) {
            return;
        }
        for (int i = 0; i < paddles.size(); i++) {
            paddles.get(i).render(gc);
        }
    }

    private void renderLevel(List<Level> levels) {
        if (levels.isEmpty()) {
            return;
        }
        levels.get(0).render(gc);
    }

    private void renderPowerUps(List<PowerUp> powerUps) {
        if (powerUps.isEmpty()) {
            return;
        }
        for (int i = 0; i < powerUps.size(); i++) {
            powerUps.get(i).render(gc);
        }
    }

    public void renderUI(int totalScores, int lives, int currentLevel) {
        Font originalFont = gc.getFont();

        gc.setFont(uiFont);
        gc.setFill(GameConfig.UI_TEXT_COLOR);

        String scoreText = "Scores: " + String.valueOf(totalScores);
        Text scoreTextNode = new Text(scoreText);
        scoreTextNode.setFont(uiFont);
        double textWidth = scoreTextNode.getLayoutBounds().getWidth();
        double textHeight = scoreTextNode.getLayoutBounds().getHeight();
        double horizontalCenter = GameConfig.WIDTH / 2.0 - textWidth / 2.0;
        gc.fillText(scoreText, horizontalCenter, Level.getDistanceY() / 2.0 + textHeight / 2.0);

        String livesText = "Lives: " + String.valueOf(lives);
        Text livesTextNode = new Text(livesText);
        livesTextNode.setFont(uiFont);
        double livesTextWidth = livesTextNode.getLayoutBounds().getWidth();
        gc.fillText(livesText, 10, Level.getDistanceY() / 2.0 + textHeight / 2.0);

        String levelText = "Levels: " + String.valueOf(currentLevel);
        Text levelTextNode = new Text(levelText);
        levelTextNode.setFont(uiFont);
        double levelTextWidth = levelTextNode.getLayoutBounds().getWidth();
        gc.fillText(levelText, GameConfig.WIDTH - 150, Level.getDistanceY() / 2.0 + textHeight / 2.0);

        drawSeparatorLine();
        gc.setFont(originalFont);
    }

    private void drawSeparatorLine() {
        double lineY = Level.getDistanceY();
        gc.setStroke(Color.rgb(242, 226, 210));
        gc.setLineWidth(3);
        gc.strokeLine(0, lineY, GameConfig.WIDTH, lineY);
    }
}
