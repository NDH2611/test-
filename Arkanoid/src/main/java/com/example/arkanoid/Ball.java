package com.example.arkanoid;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball extends MovableObject implements Prototype {
    protected double radius;
    protected Circle circle;

    private ArrayList<FireParticle> fireParticles = new ArrayList<>();

    public Ball(double x, double y, double radius, double speed) {
        super(x - radius, y - radius, radius * 2, radius * 2, speed);
        circle = new Circle(x, y, radius);
        this.radius = radius;
    }

    public Ball(Ball ball) {
        super(ball.getX(), ball.getY()
                , ball.getRadius() * 2
                , ball.getRadius() * 2
                , ball.getSpeed());

        this.circle = new Circle(ball.getX() + ball.getRadius()
                , ball.getY() + ball.getRadius()
                , ball.getRadius());

        this.radius = ball.getRadius();
    }

    public void render(GraphicsContext gc) {
        for (FireParticle p : fireParticles) {
            p.render(gc);
        }

        gc.setFill(Color.rgb(242, 226, 210));
        gc.fillOval(x, y, width, height);
    }

    public void update(double deltaTime) {
        setX(x += dx * deltaTime * 60);
        setY(y += dy * deltaTime * 60);
        CheckCollision.checkBallWallCollision(this);

        for (int i = 0; i < 3; i++) {
            fireParticles.add(new FireParticle(
                    x + radius + (Math.random() - 0.5) * 5,
                    y + radius + (Math.random() - 0.5) * 5
            ));
        }

        fireParticles.removeIf(p -> !p.update(deltaTime));
    }

    @Override
    public Ball clone() {
        return new Ball(this);
    }

    @Override
    public void setX(double x) {
        this.x = x;
        circle.setCenterX(x + width / 2);
    }

    @Override
    public void setY(double y) {
        this.y = y;
        circle.setCenterY(y + height / 2);
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
