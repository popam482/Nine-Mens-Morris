package GameLogic;

import Board.FullBoard;
import Board.SquareNode;
import Players.Player;

public class FlyingRules implements GameRules {

    @Override
    public MoveResult placePiece(FullBoard board, Player player, int nodeIndex) {
        return new MoveResult(false, "Cannot place new pieces during flying phase!");
    }

    @Override
    public MoveResult movePiece(FullBoard board, Player player, int fromIndex, int toIndex) {
        SquareNode fromNode = board.getNode(fromIndex);
        SquareNode toNode = board.getNode(toIndex);

        if (!fromNode.getColor().equals(player.getColor())) {
            return new MoveResult(false, "That's not your piece!");
        }

        if (toNode.isOccupied()) {
            return new MoveResult(false, "Destination is occupied!");
        }

        fromNode.setColor("GRAY");
        toNode.setColor(player.getColor());

        return new MoveResult(true, "Piece flew to destination!");
    }

    @Override
    public boolean canMove(FullBoard board, Player player, int fromIndex, int toIndex) {
        SquareNode fromNode = board.getNode(fromIndex);
        SquareNode toNode = board.getNode(toIndex);

        return fromNode.getColor().equals(player.getColor()) && toNode.isEmpty();
    }

    @Override
    public String getPhaseName() {
        return "Flying Phase";
    }
}