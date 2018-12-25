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

public class MainController implements Cloneable {
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
    private boolean wait = false;
    private int oldPlaceX;
    private int oldPlaceY;
    private int fromAttackBotX;
    private int fromAttackBotY;
    private boolean haveOneAttack = false;
    private boolean againAttack = false;
    private boolean haveAttack;
    private boolean haveCommonMoves;
    private Cells player = Cells.BLACK;
    private Cells[][] board = new Cells[8][8]; //[X][Y]
    private Cells[][] boardCopy = new Cells[8][8]; //[X][Y]
    private Group groupHighlight = new Group();
    private final double SQUARE_SIZE = 80.0;
    private List<Pair<ImageView, Pair<Integer, Integer>>> relations;
    private Image contour = new Image("main/fxml/Contour.png");
    private Image hatchCell = new Image("main/fxml/Hatching.png");

    private void setRelations(List<Pair<ImageView, Pair<Integer, Integer>>> list) {
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
        relations = list;
    }

    private void setBoard(Cells[][] b) {
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

//    public void showDraw() {         // Функция "Предолжить ничью" при игре с ботом не используется
//        if (count > 0) {
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Ничья?");
//            if (player == Cells.WHITE)
//                alert.setHeaderText("Белые предложили Ничью");
//            else alert.setHeaderText("Черные предложили Ничью");
//            ButtonType ok = new ButtonType("Принять");
//            ButtonType cancel = new ButtonType("Отклонить");
//            alert.getButtonTypes().clear();
//            alert.getButtonTypes().addAll(ok, cancel);
//            Optional<ButtonType> result = alert.showAndWait();
//            if (result.get() == cancel) alert.close();
//            else {
//                alert.close();
//                alert = new Alert(Alert.AlertType.CONFIRMATION);
//                alert.setTitle("Ничья");
//                alert.setHeaderText("Исход игры: НИЧЬЯ\nИграть снова?");
//                ButtonType play = new ButtonType("Играть");
//                ButtonType exit = new ButtonType("Выход");
//                alert.getButtonTypes().clear();
//                alert.getButtonTypes().addAll(play, exit);
//                Optional<ButtonType> result2 = alert.showAndWait();
//                if (result2.get() == play) btnRestart();
//                else System.exit(100000);
//            }
//        }
//    }

//    public void showLose() {
//        if (count > 0) {
//            if (player == Cells.BLACK)
//               winWhite();
//            else winBlack();
//        }
//    }

    public void btnRestart() {
        gamePanel.getChildren().remove(groupHighlight);
        groupHighlight.getChildren().clear();
        for (Pair<ImageView, Pair<Integer,Integer>> pair: relations) {
            gamePanel.getChildren().remove(pair.getKey());
        }
        setRelations(new ArrayList<>());
        setBoard(new Cells[8][8]);
        haveOneAttack = false;
        againAttack = false;
        haveAttack = false;
        haveCommonMoves = false;
        player = Cells.WHITE;
        for (Pair<ImageView, Pair<Integer,Integer>> pair: relations) {
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
        if (count == 1) {
            setBoard(new Cells[8][8]);
            setRelations(new ArrayList<>());
        }
        if (player == Cells.BLACK && !wait) {
            if (!againAttack) {
                wait = true;
                Bot blackBot = new Bot(board);
                blackBot.bestMove();
                oldPlaceX = blackBot.getMoveFromX();
                oldPlaceY = blackBot.getMoveFromY();
                int toX = blackBot.getMoveToX();
                int toY = blackBot.getMoveToY();
                if (blackBot.isAttack()) attackMove(toX, toY);
                else commonMove(toX, toY);
            } else {
                wait = true;
                Bot anotherAttackBot = new Bot(board, fromAttackBotX, fromAttackBotY);
                anotherAttackBot.bestMove();
                oldPlaceX = anotherAttackBot.getMoveFromX();
                oldPlaceY = anotherAttackBot.getMoveFromY();
                int toX = anotherAttackBot.getMoveToX();
                int toY = anotherAttackBot.getMoveToY();
                attackMove(toX, toY);
            }
        } else if (player == Cells.WHITE && !wait) {
            int x = (int) (mouseEvent.getX()/SQUARE_SIZE);
            int y = (int) (mouseEvent.getY()/SQUARE_SIZE);
            highlightAndPossibleMoves(x, y);
        }
    }

    private void highlightAndPossibleMoves(int x, int y) {
        if (!againAttack) {
            if ((board[x][y] == player || board[x][y] == queen(player)) && haveOneAttack) {
                gamePanel.getChildren().remove(groupHighlight);
                groupHighlight.getChildren().clear();
                haveAttack = false;
                haveCommonMoves = false;
                Cells[][] clone = possibleAttacks(x, y, board);

                if (haveAttack) {
                    gamePanel.getChildren().add(groupHighlight);
                    boardCopy = clone;
                } else {
                    ImageView viewContour = new ImageView(contour);
                    viewContour.relocate(x * SQUARE_SIZE, y * SQUARE_SIZE);
                    groupHighlight.getChildren().add(viewContour);
                    gamePanel.getChildren().add(groupHighlight);
                }
            }
            if ((board[x][y] == player || board[x][y] == queen(player)) && !haveOneAttack) {
                gamePanel.getChildren().remove(groupHighlight);
                groupHighlight.getChildren().clear();
                haveAttack = false;
                haveCommonMoves = false;
                boardCopy = Functions.cloneBoard(board);
                Cells[][] clone = possibleCommonMoves(x, y, board);
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
            wait = true;
            gamePanel.getChildren().remove(groupHighlight);
            groupHighlight.getChildren().clear();
            if (haveCommonMoves) commonMove(x, y);
            else {
                attackMove(x, y);
                if (againAttack) {
                    boardCopy = possibleAttacks(x, y, board);
                    gamePanel.getChildren().add(groupHighlight);
                }
            }
        }
    }

    private boolean canMakeOneAttack() {
        return Functions.canMakeOneAttack(board, player);
    }

    private Cells[][] possibleAttacks(int fromPlaceX, int fromPlaceY, Cells[][] clone) {
        haveAttack = false;
        oldPlaceX = fromPlaceX;
        oldPlaceY = fromPlaceY;
        ImageView viewContour = new ImageView(contour);
        viewContour.relocate(fromPlaceX*SQUARE_SIZE, fromPlaceY*SQUARE_SIZE);
        groupHighlight.getChildren().add(viewContour);

        Cells[][] result = Functions.possibleAttack(fromPlaceX, fromPlaceY, clone, player);
        for (int toPlaceX = 0; toPlaceX < 8; toPlaceX++) {
            for (int toPlaceY = 0; toPlaceY < 8; toPlaceY++) {
                if (result[toPlaceX][toPlaceY] == Cells.PLACE_MOVE) {
                    haveAttack = true;
                    ImageView viewHatching = new ImageView(hatchCell);
                    viewHatching.relocate(toPlaceX * SQUARE_SIZE, toPlaceY * SQUARE_SIZE);
                    groupHighlight.getChildren().add(viewHatching);
                }
            }
        }
        return result;
    }

    private Cells[][] possibleCommonMoves(int fromPlaceX, int fromPlaceY, Cells[][] clone) {
        oldPlaceX = fromPlaceX;
        oldPlaceY = fromPlaceY;
        haveCommonMoves = false;
        ImageView viewContour = new ImageView(contour);
        viewContour.relocate(fromPlaceX*SQUARE_SIZE, fromPlaceY*SQUARE_SIZE);
        groupHighlight.getChildren().add(viewContour);

        Cells[][] result = Functions.possibleCommonMoves(fromPlaceX, fromPlaceY, clone, player);
        for (int toPlaceX = 0; toPlaceX < 8; toPlaceX++) {
            for (int toPlaceY = 0; toPlaceY < 8; toPlaceY++) {
                if (result[toPlaceX][toPlaceY] == Cells.PLACE_MOVE) {
                    haveCommonMoves = true;
                    ImageView viewHatching = new ImageView(hatchCell);
                    viewHatching.relocate(toPlaceX * SQUARE_SIZE, toPlaceY * SQUARE_SIZE);
                    groupHighlight.getChildren().add(viewHatching);
                }
            }
        }
        return result;
    }

    private void commonMove(int toPlaceX, int toPlaceY) {
        for (Pair<ImageView, Pair<Integer, Integer>> element: relations) {
            if (oldPlaceX == element.getValue().getKey() &&
                    oldPlaceY == element.getValue().getValue()) {
                gamePanel.getChildren().remove(element.getKey());
                if ((toPlaceY == 0 && board[oldPlaceX][oldPlaceY] == Cells.WHITE) ||
                        (toPlaceY == 7 && board[oldPlaceX][oldPlaceY] == Cells.BLACK)) {
                    relations.remove(element);
                    Image imageQueen;
                    if (toPlaceY == 0) imageQueen = new Image("main/fxml/WhiteQueen80.png");
                    else imageQueen = new Image("main/fxml/BlackQueen80.png");
                    ImageView viewQueen = new ImageView(imageQueen);
                    viewQueen.relocate(toPlaceX * SQUARE_SIZE, toPlaceY * SQUARE_SIZE);
                    gamePanel.getChildren().add(viewQueen);
                    relations.add(new Pair<>(viewQueen, new Pair<>(toPlaceX, toPlaceY)));
                } else {
                    element.getKey().relocate(toPlaceX * SQUARE_SIZE, toPlaceY * SQUARE_SIZE);
                    gamePanel.getChildren().add(element.getKey());
                    relations.remove(element);
                    relations.add(new Pair<>(element.getKey(), new Pair<>(toPlaceX, toPlaceY)));
                }
                break;
            }
        }
        board = Functions.commonMove(oldPlaceX, oldPlaceY, toPlaceX, toPlaceY, board);

        turnPlayer();
        haveOneAttack = canMakeOneAttack();
        wait = false;
    }

    private void attackMove(int toPlaceX, int toPlaceY) {
        for (Pair<ImageView, Pair<Integer, Integer>> element: relations) {
            if (oldPlaceX == element.getValue().getKey() &&
                    oldPlaceY == element.getValue().getValue()) {
                gamePanel.getChildren().remove(element.getKey());
                if ((toPlaceY == 0 && board[oldPlaceX][oldPlaceY] == Cells.WHITE) ||
                        (toPlaceY == 7 && board[oldPlaceX][oldPlaceY] == Cells.BLACK)) {
                    relations.remove(element);
                    Image imageQueen;
                    if (toPlaceY == 0) imageQueen = new Image("main/fxml/WhiteQueen80.png");
                    else imageQueen = new Image("main/fxml/BlackQueen80.png");
                    ImageView viewQueen = new ImageView(imageQueen);
                    viewQueen.relocate(toPlaceX * SQUARE_SIZE, toPlaceY * SQUARE_SIZE);
                    gamePanel.getChildren().add(viewQueen);
                    relations.add(new Pair<>(viewQueen, new Pair<>(toPlaceX, toPlaceY)));
                } else {
                    element.getKey().relocate(toPlaceX * SQUARE_SIZE, toPlaceY * SQUARE_SIZE);
                    gamePanel.getChildren().add(element.getKey());
                    relations.remove(element);
                    relations.add(new Pair<>(element.getKey(), new Pair<>(toPlaceX, toPlaceY)));
                }
                break;
            }
        }
        int directionX = (toPlaceX - oldPlaceX) / abs(toPlaceX - oldPlaceX);
        int directionY = (toPlaceY - oldPlaceY) / abs(toPlaceY - oldPlaceY);
        for (;;) {
            if (getOpponent(board[oldPlaceX + directionX][oldPlaceY + directionY]) == player) {
                for (Pair<ImageView, Pair<Integer, Integer>> element: relations){
                    if (oldPlaceX + directionX == element.getValue().getKey() &&
                            oldPlaceY + directionY == element.getValue().getValue()) {
                        gamePanel.getChildren().remove(element.getKey());
                        relations.remove(element);
                        break;
                    }
                }
                break;
            }
            directionX += directionX / abs(directionX);
            directionY += directionY/ abs(directionY);
        }
        board = Functions.attackMove(oldPlaceX, oldPlaceY, toPlaceX, toPlaceY, board, player);

        if (player == Cells.WHITE) {
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

        Cells[][] possibleAgainAttack = Functions.possibleAttack(toPlaceX, toPlaceY, board, player);
        if (Functions.boardContainsCell(possibleAgainAttack, Cells.PLACE_MOVE)) {
            againAttack = true;
            if (player == Cells.WHITE) {
                boardCopy = possibleAgainAttack;
            }
            if (player == Cells.BLACK) {
                fromAttackBotX = toPlaceX;
                fromAttackBotY = toPlaceY;
            }
        } else {
            againAttack = false;
            turnPlayer();
            haveOneAttack = canMakeOneAttack();
        }
        wait = false;
    }

    private void turnPlayer() {
        if (player == Cells.WHITE) {
            player = Cells.BLACK;
            turnImage.setImage(new Image("main/fxml/Black20.png"));
        }
        else {
            player = Cells.WHITE;
            turnImage.setImage(new Image("main/fxml/White20.png"));
        }
    }

    private Cells queen(Cells player) {
        if (player == Cells.WHITE)
            return Cells.WHITE_QUEEN;
        else if (player == Cells.BLACK)
            return Cells.BLACK_QUEEN;
        else return null;
    }

    private Cells getOpponent(Cells player) {
        if (player == Cells.WHITE || player == Cells.WHITE_QUEEN) return Cells.BLACK;
        else if (player == Cells.BLACK || player == Cells.BLACK_QUEEN) return Cells.WHITE;
        else return null;
    }

    private static String toString(Cells[][] board) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0)
                out.append("      ");
            if (i % 2 == 0) {
                out.append(board[1][i]);
                out.append("      ");
                out.append(board[3][i]);
                out.append("      ");
                out.append(board[5][i]);
                out.append("      ");
                out.append(board[7][i]);
            } else {
                out.append(board[0][i]);
                out.append("      ");
                out.append(board[2][i]);
                out.append("      ");
                out.append(board[4][i]);
                out.append("      ");
                out.append(board[6][i]);
            }
            if (i % 2 == 1)
                out.append("      ");
            out.append("\n");
        }
        return out.toString();
    }

}
