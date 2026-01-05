package Controllers;

import Navigator.Navigator;
import javafx.fxml. FXML;
import javafx.scene.control.TextArea;

public class HowToPlayController {

    @FXML
    private TextArea rulesText;

    @FXML
    private void initialize() {
        if (rulesText != null) {
            rulesText.setWrapText(true);
            rulesText.setEditable(false);
        }
    }

    @FXML
    private void backToMainMenu() {
        Navigator.goTo("Menu");
    }
}