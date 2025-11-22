package com.example.arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FireParticle extends MovableObject {
    private double alpha;
    private double life;
    private double radius;
    private double hue;

    public FireParticle(double x, double y) {
        super(x, y, 0, 0, 0);

        this.dx = (Math.random() - 0.5) * 3;
        this.dy = (Math.random() - 0.5) * 3;
        this.radius = 2 + Math.random() * 2;
        this.life = 0.8 + Math.random() * 0.4;
        this.alpha = 1.0;
        this.hue = Math.random() * 360;
    }

    public boolean update(double deltaTime) {
        x += dx * 60 * deltaTime;
        y += dy * 60 * deltaTime;
        radius *= 0.96;
        life -= deltaTime;
        alpha = Math.max(0, life);

        hue = (hue + 120 * deltaTime) % 360;

        return life > 0;
    }

    public void render(GraphicsContext gc) {
        gc.setGlobalAlpha(alpha);
        Color color = Color.hsb(hue, 1.0, 1.0);
        gc.setFill(color);
        gc.fillOval(x, y, radius * 2, radius * 2);
        gc.setGlobalAlpha(1.0);
    }
}
