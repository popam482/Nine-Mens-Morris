package Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class GameDatabase {

    private static final String DB_URL = "jdbc:sqlite:nine_mens_morris.db";
    private Connection connection;

    public GameDatabase() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);

            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS game_history (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    player1_name TEXT NOT NULL,
                    player2_name TEXT NOT NULL,
                    winner_name TEXT NOT NULL,
                    game_duration_seconds INTEGER,
                    game_type TEXT NOT NULL,
                    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;

            Statement stmt = connection.createStatement();
            stmt.execute(createTableSQL);
            stmt.close();

            System.out.println("Database initialized successfully");

        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveGame(String player1, String player2, String winner,
                         long durationSeconds, String gameType) {
        String insertSQL = """
            INSERT INTO game_history 
            (player1_name, player2_name, winner_name, game_duration_seconds, game_type, played_at) 
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, player1);
            pstmt.setString(2, player2);
            pstmt.setString(3, winner);
            pstmt.setLong(4, durationSeconds);
            pstmt.setString(5, gameType);

            java.time.format.DateTimeFormatter formatter =
                    java.time.format. DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            pstmt.setString(6, java.time.LocalDateTime.now().format(formatter));


            pstmt.executeUpdate();
            System.out.println("Game saved to database: " + winner + " won!");

        } catch (SQLException e) {
            System.err.println("Failed to save game: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public ObservableList<GameRecord> getAllGames() {
        ObservableList<GameRecord> records = FXCollections.observableArrayList();

        String sql = "SELECT player1_name, player2_name, winner_name, game_duration_seconds, game_type, played_at FROM game_history ORDER BY played_at DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                GameRecord record = new GameRecord(
                        rs.getString("player1_name"),
                        rs.getString("player2_name"),
                        rs.getString("winner_name"),
                        rs.getLong("game_duration_seconds"),
                        rs.getString("game_type"),
                        rs.getString("played_at")
                );
                records.add(record);
            }

            System.out.println("Loaded " + records.size() + " games from database");

        } catch (SQLException e) {
            System.err.println("Failed to retrieve games: " + e.getMessage());
            e.printStackTrace();
        }

        return records;
    }

}