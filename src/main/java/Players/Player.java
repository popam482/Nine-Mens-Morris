package Players;

import GameLogic.GameManager;
import GameLogic.MoveResult;

public abstract class Player {

    private String name;
    private String color;
    private int piecesAvailable;
    private int piecesOnBoard;
    private boolean isCurrentTurn;
    private boolean isWinner;

    public Player(String name, String color) {
        this.name = name;
        this.color = color;
        this.piecesAvailable = 9;
        this.piecesOnBoard = 0;
        this.isCurrentTurn = false;
        this.isWinner = false;
    }

    public abstract MoveResult processClick(int nodeId, GameManager gameManager);

    // Getters
    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int piecesOnBoard() {
        return piecesOnBoard;
    }

    public int piecesAvailable() {
        return piecesAvailable;
    }

    public boolean isCurrentTurn() {
        return isCurrentTurn;
    }

    public boolean isWinner() {
        return isWinner;
    }

    // Setters
    public void decrementAvailablePieces() {
        if (piecesAvailable > 0) {
            piecesAvailable--;
        }
    }

    public void incrementPiecesOnBoard() {
        piecesOnBoard++;
    }

    public void decrementPiecesOnBoard() {
        if (piecesOnBoard > 0) {
            piecesOnBoard--;
        }
    }

    public void setCurrentTurn(boolean currentTurn) {
        isCurrentTurn = currentTurn;
    }

    public void setWinner() {
        isWinner = true;
    }

    public void resetForNewGame() {
        this.piecesAvailable = 9;
        this.piecesOnBoard = 0;
        this.isCurrentTurn = false;
        this.isWinner = false;
    }
}