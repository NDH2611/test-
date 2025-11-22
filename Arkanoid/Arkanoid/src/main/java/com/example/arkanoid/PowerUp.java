package com.example.arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PowerUp extends GameObject{
    private static final double FALL_SPEED = 2;
    private static final double POWER_UP_WIDTH = 20;
    private static final double POWER_UP_HEIGHT = 20;

    protected double duration;
    protected Rectangle rectangle;
    protected PowerUpType powerUpType;
    protected boolean active;

    public PowerUp(double x, double y, PowerUpType type , double duration, boolean active) {
        super(x, y, POWER_UP_WIDTH, POWER_UP_HEIGHT);
        this.duration = duration;
        this.powerUpType = type;
        this.rectangle = new Rectangle(x, y, POWER_UP_WIDTH, POWER_UP_HEIGHT);
    }

    public void render(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillRect(x, y, POWER_UP_WIDTH, POWER_UP_HEIGHT);
    }

    public void update(double deltaTime) {
        y += (FALL_SPEED*deltaTime*60);
        rectangle.setY(y);
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public PowerUpType getPowerUpType() {
        return powerUpType;
    }

    public void setPowerUpType(PowerUpType powerUpType) {
        this.powerUpType = powerUpType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    enum PowerUpType {
        EXPAND_PADDLE,
        SHRINK_PADDLE,
        DOUBLE_BALL,
        HEALTH
    }
}
