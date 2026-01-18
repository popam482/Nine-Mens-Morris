package GameLogic;

import Players.Player;

public class MoveResult {
    private boolean success;
    private String message;
    private boolean millFormed;
    private boolean gameOver;
    private Player winner;
    private boolean removePhase;

    public MoveResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.millFormed = false;
        this.gameOver = false;
        this.removePhase = false;
        this.winner = null;
    }

    // GETTERS
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Player getWinner() {
        return winner;
    }


    // SETTERS
    public void setMillFormed(boolean millFormed) {
        this.millFormed = millFormed;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public void setRemovePhase(boolean removePhase) {
        this.removePhase = removePhase;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}