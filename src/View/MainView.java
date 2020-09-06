package View;

import Controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainView extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("layout.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Ray casting");
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(keyEvent -> {
            MainController mainController = loader.getController();
            mainController.anchor_pane_key_pressed(keyEvent);
        });
        scene.setOnKeyReleased(keyEvent -> {
            MainController mainController = loader.getController();
            mainController.anchor_pane_key_pressed(keyEvent);
        });
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getRoot().requestFocus();
    }
}
