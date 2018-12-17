package main;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @FXML
    public Pane gamePanel;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Russian Checkers");
        Parent root = FXMLLoader.load(getClass().getResource("/main/fxml/Board.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
