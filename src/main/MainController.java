package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.abs;
import static java.lang.Math.pow;


public class MainController {
    public ImageView b1;
    public ImageView b2;
    public ImageView b3;
    public ImageView b4;
    public ImageView b5;
    public ImageView b6;
    public ImageView b7;
    public ImageView b8;
    public ImageView b9;
    public ImageView b10;
    public ImageView b11;
    public ImageView b12;
    public ImageView w1;
    public ImageView w2;
    public ImageView w3;
    public ImageView w4;
    public ImageView w5;
    public ImageView w6;
    public ImageView w7;
    public ImageView w8;
    public ImageView w9;
    public ImageView w10;
    public ImageView w11;
    public ImageView w12;

    public ImageView turnImage;
    public Text txtScore;
    private int whiteScore = 0;
    private int blackScore = 0;

    public Pane gamePanel;

    private int count = 0;
    private static boolean againAttack = false;
    private static int oldPlaceX;
    private static int oldPlaceY;
    private static boolean haveAttack;
    private static boolean haveCommonMoves;
    private static Integer player = 1;
    private static int[][] board = new int[8][8]; //[X][Y]
    private static int[][] boardCopy = new int[8][8]; //[X][Y]
    private Group groupHighlight = new Group();
    private final int HEIGHT = 8;
    private final int WIDTH = 8;
    private static List<Pair<ImageView, Pair<Integer, Integer>>> link;
    private Image contour = new Image("main/fxml/Contour.png");
    private Image hatchCell = new Image("main/fxml/Hatching.png");

    private void setLink(List<Pair<ImageView, Pair<Integer, Integer>>> link) {
        link.add(new Pair<>(b1, new Pair<>(1, 0)));
        link.add(new Pair<>(b2, new Pair<>(3, 0)));
        link.add(new Pair<>(b3, new Pair<>(5, 0)));
        link.add(new Pair<>(b4, new Pair<>(7, 0)));
        link.add(new Pair<>(b5, new Pair<>(0, 1)));
        link.add(new Pair<>(b6, new Pair<>(2, 1)));
        link.add(new Pair<>(b7, new Pair<>(4, 1)));
        link.add(new Pair<>(b8, new Pair<>(6, 1)));
        link.add(new Pair<>(b9, new Pair<>(1, 2)));
        link.add(new Pair<>(b10, new Pair<>(3, 2)));
        link.add(new Pair<>(b11, new Pair<>(5, 2)));
        link.add(new Pair<>(b12, new Pair<>(7, 2)));
        link.add(new Pair<>(w1, new Pair<>(0, 5)));
        link.add(new Pair<>(w2, new Pair<>(2, 5)));
        link.add(new Pair<>(w3, new Pair<>(4, 5)));
        link.add(new Pair<>(w4, new Pair<>(6, 5)));
        link.add(new Pair<>(w5, new Pair<>(1, 6)));
        link.add(new Pair<>(w6, new Pair<>(3, 6)));
        link.add(new Pair<>(w7, new Pair<>(5, 6)));
        link.add(new Pair<>(w8, new Pair<>(7, 6)));
        link.add(new Pair<>(w9, new Pair<>(0, 7)));
        link.add(new Pair<>(w10, new Pair<>(2, 7)));
        link.add(new Pair<>(w11, new Pair<>(4, 7)));
        link.add(new Pair<>(w12, new Pair<>(6, 7)));
        MainController.link = link;
    }

    private void setBoard(int[][] board) {
        for (int x=0; x<8; x++) {
            for (int y=0; y<3; y++) {
                if ((x+y)%2==1) board[x][y]=2;
                else board[x][y]=0;
            }
        }
        for (int x=0; x<8; x++) {
            for (int y=5; y<8; y++) {
                if ((x+y)%2==1) board[x][y]=1;
                else board[x][y]=0;
            }
        }
        MainController.board = board;
    }

    private int[][] cloneBoard(int [][] mainBoard) {
        int[][] boardCopy = new int[8][8];
        for (int i=0; i<8; i++) {
            System.arraycopy(mainBoard[i], 0, boardCopy[i], 0, 8);
        }
        return boardCopy;
    }

