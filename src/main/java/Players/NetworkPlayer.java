package Players;

import Network.NetworkConnection;

public class NetworkPlayer extends Player {

    private final NetworkConnection connection;
    private final boolean isLocal;  // True dacă e jucătorul local (trimite), false dacă e remote (primește)
    private Move pendingMove = null;

    /**
     * Constructor pentru NetworkPlayer
     * @param isLocal - true dacă e jucătorul local (trimite mutări), false dacă e remote (primește)
     */
    public NetworkPlayer(String name, String color, NetworkConnection connection, boolean isLocal) {
        super(name, color);
        this.connection = connection;
        this.isLocal = isLocal;
    }

    public void receiveMove(Move move) {
        this.pendingMove = move;
    }

    @Override
    public Move getNextMove() {
        Move move = pendingMove;
        pendingMove = null;
        return move;
    }

    @Override
    public void sendMove(Move move) {
        // Doar LOCAL player trimite mutări
        if (isLocal && connection != null) {
            connection. send(move.toString());
            System.out.println("NetworkPlayer (" + getName() + ") sent: " + move);
        }
    }

    public boolean isLocal() {
        return isLocal;
    }

    public boolean hasPendingMove() {
        return pendingMove != null;
    }
}