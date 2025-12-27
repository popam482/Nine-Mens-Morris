package Players;

public class Move {
    private int position;
    private String playerColor;

    public Move(int pos, String color){
        position=pos;
        playerColor=color;
    }

    public int getPosition() {
        return position;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    @Override
    public String toString(){
        return position+":"+playerColor;
    }

    public static Move fromString(String str){
        String []parts=str.split(":");
        return new Move(Integer.parseInt(parts[0]), parts[1]);
    }

}
