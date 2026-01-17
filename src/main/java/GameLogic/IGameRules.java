package GameLogic;

import Board.FullBoard;
import Players.Player;

public interface IGameRules {

    MoveResult placePiece(FullBoard board, Player player, int nodeIndex);

    MoveResult movePiece(FullBoard board, Player player, int fromIndex, int toIndex);

    boolean canMove(FullBoard board, Player player, int fromIndex, int toIndex);

    String getPhaseName();
}