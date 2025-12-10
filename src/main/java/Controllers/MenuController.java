package Controllers;

import Navigator.Navigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
        Navigator.goTo("Board.fxml");
    }

    @FXML
    private void howToPlay() {
        Navigator.goTo("HowToPlay.fxml");
    }
}