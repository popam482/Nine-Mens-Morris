package Controllers;

import Board.BoardView;
import GameLogic.GameManager;
import GameLogic.MoveResult;
import Navigator.Navigator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx. scene.control.ButtonType;
import javafx.scene.control. Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class BoardController {

    private GameManager gameManager;
    private BoardView boardView;

    @FXML private Circle circle0, circle1, circle2, circle3, circle4, circle5, circle6, circle7;
    @FXML private Circle circle8, circle9, circle10, circle11, circle12, circle13, circle14, circle15;
    @FXML private Circle circle16, circle17, circle18, circle19, circle20, circle21, circle22, circle23;

    @FXML private Label statusLabel;
    @FXML private Label player1Label;
    @FXML private Label player2Label;
    @FXML private Button resetButton;

    @FXML
    private void initialize() {
        Circle[] circles = new Circle[]{
                circle0, circle1, circle2, circle3, circle4, circle5, circle6, circle7,
                circle8, circle9, circle10, circle11, circle12, circle13, circle14, circle15,
                circle16, circle17, circle18, circle19, circle20, circle21, circle22, circle23
        };

        boardView = new BoardView(circles);
        boardView.setLabels(statusLabel, player1Label, player2Label);
    }

    public void initializeGame(String player1Name, String player2Name) {
        gameManager = new GameManager(player1Name, player2Name);
        boardView.update(gameManager);
    }

    @FXML
    private void onClick(MouseEvent e) {
        if (gameManager == null) return;

        int nodeIndex = boardView.getCircleIndex((Circle) e.getSource());
        if (nodeIndex == -1) return;

        MoveResult result = gameManager.processClick(nodeIndex);

        if (result.isSuccess()) {
            boardView.update(gameManager);

            if (result.isGameOver()) {
                showGameOverDialog(result.getWinner().getName());
            }
        } else {
            boardView.showError(result.getMessage());
        }
    }

    @FXML
    private void onResetGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Game");
        alert.setHeaderText("Are you sure you want to reset? ");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            Navigator.goTo("Board.fxml");
        }
    }

    @FXML
    private void backToMainMenu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Back to Menu");
        alert.setHeaderText("Are you sure you want to go back to main menu?");
        alert.setContentText("Current game progress will be lost.");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            Navigator.goTo("Menu.fxml");
        }
    }
    private void showGameOverDialog(String winnerName) {
        Alert alert = new Alert(Alert. AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(winnerName + " wins!");
        alert.setContentText("Congratulations!");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            Navigator.goTo("PlayerSetup.fxml");
        }
    }
}