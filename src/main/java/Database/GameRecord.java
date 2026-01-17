package Database;

public class GameRecord {
    private String player1;
    private String player2;
    private String winner;
    private long durationSeconds;
    private String gameType;
    private String date;

    public GameRecord(String player1, String player2, String winner,
                      long durationSeconds, String gameType, String date) {
        this.player1 = player1;
        this.player2 = player2;
        this.winner = winner;
        this.durationSeconds = durationSeconds;
        this.gameType = gameType;
        this.date = date;
    }

    public String getPlayer1() { return player1; }
    public String getPlayer2() { return player2; }
    public String getWinner() { return winner; }
    public String getGameType() { return gameType; }
    public String getDate() { return date; }

    public String getDuration() {
        return String.format("%d:%02d", durationSeconds / 60, durationSeconds % 60);
    }
}