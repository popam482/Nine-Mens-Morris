package Controllers;

import Database.GameDatabase;
import Navigator.Navigator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class MenuController {

    @FXML
    Button startLocalButton, startOnlineButton, howToPlayButton;

    @FXML
    private void startLocalSession() {
        TextInputDialog dialog = new TextInputDialog("Player 1");
        dialog.setTitle("Player 1 (WHITE) Name");
        dialog.setHeaderText("Enter Player 1 (WHITE) name:");
        dialog.setContentText("Name:");
        Optional<String> playerName1 = dialog.showAndWait();

        if (playerName1.isEmpty() || playerName1.get().trim().isEmpty()) {
            return;
        }

        dialog = new TextInputDialog("Player 2");
        dialog.setTitle("Player 2 (BLACK) Name");
        dialog.setHeaderText("Enter Player 2 (BLACK) name:");
        dialog.setContentText("Name:");
        Optional<String> playerName2 = dialog.showAndWait();

        if (playerName2.isEmpty() || playerName2.get().trim().isEmpty()) {
            return;
        }


        BoardController controller = Navigator.goTo("Board.fxml");
        if (controller != null) {
            controller.initializeGame(playerName1.get().trim(), playerName2.get().trim());
        }
    }

    @FXML
    private void startOnlineSession() {
        TextInputDialog nameDialog = new TextInputDialog("Player");
        nameDialog.setTitle("Online Game");
        nameDialog.setHeaderText("Enter your name:");
        nameDialog.setContentText("Name:");
        Optional<String> playerName = nameDialog.showAndWait();

        if (playerName.isEmpty() || playerName.get().trim().isEmpty()) {
            return;
        }

        TextInputDialog ipDialog = new TextInputDialog("127.0.0.1");
        ipDialog.setTitle("Online Game");
        ipDialog.setHeaderText("Enter opponent's IP address");
        ipDialog.setContentText("Leave empty to HOST, or enter IP to JOIN:");
        Optional<String> ipAddress = ipDialog.showAndWait();

        if (ipAddress.isEmpty()) {
            return;
        }

        BoardController controller = Navigator.goTo("Board.fxml");
        if (controller == null) {
            return;
        }

        try {
            String ip = ipAddress.get().trim();

            if (ip.isEmpty()) {
                //empty->host
                controller.initializeAsHost(playerName.get().trim(), 8888);
            } else {
                // ip->client
                controller.initializeAsClient(playerName.get().trim(), ip, 8888);
            }
        } catch (Exception e) {
            showError("Network error: " + e. getMessage());
        }
    }

    @FXML
    private void howToPlay() {
        Navigator.goTo("HowToPlay.fxml");
    }

    @FXML
    private void viewHistory() {
        Navigator.goTo("SessionHistory.fxml");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }


}
