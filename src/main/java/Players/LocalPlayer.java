package Players;

import GameLogic.GameManager;
import GameLogic.MoveResult;

public class LocalPlayer extends Player {

    public LocalPlayer(String name, String color) {
        super(name, color);
    }

    @Override
    public MoveResult processClick(int nodeId, GameManager gameManager) {
        return gameManager.processClick(nodeId);
    }
}