package Board;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Node {

    private boolean occupied;
    private String color;
    private int neighbours[]; // 4 - 0-left 1-up 2-right 3-down

    public Node(){
        this.occupied = false;
        this.color = "GRAY";
        this.neighbours = new int[]{-1, -1, -1, -1};
    }

    // SETTERS
    public void setNeighbours(int direction, int nodeId){
        if (direction < 0 || direction > 3) return;
        neighbours[direction] = nodeId;
    }

    public void setOccupied(boolean occupied){
        this.occupied = occupied;
    }

    public void setColor(String color){
        this.color = color;
        this.occupied = !"GRAY".equals(color);
    }

    // GETTERS
    public boolean isOccupied(){
        return occupied;
    }

    public boolean isEmpty(){
        return !occupied;
    }

    public String getColor(){
        return color;
    }

    public int[] getNeighbours(){
        return neighbours;
    }
    
    // PAINT PIECES
    public void paintEmpty(Circle c){
        c.setFill(Color.GRAY);
        c.setStroke(Color.GRAY);
        c.setStrokeWidth(2);
    }

    public void paintSlot(Circle c){
        if ("WHITE".equals(color)) paintWhite(c);
        else if ("BLACK".equals(color)) paintBlack(c);
        else paintEmpty(c);
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