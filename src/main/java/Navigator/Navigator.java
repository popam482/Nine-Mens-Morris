package Navigator;

import Navigator.Navigator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Navigator {

    private static Stage stage;

    public static <T> T goTo(String fxmlName) {
        try {
            String path;
            if (fxmlName == null || fxmlName.isBlank()) {
                throw new IllegalArgumentException(fxmlName + " cannot be null/empty");
            }
            if (fxmlName.startsWith("/")) {
                path = fxmlName;
            } else {
                String base = "/";
                path = base + (fxmlName.endsWith(".fxml") ? fxmlName : fxmlName + ".fxml");
            }

            URL fxmlUrl = Navigator.class.getResource(path);
            if (fxmlUrl == null) {
                throw new RuntimeException("FXML not found on classpath: " + path +
                        "  (ensure file is in src/main/resources/ or call with absolute path)");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Scene scene = stage.getScene();
            if (scene != null) {
                scene.setRoot(root);
            } else {
                stage.setScene(new Scene(root));
            }
            stage.centerOnScreen();
            T controller = (T) loader.getController();
            return controller;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML: " + fxmlName, e);
        }
    }

    public static void init(Stage primaryStage) {
        stage=primaryStage;
    }
}
