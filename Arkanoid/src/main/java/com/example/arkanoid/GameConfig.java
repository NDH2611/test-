package com.example.arkanoid;

import javafx.scene.paint.Color;

public interface GameConfig {
    public static final int CANVAS_WIDTH = 800;
    public static final int CANVAS_HEIGHT = 650;

    public static final Color BACKGROUND_COLOR = Color.rgb(46, 26, 71);
    public static final Color UI_TEXT_COLOR = Color.rgb(242, 226, 210);
    public static final Color BALL_COLOR = Color.rgb(242, 226, 210);
    public static final Color SEPARATOR_COLOR = Color.BLACK;

    public static final String URL = "jdbc:sqlite:Arkanoid/arkanoid.db";

    public static final int WIDTH = 800;
    public static final int HEIGHT = 650;
    public static final double BALL_SPEED = 4.0;
    public static final double PADDLE_WIDTH = 75;
    public static final double BALL_RADIUS = 10;

    public static final String totalMap = "totalMap.txt";
    public static final String fontUse = "PressStart2P-Regular.ttf";
}
