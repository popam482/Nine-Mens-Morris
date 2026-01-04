package Players;

public class Move {

    private final int from;
    private final int to;
    private final String playerColor;

    //PLACE/REMOVE
    public Move(int position, String playerColor) {
        this.from = -1;
        this.to = position;
        this.playerColor = playerColor;
    }

    //MOVE/FLY
    public Move(int from, int to, String playerColor) {
        this.from = from;
        this.to = to;
        this.playerColor = playerColor;
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

    public boolean isSinglePosition() {
        return from == -1;
    }

    public boolean isDoublePosition() {
        return from != -1;
    }

    public int getPosition() {
        return to;
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
            System.err.println("Failed to parse Move: " + str);
            return null;
        }
    }
}