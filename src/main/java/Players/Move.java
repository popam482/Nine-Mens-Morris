package Players;

public class Move {

    private final int from; //-1 place/remove
    private final int to;
    private final String playerColor;

    //place/remove
    public Move(int position, String playerColor){
        from=-1;
        to=position;
        this.playerColor=playerColor;
    }

    //move/fly
    public Move(int from, int to, String playerColor){
        this.from=from;
        this.to=to;
        this.playerColor=playerColor;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public boolean isSinglePosition(){
        return from==-1;
    }

    public boolean isDoublePosition(){
        return from!=-1;
    }

    @Override
    public String toString() {
        return String.format("%s:%d:%d", playerColor, from, to);
    }

    public static Move fromString(String str) {
        try {
            String[] parts = str.split(":");
            if (parts.length != 3) {
                return null;
            }

            String color = parts[0];
            int from = Integer.parseInt(parts[1]);
            int to = Integer.parseInt(parts[2]);

            return new Move(from, to, color);

        } catch (Exception e) {
            System.out.println("Failed to parse Move: " + str);
            return null;
        }
    }

}
