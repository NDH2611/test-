package com.example.arkanoid;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static Connection connection;
    private static DatabaseManager instance;

    private DatabaseManager() {
        connectToDatabase();
    }
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connectToDatabase();
        }
        return connection;
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void connectToDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(GameConfig.URL);
            createTable();
        } catch (ClassNotFoundException e) {
            System.err.println("Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Cannot connect to database!");
            e.printStackTrace();
        }
    }
    private void createTable() {
        String sql = "create table if not exists main (\n" +
                "    id integer primary key autoincrement,\n" +
                "    mode_name text not null,\n" +
                "    user_name text not null,\n" +
                "    level_num integer not null default 1,\n" +
                "    score integer not null default 0,\n" +
                "    time_played TEXT DEFAULT (datetime('now', 'localtime'))\n" +
                ");";
        try (Statement st = connection.createStatement()){
            st.execute(sql);
            System.out.println("Table created!");
        } catch (SQLException e) {
            System.err.println("Error creating table!");
            e.printStackTrace();
        }
    }
    public boolean insertScore(String modeName, String userName, int levelNum, int score) {
        String sql = "INSERT INTO main (mode_name, user_name, level_num, score) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, modeName);
            ps.setString(2, userName);
            ps.setInt(3, levelNum);
            ps.setInt(4, score);
            int rowsAffected = ps.executeUpdate();
            System.out.println("Score saved! Rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Cannot insert score!");
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateScore(int id, String modeName, String userName, int levelNum, int score) {
        String sql = "UPDATE main SET user_name = ?, level_num = ?, score = ? WHERE id = ? AND mode_name = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, userName);
            ps.setInt(2, levelNum);
            ps.setInt(3, score);
            ps.setInt(4, id);
            ps.setString(5, modeName);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating score: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<ScoreRecord> getTopScore(String modeName, int limit) {
        List<ScoreRecord> scores = new ArrayList<>();
        String sql = "SELECT * FROM main WHERE mode_name = ? ORDER BY score DESC LIMIT ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, modeName);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ScoreRecord record = new ScoreRecord(
                        rs.getInt("id"),
                        rs.getString("mode_name"),
                        rs.getString("user_name"),
                        rs.getInt("level_num"),
                        rs.getInt("score")
                );
                scores.add(record);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching scores!");
            e.printStackTrace();
        }
        return scores;
    }
    public void closeConnection() {
        try {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection!");
            e.printStackTrace();
        }
    }
}