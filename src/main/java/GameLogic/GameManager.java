package GameLogic;

import Board.FullBoard;
import Board.SquareNode;
import Players.LocalPlayer;
import Players.Player;

public class GameManager {
    private FullBoard board;
    private final Player player1, player2;
    private GameRules currentRule;
    private final MillChecker millChecker;
    private int selectedNodeIndex = -1;
    private boolean removePhaseActive = false;
    private boolean gameOver = false;

    public GameManager(String name1, String name2) {
        this.board = new FullBoard();
        this.player1 = new LocalPlayer(name1, "WHITE");
        this.player2 = new LocalPlayer(name2, "BLACK");

        player1.setCurrentTurn(true);
        player2.setCurrentTurn(false);

        currentRule = new PlacingRules();
        millChecker = new MillChecker();
    }

    public MoveResult processClick(int nodeIndex) {
        if (gameOver) {
            return new MoveResult(false, "Game over!");
        }

        if (removePhaseActive) {
            return handleRemovePhase(nodeIndex);
        }

        return handleNormalMove(nodeIndex);
    }

    private MoveResult handleNormalMove(int nodeIndex) {
        Player currentPlayer = getCurrentPlayer();
        GameRules playerRule = getPlayerRules(currentPlayer);

        if (currentPlayer.piecesAvailable() > 0) {
            MoveResult result = playerRule.placePiece(board, currentPlayer, nodeIndex);
            if (result.isSuccess()) {
                handleMillCheck(nodeIndex, result);
            }
            return result;
        }

        if (selectedNodeIndex == -1) {
            return selectPiece(nodeIndex, currentPlayer);
        } else {
            return movePieceToDestination(nodeIndex, currentPlayer, playerRule);
        }
    }

    private MoveResult selectPiece(int nodeIndex, Player currentPlayer) {
        SquareNode node = board.getNode(nodeIndex);

        if (currentPlayer.getColor().equals(node.getColor())) {
            selectedNodeIndex = nodeIndex;
            return new MoveResult(true, "Piece selected. Click destination");
        }
        return new MoveResult(false, "Select your own piece");
    }

    private MoveResult movePieceToDestination(int toIndex, Player currentPlayer, GameRules playerRules) {
        MoveResult result = playerRules.movePiece(board, currentPlayer, selectedNodeIndex, toIndex);
        selectedNodeIndex = -1;

        if (result.isSuccess()) {
            handleMillCheck(toIndex, result);
        }

        return result;
    }

    private void handleMillCheck(int nodeIndex, MoveResult result) {
        boolean millFormed = millChecker.checkMill(board, nodeIndex);

        if (millFormed) {
            removePhaseActive = true;
            result. setMillFormed(true);
            result.setRemovePhase(true);
            result.setMessage(getCurrentPlayer().getName() + " formed a mill!  Remove opponent's piece.");
        } else {
            switchPlayer();
            updatePhase();

            if (checkGameOver()) {
                result.setGameOver(true);
                result.setWinner(getWinner());
            }
        }
    }

    private MoveResult handleRemovePhase(int nodeIndex) {
        Player opponent = getOpponent();
        SquareNode node = board.getNode(nodeIndex);

        if (!opponent.getColor().equals(node.getColor())) {
            return new MoveResult(false, "You must remove an opponent's piece!");
        }

        if (!millChecker.canRemovePiece(board, nodeIndex, opponent.getColor())) {
            return new MoveResult(false, "Cannot remove piece from a mill!");
        }

        node.setColor("GRAY");
        node.setOccupied(false);
        opponent.decrementPiecesOnBoard();

        removePhaseActive = false;
        MoveResult result = new MoveResult(true, opponent.getName() + "'s piece removed!");

        if (checkGameOverAfterRemoval(opponent)) {
            result.setGameOver(true);
            result. setWinner(getWinner());
        } else {
            switchPlayer();
            updatePhase();
        }

        return result;
    }

    private void updatePhase() {
        if (player1.piecesAvailable() == 0 && player2.piecesAvailable() == 0) {
            currentRule = new MovingRules();
        }
    }

    private GameRules getPlayerRules(Player player) {
        if (player.piecesOnBoard() == 3 && player.piecesAvailable() == 0) {
            return new FlyingRules();
        }
        return currentRule;
    }

    private boolean checkGameOver() {
        Player currentPlayer = getCurrentPlayer();

        if (currentPlayer.piecesAvailable() > 0) {
            return false;
        }

        if (currentPlayer.piecesOnBoard() < 3) {
            gameOver = true;
            getOpponent().setWinner();
            return true;
        }

        GameRules playerRules = getPlayerRules(currentPlayer);
        if (!board.hasValidMoves(currentPlayer, playerRules)) {
            gameOver = true;
            getOpponent().setWinner();
            return true;
        }

        return false;
    }

    private boolean checkGameOverAfterRemoval(Player playerWhoLostPiece) {
        if (playerWhoLostPiece. piecesAvailable() > 0) {
            return false;
        }

        if (playerWhoLostPiece. piecesOnBoard() < 3) {
            gameOver = true;
            getCurrentPlayer().setWinner();
            return true;
        }

        GameRules playerRules = getPlayerRules(playerWhoLostPiece);
        if (!board. hasValidMoves(playerWhoLostPiece, playerRules)) {
            gameOver = true;
            getCurrentPlayer().setWinner();
            return true;
        }

        return false;
    }

    public void resetGame(){
        board.reset();
        player1.resetForNewGame();
        player2.resetForNewGame();

        player1.setCurrentTurn(true);
        player2.setCurrentTurn(false);

        currentRule=new PlacingRules();
        selectedNodeIndex=-1;
        removePhaseActive=false;
        gameOver=false;
    }

    //getters

    public FullBoard getBoard() {
        return board;
    }


    private Player getOpponent() {
        return player1.isCurrentTurn() ? player2 : player1;
    }

    private void switchPlayer() {
        player1.setCurrentTurn(!player1.isCurrentTurn());
        player2.setCurrentTurn(! player2.isCurrentTurn());
    }


    public Player getCurrentPlayer() {
        return player1.isCurrentTurn() ? player1 : player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getSelectedNodeIndex() {
        return selectedNodeIndex;
    }


    public Player getWinner() {
        if (player1.isWinner()) {
            return player1;
        } else if (player2.isWinner()) {
            return player2;
        }
        return null;
    }

    public String getGameStatus() {
        Player currentPlayer = getCurrentPlayer();

        if (gameOver) {
            Player winner = getWinner();
            return "Game over! " + winner.getName() + " wins!";
        }

        if (removePhaseActive) {
            return currentPlayer.getName() + ": Remove opponent's piece";
        }

        if (selectedNodeIndex != -1) {
            return currentPlayer.getName() + ": Select destination";
        }

        GameRules currentPlayerRules = getPlayerRules(currentPlayer);
        return currentPlayer.getName() + "'s turn - " + currentPlayerRules.getPhaseName();
    }
}