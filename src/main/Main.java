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

    private static Group newCheckers = new Group();
    final static double SQUARE_SIZE = 80.0;
    private List<Pair<Integer,Integer>> directions;


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
