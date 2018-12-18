package main;

import java.util.ArrayList;

class Node {

    private ArrayList<Node> children = null;

    private Cells[][] currentBoard = null;

    private int valueMinOrMax;

    Node(Cells[][] currentBoard) {
        this.children = new ArrayList<>();
        this.currentBoard = currentBoard;
    }

    Node() {
        this.children = new ArrayList<>();
    }

    int getValueMinOrMax() { return this.valueMinOrMax; }

    ArrayList<Node> getChildren() {
        return this.children;
    }

    Cells[][] getBoard() {
        return this.currentBoard;
    }

    void setChild(Node child) {
        this.children.add(child);
    }

    void setValueMinOrMax(int minOrMax) { this.valueMinOrMax = minOrMax; }

}

