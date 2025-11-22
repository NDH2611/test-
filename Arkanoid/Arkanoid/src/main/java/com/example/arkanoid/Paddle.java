package com.example.arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;

public class Paddle extends MovableObject {
    private static final double SPEED = 6.0;

    private boolean moveLeft = false;
    private boolean moveRight = false;
    private Rectangle rectangle;

    private Image paddleImage;

    private Image fireSpriteSheet;
    private int frameCount = 6;
    private int currentFrame = 0;
    private double frameWidth = 32;
    private double frameHeight = 32;
    private long lastFrameTime = 0;
    private long frameDuration = 100_000_000;

    public Paddle(double x, double y, double width, double height, double speed) {
        super(x, y, width, height, speed);
        rectangle = new Rectangle(x, y, width, height);

        try {
            paddleImage = new Image(getClass().getResourceAsStream("images/paddle.png"));
        } catch (Exception e) {
            System.err.println("Không tìm thấy paddle.png");
        }

        try {
            fireSpriteSheet = new Image(getClass().getResourceAsStream("images/firelgbt.png"));
        } catch (Exception e) {
            System.err.println("Không tìm thấy firelgbt.png");
        }
    }

    public void handleInput(KeyEvent event, boolean isPlayer2) {
        if (isPlayer2) {
            if (event.getEventType() == KeyEvent.KEY_PRESSED) {
                switch (event.getCode()) {
                    case A:
                        moveLeft = true;
                        break;
                    case D:
                        moveRight = true;
                        break;
                    default:
                        break;
                }
            } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
                switch (event.getCode()) {
                    case A:
                        moveLeft = false;
                        break;
                    case D:
                        moveRight = false;
                        break;
                    default:
                        break;
                }
            }
        } else {
            if (event.getEventType() == KeyEvent.KEY_PRESSED) {
                switch (event.getCode()) {
                    case LEFT:
                        moveLeft = true;
                        break;
                    case RIGHT:
                        moveRight = true;
                        break;
                    default:
                        break;
                }
            } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
                switch (event.getCode()) {
                    case LEFT:
                        moveLeft = false;
                        break;
                    case RIGHT:
                        moveRight = false;
                        break;
                    default:
                        break;
                }
            }
        }

        if (moveLeft && !moveRight) {
            dx = -SPEED;
        } else if (moveRight && !moveLeft) {
            dx = SPEED;
        } else {
            dx = 0;
        }
    }

    public void render(GraphicsContext gc) {
        if (fireSpriteSheet != null) {
            double fireWidth = 96;
            double fireHeight = 96;
            double fireX = x + (width - fireWidth) / 2;
            double fireY = y + height - 5;

            double sx = currentFrame * frameWidth;
            double sy = 0;

            gc.drawImage(
                    fireSpriteSheet,
                    sx, sy, frameWidth, frameHeight,
                    fireX, fireY, fireWidth, fireHeight
            );
        }

        if (paddleImage != null) {
            gc.drawImage(paddleImage, x, y, width, height);
        } else {
            gc.setFill(Color.rgb(242, 226, 210));
            gc.fillRect(x, y, width, height);
        }

    }

    public void update(double deltaTime) {
        setX(x + dx*deltaTime*60);
        CheckCollision.checkPaddleWallCollision(this);

        long now = System.nanoTime();
        if (now - lastFrameTime > frameDuration) {
            currentFrame = (currentFrame + 1) % frameCount;
            lastFrameTime = now;
        }
    }

    @Override
    public void setX(double x) {
        this.x = x;
        rectangle.setX(x);
    }

    @Override
    public void setY(double y) {
        this.y = y;
        rectangle.setY(y);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }
}
