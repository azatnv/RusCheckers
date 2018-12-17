package main;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

class Functions {

    static boolean canMakeOneAttack(Cells[][] board, Cells curPlayer) {
        return !placesFromAttackIsPossible(board, curPlayer).isEmpty();
    }

    static private Set<Pair<Integer, Integer>> placesFromAttackIsPossible(Cells[][] board, Cells curPlayer) {
        Cells[][] clone = cloneBoard(board);
        Cells[][] compare;
        Set<Pair<Integer, Integer>> result = new HashSet<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (clone[i][j] == curPlayer || clone[i][j] == queen(curPlayer)) {
                    compare = possibleAttack(i, j, clone, curPlayer);
                    if (boardContainsCell(compare, Cells.PLACE_MOVE)) result.add(new Pair<>(i, j));
                }
            }
        }
        return result;
    }

    static Cells[][] possibleAttack(int fromPlaceX, int fromPlaceY,
                                                   Cells[][] board, Cells curPlayer) {
        Cells[][] clone = cloneBoard(board);
        if (board[fromPlaceX][fromPlaceY] != curPlayer) {
            return clone;
        }
        for (int i=1; i<3; i++) {
            for (int j = 1; j < 3; j++) {
                boolean flag = false;
                for (int g = 1; g < 7; g++) {
                    int toPlaceX = (int) (fromPlaceX + g * pow(-1, i));
                    int toPlaceY = (int) (fromPlaceY + g * pow(-1, j));
                    int newNextX = (int) (fromPlaceX + (g + 1) * pow(-1, i));
                    int newNextY = (int) (fromPlaceY + (g + 1) * pow(-1, j));
                    if (isOnBoard(toPlaceX, toPlaceY)) {
                        if (clone[toPlaceX][toPlaceY] == curPlayer || clone[toPlaceX][toPlaceY] == queen(curPlayer)
                                || flag && clone[toPlaceX][toPlaceY] != Cells.EMPTY)
                            break;
                        if (flag) {
                            clone[toPlaceX][toPlaceY] = Cells.PLACE_MOVE;
                        }
                        if (getOpponent(clone[toPlaceX][toPlaceY]) == curPlayer) {
                            if (isOnBoard(newNextX, newNextY) && clone[newNextX][newNextY] == Cells.EMPTY) {
                                flag = true;
                            } else break;
                        }
                    } else break;
                    if ((clone[fromPlaceX][fromPlaceY] == Cells.WHITE ||
                            clone[fromPlaceX][fromPlaceY] == Cells.BLACK) && g == 2) break;
                }
            }
        }
        return clone;
    }

    static Cells[][] possibleCommonMoves(int fromPlaceX, int fromPlaceY,
                                                 Cells[][] board, Cells curPlayer) {
        Cells[][] clone = cloneBoard(board);
        if (board[fromPlaceX][fromPlaceY] != curPlayer) {
            return clone;
        }
        for (int i=1; i<3; i++) {
            if (clone[fromPlaceX][fromPlaceY] == Cells.WHITE ||
                    clone[fromPlaceX][fromPlaceY] == Cells.BLACK) {
                int toPlaceX = (int) (fromPlaceX + pow(-1, i));
                int toPlaceY;
                if (curPlayer == Cells.WHITE) toPlaceY = fromPlaceY - 1;
                else toPlaceY = fromPlaceY + 1;
                if (isOnBoard(toPlaceX, toPlaceY) && clone[toPlaceX][toPlaceY] == Cells.EMPTY) {
                    clone[toPlaceX][toPlaceY] = Cells.PLACE_MOVE;
                }
            } else {
                for (int j=1; j<3; j++) {
                    for (int g = 1; g < 8; g++) {
                        int toPlaceX = (int) (fromPlaceX + g * pow(-1, i));
                        int toPlaceY = (int) (fromPlaceY + g * pow(-1, j));
                        if (isOnBoard(toPlaceX, toPlaceY) && clone[toPlaceX][toPlaceY] == Cells.EMPTY) {
                            clone[toPlaceX][toPlaceY] = Cells.PLACE_MOVE;
                        } else break;
                    }
                }
            }
        }
        return clone;
    }

    static Cells[][] commonMove(int fromPlaceX, int fromPlaceY, int toPlaceX, int toPlaceY,
                                    Cells[][] board) {
        board[toPlaceX][toPlaceY] = board[fromPlaceX][fromPlaceY];
        board[fromPlaceX][fromPlaceY] = Cells.EMPTY;

        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                if (board[i][j] == Cells.PLACE_MOVE) {
                    board[i][j] = Cells.EMPTY;
                }
            }
        }

        if ((toPlaceY == 0 || toPlaceY == 7) && (board[toPlaceX][toPlaceY] == Cells.WHITE ||
                board[toPlaceX][toPlaceY] == Cells.BLACK)) {
            if (toPlaceY == 0) board[toPlaceX][toPlaceY] = Cells.WHITE_QUEEN;
            else board[toPlaceX][toPlaceY] = Cells.BLACK_QUEEN;
        }

        return board;
    }

    static Cells[][] attackMove(int fromPlaceX, int fromPlaceY, int toPlaceX, int toPlaceY,
                                         Cells[][] board, Cells curPlayer) {
        board[toPlaceX][toPlaceY] = board[fromPlaceX][fromPlaceY];
        board[fromPlaceX][fromPlaceY] = Cells.EMPTY;

        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                if (board[i][j] == Cells.PLACE_MOVE) {
                    board[i][j] = Cells.EMPTY;
                }
            }
        }

        if (toPlaceY == 0 && board[toPlaceX][toPlaceY] == Cells.WHITE)
            board[toPlaceX][toPlaceY] = Cells.WHITE_QUEEN;
        if (toPlaceY == 7 && board[toPlaceX][toPlaceY] == Cells.BLACK)
            board[toPlaceX][toPlaceY] = Cells.BLACK_QUEEN;

        int directionX = (toPlaceX - fromPlaceX) / abs(toPlaceX - fromPlaceX);
        int directionY = (toPlaceY - fromPlaceY) / abs(toPlaceY - fromPlaceY);
        for (;;) {
            if (getOpponent(board[fromPlaceX + directionX][fromPlaceY + directionY]) == curPlayer) {
                board[fromPlaceX + directionX][fromPlaceY + directionY] = Cells.EMPTY;
                break;
            }
            directionX += directionX / abs(directionX);
            directionY += directionY/ abs(directionY);
        }

        return board;
    }

    static ArrayList<Cells[][]> consecutiveAttacksFromOnePosition(int fromX, int fromY,
                                                                         Cells[][] board, Cells curPlayer) {
        ArrayList<Cells[][]> result = new ArrayList<>();
        for (int i=0; i<8; i++)
            for (int j=0; j<8; j++) {
                if (board[i][j] == Cells.PLACE_MOVE) {
                    Cells[][] withoutCellOfAttack = attackMove(fromX, fromY, i, j, board, curPlayer);
                    Cells[][] newBoard = possibleAttack(i, j, withoutCellOfAttack, curPlayer);
                    if (boardContainsCell(newBoard, Cells.PLACE_MOVE)) {
                        result.addAll(consecutiveAttacksFromOnePosition(i, j, newBoard, curPlayer));
                    } else result.add(withoutCellOfAttack);
                }
            }
        return result;
    }

    static private Cells queen(Cells player) {
        if (player == Cells.WHITE)
            return Cells.WHITE_QUEEN;
        else if (player == Cells.BLACK)
            return Cells.BLACK_QUEEN;
        else return null;
    }

    static private Cells[][] cloneBoard(Cells[][] mainBoard) {
        Cells[][] boardCopy = new Cells[8][8];
        for (int i=0; i<8; i++) {
            System.arraycopy(mainBoard[i], 0, boardCopy[i], 0, 8);
        }
        return boardCopy;
    }

    static private Cells getOpponent(Cells player) {
        if (player == Cells.WHITE || player == Cells.WHITE_QUEEN) return Cells.BLACK;
        else if (player == Cells.BLACK || player == Cells.BLACK_QUEEN) return Cells.WHITE;
        else return null;
    }

    static private boolean isOnBoard(int x, int y) {
        int HEIGHT = 8;
        int WIDTH = 8;
        return (x >= 0 && x < HEIGHT && y >= 0  && y < WIDTH);
    }

    static boolean boardContainsCell(Cells[][] board, Cells cell) {
        for (Cells[] cells : board)
            for (Cells element : cells)
                if (element == cell) return true;
        return false;
    }

}
