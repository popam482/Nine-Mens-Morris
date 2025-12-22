package Board;

import GameLogic.GameManager;
import Players.Player;
import javafx.scene. control.Alert;
import javafx.scene.control.Label;
import javafx.scene.shape. Circle;

public class BoardView {

    private static final String HIGHLIGHT_STYLE = "-fx-effect: dropshadow(gaussian, yellow, 10, 0.7, 0, 0);";
    private static final String DEFAULT_STYLE = "";

    private final Circle[] circles;

    private Label statusLabel;
    private Label player1Label;
    private Label player2Label;

    public BoardView(Circle[] circles) {
        this.circles = circles;
    }

    public void setLabels(Label statusLabel, Label player1Label, Label player2Label) {
        this.statusLabel = statusLabel;
        this.player1Label = player1Label;
        this.player2Label = player2Label;
    }

    public void update(GameManager gameManager) {
        if (gameManager == null || circles == null)
            return;
        updateBoard(gameManager);
        updateStatusLabel(gameManager);
        updatePlayerLabels(gameManager);
        highlightSelectedPiece(gameManager.getSelectedNodeIndex());
    }

    private void updateBoard(GameManager gameManager) {
        gameManager.getBoard().updateVisuals(circles);
    }

    private void updateStatusLabel(GameManager gameManager) {
        if (statusLabel != null) {
            statusLabel.setText(gameManager.getGameStatus());
        }
    }

    private void updatePlayerLabels(GameManager gameManager) {
        if (player1Label != null) {
            player1Label.setText(formatPlayerInfo(gameManager.getPlayer1(), "WHITE"));
        }

        if (player2Label != null) {
            player2Label.setText(formatPlayerInfo(gameManager.getPlayer2(), "BLACK"));
        }
    }

    public String formatPlayerInfo(Player player, String color) {
        return String.format("%s (%s) - Pieces: %d on board, %d to place",
                player.getName(),
                color,
                player.piecesOnBoard(),
                player. piecesAvailable()
        );
    }

    private void highlightSelectedPiece(int selectedIndex) {
        for (int i = 0; i < circles.length; i++) {
            circles[i].setStyle(i == selectedIndex ? HIGHLIGHT_STYLE : DEFAULT_STYLE);
        }
    }

    public int getCircleIndex(Circle circle) {
        for (int i = 0; i < circles.length; i++) {
            if (circles[i] == circle) {
                return i;
            }
        }
        return -1;
    }

    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Move");
        alert.setHeaderText("Cannot perform this action");
        alert.setContentText(message);
        alert.showAndWait();
    }
}