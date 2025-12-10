package Players;

public abstract class Player {

    private String name;
    private String color; // "WHITE" sau "BLACK"
    private int piecesAvailable;
    private int piecesOnBoard;

    public Player(String name, String color){
        this.name = name;
        this.color = color;
        this.piecesAvailable = 9;
        this.piecesOnBoard = 0;
    }

    // GETTERS
    public String getName(){
        return name;
    }

    public String getColor(){
        return color;
    }

    public int piecesOnBoard(){
        return piecesOnBoard;
    }

    public int piecesAvailable(){
        return piecesAvailable;
    }

    // SETTERS
    public void decrementAvailablePieces(){
        if(piecesAvailable > 0) {
            piecesAvailable--;
        }
    }

    public void incrementPiecesOnBoard(){
        piecesOnBoard++;
    }

    public void decrementPiecesOnBoard(){
        if(piecesOnBoard > 0) {
            piecesOnBoard--;
        }
    }

    public void resetForNewGame() {
        this.piecesAvailable = 9;
        this.piecesOnBoard = 0;
    }

}