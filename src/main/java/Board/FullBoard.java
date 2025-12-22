package Board;

import GameLogic.GameRules;
import Players.Player;
import javafx.scene.shape.Circle;

public class FullBoard {

    private final SquareNode[] allNodes = new SquareNode[24];

    public FullBoard() {
        initializeNodes();
        initializeRingNeighbours();
        initializeVerticalLinks();
    }

    private void initializeNodes() {
        for (int i = 0; i < 8; i++) {
            allNodes[i] = new SquareNode(0, i);       // outer 0..7
            allNodes[8 + i] = new SquareNode(1, i);   // middle 8..15
            allNodes[16 + i] = new SquareNode(2, i);  // inner 16..23
        }
    }

    private void initializeRingNeighbours() {
        for (int base = 0; base <= 16; base += 8) {
            for (int i = 0; i < 8; i++) {
                int leftIdx = (i + 7) % 8;
                int rightIdx = (i + 1) % 8;
                allNodes[base + i].setNeighbours(0, base + leftIdx);
                allNodes[base + i].setNeighbours(2, base + rightIdx);
            }
        }
    }

    private void initializeVerticalLinks() {
        int[] cols = {1, 3, 5, 7};
        for (int pos : cols) {
            allNodes[0 + pos].setNeighbours(3, 8 + pos);
            allNodes[8 + pos].setNeighbours(1, 0 + pos);

            allNodes[8 + pos].setNeighbours(3, 16 + pos);
            allNodes[16 + pos].setNeighbours(1, 8 + pos);
        }
    }

    public SquareNode getNode(int index) {
        if (index < 0 || index >= 24) return null;
        return allNodes[index];
    }

    public SquareNode[] getAllNodes() {
        return allNodes;
    }


    public int getNodeIndex(SquareNode node) {
        for (int i = 0; i < 24; i++) {
            if (allNodes[i] == node) {
                return i;
            }
        }
        return -1;
    }


    public int countPieces(String color) {
        int count = 0;
        for (SquareNode node : allNodes) {
            if (color.equals(node.getColor())) {
                count++;
            }
        }
        return count;
    }


    public boolean hasValidMoves(Player player, GameRules rules) {
        String color = player.getColor();

        for (int from = 0; from < 24; from++) {
            if (color.equals(allNodes[from].getColor())) {
                for (int to = 0; to < 24; to++) {
                    if (rules.canMove(this, player, from, to)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void updateVisuals(Circle[] circles) {
        for (int i = 0; i < 24; i++) {
            allNodes[i].paintSlot(circles[i]);
        }
    }

    public void reset() {
        for (SquareNode node : allNodes) {
            node.setColor("GRAY");
            node.setOccupied(false);
        }
    }
}