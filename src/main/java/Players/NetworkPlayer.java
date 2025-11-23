package Players;

public class NetworkPlayer extends Player{

    private final String connectionId;

    public NetworkPlayer(String name, String connectionId) {
        super(name);
        this.connectionId=connectionId;
    }

    public String getConnectionId(){
        return connectionId;
    }

}
