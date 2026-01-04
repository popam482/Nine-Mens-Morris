package Players;

public class LocalPlayer extends Player {

    private Move pendingMove = null;

    public LocalPlayer(String name, String color) {
        super(name, color);
    }

    public void setMove(int position) {
        this.pendingMove = new Move(position, getColor());
    }


    public void setMove(int from, int to) {
        this.pendingMove = new Move(from, to, getColor());
    }

    @Override
    public Move getNextMove() {
        Move move = pendingMove;
        pendingMove = null;
        return move;
    }


    @Override
    public void sendMove(Move move) {

    }

    public boolean hasPendingMove() {
        return pendingMove != null;
    }
}