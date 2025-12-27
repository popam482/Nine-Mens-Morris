package Controllers;

import Board.BoardView;
import GameLogic.GameManager;
import GameLogic.MoveResult;
import Navigator.Navigator;
import Network.NetworkConnection;
import Players.LocalPlayer;
import Players.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx. scene.control.ButtonType;
import javafx.scene.control. Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

    private NetworkConnection networkConnection;
    private Thread receiveThread;
    private boolean isNetworkGame = false;


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
        Player p1=new LocalPlayer(player1Name, "WHITE");
        Player p2=new LocalPlayer(player2Name, "BLACK");
        gameManager = new GameManager(p1, p2);
        boardView.update(gameManager);
        isNetworkGame=false;
    }

    public void initializeAsHost(String playerName, int port) {
        try {
            Platform.runLater(() -> statusLabel.setText("Waiting for opponent on port " + port + "..."));

            ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();

            Platform.runLater(() -> statusLabel.setText("✅ Opponent connected! "));

            networkConnection = new NetworkConnection(clientSocket);

            Player p1 = new LocalPlayer(playerName, "WHITE");
            Player p2 = new LocalPlayer("Opponent", "BLACK");
            gameManager = new GameManager(p1, p2);
            boardView.update(gameManager);

            isNetworkGame = true;
            startReceiveThread();

        } catch (IOException e) {
            showError("Failed to start server: " + e.getMessage());
        }
    }


    public void initializeAsClient(String playerName, String host, int port) {
        try {
            Platform.runLater(() -> statusLabel.setText("Connecting to " + host + ":" + port + "..."));

            Socket socket = new Socket(host, port);

            Platform.runLater(() -> statusLabel.setText("" + "Connected to host!"));

            networkConnection = new NetworkConnection(socket);

            Player p1 = new LocalPlayer("Opponent", "WHITE");
            Player p2 = new LocalPlayer(playerName, "BLACK");
            gameManager = new GameManager(p1, p2);
            boardView.update(gameManager);

            isNetworkGame = true;
            startReceiveThread();

        } catch (IOException e) {
            showError("Failed to connect: " + e.getMessage());
        }
    }

    private void startReceiveThread() {
        receiveThread = new Thread(() -> {
            try {
                while (true) {
                    String message = networkConnection.receive();
                    int position = Integer.parseInt(message. split(":")[0]);
                    Platform.runLater(() -> {
                        MoveResult result = gameManager.processClick(position);
                        boardView.update(gameManager);

                        if (result. isSuccess() && result.isGameOver()) {
                            showGameOverDialog(result. getWinner().getName());
                        }
                    });
                }
            } catch (IOException e) {
                System.out.println("Connection closed");
            }
        });
        receiveThread.setDaemon(true);
        receiveThread.start();
    }


    @FXML
    private void onClick(MouseEvent e) {
        if (gameManager == null) return;

        int nodeIndex = boardView.getCircleIndex((Circle) e.getSource());
        if (nodeIndex == -1) return;

        MoveResult result = gameManager.processClick(nodeIndex);

        if (result.isSuccess()) {
            boardView.update(gameManager);
            if (isNetworkGame && networkConnection != null) {
                String message = nodeIndex + ":" + gameManager.getCurrentPlayer().getColor();
                networkConnection.send(message);
            }
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
            gameManager.resetGame();
            boardView.update(gameManager);
        }
    }

    @FXML
    private void backToMainMenu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Back to Menu");
        alert.setHeaderText("Are you sure you want to go back to main menu?");
        alert.setContentText("Current game will be lost.");

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
            Navigator.goTo("Menu.fxml");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

}