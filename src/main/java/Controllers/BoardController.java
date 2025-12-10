package Controllers;

import GameLogic.GameManager;
import GameLogic.MoveResult;
import Navigator.Navigator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class BoardController {

    private GameManager gameManager;

    @FXML private Circle circle0, circle1, circle2, circle3, circle4, circle5, circle6, circle7;
    @FXML private Circle circle8, circle9, circle10, circle11, circle12, circle13, circle14, circle15;
    @FXML private Circle circle16, circle17, circle18, circle19, circle20, circle21, circle22, circle23;

    @FXML private Label statusLabel;
    @FXML private Label player1Label;
    @FXML private Label player2Label;
    @FXML private Button resetButton;

    private Circle[] circles;


    @FXML
    private void initialize() {
        circles = new Circle[]{
                circle0, circle1, circle2, circle3, circle4, circle5, circle6, circle7,
                circle8, circle9, circle10, circle11, circle12, circle13, circle14, circle15,
                circle16, circle17, circle18, circle19, circle20, circle21, circle22, circle23
        };
    }


    public void initializeGame(String player1Name, String player2Name) {
        gameManager = new GameManager(player1Name, player2Name);
        updateUI();
    }


    @FXML
    private void onClick(MouseEvent e) {
        if (gameManager == null) {
            return;
        }

        Circle clickedCircle = (Circle) e.getSource();
        int nodeIndex = getCircleIndex(clickedCircle);

        if (nodeIndex == -1) {
            return;
        }

        MoveResult result = gameManager.processClick(nodeIndex);

        if (result.isSuccess()) {
            updateUI();

            if (result.isGameOver()) {
                showGameOverDialog(result.getWinner().getName());
            }
        } else {
            showError(result.getMessage());
        }
    }


    private int getCircleIndex(Circle circle) {
        for (int i = 0; i < circles.length; i++) {
            if (circles[i] == circle) {
                return i;
            }
        }
        return -1;
    }


    private void updateUI() {
        if (gameManager == null || circles == null) {
            return;
        }

        gameManager.getBoard().updateVisuals(circles);

        if (statusLabel != null) {
            statusLabel.setText(gameManager.getGameStatus());
        }

        if (player1Label != null) {
            player1Label.setText(String.format("%s (WHITE) - Pieces: %d on board, %d to place",
                    gameManager.getPlayer1().getName(),
                    gameManager.getPlayer1().piecesOnBoard(),
                    gameManager.getPlayer1().piecesAvailable()));
        }

        if (player2Label != null) {
            player2Label.setText(String.format("%s (BLACK) - Pieces: %d on board, %d to place",
                    gameManager.getPlayer2().getName(),
                    gameManager.getPlayer2().piecesOnBoard(),
                    gameManager.getPlayer2().piecesAvailable()));
        }

        highlightSelectedPiece();
    }


    private void highlightSelectedPiece() {
        int selected = gameManager.getSelectedNodeIndex();

        for (int i = 0; i < circles.length; i++) {
            if (i == selected) {
                circles[i].setStyle("-fx-effect: dropshadow(gaussian, yellow, 10, 0.7, 0, 0);");
            } else {
                circles[i].setStyle("");
            }
        }
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Move");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void showGameOverDialog(String winnerName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("We have a winner!");
        alert.setContentText(winnerName + " has won the game!");

        ButtonType playAgain = new ButtonType("Play Again");
        ButtonType mainMenu = new ButtonType("Main Menu");

        alert.getButtonTypes().setAll(playAgain, mainMenu);

        alert.showAndWait().ifPresent(response -> {
            if (response == playAgain) {
                gameManager.resetGame();
                updateUI();
            } else {
                backToMainMenu();
            }
        });
    }


    @FXML
    private void onResetGame() {
        if (gameManager != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Reset Game");
            confirm.setHeaderText("Are you sure?");
            confirm.setContentText("This will restart the current game.");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    gameManager.resetGame();
                    updateUI();
                }
            });
        }
    }


    @FXML
    private void backToMainMenu() {
        Navigator.goTo("Menu.fxml");
    }
}