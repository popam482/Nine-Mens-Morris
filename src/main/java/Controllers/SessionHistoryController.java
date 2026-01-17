package Controllers;

import Database.GameDatabase;
import Database.GameRecord;
import Navigator.Navigator;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SessionHistoryController {

    @FXML private TableView<GameRecord> historyTable;
    @FXML private TableColumn<GameRecord, String> player1Column;
    @FXML private TableColumn<GameRecord, String> player2Column;
    @FXML private TableColumn<GameRecord, String> winnerColumn;
    @FXML private TableColumn<GameRecord, String> durationColumn;
    @FXML private TableColumn<GameRecord, String> typeColumn;
    @FXML private TableColumn<GameRecord, String> dateColumn;


    GameDatabase database;

    @FXML
    private void initialize() {

        try {
            database = new GameDatabase();
            System.out.println("SessionHistory: Database initialized");

            player1Column.setCellValueFactory(new PropertyValueFactory<>("player1"));
            player2Column.setCellValueFactory(new PropertyValueFactory<>("player2"));
            winnerColumn.setCellValueFactory(new PropertyValueFactory<>("winner"));
            durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("gameType"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

            loadHistory();

        } catch (Exception e) {
            System.out.println("Failed to initialize SessionHistoryController:");
            e.printStackTrace();
        }
    }

    private void loadHistory() {
        historyTable.setItems(database.getAllGames());
    }

    @FXML
    private void backToMenu() {
        Navigator.goTo("Menu.fxml");
    }
}