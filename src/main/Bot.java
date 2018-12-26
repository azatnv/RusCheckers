package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.max;
import static java.lang.Math.pow;

class Bot {

    private static final int maxDepth = 6;

    private int depth;                                  //Текущая глубина этого узла
    private ArrayList<Bot> children;                    //Дочерние узлы этого узла
    private Cells[][] move;                             //Ход, который привел к currentBoard
    private Cells[][] currentBoard = new Cells[8][8];
    private Cells turn;                                 //Ходит игрок с доской currentBoard
    private int value;                                  //Оценка позиции

    private int fromAttackX;           //Используется в констсрукторе, значащий повторную атаку для COMPUTER
    private int fromAttackY;

    private boolean isAttack = false;  //move - это атака?
    private int fromID;                //Координаты клеток, описывающие move
    private int toID;

    Bot(Cells[][] board, Cells turn) {
        this.depth = 0;
        this.move = null;
        this.turn = turn;
        this.currentBoard = Functions.cloneBoard(board);
        this.children = new ArrayList<>();
    }

    Bot(Cells[][] board, int fromAttackX, int fromAttackY, Cells turn) {
        this.depth = 0;
        this.move = null;
        this.turn = turn;
        this.currentBoard = Functions.possibleAttack(fromAttackX, fromAttackY, board, turn);
        this.children = new ArrayList<>();
        this.fromAttackX = fromAttackX;
        this.fromAttackY = fromAttackY;
    }

    private Bot(Cells[][] changedBoard, Cells[][] move, Cells turn, int depth) {
        this.depth = depth;
        this.move = move;
        this.turn = turn;
        this.currentBoard = Functions.cloneBoard(changedBoard);
        this.children = new ArrayList<>();
        this.isAttack = check(this.move, this.currentBoard);
        this.fromID = findCellBot(move, Cells.BOT_FROM);
        this.toID = findCellBot(move, Cells.BOT_TO);
    }

    boolean isAttack() {
        return this.isAttack;
    }

    int getMoveFromX() { return this.fromID/10; }
    int getMoveFromY() { return this.fromID%10; }
    int getMoveToX() { return this.toID/10; }
    int getMoveToY() { return this.toID%10; }

    void bestMove() {
        int bestEval = - miniMax(-100000, 100000);
        for (Bot child : this.children) {
            if (child.value == bestEval) {
                if (child.isAttack) {
                    this.isAttack = true;
                }
                this.fromID = child.fromID;
                this.toID = child.toID;
                break;
            }
        }
    }

    private int miniMax(int alpha, int beta) {
        int a = alpha;
        int b = beta;
        int value;
        Cells opponent = getOpponent(turn);

        Map<Cells[][], ArrayList<Cells[][]>> table = this.findAllPossibleMoves(turn);

        if (depth == Bot.maxDepth || table.isEmpty()) {
            value = evaluation();
            return value;
        }

        for (Cells[][] move : table.keySet()) {
            for (Cells[][] changedBoard : table.get(move)) {
                Bot node = new Bot(changedBoard, move, opponent, this.depth+1);
                this.children.add(node);

                value = -1 * node.miniMax(-1*b, -1*a);

                a = max(a, value);
                if(a >= beta) {
                    this.value = a;
                    return a;
                }
                b = a+1;
            }
        }
        this.value = a;
        return a;
    }

