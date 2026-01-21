package Players;

import GameLogic.GameManager;
import GameLogic.Move;
import GameLogic.MoveResult;
import Network.NetworkConnection;

public class NetworkPlayer extends Player {

    private final NetworkConnection connection;
    private final boolean isLocal;
    private Move pendingMove = null;

    public NetworkPlayer(String name, String color, NetworkConnection connection, boolean isLocal) {
        super(name, color);
        this.connection = connection;
        this.isLocal = isLocal;
    }

    public void receiveMove(Move move) {
        this.pendingMove = move;
    }

    @Override
    public MoveResult processClick(int nodeId, GameManager gameManager) {
        MoveResult result = gameManager.processClick(nodeId);
        if (isLocal && connection != null) {
            sendMove(new Move(nodeId, this.getColor()));
        }

        return result;
    }

    public void sendMove(Move move) {
        if (isLocal && connection != null) {
            connection.send(move.toString());
        }
    }

}