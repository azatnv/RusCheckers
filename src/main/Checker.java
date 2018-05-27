package main;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Checker extends ImageView {
    private Image whiteChecker = new Image("main/fxml/White80.png");
    private Image blackChecker = new Image("main/fxml/Black80.png");
    private Image hatchCell = new Image("main/fxml/White80.png");

    Checker(int x, int y, int type) {
        if (type==1) placeChecker(x, y, whiteChecker);
        else if (type==2) placeChecker(x, y, blackChecker);
        else placeChecker(x, y, hatchCell);
    }

    private void placeChecker(int x, int y, Image image) {
        System.out.println("ХУЙ");
        setImage(image);
        relocate(x * Main.SQUARE_SIZE, y * Main.SQUARE_SIZE);
    }


   // @Test
   // void test() {
   //     assertEquals(new ImageView(new Image("main/fxml/White80.png")), new Checker(1, 1, 1));
   // }
}
