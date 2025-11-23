package Controllers;

//import Model.LocalPlayer;
//import Model.Pieces;
//import Model.Player;
import GameLogic.FullBoard;
import Navigator.Navigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class MenuController {

    @FXML
    Button startLocalButton, startOnlineButton, howToPlayButton;

    @FXML
    private void startLocalSession()
    {
        TextInputDialog dialog=new TextInputDialog();
        dialog.setTitle("Player 1 (white) name:");
        dialog.setHeaderText("Enter player 1 (white) name: ");
        dialog.setContentText("Name:");
        Optional<String> playerName1=dialog.showAndWait();
        if(playerName1.isEmpty() || playerName1.get().trim().isEmpty()){
            return;
        }

        dialog=new TextInputDialog();
        dialog.setTitle("Player 2 (black) name:");
        dialog.setHeaderText("Enter player 2 (black) name: ");
        dialog.setContentText("Name:");
        Optional<String> playerName2=dialog.showAndWait();
        if(playerName2.isEmpty() || playerName2.get().trim().isEmpty()){
            return;
        }

        //LocalPlayer p1 = new LocalPlayer(playerName1.get().trim(), Pieces.WHITE);
        //LocalPlayer p2 = new LocalPlayer(playerName2.get().trim(), Pieces.BLACK);

        //BoardController.setPlayers(p1, p2);

        Navigator.goTo("Board.fxml");

    }

    @FXML
    private void startOnlineSession()
    {

        Navigator.goTo("Board.fxml");
    }

    @FXML
    private void howToPlay()
    {
        Navigator.goTo("HowToPlay.fxml");
    }

}