package com.example.arkanoid;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



public class PlayerArea {
    private Canvas canvas;
    private GraphicsContext gc;
    private Font renderFont;
    private GameRenderer gameRenderer;

    private List<Paddle> paddles = new ArrayList<>();
    private List<Ball> balls = new ArrayList<>();
    private List<Level> levels = new ArrayList<>();
    private List<PowerUp> powerUps = new ArrayList<>();
    private List<PowerUp> activePowerUps = new ArrayList<>();
    private List<String> mapFiles = new ArrayList<>();
    private MusicManager musicManager = MusicManager.getInstance();
    private AnimationTimer gameLoop;
    private long startTime = 0;


    private int lives = 3;
    private int totalScores = 0;
    private int currentLevel = 1;

    public PlayerArea() {
        initialize();
    }

    private void initialize() {
        canvas = new Canvas(GameConfig.CANVAS_WIDTH , GameConfig.CANVAS_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        renderFont = loadFont(GameConfig.fontUse, 16);
        gameRenderer = new GameRenderer(gc, renderFont);
        loadMap(GameConfig.totalMap);
        Ball initialBall = new Ball(canvas.getWidth() / 2.0, GameConfig.HEIGHT - 120
                , GameConfig.BALL_RADIUS, GameConfig.BALL_SPEED);
        initialBall.setDx(0);
        initialBall.setDy(0);
        balls.add(initialBall);
        Paddle paddle = new Paddle(canvas.getWidth() / 2.0 - GameConfig.PADDLE_WIDTH / 2.0
                , canvas.getHeight() - 100, GameConfig.PADDLE_WIDTH, 15, 0);
        paddles.add(paddle);

        createLevel();
    }

    public void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
            startTime = 0;
            System.out.println("game stopped.");
        }
    }

    public void restartGame() {
        stopGameLoop();
        balls.clear();
        powerUps.clear();
        levels.clear();
        paddles.clear();
        this.setTotalScores(0);
        this.setLives(3);
        this.currentLevel = 1;
        initialize();

    }
    public void update(double deltaTime) {
        if(isGameOver()) {
            return;
        }
        for (int i = balls.size() - 1; i >= 0; i--) {
            Ball currentBall = balls.get(i);
            currentBall.update(deltaTime);
            CheckCollision.CollisionSide side = CheckCollision.checkCollision(currentBall.getCircle(), paddles.get(0).getRectangle());
            if (side == CheckCollision.CollisionSide.TOP) {
                musicManager.playSoundEffect("collide");
                CheckCollision.caculatedBallBounceAngle(balls.get(i), paddles.get(0));
            } else if (side == CheckCollision.CollisionSide.LEFT || side == CheckCollision.CollisionSide.RIGHT) {
                musicManager.playSoundEffect("collide");
                currentBall.setDx(-currentBall.getDx());
            }
            if (currentBall.getY() > canvas.getHeight()) {
                balls.remove(i);

            }
            if (balls.isEmpty()) {
                loseLife();

            }
            paddles.get(0).update(deltaTime);
            updatePowerUp(deltaTime);
        }
        updateLevel();
        if (checkLevelComplete()) {
            handleLevelComplete();
        }
    }

    private void loadMap(String fileName) {
        try {
            InputStream is = getClass().getResourceAsStream("map/" + fileName);
            System.out.println("Doc file!");
            if (is == null) {
                System.err.println("File not found");
                return;
            }
            System.out.println("Doc thanh cong");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    mapFiles.add(line);
                }
            }
            br.close();
        } catch (Exception e) {
            System.err.println("Error loading Map");
            e.printStackTrace();
        }
    }

    private Font loadFont(String fontName, int fontSize) {
        try {
            InputStream is = this.getClass().getResourceAsStream("font/" + fontName);
            if (is == null) {
                System.err.println("Font not found");
                return Font.font("Arial", fontSize);
            }
            return Font.loadFont(is, fontSize);
        } catch (Exception e) {
            System.err.println("Error loading font");
            e.printStackTrace();
            return Font.font("Arial", fontSize);
        }
    }


    private void createLevel() {
        if (mapFiles.isEmpty()) {
            System.err.println("No map files found");
            return;
        }
        int randomIndex = (int) (Math.random() * mapFiles.size());
        String fileName = mapFiles.get(randomIndex);
        System.out.println("Loading random map: " + fileName);
        Level level = new Level(fileName);
        levels.clear();
        levels.add(level);
    }

    private void updateLevel() {
        for (Ball ball : balls) {
            for (Brick brick : levels.get(0).getBricks()) {
                if (!brick.isVisible()) {
                    continue;
                }
                CheckCollision.CollisionSide side = CheckCollision.checkCollision(ball.getCircle(), brick.getRectangle());
                if (side != CheckCollision.CollisionSide.NONE) {
                    musicManager.playSoundEffect("collide");
                    if (side == CheckCollision.CollisionSide.LEFT || side == CheckCollision.CollisionSide.RIGHT) {
                        ball.setDx(-ball.getDx());
                    } else if (side == CheckCollision.CollisionSide.TOP || side == CheckCollision.CollisionSide.BOTTOM) {
                        ball.setDy(-ball.getDy());
                    }
                    brick.setStrength(brick.getStrength() - 1);
                    if (brick.getStrength() <= 0) {
                        brick.setVisible(false);
                        totalScores += brick.getScore();
                        System.out.println(totalScores);

                        System.out.println(brick.getType());
                        if (brick.getType() == Brick.TYPE.BLUE) {
                            PowerUp powerUp = new DoubleBallPowerUp(
                                    brick.getX() + brick.getWidth() / 2 - 10,
                                    brick.getY());
                            powerUps.add(powerUp);
                        } else if (brick.getType() == Brick.TYPE.PINK) {
                            PowerUp powerUp = new HealthPowerUp(brick.getX() + brick.getWidth() / 2 - 10,
                                    brick.getY());
                            powerUps.add(powerUp);
                        } else if (Math.random() < 0.25) {
                            if (Math.random() < 0.5) {
                                powerUps.add(new ExpandPaddlePowerUp(brick.getX() + 5, brick.getY() + 5));
                            } else {
                                powerUps.add(new ShrinkPaddlePowerUp(brick.getX() + 5, brick.getY() + 5));
                            }
                        }
                    }

                    break;
                }
            }
        }

    }

    private void updatePowerUp(double deltaTime) {
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp powerUp = powerUps.get(i);
            powerUp.update(deltaTime);
            if (powerUp.getY() > GameConfig.HEIGHT) {
                powerUps.remove(i);
                continue;
            }

            if (CheckCollision.checkCollision(powerUp.getRectangle(), paddles.get(0).getRectangle())) {
                if (powerUp instanceof ExpandPaddlePowerUp) {
                    System.out.println("ExpandPaddlePowerUp");
                    ExpandPaddlePowerUp exp = (ExpandPaddlePowerUp) powerUp;
                    exp.applyEffect(paddles.get(0));
                    activePowerUps.add(exp);
                    exp.setActive(true);
                } else if (powerUp instanceof ShrinkPaddlePowerUp) {
                    System.out.println("ShrinkPaddlePowerUp");
                    ShrinkPaddlePowerUp shr = (ShrinkPaddlePowerUp) powerUp;
                    shr.applyEffect(paddles.get(0));
                    activePowerUps.add(shr);
                    shr.setActive(true);
                } else if (powerUp instanceof DoubleBallPowerUp) {
                    System.out.println("DoubleBallPowerUp");
                    DoubleBallPowerUp dbl = (DoubleBallPowerUp) powerUp;
                    if (!balls.isEmpty()) {
                        Ball newBall = dbl.applyEffect(balls.get(0));
                        balls.add(newBall);
                    }
                } else if (powerUp instanceof HealthPowerUp) {
                    System.out.println("HealthPowerUp");
                    if (lives < 3) {
                        lives++;
                    }
                }
                powerUps.remove(i);
            }
        }
    }

    private void resetBallAndPaddle() {
        balls.clear();
        Ball newBall = new Ball(canvas.getWidth() / 2.0 - GameConfig.BALL_RADIUS / 2.0,
                GameConfig.HEIGHT / 2.0, GameConfig.BALL_RADIUS, GameConfig.BALL_SPEED);
        newBall.setX(canvas.getWidth() / 2.0);
        newBall.setY(GameConfig.HEIGHT - 120);
        newBall.setDx(0);
        newBall.setDy(0);
        balls.add(newBall);
        paddles.clear();
        Paddle paddle = new Paddle(canvas.getWidth() / 2.0 - GameConfig.PADDLE_WIDTH / 2.0,
                GameConfig.HEIGHT - 100, GameConfig.PADDLE_WIDTH, 15, 0);
        paddles.add(paddle);
    }

    public void startBall() {
        if(!balls.isEmpty()) {
            Ball ball=balls.get(0);
            if (ball.getDx() == 0 && ball.getDy() == 0) {
                ball.setDx(GameConfig.BALL_SPEED);
                ball.setDy(-GameConfig.BALL_SPEED);
            }
        }
    }

    private boolean checkLevelComplete() {
        if (levels.isEmpty()) {
            return false;
        }
        for (Brick brick : levels.get(0).getBricks()) {
            if (brick.isVisible()) {
                return false;
            }
        }
        return true;
    }

    private void handleLevelComplete() {
        System.out.println("Level Complete");
        currentLevel++;
        totalScores += 100;
        loadNextLevel();
    }

    private void loadNextLevel() {
        balls.clear();
        powerUps.clear();
        activePowerUps.clear();
        createLevel();
        resetBallAndPaddle();
    }
    public boolean isGameOver() {
        return lives <= 0;
    }

    public boolean needRestart() {
        if (!balls.isEmpty()) {
            Ball ball = balls.get(0);
            return ball.getDx() == 0 && ball.getDy() == 0;
        }
        return false;
    }
    private void loseLife() {
        lives--;
        System.out.println("Life loss. Lives remained: " + lives);
        if (lives > 0) {
            resetBallAndPaddle();
        } else {
            System.out.println("Game over");
        }
    }


    public void render() {
        gameRenderer.render(balls, paddles, levels, powerUps);
        gameRenderer.renderUI(totalScores, lives, currentLevel);
    }

    public Canvas getCanvas() {
        return canvas;
    }
    public int getTotalScores(){
        return totalScores;
    }

    public void setTotalScores(int totalScores) {
        this.totalScores = totalScores;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public List<Paddle> getPaddles() {
        return paddles;
    }
}
