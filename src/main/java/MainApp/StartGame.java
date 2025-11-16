package MainApp;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import Navigator.Navigator;

public class StartGame extends Application {

    @Override
    public void start(Stage stage){

        try{

            Navigator.init(stage);

            Navigator.goTo("Menu.fxml");

            Image logo=new Image(getClass().getResourceAsStream("/logo.png"));
            stage.getIcons().add(logo);

            stage.setResizable(false);
            stage.setTitle("Nine Men's Morris");
            stage.centerOnScreen();
            stage.show();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String [] args){

        launch(args);

    }


}
