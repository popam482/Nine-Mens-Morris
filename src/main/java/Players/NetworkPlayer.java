package Players;

import Network.NetworkConnection;
import java.io.IOException;

public class NetworkPlayer extends Player {

    private final String connectionId;
    private NetworkConnection connection;
    private Move pendingMove=null;

    public NetworkPlayer(String name, String color, String connectionId) {
        super(name, color);
        this.connectionId = connectionId;
    }

    public void setConnection(NetworkConnection connection) {
        this.connection = connection;
    }

    public void receiveMove(Move move){
        this.pendingMove=move;
    }

    @Override
    public Move getNextMove() {
        if (connection == null) return null;

        try {
            String message = connection.receive();
            return Move.fromString(message);
        } catch (IOException e) {
            System.err.println("Error receiving move: " + e.getMessage());
            return null;
        }
    }

    public void sendMove(int position) {
        if (connection != null) {
            Move move = new Move(position, getColor());
            connection.send(move.toString());  // Trimite "5:WHITE"
        }
    }

    public String getConnectionId() {
        return connectionId;
    }
}