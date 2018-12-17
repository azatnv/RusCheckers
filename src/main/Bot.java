package main;

import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

class Bot {

    private Cells bot = Cells.BLACK;
    private Cells human = Cells.WHITE;

    private int depth = 9;
    private int curDepth = 1;

    private Cells[][] board = new Cells[8][8];
    private Node root = null;

    private boolean isAttack = false;

    Bot(Cells[][] board) {
        this.board = board;
        createTree();
    }

    boolean isAttack() {
        return this.isAttack;
    }

    Cells[][] bestMove() {
        int bestEval = miniMax(root, depth, -100000, 100000, bot);
        for (Node child : root.getChildren()) {
            if (child.getValueMinOrMax() == bestEval) {
                int notCompare = 0;
                for (int x = 0; x < 8; x++)
                    for (int y = 0; y < 8; y++)
                        if (board[x][y] != child.getBoard()[x][y]) notCompare++;
                isAttack = notCompare != 2;
                return child.getBoard();  //Если не совпадений - 2, то был обычный ход, иначе >2 была атака
            }
        }
        return null;
    }

    private void createTree() {
        root = new Node(board);
        ArrayList<Cells[][]> moves = findAllPossibleMoves(board, bot);
        if (moves.size() > 0) {
           createNods(root, moves, board, bot, human);
        }
    }

    private void createNods(Node parent, ArrayList<Cells[][]> moves, Cells[][] board, Cells curPlayer, Cells opponent) {
        if (curDepth++ < depth) {
            for (Cells[][] move : moves) {
                Node nextNode = new Node(move);
                parent.setChild(nextNode);

                ArrayList<Cells[][]> nextMoves = findAllPossibleMoves(board, curPlayer);
                if (nextMoves.size() > 0) {
                    createNods(nextNode, nextMoves, board, opponent, curPlayer);
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

    private ArrayList<Cells[][]> findAllPossibleMoves(Cells[][] board, Cells curPlayer) {
        ArrayList<Cells[][]> allPossibleMoves = new ArrayList<>();
        if (Functions.canMakeOneAttack(board, curPlayer)) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Cells[][] temp = Functions.possibleAttack(x, y, board, curPlayer);
                    if (Functions.boardContainsCell(temp, Cells.PLACE_MOVE)) {
                        allPossibleMoves.addAll(Functions.consecutiveAttacksFromOnePosition(x, y,
                                temp, curPlayer));
                    }
                }
            }
        } else {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Cells[][] temp = Functions.possibleCommonMoves(x, y, board, curPlayer);
                    for (int i = 0; i < 8; i++)
                        for (int j = 0; j < 8; j++) {
                            if (temp[i][j] == Cells.PLACE_MOVE)
                                allPossibleMoves.add(Functions.commonMove(x, y, i, j, temp));
                        }
                }
            }
        }
        return allPossibleMoves;
    }

}