    private int evaluation() {
        Cells[][] endPos = this.currentBoard;
        int commonWhite = 0;
        int queenWhite = 0;
        int commonBlack = 0;
        int queenBlack = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (endPos[x][y] == Cells.WHITE) commonWhite++;
                if (endPos[x][y] == Cells.WHITE_QUEEN) queenWhite++;
                if (endPos[x][y] == Cells.BLACK) commonBlack++;
                if (endPos[x][y] == Cells.BLACK_QUEEN) queenBlack++;
            }
        }
        if (commonBlack + commonWhite < 14) return 43*(((int) (pow(4, queenBlack + 1) - 4))-((int) (pow(4, queenWhite + 1) - 4)))
                + 71*(commonBlack-commonWhite);
        return 43*(((int) (pow(4, queenBlack + 1) - 4))-((int) (pow(4, queenWhite + 1) - 4))) + 89*(commonBlack-commonWhite);
    }

    /**
    * Поиск ходов из текущей позиции currentBoard для игрока turn
    */
    private Map<Cells[][], ArrayList<Cells[][]>> findAllPossibleMoves(Cells curPlayer) {
        Map<Cells[][], ArrayList<Cells[][]>> firstStepRelatesWithHisMoves = new HashMap<>();
        if (Functions.boardContainsCell(this.currentBoard, Cells.PLACE_MOVE)) {
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {
                    if (this.currentBoard[i][j] == Cells.PLACE_MOVE) {
                        Cells[][] onePlaceAttack = createBoardWithOnePlaceMove(this.currentBoard, i, j);
                        ArrayList<Cells[][]> manyMoves = Functions.consecutiveAttacksFromOnePosition(fromAttackX, fromAttackY,
                                onePlaceAttack, curPlayer);

                        onePlaceAttack[fromAttackX][fromAttackY] = Cells.BOT_FROM;
                        onePlaceAttack[i][j] = Cells.BOT_TO;

                        firstStepRelatesWithHisMoves.put(onePlaceAttack, manyMoves);
                    }
                }
        } else {
            if (Functions.canMakeOneAttack(this.currentBoard, curPlayer)) {
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        Cells[][] temp = Functions.possibleAttack(x, y, this.currentBoard, curPlayer);
                        for (int i = 0; i < 8; i++)
                            for (int j = 0; j < 8; j++) {
                                if (temp[i][j] == Cells.PLACE_MOVE) {
                                    Cells[][] onePlaceAttack = createBoardWithOnePlaceMove(temp, i, j);
                                    ArrayList<Cells[][]> manyMoves = Functions.consecutiveAttacksFromOnePosition(x, y,
                                            onePlaceAttack, curPlayer);

                                    onePlaceAttack[x][y] = Cells.BOT_FROM;
                                    onePlaceAttack[i][j] = Cells.BOT_TO;

                                    firstStepRelatesWithHisMoves.put(onePlaceAttack, manyMoves);
                                }
                            }
                    }
                }
            } else {
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        Cells[][] temp = Functions.possibleCommonMoves(x, y, this.currentBoard, curPlayer);
                        for (int i = 0; i < 8; i++)
                            for (int j = 0; j < 8; j++) {
                                if (temp[i][j] == Cells.PLACE_MOVE) {
                                    Cells[][] finish = Functions.commonMove(x, y, i, j, temp);
                                    ArrayList<Cells[][]> oneMove = new ArrayList<>();
                                    oneMove.add(finish);

                                    Cells[][] onePlaceMove = createBoardWithOnePlaceMove(temp, i, j);
                                    onePlaceMove[x][y] = Cells.BOT_FROM;
                                    onePlaceMove[i][j] = Cells.BOT_TO;

                                    firstStepRelatesWithHisMoves.put(onePlaceMove, oneMove);
                                }
                            }
                    }
                }
            }
        }
        return firstStepRelatesWithHisMoves;
    }

    private Cells[][] createBoardWithOnePlaceMove(Cells[][] severalMove, int attackX, int attackY) {
        Cells[][] clone = Functions.cloneBoard(severalMove);
        for (int x = 0; x < 8; x++)
            for (int y = 0; y < 8; y++) {
                if (clone[x][y] == Cells.PLACE_MOVE)
                    clone[x][y] = Cells.EMPTY;
                if (x == attackX && y == attackY)
                    clone[x][y] = Cells.PLACE_MOVE;
            }
        return clone;
    }

    private Cells getOpponent(Cells player) {
        if (player == Cells.WHITE || player == Cells.WHITE_QUEEN) return Cells.BLACK;
        else if (player == Cells.BLACK || player == Cells.BLACK_QUEEN) return Cells.WHITE;
        else return Cells.PLACE_MOVE;
    }

    private int findCellBot(Cells[][] board, Cells find) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[x][y] == find) {
                    return x * 10 + y;
                }
            }
        }
        return -99;
    }

    private boolean check(Cells[][] first, Cells[][] sec) {
        return Functions.amountOfDifferences(first, sec) > 2;
    }

}
