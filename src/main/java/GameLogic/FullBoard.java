package GameLogic;

public class FullBoard {

    private SquareNode[] allNodes;
    String currentPlayer;

    public FullBoard() {
        allNodes = new SquareNode[3];
        intializeNodes();
        setAllNeighbours();
        intializeGame();
    }

    private void intializeNodes(){
        for(int i=0; i<8; i++){
            allNodes[i]=new SquareNode(0, i);
        }

        for(int i=0; i<8; i++){
            allNodes[i+8]=new SquareNode(1, i);
        }

        for(int i=0; i<8; i++){
            allNodes[i+16]=new SquareNode(2, i);
        }

    }

    private void setAllNeighbours() {
        // Setează conexiunile conform Board.fxml

        // Outer square - conexiuni pe laturi (orizontale/verticale)
        allNodes[0].setNeighbours(2, 1);  // dreapta
        allNodes[0].setNeighbours(3, 7);  // jos

        allNodes[1].setNeighbours(0, 0);  // stânga
        allNodes[1].setNeighbours(2, 2);  // dreapta
        allNodes[1].setNeighbours(3, 9);  // jos (către middle square)

        allNodes[2].setNeighbours(0, 1);  // stânga
        allNodes[2].setNeighbours(3, 3);  // jos

        allNodes[3].setNeighbours(1, 2);  // sus
        allNodes[3].setNeighbours(2, 11); // dreapta (către middle square)
        allNodes[3].setNeighbours(3, 4);  // jos

        allNodes[4].setNeighbours(1, 3);  // sus
        allNodes[4].setNeighbours(0, 5);  // stânga

        allNodes[5].setNeighbours(2, 4);  // dreapta
        allNodes[5].setNeighbours(0, 6);  // stânga
        allNodes[5].setNeighbours(1, 13); // sus (către middle square)

        allNodes[6].setNeighbours(2, 5);  // dreapta
        allNodes[6].setNeighbours(1, 7);  // sus

        allNodes[7].setNeighbours(3, 6);  // jos
        allNodes[7].setNeighbours(1, 0);  // sus
        allNodes[7].setNeighbours(2, 15); // dreapta (către middle square)

        // Middle square
        allNodes[8].setNeighbours(2, 9);   // dreapta
        allNodes[8].setNeighbours(3, 15);  // jos

        allNodes[9].setNeighbours(0, 8);   // stânga
        allNodes[9].setNeighbours(2, 10);  // dreapta
        allNodes[9].setNeighbours(1, 1);   // sus (către outer square)
        allNodes[9].setNeighbours(3, 17);  // jos (către inner square)

        allNodes[10].setNeighbours(0, 9);  // stânga
        allNodes[10].setNeighbours(3, 11); // jos

        allNodes[11].setNeighbours(1, 10); // sus
        allNodes[11].setNeighbours(0, 3);  // stânga (către outer square)
        allNodes[11].setNeighbours(2, 19); // dreapta (către inner square)
        allNodes[11].setNeighbours(3, 12); // jos

        allNodes[12].setNeighbours(1, 11); // sus
        allNodes[12].setNeighbours(0, 13); // stânga

        allNodes[13].setNeighbours(2, 12); // dreapta
        allNodes[13].setNeighbours(0, 14); // stânga
        allNodes[13].setNeighbours(2, 5);  // dreapta (către outer square)
        allNodes[13].setNeighbours(1, 21); // sus (către inner square)

        allNodes[14].setNeighbours(2, 13); // dreapta
        allNodes[14].setNeighbours(1, 15); // sus

        allNodes[15].setNeighbours(3, 14); // jos
        allNodes[15].setNeighbours(1, 8);  // sus
        allNodes[15].setNeighbours(0, 7);  // stânga (către outer square)
        allNodes[15].setNeighbours(2, 23); // dreapta (către inner square)

        // Inner square
        allNodes[16].setNeighbours(2, 17); // dreapta
        allNodes[16].setNeighbours(3, 23); // jos

        allNodes[17].setNeighbours(0, 16); // stânga
        allNodes[17].setNeighbours(2, 18); // dreapta
        allNodes[17].setNeighbours(1, 9);  // sus (către middle square)

        allNodes[18].setNeighbours(0, 17); // stânga
        allNodes[18].setNeighbours(3, 19); // jos

        allNodes[19].setNeighbours(1, 18); // sus
        allNodes[19].setNeighbours(0, 11); // stânga (către middle square)
        allNodes[19].setNeighbours(3, 20); // jos

        allNodes[20].setNeighbours(1, 19); // sus
        allNodes[20].setNeighbours(0, 21); // stânga

        allNodes[21].setNeighbours(2, 20); // dreapta
        allNodes[21].setNeighbours(0, 22); // stânga
        allNodes[21].setNeighbours(3, 13); // jos (către middle square)

        allNodes[22].setNeighbours(2, 21); // dreapta
        allNodes[22].setNeighbours(1, 23); // sus

        allNodes[23].setNeighbours(3, 22); // jos
        allNodes[23].setNeighbours(1, 16); // sus
        allNodes[23].setNeighbours(0, 15); // stânga (către middle square)
    }

    private void intializeGame(){
        
    }

}
