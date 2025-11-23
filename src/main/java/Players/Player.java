package Players;

public abstract class Player {

    private String name;
    private int piecesAvailable;
    private int piecesOnBoard;

    public Player(String name){
        this.name=name;
        piecesAvailable=9;
        piecesOnBoard=0;
    }

    //GETTERS
    public String getName(){
        return name;
    }

    public int piecesOnBoard(){
        return piecesOnBoard;
    }

    public int piecesAvailable(){
        return piecesAvailable;
    }

}
