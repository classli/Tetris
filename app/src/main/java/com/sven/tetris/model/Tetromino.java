package com.sven.tetris.model;

/**
 * Created by weixiao on 18/3/12.
 */

public class Tetromino {

    public final static String TYPE_Z = "Z";
    public final static String TYPE_S = "S";
    public final static String TYPE_J = "J";
    public final static String TYPE_L = "L";
    public final static String TYPE_O = "O";
    public final static String TYPE_I = "I";
    public final static String TYPE_T = "T";

    public String type;
    public Cell [] cells ;

    public Tetromino(String type) {
        this.type = type;
    }

    public void moveDown() {
        if (cells == null) {
            return;
        }
        for (int i=0; i<cells.length;i++) {
            cells[i].moveDown();
        }
    }

    public void moveLeft() {
        if (cells == null) {
            return;
        }
        for (int i=0; i<cells.length;i++) {
            cells[i].moveLeft();
        }
    }

    public void moveRight() {
        if (cells == null) {
            return;
        }
        for (int i=0; i<cells.length;i++) {
            cells[i].moveRight();
        }
    }

    public void rotation(Cell center) {
        if (cells == null || center == null) {
            return;
        }
        if (TYPE_O.equals(type)) {
            return;
        }
        for (int i=0; i<cells.length;i++) {
            cells[i].rotation(center);
        }
    }

    public void antRotation(Cell center) {
        if (cells == null || center == null) {
            return;
        }
        if (TYPE_O.equals(type)) {
            return;
        }
        for (int i=0; i<cells.length;i++) {
            cells[i].antRotation(center);
        }
    }
}
