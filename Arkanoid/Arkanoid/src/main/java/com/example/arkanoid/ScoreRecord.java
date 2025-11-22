package com.example.arkanoid;

public class ScoreRecord {
    private int id;
    private String modeName;
    private String userName;
    private int levelNum;
    private int score;

    public ScoreRecord(int id, String modeName, String userName, int levelNum, int score) {
        this.id = id;
        this.modeName = modeName;
        this.userName = userName;
        this.levelNum = levelNum;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    @Override
    public String toString() {
        return "ScoreRecord{" +
                "id=" + id +
                ", modeName='" + modeName + '\'' +
                ", userName='" + userName + '\'' +
                ", levelNum=" + levelNum +
                ", score=" + score +
                '}';
    }
}
