package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.max;
import static java.lang.Math.min;

class Bot {

    private Cells bot = Cells.BLACK;
    private Cells human = Cells.WHITE;

    private int depth = 9;
    private int curDepth = 1;

    private Cells[][] board = new Cells[8][8];
    private Node root = null;
    private Map<Cells[][], ArrayList<Cells[][]>> firstStepsAndHisMoves;

    private int fromAttackX;
    private int fromAttackY;

    private boolean isAttack = false;
    private int moveFromX;
    private int moveFromY;
    private int moveToX;
    private int moveToY;

    Bot(Cells[][] board) {
        this.board = board;
        createTree();
    }

    Bot(Cells[][] board, int fromAttackX, int fromAttackY) {
        this.board = board;
        this.fromAttackX = fromAttackX;
        this.fromAttackY = fromAttackY;
        createTree();
    }

    boolean isAttack() {
        return this.isAttack;
    }

    int getMoveFromX() { return this.moveFromX; }
    int getMoveFromY() { return this.moveFromY; }
    int getMoveToX() { return this.moveToX; }
    int getMoveToY() { return this.moveToY; }

    void bestMove() {
        int bestEval = miniMax(root, depth, -100000, 100000, bot);
        Node bestNode = new Node();
        for (Node child : root.getChildren()) {
            if (child.getValueMinOrMax() == bestEval) {
                bestNode = child;
                break;
            }
        }
        Cells[][] bestFirstStep = new Cells[8][8];
        for (Cells[][] firstStep: firstStepsAndHisMoves.keySet()) {
            if (firstStepsAndHisMoves.get(firstStep).contains(bestNode.getBoard())) {
                bestFirstStep = firstStep;
            }
        }
        int fromID = findCellBot_From(bestFirstStep, Cells.BOT_FROM);
        this.moveFromX = fromID / 10;
        this.moveFromY = fromID % 10;
        int toID = findCellBot_From(bestFirstStep, Cells.BOT_TO);
        this.moveToX = toID / 10;
        this.moveToY = toID % 10;
    }

    private int findCellBot_From(Cells[][] board, Cells find) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[x][y] == find) {
                    return x * 10 + y;
                }
            }
        }
        return -99;
    }

    private void createTree() {
        root = new Node(board);
        Map<Cells[][], ArrayList<Cells[][]>> tableFirstMoves = findAllPossibleMoves(board, bot);
        this.firstStepsAndHisMoves = tableFirstMoves;

        if (tableFirstMoves.size() > 0) {
           createNodes(root, tableFirstMoves, board, bot, human);
        }
    }

    private void createNodes(Node parent, Map<Cells[][], ArrayList<Cells[][]>> table, Cells[][] board, Cells curPlayer, Cells opponent) {
        if (curDepth++ < depth) {
            for (ArrayList<Cells[][]> moves: table.values()) {
                for (Cells[][] move: moves) {
                    Node nextNode = new Node(move);
                    parent.setChild(nextNode);

                    Map<Cells[][], ArrayList<Cells[][]>> nextMoves = findAllPossibleMoves(board, curPlayer);
                    if (nextMoves.size() > 0) {
                        createNodes(nextNode, nextMoves, board, opponent, curPlayer);
                    }
                }
            }
        }
    }

    private int miniMax(Node position, int depth, int alpha, int beta, Cells maximizingPlayer) {
        if (depth == 0) {
            return evaluation(position);
        }

        if (maximizingPlayer == bot) {
            int maxEval = -100000;
            for (Node child: position.getChildren()){
                int eval = miniMax(child, depth - 1, alpha, beta, human);
                maxEval = max(maxEval, eval);
                alpha = max(alpha, eval);
                if (beta <= alpha) break;
            }
            position.setValueMinOrMax(maxEval);
            return maxEval;
        } else {
            int minEval = 100000;
            for (Node child: position.getChildren()){
                int eval = miniMax(child, depth - 1, alpha, beta, bot);
                minEval = min(minEval, eval);
                beta = min(beta, eval);
                if (beta <= alpha) break;
            }
            position.setValueMinOrMax(minEval);
            return minEval;
        }
    }

    private int evaluation(Node endPos) {
        Cells[][] endPosBoard = endPos.getBoard();
        int countWhite = 0;
        int countBlack = 0;
        for (int x = 0; x < 8; x++)
            for (int y = 0; y < 8; y++) {
                if (endPosBoard[x][y] == Cells.WHITE_QUEEN) countWhite += 4;
                else if (endPosBoard[x][y] == Cells.WHITE) countWhite += 1;
                else if (endPosBoard[x][y] == Cells.BLACK_QUEEN) countBlack += 4;
                else if (endPosBoard[x][y] == Cells.BLACK) countBlack += 1;
            }
        return countWhite - countBlack;
    }

    private Map<Cells[][], ArrayList<Cells[][]>> findAllPossibleMoves(Cells[][] board, Cells curPlayer) {
        Map<Cells[][], ArrayList<Cells[][]>> firstStepRelatesWithHisMoves = new HashMap<>();
        if (Functions.boardContainsCell(board, Cells.PLACE_MOVE)) {
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] == Cells.PLACE_MOVE) {
                        Cells[][] onePlaceAttack = createBoardWithOnePlaceAttack(board, i, j);
                        ArrayList<Cells[][]> manyMoves = Functions.consecutiveAttacksFromOnePosition(fromAttackX, fromAttackY,
                                onePlaceAttack, curPlayer);

                        Cells[][] start = Functions.cloneBoard(board);
                        start[fromAttackX][fromAttackY] = Cells.BOT_FROM;
                        start[i][j] = Cells.BOT_TO;

                        firstStepRelatesWithHisMoves.put(start, manyMoves);
                    }
                }
        } else {
            if (Functions.canMakeOneAttack(board, curPlayer)) {
                this.isAttack = true;
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        Cells[][] temp = Functions.possibleAttack(x, y, board, curPlayer);
                        for (int i = 0; i < 8; i++)
                            for (int j = 0; j < 8; j++) {
                                if (temp[i][j] == Cells.PLACE_MOVE) {
                                    Cells[][] onePlaceAttack = createBoardWithOnePlaceAttack(temp, i, j);
                                    ArrayList<Cells[][]> manyMoves = Functions.consecutiveAttacksFromOnePosition(x, y,
                                            onePlaceAttack, curPlayer);

                                    Cells[][] start = Functions.cloneBoard(board);
                                    start[x][y] = Cells.BOT_FROM;
                                    start[i][j] = Cells.BOT_TO;

                                    firstStepRelatesWithHisMoves.put(start, manyMoves);
                                }
                            }
                    }
                }
            } else {
                this.isAttack = false;
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        Cells[][] temp = Functions.possibleCommonMoves(x, y, board, curPlayer);
                        for (int i = 0; i < 8; i++)
                            for (int j = 0; j < 8; j++) {
                                if (temp[i][j] == Cells.PLACE_MOVE) {
                                    Cells[][] finish = Functions.commonMove(x, y, i, j, temp);
                                    ArrayList<Cells[][]> oneMove = new ArrayList<>();
                                    oneMove.add(finish);

                                    Cells[][] start = Functions.cloneBoard(board);
                                    start[x][y] = Cells.BOT_FROM;
                                    start[i][j] = Cells.BOT_TO;

                                    firstStepRelatesWithHisMoves.put(start, oneMove);
                                }
                            }
                    }
                }
            }
        }
        return firstStepRelatesWithHisMoves;
    }

    private Cells[][] createBoardWithOnePlaceAttack(Cells[][] severalMove, int attackX, int attackY) {
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

}
