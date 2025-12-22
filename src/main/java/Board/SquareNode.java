package Board;

public class SquareNode extends Node {

    private int positionInSquare;
    private int level; // 0-outside square 1-middle square 2-inside square

    public SquareNode(int level, int positionInSquare) {
        super();
        this.level = level;
        this.positionInSquare = positionInSquare;
    }

    public int getLevel(){
        return level;
    }

    public int getPositionInSquare(){
        return positionInSquare;
    }


}