    private void winWhite(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Конец игры");
        alert.setHeaderText("Исход игры: победа БЕЛЫХ!\nИграть снова?");
        ButtonType play = new ButtonType("Играть");
        ButtonType exit = new ButtonType("Выход");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(play, exit);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == play) btnRestart(actionEvent);
        else System.exit(100000);
    }

    private void winBlack(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Конец игры");
        alert.setHeaderText("Исход игры: победа ЧЕРНЫХ!\nИграть снова?");
        ButtonType play = new ButtonType("Играть");
        ButtonType exit = new ButtonType("Выход");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(play, exit);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == play) btnRestart(actionEvent);
        else System.exit(100000);
    }

    public void showDraw(ActionEvent actionEvent) {
        if (count > 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Ничья?");
            if (player == 1)
                alert.setHeaderText("Белые предложили Ничью");
            else alert.setHeaderText("Черные предложили Ничью");
            ButtonType ok = new ButtonType("Принять");
            ButtonType cancel = new ButtonType("Отклонить");
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(ok, cancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == cancel) alert.close();
            else {
                alert.close();
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Ничья");
                alert.setHeaderText("Исход игры: НИЧЬЯ\nИграть снова?");
                ButtonType play = new ButtonType("Играть");
                ButtonType exit = new ButtonType("Выход");
                alert.getButtonTypes().clear();
                alert.getButtonTypes().addAll(play, exit);
                Optional<ButtonType> result2 = alert.showAndWait();
                if (result2.get() == play) btnRestart(actionEvent);
                else System.exit(100000);
            }
        }
    }

    public void showLose(ActionEvent actionEvent) {
        if (count > 0) {
            if (player == 2)
               winWhite(actionEvent);
            else winBlack(actionEvent);
        }
    }

    public void btnRestart(ActionEvent actionEvent) {
        gamePanel.getChildren().remove(groupHighlight);
        for (Pair<ImageView, Pair<Integer,Integer>> pair: link) {
            gamePanel.getChildren().remove(pair.getKey());
        }
        setLink(new ArrayList<>());
        setBoard(new int[8][8]);
        player = 1;
        for (Pair<ImageView, Pair<Integer,Integer>> pair: link) {
            pair.getKey().relocate(pair.getValue().getKey()*80, pair.getValue().getValue()*80);
            gamePanel.getChildren().add(pair.getKey());
        }
        turnImage.setImage(new Image("main/fxml/White20.png"));
        whiteScore = 0;
        blackScore = 0;
        txtScore.setText(whiteScore + "-" + blackScore);
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        count++;
        if (count == 1){
            setBoard(new int[8][8]);
            setLink(new ArrayList<>());
        }
        int x = (int) (mouseEvent.getX()/Main.SQUARE_SIZE);
        int y = (int) (mouseEvent.getY()/Main.SQUARE_SIZE);
        highlightAndPossibleMoves(x, y);
    }

    private void highlightAndPossibleMoves(int x, int y) {
        if (!againAttack) {
            if (board[x][y] % 10 == player) {
                int[][] clone = cloneBoard(board);
                gamePanel.getChildren().remove(groupHighlight);
                groupHighlight.getChildren().clear();
                haveAttack = false;
                haveCommonMoves = false;
                clone = possibleAttack(x, y, clone);
                if (!haveAttack) {
                    groupHighlight.getChildren().clear();
                    clone = possibleCommonMoves(x, y, clone);
                }
                if (haveCommonMoves || haveAttack) {
                    gamePanel.getChildren().add(groupHighlight);
                    boardCopy = clone;
                } else {
                    ImageView viewContour = new ImageView(contour);
                    viewContour.relocate(x * Main.SQUARE_SIZE, y * Main.SQUARE_SIZE);
                    groupHighlight.getChildren().add(viewContour);
                    gamePanel.getChildren().add(groupHighlight);
                }
            }
        }
        if (boardCopy[x][y] == 3) {
            board = boardCopy;
            gamePanel.getChildren().remove(groupHighlight);
            groupHighlight.getChildren().clear();
            if (haveCommonMoves) {
                commonMoves(x, y);
                turnPlayer();
            } else {
                attackMoves(x, y);
                int[][] clone = cloneBoard(board);
                possibleAttack(x, y, clone);
                gamePanel.getChildren().remove(groupHighlight);
                if (haveAttack) {
                    againAttack = true;
                    clone = possibleAttack(x, y, clone);
                    gamePanel.getChildren().add(groupHighlight);
                    boardCopy = clone;
                } else {
                    againAttack = false;
                    turnPlayer();
                }
            }
        }
    }

    private int[][] possibleAttack(int x, int y, int[][] clone) {
        oldPlaceX = x;
        oldPlaceY = y;
        ImageView viewContour = new ImageView(contour);
        viewContour.relocate(x*Main.SQUARE_SIZE, y*Main.SQUARE_SIZE);
        groupHighlight.getChildren().add(viewContour);
        int count = 0;
        for (int i=1; i<3; i++) {
            for (int j = 1; j < 3; j++) {
                boolean flag = false;
                for (int g=1; g < 7; g++) {
                    int newX = (int) (oldPlaceX + g * pow(-1, i));
                    int newY = (int) (oldPlaceY + g * pow(-1, j));
                    int newNextX = (int) (oldPlaceX + (g + 1) * pow(-1, i));
                    int newNextY = (int) (oldPlaceY + (g + 1) * pow(-1, j));
                    if (isOnBoard(newX, newY)) {
                        if (clone[newX][newY] % 10 == player || flag && clone[newX][newY] != 0)
                            break;
                        if (flag) {
                            count++;
                            haveAttack = true;
                            clone[newX][newY] = 3;
                            ImageView viewHatching = new ImageView(hatchCell);
                            viewHatching.relocate(newX * Main.SQUARE_SIZE, newY * Main.SQUARE_SIZE);
                            groupHighlight.getChildren().add(viewHatching);
                        }
                        if (clone[newX][newY] % 10 == getOpponent(player)) {
                            if (isOnBoard(newNextX, newNextY) && clone[newNextX][newNextY] == 0)
                            flag = true;
                            else break;
                        }
                    } else break;
                    if (clone[oldPlaceX][oldPlaceY] / 10 == 0 && g == 2) break;
                }
            }
        }
        if (count == 0) haveAttack = false;
        return clone;
    }

    private int[][] possibleCommonMoves(int x, int y, int[][] clone) {
        oldPlaceX = x;
        oldPlaceY = y;
        haveCommonMoves = false;
        ImageView viewContour = new ImageView(contour);
        viewContour.relocate(x*Main.SQUARE_SIZE, y*Main.SQUARE_SIZE);
        groupHighlight.getChildren().add(viewContour);
        for (int i=1; i<3; i++) {
            if (clone[oldPlaceX][oldPlaceY] / 10 == 0){
                int newX = (int) (oldPlaceX + pow(-1, i));
                int newY = (int) (oldPlaceY + pow(-1, player));
                if (isOnBoard(newX, newY) && clone[newX][newY] == 0) {
                    haveCommonMoves = true;
                    clone[newX][newY] = 3;
                    ImageView viewHatching = new ImageView(hatchCell);
                    viewHatching.relocate(newX * Main.SQUARE_SIZE, newY * Main.SQUARE_SIZE);
                    groupHighlight.getChildren().add(viewHatching);
                }
            } else {
                for (int j=1; j<3; j++) {
                    for (int g = 1; g < 8; g++) {
                        int newX = (int) (oldPlaceX + g * pow(-1, i));
                        int newY = (int) (oldPlaceY + g * pow(-1, j));
                        if (isOnBoard(newX, newY) && clone[newX][newY] == 0) {
                            haveCommonMoves = true;
                            clone[newX][newY] = 3;
                            ImageView viewHatching = new ImageView(hatchCell);
                            viewHatching.relocate(newX * Main.SQUARE_SIZE, newY * Main.SQUARE_SIZE);
                            groupHighlight.getChildren().add(viewHatching);
                        } else break;
                        if (clone[oldPlaceX][oldPlaceY] / 10 == 0) break;
                    }
                }
            }
        }
        return clone;
    }

    private void commonMoves(int newX, int newY) {
        board[newX][newY] = board[oldPlaceX][oldPlaceY];
        board[oldPlaceX][oldPlaceY] = 0;
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                if (board[i][j] == 3) {
                    board[i][j] = 0;
                }
            }
        }
        for (Pair<ImageView, Pair<Integer, Integer>> element: link) {
            if (oldPlaceX == element.getValue().getKey() &&
                    oldPlaceY == element.getValue().getValue()) {
                gamePanel.getChildren().remove(element.getKey());
                if ((newY == 0 || newY == 7) && board[newX][newY] / 10 == 0) {
                    link.remove(element);
                    Image imageQueen;
                    ImageView viewQueen;
                    if (newY == 0) {
                        board[newX][newY] = 11;
                        imageQueen = new Image("main/fxml/WhiteQueen80.png");
                    } else {
                        board[newX][newY] = 22;
                        imageQueen = new Image("main/fxml/BlackQueen80.png");
                    }
                    viewQueen = new ImageView(imageQueen);
                    viewQueen.relocate(newX * Main.SQUARE_SIZE, newY * Main.SQUARE_SIZE);
                    gamePanel.getChildren().add(viewQueen);
                    link.add(new Pair<>(viewQueen, new Pair<>(newX, newY)));
                } else {
                    element.getKey().relocate(newX * Main.SQUARE_SIZE, newY * Main.SQUARE_SIZE);
                    gamePanel.getChildren().add(element.getKey());
                    link.remove(element);
                    link.add(new Pair<>(element.getKey(), new Pair<>(newX, newY)));
                }
                break;
            }
        }
    }

    private void attackMoves(int newX, int newY) {
        board[newX][newY] = board[oldPlaceX][oldPlaceY];
        board[oldPlaceX][oldPlaceY] = 0;
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                if (board[i][j] == 3) {
                    board[i][j] = 0;
                }
            }
        }
        for (Pair<ImageView, Pair<Integer, Integer>> element: link) {
            if (oldPlaceX == element.getValue().getKey() &&
                    oldPlaceY == element.getValue().getValue()) {
                gamePanel.getChildren().remove(element.getKey());
                if ((newY == 0 || newY == 7) && board[newX][newY] / 10 == 0) {
                    link.remove(element);
                    Image imageQueen;
                    ImageView viewQueen;
                    if (newY == 0) {
                        board[newX][newY] = 11;
                        imageQueen = new Image("main/fxml/WhiteQueen80.png");
                    } else {
                        board[newX][newY] = 22;
                        imageQueen = new Image("main/fxml/BlackQueen80.png");
                    }
                    viewQueen = new ImageView(imageQueen);
                    viewQueen.relocate(newX * Main.SQUARE_SIZE, newY * Main.SQUARE_SIZE);
                    gamePanel.getChildren().add(viewQueen);
                    link.add(new Pair<>(viewQueen, new Pair<>(newX, newY)));
                } else {
                    element.getKey().relocate(newX * Main.SQUARE_SIZE, newY * Main.SQUARE_SIZE);
                    gamePanel.getChildren().add(element.getKey());
                    link.remove(element);
                    link.add(new Pair<>(element.getKey(), new Pair<>(newX, newY)));
                }
                break;
            }
        }
        int directionX = (newX - oldPlaceX) / abs(newX - oldPlaceX);
        int directionY = (newY - oldPlaceY) / abs(newY - oldPlaceY);
        for (;;) {
            if (board[oldPlaceX + directionX][oldPlaceY + directionY] % 10 == getOpponent(player)){
                for (Pair<ImageView, Pair<Integer, Integer>> element: link){
                    if (oldPlaceX + directionX == element.getValue().getKey() &&
                            oldPlaceY + directionY == element.getValue().getValue()) {
                        board[oldPlaceX + directionX][oldPlaceY + directionY] = 0;
                        gamePanel.getChildren().remove(element.getKey());
                        link.remove(element);
                        break;
                    }
                }
                break;
            }
            directionX += directionX / abs(directionX);
            directionY += directionY/ abs(directionY);
        }
        if (player == 1) {
            blackScore++;
            txtScore.setText(whiteScore + "-" + blackScore);
        } else {
            whiteScore++;
            txtScore.setText(whiteScore + "-" + blackScore);
        }
        if (blackScore == 12) winWhite(new ActionEvent());
        else if (whiteScore == 12) winBlack(new ActionEvent());
    }

    private void turnPlayer() {
        if (player == 1) {
            player = 2;
            turnImage.setImage(new Image("main/fxml/Black20.png"));
        }
        else {
            player = 1;
            turnImage.setImage(new Image("main/fxml/White20.png"));
        }
    }

    private int getOpponent(int player) {
        if (player==1) return 2;
        else return 1;
    }

    private boolean isOnBoard(int x, int y) {
        return (x>=0 && x<HEIGHT && y>=0  && y<WIDTH);
    }
}
