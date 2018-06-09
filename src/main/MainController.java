package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            for (int j=0; j<8; j++) {
                boardCopy[i][j] = mainBoard[i][j];
            }
        }
        return boardCopy;
    }

    public void showDraw(ActionEvent actionEvent) {

    }

    public void showLose(ActionEvent actionEvent) {

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
                for (int g=1; g < 8; g++) {
                    int newX = (int) (oldPlaceX + g * pow(-1, i));
                    int newY = (int) (oldPlaceY + g * pow(-1, j));
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
                        if (clone[newX][newY] % 10 == getOpponent(player) &&
                                isOnBoard(newX + (int) (pow(-1, i)), newY + (int) (pow(-1, j))) &&
                                clone[newX + (int) (pow(-1, i))][newY + (int) (pow(-1, j))] == 0) {
                            flag = true;
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
            for (int g=1; g < 8; g++) {
                int newX = (int) (oldPlaceX + g*pow(-1, i));
                int newY = (int) (oldPlaceY + g*pow(-1, player));
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
        return clone;
    }

    private void commonMoves(int newX, int newY) {
        board[newX][newY] = player;
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
                element.getKey().relocate(newX*Main.SQUARE_SIZE, newY*Main.SQUARE_SIZE);
                gamePanel.getChildren().add(element.getKey());
                link.remove(element);
                link.add(new Pair<>(element.getKey(), new Pair<>(newX, newY)));
                break;
            }
        }
    }

    private void attackMoves(int newX, int newY) {
        board[newX][newY] = player;
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
                element.getKey().relocate(newX*Main.SQUARE_SIZE, newY*Main.SQUARE_SIZE);
                gamePanel.getChildren().add(element.getKey());
                link.remove(element);
                link.add(new Pair<>(element.getKey(), new Pair<>(newX, newY)));
                break;
            }
        }
        int deadX = (newX + oldPlaceX)/2;
        int deadY = (newY + oldPlaceY)/2;
        board[deadX][deadY] = 0;
        for (Pair<ImageView, Pair<Integer, Integer>> element: link){
            if (deadX == element.getValue().getKey() &&
                    deadY == element.getValue().getValue()) {
                gamePanel.getChildren().remove(element.getKey());
                link.remove(element);
                break;
            }
        }

    }

    private void turnPlayer() {
        if (player == 1) player = 2;
        else player = 1;
    }

    private int getOpponent(int player) {
        if (player==1) return 2;
        else return 1;
    }

    private boolean isOnBoard(int x, int y) {
        return (x>=0 && x<HEIGHT && y>=0  && y<WIDTH);
    }
}
