package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;


public class Main extends Application {

    final static double SQUARE_SIZE = 80.0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Russian Checkers");
        Parent root = FXMLLoader.load(getClass().getResource("/main/fxml/Board.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
