package com.example.arkanoid;

import javafx.scene.canvas.GraphicsContext;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Brick extends GameObject {
    private TYPE type;
    private boolean visible;
    private Rectangle rectangle;
    private int strength;
    private int score;

    public Brick(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.x = x;
        this.y = y;
        this.visible = true;
        this.rectangle = new Rectangle(x, y, width, height);
        //this.breakable =true;
    }

    public void brickStatus() {
        switch (type) {
            case ORANGE:
                this.setStrength(2);
                this.setScore(20);
                break;
            case YELLOW:
                this.setStrength(1);
                this.setScore(10);
                break;
            case BLUE:
                this.setScore(30);
                this.setStrength(1);
                break;
            default:
                this.setStrength(1);
                this.setScore(10);
                break;
        }
    }

    public void render(GraphicsContext gc) {
        if (visible) {
            Color color;
            switch (type) {
                case GREEN:
                    color = Color.rgb(94, 252, 141);
                    break;
                case ORANGE:
                    color = Color.rgb(250, 130, 76);
                    break;
                case YELLOW:
                    color = Color.rgb(255, 230, 109);
                    break;
                case BLUE:
                    color = Color.rgb(124, 152, 179);
                    break;
                case PINK:
                    color = Color.rgb(220, 117, 143);
                    break;
                case MAROON:
                    color = Color.MAROON;
                    break;
                default:
                    color = Color.rgb(94, 252, 141);
                    break;
            }
            if (type == TYPE.ORANGE) {
                double opacity = strength / 2.0;
                color = Color.color(
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue(),
                        opacity
                );
            }
            gc.setFill(color);
            gc.fillRect(x, y, width, height);
        }
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    enum TYPE {
        GREEN,
        YELLOW,
        BLUE,
        PINK,
        ORANGE,
        MAROON
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
