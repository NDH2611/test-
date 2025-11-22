package com.example.arkanoid;

public abstract class MovableObject extends GameObject {
    protected double dx;
    protected double dy;
    protected double speed;

    public MovableObject(double x, double y, double width, double height, double speed) {
        super(x, y, width, height);
        this.speed = speed;
        dx = speed;
        dy = speed;
    }

    public void move() {
        this.x += dx;
        this.y += dy;
    }

    public void stop() {
        dx = dy = 0;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
