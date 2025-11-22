package com.example.arkanoid;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.PieChart;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class GameEngine {
    private GameRenderer gameRenderer;
    private Canvas canvas;
    private GraphicsContext gc;
    private Scene scene;
    private long startTime = 0;
    private AnimationTimer gameLoop;
    private Stage stage;
    private StackPane root;
    private GameStateController troller;
    private boolean pPressed = false;
    private Font renderFont;
    private MusicManager musicManager = MusicManager.getInstance();

    private ArrayList<Paddle> paddles = new ArrayList<>();
    private ArrayList<Level> levels = new ArrayList<>();
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private ArrayList<PowerUp> activePowerUps = new ArrayList<>();
    private ArrayList<Ball> balls = new ArrayList<>();
    private ArrayList<String> mapFiles = new ArrayList<>();

    private int lives = 3;
    private int totalScores = 0;
    private int currentLevel = 1;

    private DatabaseManager dbManager;
    private String currentMode = "Solo";
    private String playerName;

    public GameEngine(Stage stage) {
        this.stage = stage;
        dbManager = DatabaseManager.getInstance();
        this.setTotalScores(0);
        this.setLives(3);
        loadMap(GameConfig.totalMap);
        initialize();
        troller = new GameStateController(stage, this);
    }

    public void initialize() {
        canvas = new Canvas(GameConfig.WIDTH, GameConfig.HEIGHT);
        gc = canvas.getGraphicsContext2D();
        renderFont = loadFont(GameConfig.fontUse, 16);
        gameRenderer = new GameRenderer(gc, renderFont);
        Ball initialBall = new Ball(GameConfig.WIDTH / 2.0 - GameConfig.BALL_RADIUS / 2.0,
                GameConfig.HEIGHT - 120, GameConfig.BALL_RADIUS, GameConfig.BALL_SPEED);
        balls.add(initialBall);
        initialBall.setDx(0);
        initialBall.setDy(-GameConfig.BALL_SPEED);
        Paddle paddle = new Paddle(GameConfig.WIDTH / 2.0 - GameConfig.PADDLE_WIDTH / 2.0
                , GameConfig.HEIGHT - 100, GameConfig.PADDLE_WIDTH, 15, 0);
        paddles.add(paddle);
        createLevel();

        StackPane gameContainer = new StackPane(canvas);
        StackPane.setAlignment(canvas, Pos.CENTER);
        root = new StackPane(canvas);

        scene = new Scene(root, GameConfig.WIDTH, GameConfig.HEIGHT);

        scene.setOnKeyPressed(event -> handleKeyInput(event));
        scene.setOnKeyReleased(event -> {
            handleKeyInput(event);
            if (event.getCode() == KeyCode.P) {
                pPressed = false;
            }
        });
    }

    public void inputUsername() {
        UsernameLog log = new UsernameLog(stage);
        String username = log.showAndWait();
        if (log.isConfirmed()) {
            this.playerName = username;
        } else {
            this.playerName = "player";
        }
    }

    public void startGameLoop() {
        if (gameLoop != null) {
            return;
        }
        startTime = System.nanoTime();
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double deltaTime = (now - startTime) / 1000000000.0;
                startTime = now;
                updateGame(deltaTime);
                renderGame();
            }
        };
        gameLoop.start();
        System.out.println("game started");
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

        stage.setScene(scene);
        scene.getRoot().requestFocus();
        startGameLoop();
        troller.setState(GameState.READY);

    }

    private void saveScore() {
        if (dbManager == null) {
            System.err.println("dbManager is null");
            return;
        }
        boolean success = dbManager.insertScore(
                currentMode,
                playerName,
                currentLevel,
                totalScores
        );
        if (success) {
            System.out.println("Score saved! Mode: " + currentMode +
                    ", Player: " + playerName +
                    ", Score: " + totalScores);
        } else {
            System.out.println("Failed to save score.");
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

    private void loadMap(String fileName) {
        try {
            InputStream is = getClass().getResourceAsStream("map/" + fileName);
            if (is == null) {
                System.err.println("File not found");
                return;
            }
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

        for (int i = activePowerUps.size() - 1; i >= 0; i--) {
            if (!activePowerUps.get(i).isActive()) {
                activePowerUps.remove(i);
            }
        }
    }

    public void updateGame(double deltaTime) {

        if (troller != null && troller.getState() != GameState.RUNNING) {
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
            if (currentBall.getY() > GameConfig.HEIGHT) {
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

    public void renderGame() {
        gameRenderer.render(balls, paddles, levels, powerUps);
        gameRenderer.renderUI(totalScores, lives, currentLevel);
    }

    private void loseLife() {
        lives--;
        System.out.println("Life loss. Lives remained: " + lives);
        if (lives > 0) {
            resetBallAndPaddle();
            troller.setState(GameState.READY);
        } else {
            saveScore();
            System.out.println("Game over");
            troller.setState(GameState.GAME_OVER);
        }
    }

    private void resetBallAndPaddle() {
        balls.clear();
        Ball newBall = new Ball(GameConfig.WIDTH / 2.0 - GameConfig.BALL_RADIUS / 2.0,
                GameConfig.HEIGHT / 2.0, GameConfig.BALL_RADIUS, GameConfig.BALL_SPEED);
        newBall.setX(GameConfig.WIDTH / 2.0);
        newBall.setY(GameConfig.HEIGHT - 120);
        newBall.setDx(0);
        newBall.setDy(-GameConfig.BALL_SPEED);
        balls.add(newBall);
        paddles.clear();
        Paddle paddle = new Paddle(GameConfig.WIDTH / 2.0 - GameConfig.PADDLE_WIDTH / 2.0,
                GameConfig.HEIGHT - 100, GameConfig.PADDLE_WIDTH, 15, 0);
        paddles.add(paddle);
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
        troller.setState(GameState.READY);
    }

    public void handleKeyInput(KeyEvent event) {
        if (troller == null) {
            return;
        }
        if (troller.getState() == GameState.RUNNING) {
            paddles.get(0).handleInput(event, false);
        }
        switch (event.getCode()) {
            case P:
                if (!pPressed) {
                    pPressed = true;
                    if (troller.getState() == GameState.RUNNING) {
                        troller.setState(GameState.PAUSE);
                    } else if (troller.getState() == GameState.PAUSE) {
                        troller.setState(GameState.RUNNING);
                    }
                }
                break;

            case R:
                if (troller.getState() == GameState.GAME_OVER) {
                    restartGame();
                }
                break;
            case SPACE:
                if (troller.getState() == GameState.READY) {
                    if (!balls.isEmpty()) {
                        balls.get(0).setDx(GameConfig.BALL_SPEED);
                        balls.get(0).setDy(GameConfig.BALL_SPEED);
                    }
                    troller.setState(GameState.RUNNING);
                }
                break;
        }
    }

    public Scene getScene() {
        return scene;
    }

    public GameStateController getTroller() {
        return troller;
    }

    public int getTotalScores() {
        return totalScores;
    }

    public void setTotalScores(int totalScores) {
        this.totalScores = totalScores;
    }

    public ArrayList<PowerUp> getPowerUps() {
        return powerUps;
    }

    public void setPowerUps(ArrayList<PowerUp> powerUps) {
        this.powerUps = powerUps;
    }

    public ArrayList<PowerUp> getActivePowerUps() {
        return activePowerUps;
    }

    public void setActivePowerUps(ArrayList<PowerUp> activePowerUps) {
        this.activePowerUps = activePowerUps;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Font getRenderFont() {
        return renderFont;
    }

    public void setRenderFont(Font renderFont) {
        this.renderFont = renderFont;
    }

    public StackPane getRoot() {
        return root;
    }
}
