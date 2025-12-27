package Players;

public class LocalPlayer extends Player {

    private Move pendingMove=null;

    public LocalPlayer(String name, String color) {
        super(name, color);
    }

    @Override
    public Move getNextMove(){
        Move move=pendingMove;
        pendingMove=null;
        return move;
    }

    public void setMove(int position){
        this.pendingMove=new Move(position, getColor());
    }

}