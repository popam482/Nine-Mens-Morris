package Controllers;

import Navigator.Navigator;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class BoardController {

    @FXML
    private void onClick(MouseEvent e){
        System.out.println("click");
    }

    @FXML
    private void backToMainMenu(){
        Navigator.goTo("Menu.fxml");
    }

}
