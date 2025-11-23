package GameLogic;

import Players.LocalPlayer;
import javafx.scene.shape.Circle;

public class FullBoard {

    private final SquareNode[] allNodes = new SquareNode[24];
    LocalPlayer player1, player2;

    public FullBoard() {
        initializeNodes();
        initializeRingNeighbours();   // left/right inside each square
        initializeVerticalLinks();    // up/down between squares for positions 1,3,5,7
        //
        // initializeGame();
    }

    public FullBoard(String playerName1, String playerName2) {
        initializeNodes();
        initializeRingNeighbours();
        initializeVerticalLinks();
        setPlayers(playerName1, playerName2);
        // alte initializari de joc (daca este cazul)
    }


    private void initializeNodes() {
        for (int i = 0; i < 8; i++) {
            allNodes[i] = new SquareNode(0, i);       // outer 0..7
            allNodes[8 + i] = new SquareNode(1, i);   // middle 8..15
            allNodes[16 + i] = new SquareNode(2, i);  // inner 16..23
        }
    }

    // set left(0) and right(2) neighbours for each node in each 8-block
    private void initializeRingNeighbours() {
        for (int base = 0; base <= 16; base += 8) { // 0,8,16
            for (int i = 0; i < 8; i++) {
                int leftIdx = (i + 7) % 8;
                int rightIdx = (i + 1) % 8;
                allNodes[base + i].setNeighbours(0, base + leftIdx);   // left
                allNodes[base + i].setNeighbours(2, base + rightIdx);  // right
            }
        }
    }

    // set up(1) and down(3) neighbours for the four vertical columns: 1,3,5,7
    private void initializeVerticalLinks() {
        int[] cols = {1, 3, 5, 7};
        for (int pos : cols) {
            // outer -> middle
            allNodes[0 + pos].setNeighbours(3, 8 + pos);   // outer down -> middle
            allNodes[8 + pos].setNeighbours(1, 0 + pos);   // middle up -> outer

            // middle -> inner
            allNodes[8 + pos].setNeighbours(3, 16 + pos);  // middle down -> inner
            allNodes[16 + pos].setNeighbours(1, 8 + pos);  // inner up -> middle
        }
    }



    public void setPlayers(String name1, String name2) {
        if (name1 == null) name1 = "Player1";
        if (name2 == null) name2 = "Player2";
        player1 = new LocalPlayer(name1);
    }


    public void switchPlayer() {
        //currentPlayer = currentPlayer.equals("WHITE") ? "BLACK" : "WHITE";
    }

  /* public boolean isGameOver() {
       // return whitePiecesOnBoard < 3 || blackPiecesOnBoard < 3;
    }
   */

    public String getWinner() {
       // if (whitePiecesOnBoard < 3) return "BLACK";
        //if (blackPiecesOnBoard < 3) return "WHITE";
        return null;
    }

    // GETTERS / UTIL
    public SquareNode getNode(int index) { return allNodes[index]; }
    public SquareNode[] getAllNodes() { return allNodes; }
    //public String getCurrentPlayer() { return currentPlayer; }
   // public GamePhase getPhase() { return phase; }

    public void updateVisuals(Circle[] circles) {
        for (int i = 0; i < 24; i++) {
            allNodes[i].paintSlot(circles[i]);
        }
    }
}