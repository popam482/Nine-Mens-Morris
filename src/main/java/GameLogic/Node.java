package GameLogic;

import Controllers.BoardController;
import javafx.scene.shape.Circle;

import javafx.scene.paint.Color;

public class Node {

    private boolean occupied;
    private String color;
    private int neighbours[]; // 4 - 0-left 1-up 2- right 3- down

    public Node(){
        this.occupied=false;
        this.color="GRAY";
        this.neighbours=new int[]{-1, -1, -1, -1}; // we don't know the neighbours yet
    }

    //SETTERS

    public void setNeighbours(int direction, int nodeId){ // direction needs to be between 0-4, node id between 0-23
        neighbours[direction]=nodeId;
    }

    public void setOccupied(boolean occupied){
        this.occupied=occupied;
    }

    public void setColor(String color){
        this.color=color;
        this.occupied=!color.equals("GRAY");
    }

    //GETTERS

    public boolean isOccupied(){
        return occupied;
    }

    public String getColor(){
        return color;
    }

    public int[] getNeighbours(){
        return neighbours;
    }

    public int getNeighbour(int direction){
        return neighbours[direction];
    }

    //PAINT PIECES

    public void paintEmpty(Circle c){

        c.setFill(Color.GRAY);
        c.setStroke(Color.GRAY);
        c.setStrokeWidth(2);

    }

    public void paintSlot(Circle c){

        switch (color) {
            case "WHITE" -> paintWhite(c);
            case "BLACK" -> paintBlack(c);
        }

    }

    public void paintWhite(Circle c){

        c.setFill(Color.WHITE);
        c.setStroke(Color.BLACK);
        c.setStrokeWidth(2);

    }

    public void paintBlack(Circle c){

        c.setFill(Color.BLACK);
        c.setStroke(Color.BLACK);
        c.setStrokeWidth(2);

    }

}
