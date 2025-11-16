package Controllers;

import Navigator.Navigator;
import javafx.fxml.FXML;

public class HowToPlayController {

    @FXML
    private void backToMainMenu()
    {
        Navigator.goTo("Menu.fxml");
    }


}
