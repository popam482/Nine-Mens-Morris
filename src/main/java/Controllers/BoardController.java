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
    private boolean isHost=false;


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
        Player p1 = new LocalPlayer(playerName, "WHITE");
        Player p2 = new LocalPlayer("Opponent", "BLACK");
        gameManager = new GameManager(p1, p2);
        boardView.update(gameManager);

        isNetworkGame = true;
        isHost = true;

        Platform.runLater(() -> statusLabel.setText("Waiting for opponent on port " + port));

        Thread serverThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();

                networkConnection = new NetworkConnection(clientSocket);

                Platform.runLater(() -> {
                    statusLabel.setText("Opponent connected!  Your turn!");
                    boardView.update(gameManager);
                });

                startReceiveThread();

            } catch (IOException e) {
                Platform.runLater(() -> showError("Failed to start server: " + e.getMessage()));
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
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
            isHost=false;
            startReceiveThread();

        } catch (IOException e) {
            showError("Failed to connect: " + e.getMessage());
        }
    }

    private void startReceiveThread() {
        receiveThread = new Thread(() -> {
            try {
                while (true) {
                    String message = networkConnection. receive();
                    System.out. println("📥 RECEIVED:  " + message);

                    if (message.equals("RESET")) {
                        Platform.runLater(() -> {
                            gameManager.resetGame();
                            boardView.update(gameManager);
                            showInfo("Opponent reset the game!");
                        });
                        continue;
                    }

                    if (message.equals("MENU")) {
                        Platform. runLater(() -> {
                            showInfo("Opponent left the game.");
                            Navigator.goTo("Menu.fxml");
                        });
                        break;
                    }

                    // Parse protocol
                    String[] parts = message.split(":");
                    String type = parts[0];
                    int pos1 = Integer.parseInt(parts[1]);
                    int pos2 = Integer.parseInt(parts[2]);
                    String color = parts[3];

                    Platform.runLater(() -> {
                        try {
                            switch (type) {
                                case "PLACE":
                                case "REMOVE":
                                    // ✅ Un singur click - folosește processClick
                                    MoveResult result = gameManager.processClick(pos1);
                                    System.out.println("   Applied " + type + " at " + pos1 + ":  " + result.getMessage());
                                    break;

                                case "MOVE":
                                    // ✅ MOVE trebuie aplicat FORȚAT fără validare de tură!
                                    // Setăm manual selectedNodeIndex și aplicăm mutarea

                                    System.out.println("   Applying MOVE from " + pos1 + " to " + pos2);

                                    // Hack:  Setăm selectedNodeIndex direct în GameManager
                                    // (trebuie să adaugi setter în GameManager!)
                                    gameManager.setSelectedNodeIndex(pos1);

                                    // Acum aplicăm mutarea
                                    MoveResult moveResult = gameManager. processClick(pos2);
                                    System.out.println("   Move result: " + moveResult.getMessage());
                                    break;
                            }

                            boardView.update(gameManager);

                        } catch (Exception e) {
                            System.err.println("Error applying move: " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                }
            } catch (IOException e) {
                System.out.println("Connection closed");
            }
        });
        receiveThread. setDaemon(true);
        receiveThread.start();
    }

    private void showInfo(String s) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setContentText(s);
            alert.showAndWait();
        });
    }


    @FXML
    private void onClick(MouseEvent e) {
        if (gameManager == null) return;

        if (isNetworkGame) {
            Player currentPlayer = gameManager.getCurrentPlayer();
            Player localPlayer = getLocalPlayer();

            if (currentPlayer != localPlayer) {
                boardView. showError("Wait for opponent's turn");
                return;
            }
        }

        int nodeIndex = boardView.getCircleIndex((Circle) e.getSource());
        if (nodeIndex == -1) return;


        String currentColor = gameManager.getCurrentPlayer().getColor();
        Player playerBeforeMove = gameManager.getCurrentPlayer();

        MoveResult result = gameManager.processClick(nodeIndex);

        if (result.isSuccess()) {
            boardView.update(gameManager);
            if (isNetworkGame && networkConnection != null) {
                Player playerAfterMove = gameManager.  getCurrentPlayer();

                boolean turnChanged = (playerBeforeMove != playerAfterMove);
                boolean isRemovePhase = result.isRemovePhase();
                if (turnChanged || isRemovePhase) {
                    String message = nodeIndex + ":" + currentColor;
                    networkConnection.send(message);
                    System.out.println("SENT: " + message + " (turn changed:  " + turnChanged + ", remove:  " + isRemovePhase + ")");
                } else {
                    System.out.println("Piece selected (turn NOT changed), not sending");
                }
            }

            if (result.isGameOver()) {
                showGameOverDialog(result.getWinner().getName());
            }
        } else {
            boardView.showError(result.getMessage());
        }
    }
    private Player getLocalPlayer() {
        return isHost?gameManager.getPlayer1(): gameManager.getPlayer2();
    }

    @FXML
    private void onResetGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Game");
        alert.setHeaderText("Are you sure you want to reset? ");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            gameManager.resetGame();
            boardView.update(gameManager);
            if(isNetworkGame && networkConnection!=null){
                networkConnection.send("RESET");
            }
        }
    }

    @FXML
    private void backToMainMenu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Back to Menu");
        alert.setHeaderText("Are you sure you want to go back to main menu?");
        alert.setContentText("Current game will be lost.");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

            if(isNetworkGame && networkConnection!=null){
                networkConnection.send("MENU");
                try{
                    networkConnection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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