package GameLogic;

import Board.FullBoard;
import Board.SquareNode;
import Players.LocalPlayer;
import Players.Player;

public class GameManager {
    private FullBoard board;
    private Player player1, player2;
    private Player currentPlayer;
    private GameRules globalRules;
    private int selectedNodeIndex = -1;
    private boolean removePhaseActive = false;
    private boolean gameOver = false;
    private Player winner = null;

    public GameManager(String name1, String name2) {
        this.board = new FullBoard();
        this.player1 = new LocalPlayer(name1, "WHITE");
        this.player2 = new LocalPlayer(name2, "BLACK");
        this.currentPlayer = player1;
        this.globalRules = new PlacingRules();
    }


    public MoveResult processClick(int nodeIndex) {
        if (gameOver) {
            return new MoveResult(false, "Game is over!");
        }

        if (removePhaseActive) {
            return handleRemovePhase(nodeIndex);
        }

        return handleNormalMove(nodeIndex);
    }


    private MoveResult handleNormalMove(int nodeIndex) {
        MoveResult result;

        if (globalRules instanceof PlacingRules) {
            result = globalRules.placePiece(board, currentPlayer, nodeIndex);

            if (result. isSuccess()) {
                boolean millFormed = MillChecker. checkMill(board, nodeIndex);

                if (millFormed) {
                    removePhaseActive = true;
                    result. setMillFormed(true);
                    result.setRemovePhase(true);
                    result.setMessage(currentPlayer.getName() + " formed a mill!  Remove opponent's piece.");
                } else {
                    switchPlayer();
                    updatePhase();
                }
            }
        } else {
            GameRules currentPlayerRules = getPlayerRules(currentPlayer);

            if (selectedNodeIndex == -1) {
                SquareNode node = board.getNode(nodeIndex);
                if (currentPlayer.getColor().equals(node.getColor())) {
                    selectedNodeIndex = nodeIndex;
                    return new MoveResult(true, "Piece selected.  Click destination.");
                } else {
                    return new MoveResult(false, "Select your own piece!");
                }
            } else {
                result = currentPlayerRules.movePiece(board, currentPlayer, selectedNodeIndex, nodeIndex);

                if (result.isSuccess()) {
                    boolean millFormed = MillChecker.checkMill(board, nodeIndex);

                    selectedNodeIndex = -1;

                    if (millFormed) {
                        removePhaseActive = true;
                        result.setMillFormed(true);
                        result.setRemovePhase(true);
                        result. setMessage(currentPlayer.getName() + " formed a mill! Remove opponent's piece.");
                    } else {
                        switchPlayer();
                        updatePhase();
                        if (checkGameOver()) {
                            result.setGameOver(true);
                            result. setWinner(winner);
                        }
                    }
                } else {
                    selectedNodeIndex = -1;
                }
            }
        }

        return result;
    }


    private MoveResult handleRemovePhase(int nodeIndex) {
        Player opponent = getOpponent();
        String opponentColor = opponent.getColor();

        SquareNode node = board.getNode(nodeIndex);

        if (! opponentColor.equals(node. getColor())) {
            return new MoveResult(false, "You must remove an opponent's piece!");
        }

        if (! MillChecker.canRemovePiece(board, nodeIndex, opponentColor)) {
            return new MoveResult(false, "Cannot remove piece from a mill (opponent has pieces outside mills)!");
        }

        node.setColor("GRAY");
        node.setOccupied(false);
        opponent.decrementPiecesOnBoard();

        removePhaseActive = false;

        MoveResult result = new MoveResult(true, opponent.getName() + "'s piece removed!");


        if (checkGameOverAfterRemoval(opponent)) {
            result.setGameOver(true);
            result. setWinner(winner);
        } else {
            switchPlayer();
            updatePhase();
        }

        return result;
    }


    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ?  player2 : player1;
    }


    private void updatePhase() {
        if (globalRules instanceof PlacingRules) {
            if (player1.piecesAvailable() == 0 && player2.piecesAvailable() == 0) {
                globalRules = new MovingRules();
            }
        }
    }


    private GameRules getPlayerRules(Player player) {
        if (player.piecesOnBoard() == 3 && !(globalRules instanceof PlacingRules)) {
            return new FlyingRules();
        }
        return globalRules;
    }


    private boolean checkGameOver() {
        if (globalRules instanceof PlacingRules) {
            return false;
        }


        if (currentPlayer.piecesOnBoard() < 3) {
            gameOver = true;
            winner = getOpponent();
            return true;
        }

        GameRules currentPlayerRules = getPlayerRules(currentPlayer);
        if (! board.hasValidMoves(currentPlayer, currentPlayerRules)) {
            gameOver = true;
            winner = getOpponent();
            return true;
        }

        return false;
    }

    private boolean checkGameOverAfterRemoval(Player playerWhoLostPiece) {
        if (globalRules instanceof PlacingRules) {
            return false;
        }

        if (playerWhoLostPiece.piecesOnBoard() < 3) {
            gameOver = true;
            winner = currentPlayer;
            return true;
        }

        GameRules playerRules = getPlayerRules(playerWhoLostPiece);
        if (!board.hasValidMoves(playerWhoLostPiece, playerRules)) {
            gameOver = true;
            winner = currentPlayer;
            return true;
        }

        return false;
    }


    private Player getOpponent() {
        return (currentPlayer == player1) ?  player2 : player1;
    }


    public void resetGame() {
        board.reset();
        String name1 = player1.getName();
        String name2 = player2. getName();
        player1 = new LocalPlayer(name1, "WHITE");
        player2 = new LocalPlayer(name2, "BLACK");
        currentPlayer = player1;
        globalRules = new PlacingRules();
        selectedNodeIndex = -1;
        removePhaseActive = false;
        gameOver = false;
        winner = null;
    }

    // GETTERS
    public FullBoard getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public GameRules getCurrentRules() {
        return globalRules;
    }

    public int getSelectedNodeIndex() {
        return selectedNodeIndex;
    }

    public boolean isRemovePhaseActive() {
        return removePhaseActive;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Player getWinner() {
        return winner;
    }

    public String getGameStatus() {
        if (gameOver) {
            return "GAME OVER! " + winner.getName() + " WINS! ";
        }

        if (removePhaseActive) {
            return currentPlayer.getName() + ": REMOVE opponent's piece";
        }

        if (globalRules instanceof PlacingRules) {
            return currentPlayer.getName() + "'s turn - PLACE piece ";
        }

        if (selectedNodeIndex != -1) {
            return currentPlayer.getName() + ": Select DESTINATION";
        }

        GameRules currentPlayerRules = getPlayerRules(currentPlayer);
        String phaseText = currentPlayerRules.getPhaseName();
        return currentPlayer.getName() + "'s turn - " + phaseText;
    }
}