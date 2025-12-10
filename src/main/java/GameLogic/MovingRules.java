package GameLogic;

import Players.Player;

public class MovingRules implements GameRules {

    @Override
    public MoveResult placePiece(FullBoard board, Player player, int nodeIndex) {
        return new MoveResult(false, "Cannot place new pieces during moving phase!");
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

        if (!areNeighbors(fromIndex, toIndex, board)) {
            return new MoveResult(false, "Can only move to adjacent positions!");
        }

        fromNode.setColor("GRAY");
        toNode.setColor(player.getColor());

        return new MoveResult(true, "Piece moved!");
    }

    @Override
    public boolean canMove(FullBoard board, Player player, int fromIndex, int toIndex) {
        SquareNode fromNode = board.getNode(fromIndex);
        SquareNode toNode = board.getNode(toIndex);

        return fromNode.getColor().equals(player.getColor())
                && toNode.isEmpty()
                && areNeighbors(fromIndex, toIndex, board);
    }

    private boolean areNeighbors(int fromIndex, int toIndex, FullBoard board) {
        SquareNode fromNode = board.getNode(fromIndex);
        int[] neighbors = fromNode.getNeighbours();

        for (int neighborIndex : neighbors) {
            if (neighborIndex == toIndex) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getPhaseName() {
        return "Moving Phase";
    }
}