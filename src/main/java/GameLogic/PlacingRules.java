package GameLogic;

import Board.FullBoard;
import Board.SquareNode;
import Players.Player;

public class PlacingRules implements GameRules {

    @Override
    public MoveResult placePiece(FullBoard board, Player player, int nodeIndex) {
        SquareNode node = board.getNode(nodeIndex);

        if (node.isOccupied()) {
            return new MoveResult(false, "Position already occupied!");
        }

        if (player.piecesAvailable() <= 0) {
            return new MoveResult(false, "No pieces left to place!");
        }

        node.setColor(player.getColor());
        player.decrementAvailablePieces();
        player.incrementPiecesOnBoard();

        return new MoveResult(true, "Piece placed!");
    }

    @Override
    public MoveResult movePiece(FullBoard board, Player player, int fromIndex, int toIndex) {
        return new MoveResult(false, "Cannot move pieces during placing phase!");
    }

    @Override
    public boolean canMove(FullBoard board, Player player, int fromIndex, int toIndex) {
        return false;
    }

    @Override
    public String getPhaseName() {
        return "Placing Phase";
    }
}