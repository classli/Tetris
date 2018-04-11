package com.sven.tetris.model;


/**
 * Created by weixiao on 18/3/12.
 */

public class Cell {

    public final static int CELL_MOVE = 0;
    public final static int CELL_STOP = 1;
    public final static int CELL_DID_DEAD = 2;
    public final static int CELL_WILL_DEAD = 3;
    public final static int CELL_CENTER = 4;
    public final static int LEFT_ADD_TIME = 4;
    private int row;
    private int col;
    private int state;
    private int lifeTime = 0;

    public void addLifeTime() {
        lifeTime ++;
        if (lifeTime == LEFT_ADD_TIME) {
            state = CELL_DID_DEAD;
        }
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        state = CELL_MOVE;
    }

    public Cell(int row, int col, int state) {
        this.row = row;
        this.col = col;
        this.state = state;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void moveDown() {
        row = row + 1;
    }

    public void moveLeft() {
        col = col - 1;
    }

    public void moveRight() {
        col = col + 1;
    }

    public void rotation(Cell centerCell) {
        if (centerCell == null) {
            return;
        }
        int tempRow;
        tempRow = centerCell.getRow() - centerCell.getCol() + col;
        col = centerCell.getRow() + centerCell.getCol() - row;
        row = tempRow;
    }

    public void antRotation(Cell centerCell) {
        if (centerCell == null) {
            return;
        }
        int tempRow;
        tempRow = centerCell.getRow() + centerCell.getCol() - col;
        col = centerCell.getCol() - centerCell.getRow() + row;
        row = tempRow;
    }

}
