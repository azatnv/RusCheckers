package main;

import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Pair;
import java.util.ArrayList;
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
    private int oldPlaceX;
    private int oldPlaceY;
    private boolean haveOneAttack = false;
    private boolean againAttack = false;
    private boolean haveAttack;
    private boolean haveCommonMoves;
    private Integer player = 1;
    private Enum[][] board = new Enum[8][8]; //[X][Y]
    private Enum[][] boardCopy = new Enum[8][8]; //[X][Y]
    private Group groupHighlight = new Group();
    private final double SQUARE_SIZE = 80.0;
    private List<Pair<ImageView, Pair<Integer, Integer>>> link;
    private Image contour = new Image("main/fxml/Contour.png");
    private Image hatchCell = new Image("main/fxml/Hatching.png");

    private void setLink(List<Pair<ImageView, Pair<Integer, Integer>>> list) {
        list.add(new Pair<>(b1, new Pair<>(1, 0)));
        list.add(new Pair<>(b2, new Pair<>(3, 0)));
        list.add(new Pair<>(b3, new Pair<>(5, 0)));
        list.add(new Pair<>(b4, new Pair<>(7, 0)));
        list.add(new Pair<>(b5, new Pair<>(0, 1)));
        list.add(new Pair<>(b6, new Pair<>(2, 1)));
        list.add(new Pair<>(b7, new Pair<>(4, 1)));
        list.add(new Pair<>(b8, new Pair<>(6, 1)));
        list.add(new Pair<>(b9, new Pair<>(1, 2)));
        list.add(new Pair<>(b10, new Pair<>(3, 2)));
        list.add(new Pair<>(b11, new Pair<>(5, 2)));
        list.add(new Pair<>(b12, new Pair<>(7, 2)));
        list.add(new Pair<>(w1, new Pair<>(0, 5)));
        list.add(new Pair<>(w2, new Pair<>(2, 5)));
        list.add(new Pair<>(w3, new Pair<>(4, 5)));
        list.add(new Pair<>(w4, new Pair<>(6, 5)));
        list.add(new Pair<>(w5, new Pair<>(1, 6)));
        list.add(new Pair<>(w6, new Pair<>(3, 6)));
        list.add(new Pair<>(w7, new Pair<>(5, 6)));
        list.add(new Pair<>(w8, new Pair<>(7, 6)));
        list.add(new Pair<>(w9, new Pair<>(0, 7)));
        list.add(new Pair<>(w10, new Pair<>(2, 7)));
        list.add(new Pair<>(w11, new Pair<>(4, 7)));
        list.add(new Pair<>(w12, new Pair<>(6, 7)));
        link = list;
    }

    private void setBoard(Enum[][] b) {
        for (int x=0; x<8; x++) {
            for (int y=0; y<3; y++) {
                if ((x+y)%2==1) b[x][y] = Cells.BLACK;
                else b[x][y] = Cells.EMPTY;
            }
            for (int y=3; y<5; y++) {
                b[x][y] = Cells.EMPTY;
            }
            for (int y=5; y<8; y++) {
                if ((x+y)%2==1) b[x][y] = Cells.WHITE;
                else b[x][y] = Cells.EMPTY;
            }
        }
        board = b;
    }

    private Enum[][] cloneBoard(Enum[][] mainBoard) {
        Enum[][] boardCopy = new Enum[8][8];
        for (int i=0; i<8; i++) {
            System.arraycopy(mainBoard[i], 0, boardCopy[i], 0, 8);
        }
        return boardCopy;
    }

    private void winWhite() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Конец игры");
        alert.setHeaderText("Исход игры: победа БЕЛЫХ!\nИграть снова?");
        ButtonType play = new ButtonType("Играть");
        ButtonType exit = new ButtonType("Выход");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(play, exit);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == play) btnRestart();
        else System.exit(100000);
    }

    private void winBlack() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Конец игры");
        alert.setHeaderText("Исход игры: победа ЧЕРНЫХ!\nИграть снова?");
        ButtonType play = new ButtonType("Играть");
        ButtonType exit = new ButtonType("Выход");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(play, exit);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == play) btnRestart();
        else System.exit(100000);
    }

    public void showDraw() {
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
                if (result2.get() == play) btnRestart();
                else System.exit(100000);
            }
        }
    }

    public void showLose() {
        if (count > 0) {
            if (player == 2)
               winWhite();
            else winBlack();
        }
    }

    public void btnRestart() {
        gamePanel.getChildren().remove(groupHighlight);
        for (Pair<ImageView, Pair<Integer,Integer>> pair: link) {
            gamePanel.getChildren().remove(pair.getKey());
        }
        setLink(new ArrayList<>());
        setBoard(new Enum[8][8]);
        player = 1;
        for (Pair<ImageView, Pair<Integer,Integer>> pair: link) {
            pair.getKey().relocate(pair.getValue().getKey()*SQUARE_SIZE, pair.getValue().getValue()*SQUARE_SIZE);
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
            setBoard(new Enum[8][8]);
            setLink(new ArrayList<>());
        }
        int x = (int) (mouseEvent.getX()/SQUARE_SIZE);
        int y = (int) (mouseEvent.getY()/SQUARE_SIZE);
        highlightAndPossibleMoves(x, y);
    }

    private void highlightAndPossibleMoves(int x, int y) {
        if (!againAttack) {
            if (getPlayerByEnum(board[x][y]) == player && haveOneAttack) {
                Enum[][] clone = cloneBoard(board);
                gamePanel.getChildren().remove(groupHighlight);
                groupHighlight.getChildren().clear();
                haveAttack = false;
                haveCommonMoves = false;
                clone = possibleAttack(x, y, clone);
                if (haveAttack) {
                    gamePanel.getChildren().add(groupHighlight);
                    boardCopy = clone;
                }
            }
            if (getPlayerByEnum(board[x][y]) == player && !haveOneAttack) {
                Enum[][] clone = cloneBoard(board);
                gamePanel.getChildren().remove(groupHighlight);
                groupHighlight.getChildren().clear();
                haveAttack = false;
                haveCommonMoves = false;
                clone = possibleCommonMoves(x, y, clone);
                if (haveCommonMoves) {
                    gamePanel.getChildren().add(groupHighlight);
                    boardCopy = clone;
                } else {
                    ImageView viewContour = new ImageView(contour);
                    viewContour.relocate(x * SQUARE_SIZE, y * SQUARE_SIZE);
                    groupHighlight.getChildren().add(viewContour);
                    gamePanel.getChildren().add(groupHighlight);
                }
            }
        }
        if (boardCopy[x][y] == Cells.PLACE_MOVE) {
            board = boardCopy;
            gamePanel.getChildren().remove(groupHighlight);
            groupHighlight.getChildren().clear();
            if (haveCommonMoves) {
                commonMoves(x, y);
                turnPlayer();
                haveOneAttack = canMakeOneAttack();
            } else {
                attackMoves(x, y);
                Enum[][] clone = cloneBoard(board);
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
                    haveOneAttack = canMakeOneAttack();
                }
            }
        }
    }

    private boolean canMakeOneAttack() {
        Enum[][] clone = cloneBoard(board);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (getPlayerByEnum(clone[i][j]) == player) {
                    possibleAttack(i, j, clone);
                    groupHighlight.getChildren().clear();
                }
                if (haveAttack) {
                    haveAttack = false;
                    return true;
                }
            }
        }
        return false;
    }

    private Enum[][] possibleAttack(int x, int y, Enum[][] clone) {
        oldPlaceX = x;
        oldPlaceY = y;
        ImageView viewContour = new ImageView(contour);
        viewContour.relocate(x*SQUARE_SIZE, y*SQUARE_SIZE);
        groupHighlight.getChildren().add(viewContour);
        int count = 0;
        for (int i=1; i<3; i++) {
            for (int j = 1; j < 3; j++) {
                boolean flag = false;
                for (int g = 1; g < 7; g++) {
                    int newX = (int) (oldPlaceX + g * pow(-1, i));
                    int newY = (int) (oldPlaceY + g * pow(-1, j));
                    int newNextX = (int) (oldPlaceX + (g + 1) * pow(-1, i));
                    int newNextY = (int) (oldPlaceY + (g + 1) * pow(-1, j));
                    if (isOnBoard(newX, newY)) {
                        if (getPlayerByEnum(clone[newX][newY]) == player || flag && clone[newX][newY] != Cells.EMPTY)
                            break;
                        if (flag) {
                            count++;
                            haveAttack = true;
                            clone[newX][newY] = Cells.PLACE_MOVE;
                            ImageView viewHatching = new ImageView(hatchCell);
                            viewHatching.relocate(newX * SQUARE_SIZE, newY * SQUARE_SIZE);
                            groupHighlight.getChildren().add(viewHatching);
                        }
                        if (getPlayerByEnum(clone[newX][newY]) == getOpponent(player)) {
                            if (isOnBoard(newNextX, newNextY) && clone[newNextX][newNextY] == Cells.EMPTY) {
                                flag = true;
                            }
                            else break;
                        }
                    } else break;
                    if ((clone[oldPlaceX][oldPlaceY] == Cells.WHITE ||
                            clone[oldPlaceX][oldPlaceY] == Cells.BLACK) && g == 2) break;
                }
            }
        }
        if (count == 0) haveAttack = false;
        return clone;
    }

    private Enum[][] possibleCommonMoves(int x, int y, Enum[][] clone) {
        oldPlaceX = x;
        oldPlaceY = y;
        haveCommonMoves = false;
        ImageView viewContour = new ImageView(contour);
        viewContour.relocate(x*SQUARE_SIZE, y*SQUARE_SIZE);
        groupHighlight.getChildren().add(viewContour);
        for (int i=1; i<3; i++) {
            if (clone[oldPlaceX][oldPlaceY] == Cells.WHITE ||
                    clone[oldPlaceX][oldPlaceY] == Cells.BLACK) {
                int newX = (int) (oldPlaceX + pow(-1, i));
                int newY = (int) (oldPlaceY + pow(-1, player));
                if (isOnBoard(newX, newY) && clone[newX][newY] == Cells.EMPTY) {
                    haveCommonMoves = true;
                    clone[newX][newY] = Cells.PLACE_MOVE;
                    ImageView viewHatching = new ImageView(hatchCell);
                    viewHatching.relocate(newX * SQUARE_SIZE, newY * SQUARE_SIZE);
                    groupHighlight.getChildren().add(viewHatching);
                }
            } else {
                for (int j=1; j<3; j++) {
                    for (int g = 1; g < 8; g++) {
                        int newX = (int) (oldPlaceX + g * pow(-1, i));
                        int newY = (int) (oldPlaceY + g * pow(-1, j));
                        if (isOnBoard(newX, newY) && clone[newX][newY] == Cells.EMPTY) {
                            haveCommonMoves = true;
                            clone[newX][newY] = Cells.PLACE_MOVE;
                            ImageView viewHatching = new ImageView(hatchCell);
                            viewHatching.relocate(newX * SQUARE_SIZE, newY * SQUARE_SIZE);
                            groupHighlight.getChildren().add(viewHatching);
                        } else break;
                        if (clone[oldPlaceX][oldPlaceY] == Cells.WHITE ||
                                clone[oldPlaceX][oldPlaceY] == Cells.BLACK) break;
                    }
                }
            }
        }
        return clone;
    }

    private void commonMoves(int newX, int newY) {
        board[newX][newY] = board[oldPlaceX][oldPlaceY];
        board[oldPlaceX][oldPlaceY] = Cells.EMPTY;
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                if (board[i][j] == Cells.PLACE_MOVE) {
                    board[i][j] = Cells.EMPTY;
                }
            }
        }
        for (Pair<ImageView, Pair<Integer, Integer>> element: link) {
            if (oldPlaceX == element.getValue().getKey() &&
                    oldPlaceY == element.getValue().getValue()) {
                gamePanel.getChildren().remove(element.getKey());
                if ((newY == 0 || newY == 7) && (board[newX][newY] == Cells.WHITE ||
                        board[newX][newY] == Cells.BLACK)) {
                    link.remove(element);
                    Image imageQueen;
                    ImageView viewQueen;
                    if (newY == 0) {

                        board[newX][newY] = Cells.WHITE_QUEEN;
                        imageQueen = new Image("main/fxml/WhiteQueen80.png");
                    } else {
                        board[newX][newY] = Cells.BLACK_QUEEN;
                        imageQueen = new Image("main/fxml/BlackQueen80.png");
                    }
                    viewQueen = new ImageView(imageQueen);
                    viewQueen.relocate(newX * SQUARE_SIZE, newY * SQUARE_SIZE);
                    gamePanel.getChildren().add(viewQueen);
                    link.add(new Pair<>(viewQueen, new Pair<>(newX, newY)));
                } else {
                    element.getKey().relocate(newX * SQUARE_SIZE, newY * SQUARE_SIZE);
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
        board[oldPlaceX][oldPlaceY] = Cells.EMPTY;
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                if (board[i][j] == Cells.PLACE_MOVE) {
                    board[i][j] = Cells.EMPTY;
                }
            }
        }
        for (Pair<ImageView, Pair<Integer, Integer>> element: link) {
            if (oldPlaceX == element.getValue().getKey() &&
                    oldPlaceY == element.getValue().getValue()) {
                gamePanel.getChildren().remove(element.getKey());
                if ((newY == 0 || newY == 7) && (board[newX][newY] == Cells.WHITE ||
                        board[newX][newY] == Cells.BLACK)) {
                    link.remove(element);
                    Image imageQueen;
                    ImageView viewQueen;
                    if (newY == 0) {
                        board[newX][newY] = Cells.WHITE_QUEEN;
                        imageQueen = new Image("main/fxml/WhiteQueen80.png");
                    } else {
                        board[newX][newY] = Cells.BLACK_QUEEN;
                        imageQueen = new Image("main/fxml/BlackQueen80.png");
                    }
                    viewQueen = new ImageView(imageQueen);
                    viewQueen.relocate(newX * SQUARE_SIZE, newY * SQUARE_SIZE);
                    gamePanel.getChildren().add(viewQueen);
                    link.add(new Pair<>(viewQueen, new Pair<>(newX, newY)));
                } else {
                    element.getKey().relocate(newX * SQUARE_SIZE, newY * SQUARE_SIZE);
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
            if (getPlayerByEnum(board[oldPlaceX + directionX][oldPlaceY + directionY]) == getOpponent(player)){
                for (Pair<ImageView, Pair<Integer, Integer>> element: link){
                    if (oldPlaceX + directionX == element.getValue().getKey() &&
                            oldPlaceY + directionY == element.getValue().getValue()) {
                        board[oldPlaceX + directionX][oldPlaceY + directionY] = Cells.EMPTY;
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
        if (blackScore == 12) {
            winWhite();
            turnPlayer();
        }
        else if (whiteScore == 12) {
            winBlack();
            turnPlayer();
        }
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

    private int getPlayerByEnum(Enum player) {
        if (player == Cells.WHITE || player == Cells.WHITE_QUEEN) return 1;
        else if (player == Cells.BLACK || player == Cells.BLACK_QUEEN) return 2;
        else return 0;
    }

    private boolean isOnBoard(int x, int y) {
        int HEIGHT = 8;
        int WIDTH = 8;
        return (x>=0 && x< HEIGHT && y>=0  && y< WIDTH);
    }
}
