package com.example.arkanoid;

import javafx.scene.canvas.GraphicsContext;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Level {
    private static final int WIDTH_BRICK = 80;
    private static final int HEIGHT_BRICK = 40;
    private static final int LEVEL_COLUMN = 9;
    private static final int LEVEL_ROW = 5;
    private static final int BRICK_SPACE = 5;
    private static final int DISTANCE_Y = 50;
    private static final int BRICK_TYPE = 5;

    private ArrayList<Brick> bricks = new ArrayList<>();

    public Level(String fileName) {
        loadFromFile(fileName);
    }

    private void loadFromFile(String fileName) {
        try {
            InputStream is = getClass().getResourceAsStream("map/" + fileName);
            if (is == null) {
                System.err.println("File not found");
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            ArrayList<String> lines = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
            reader.close();
            if (lines.isEmpty()) {
                System.err.println("ERROR: File is empty: " + fileName);
                return;
            }
            int row = lines.size();
            int col = lines.get(0).split("\\s+").length;

            int DISTANCE_X = (GameConfig.WIDTH - WIDTH_BRICK * col - BRICK_SPACE * (col - 1)) / 2;
            for (int i = 0; i < row; i++) {
                String[] values = lines.get(i).trim().split("\\s+");
                for (int j = 0; j < values.length; j++) {
                    int type = Integer.parseInt(values[j]);
                    if (type == 0) {
                        continue;
                    }
                    int x = j * (WIDTH_BRICK + BRICK_SPACE) + DISTANCE_X;
                    int y = i * (HEIGHT_BRICK + BRICK_SPACE) + DISTANCE_Y;

                    Brick brick = new Brick(x, y, WIDTH_BRICK, HEIGHT_BRICK);
                    switch (type) {
                        case 1:
                            brick.setType(Brick.TYPE.PINK);
                            break;
                        case 2:
                            brick.setType(Brick.TYPE.YELLOW);
                            break;
                        case 3:
                            brick.setType(Brick.TYPE.GREEN);
                            break;
                        case 4:
                            brick.setType(Brick.TYPE.ORANGE);
                            break;
                        case 5:
                            brick.setType(Brick.TYPE.BLUE);
                            break;
                        case 6:
                            brick.setType(Brick.TYPE.MAROON);
                            break;
                    }
                    brick.brickStatus();
                    bricks.add(brick);
                }
            }

        } catch (Exception e) {
            System.err.println("Error loading file");
            e.printStackTrace();
        }
    }

    public void render(GraphicsContext gc) {
        for (Brick brick : bricks) {
            brick.render(gc);
        }
    }

    public ArrayList<Brick> getBricks() {
        return bricks;
    }

    public void setBricks(ArrayList<Brick> bricks) {
        this.bricks = bricks;
    }

    public static int getDistanceY() {
        return DISTANCE_Y;
    }
}
