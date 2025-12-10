package Players;

public class NetworkPlayer extends Player{

    private final String connectionId;

    public NetworkPlayer(String name, String color, String connectionId) {
        super(name, color);
        this.connectionId = connectionId;
    }

    public String getConnectionId(){
        return connectionId;
    }
}