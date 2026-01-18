package Controllers;

import Board.BoardView;
import Database.GameDatabase;
import GameLogic.GameManager;
import GameLogic.MoveResult;
import Navigator.Navigator;
import Network.NetworkConnection;
import Players.LocalPlayer;
import GameLogic.Move;
import Players.NetworkPlayer;
import Players.Player;
import javafx.application.Platform;
import javafx.fxml. FXML;
import javafx. scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene. control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape. Circle;

import java.io. IOException;
import java.net. ServerSocket;
import java.net. Socket;

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
    private NetworkPlayer remotePlayer;
    private Player localPlayer;
    private Thread receiveThread;
    private boolean isNetworkGame = false;
    private boolean isHost = false;

    private GameDatabase database = new GameDatabase();
    private String gameType="Local";

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
        Player p1 = new LocalPlayer(player1Name, "WHITE");
        Player p2 = new LocalPlayer(player2Name, "BLACK");
        gameManager = new GameManager(p1, p2);
        boardView.update(gameManager);
        isNetworkGame = false;
        gameType="Local";
    }

    public void initializeAsHost(String playerName, int port) {
        isNetworkGame = true;
        isHost = true;
        gameType="Online";

        Platform.runLater(() -> statusLabel.setText("Waiting for opponent on port " + port));

        Thread serverThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Server waiting for connection.. .");

                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected!");

                networkConnection = new NetworkConnection(clientSocket);

                networkConnection.send("NAME:" + playerName);

                String opponentMessage = networkConnection.receive();
                String opponentName = "Opponent";
                if (opponentMessage.startsWith("NAME:")) {
                    opponentName = opponentMessage.substring(5);
                }

                String finalOpponentName=opponentName;

                Player p1 = new NetworkPlayer(playerName, "WHITE", networkConnection, true);
                Player p2 = new NetworkPlayer(finalOpponentName, "BLACK", networkConnection, false);

                remotePlayer = (NetworkPlayer) p2;
                localPlayer = p1;

                Platform.runLater(() -> {
                    gameManager = new GameManager(p1, p2);
                    boardView.update(gameManager);
                    statusLabel.setText("Opponent connected!  Your turn!");
                    setupWindowCloseHandler();
                });

                startReceiveThread();

            } catch (IOException e) {
                Platform.runLater(() -> showError("Failed to start server: " + e.getMessage()));
            }
        });
        serverThread. setDaemon(true);
        serverThread.start();
    }

    public void initializeAsClient(String playerName, String host, int port) {
        try {
            Platform.runLater(() -> statusLabel.setText("Connecting to " + host + ":" + port));

            Socket socket = new Socket(host, port);
            System.out.println("Connected to host!");

            networkConnection = new NetworkConnection(socket);

            String hostMessage = networkConnection.receive();
            String hostName = "Opponent";
            if (hostMessage.startsWith("NAME:")) {
                hostName = hostMessage. substring(5);
            }

            networkConnection.send("NAME:" + playerName);

            String finalHostName = hostName;

            Player p1 = new NetworkPlayer(finalHostName, "WHITE", networkConnection, false);
            Player p2 = new NetworkPlayer(playerName, "BLACK", networkConnection, true);

            remotePlayer = (NetworkPlayer) p1;
            localPlayer = p2;

            Platform.runLater(() -> {
                gameManager = new GameManager(p1, p2);
                boardView.update(gameManager);
                statusLabel.setText("Connected to " + finalHostName + "!");
                setupWindowCloseHandler();
            });

            isNetworkGame = true;
            isHost = false;
            gameType = "Online";

            startReceiveThread();

        } catch (IOException e) {
            showError("Failed to connect:  " + e.getMessage());
        }
    }

    private void startReceiveThread() {
        receiveThread = new Thread(() -> {
            try {
                while (true) {
                    String message = networkConnection.receive();

                    if (message. startsWith("NAME:")) {
                        continue;
                    }

                    if (message.equals("RESET")) {
                        Platform.runLater(() -> {
                            gameManager.resetGame();
                            boardView.update(gameManager);
                            showInfo("Opponent reset the game!");
                        });
                        continue;
                    }

                    if (message.equals("MENU")) {
                        Platform.runLater(() -> {
                            showInfo("Opponent left the game.");
                            Navigator.goTo("Menu");
                        });
                        break;
                    }

                    if (message.contains(":") && !message.equals("RESET") && !message.equals("MENU")) {
                        Move move = Move.fromString(message);
                        if (move != null) {
                            remotePlayer.receiveMove(move);
                            Platform.runLater(() -> applyOpponentMove(move));
                            continue;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Connection closed: " + e.getMessage());
            }
        });
        receiveThread.setDaemon(true);
        receiveThread. start();
    }

    private void applyOpponentMove(Move move) {
        System.out.println("Applying opponent move: from=" + move.getFrom() + " to=" + move.getTo());

        MoveResult result;

        if (move.isSinglePosition()) {
            result = gameManager.processClick(move.getTo());
        } else {
            result = gameManager.processClick(move.getFrom());
            if (result.isSuccess()) {
                result = gameManager.processClick(move.getTo());
            }
        }

        boardView.update(gameManager);

        if (result.isSuccess() && result.isGameOver()) {
            showGameOverDialog(result.getWinner().getName());
        }
    }

    @FXML
    private void onClick(MouseEvent e) {
        if (gameManager == null) return;

        if (isNetworkGame) {
            Player currentPlayer = gameManager. getCurrentPlayer();
            if (currentPlayer != localPlayer) {
                boardView.showError("Wait for opponent's turn!");
                return;
            }
        }

        int nodeIndex = boardView.getCircleIndex((Circle) e.getSource());
        if (nodeIndex == -1) return;

        Player currentPlayer = gameManager. getCurrentPlayer();
        MoveResult result = currentPlayer.processClick(nodeIndex, gameManager);

        if (result.isSuccess()) {
            boardView.update(gameManager);

            if (result.isGameOver()) {
                saveGameToDatabase(result.getWinner());
                showGameOverDialog(result. getWinner().getName());
            }
        } else {
            gameManager.clearSelection();
            boardView.update(gameManager);
            boardView.showError(result.getMessage());
        }
    }



    @FXML
    private void onResetGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Game");
        alert.setHeaderText("Are you sure you want to reset? ");

        if (alert.showAndWait().orElse(ButtonType. CANCEL) == ButtonType.OK) {
            gameManager.resetGame();
            boardView.update(gameManager);

            if (isNetworkGame && networkConnection != null) {
                networkConnection.send("RESET");
            }
        }
    }

    @FXML
    private void backToMainMenu() {
        Alert alert = new Alert(Alert.AlertType. CONFIRMATION);
        alert.setTitle("Back to Menu");
        alert.setHeaderText("Are you sure you want to go back to main menu?");
        alert.setContentText("Current game will be lost.");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (isNetworkGame && networkConnection != null) {
                networkConnection.send("MENU");
                new Thread(() -> {
                    try {
                        networkConnection.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }

            Navigator.goTo("Menu");
        }
    }

    public void setupWindowCloseHandler() {
        Platform.runLater(() -> {
            javafx.stage.Stage stage = (javafx.stage.Stage) statusLabel.getScene().getWindow();

            if (stage != null) {
                stage. setOnCloseRequest(event -> {
                    if (isNetworkGame && networkConnection != null) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Exit Game");
                        alert.setHeaderText("Are you sure you want to exit?");

                        if (alert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
                            event.consume();
                            return;
                        }

                        networkConnection.send("MENU");

                        final NetworkConnection conn = networkConnection;
                        networkConnection = null;

                        new Thread(() -> {
                            try {
                                Thread.sleep(100);
                                conn.close();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }).start();
                    }
                });
            }
        });
    }

    private void saveGameToDatabase(Player winner) {
        try {
            String player1Name = gameManager.getPlayer1().getName();
            String player2Name = gameManager.getPlayer2().getName();
            String winnerName = winner.getName();
            long duration = gameManager.getGameDurationSeconds();

            database.saveGame(player1Name, player2Name, winnerName, duration, gameType);

            System.out.println("Game saved: " + winnerName + " won in " + duration + " seconds");

        } catch (Exception e) {
            System.err.println("Failed to save game to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showGameOverDialog(String winnerName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(winnerName + " wins!");
        alert.setContentText("Congratulations!");

        if (alert.showAndWait().orElse(ButtonType. CANCEL) == ButtonType.OK) {
            Navigator.goTo("Menu");
        }
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert. setContentText(message);
            alert.showAndWait();
        });
    }

    private void showInfo(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType. INFORMATION);
            alert.setTitle("Info");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

}