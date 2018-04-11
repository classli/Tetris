package com.sven.tetris.model;


import java.util.Random;

/**
 * Created by weixiao on 18/3/12.
 */

public class TetrisFactory {


    private static TetrisFactory instance;

    private TetrisFactory() {

    }

    public static TetrisFactory getInstance() {
        if (instance == null) {
            instance = new TetrisFactory();
        }
        return instance;
    }


    class Z extends Tetromino {
        public Z() {
            super(TYPE_Z);
            cells = new Cell[4];
            cells[0] = new Cell(-1, 4);
            cells[1] = new Cell(-1, 5);
            cells[2] = new Cell(0, 5);
            cells[3] = new Cell(0, 6);
        }
    }

    class S extends Tetromino {
        public S() {
            super(TYPE_S);
            cells = new Cell[4];
            cells[0] = new Cell(-1, 6);
            cells[1] = new Cell(-1, 5);
            cells[2] = new Cell(0, 4);
            cells[3] = new Cell(0, 5);
        }
    }

    class J extends Tetromino {
        public J() {
            super(TYPE_J);
            cells = new Cell[5];
            cells[0] = new Cell(-1, 4);
            cells[1] = new Cell(-1, 5, Cell.CELL_CENTER);
            cells[2] = new Cell(0, 4);
            cells[3] = new Cell(0, 5);
            cells[4] = new Cell(0, 6);
        }
    }

    class L extends Tetromino {
        public L() {
            super(TYPE_L);
            cells = new Cell[5];
            cells[0] = new Cell(-1, 6);
            cells[1] = new Cell(-1, 5, Cell.CELL_CENTER);
            cells[2] = new Cell(0, 4);
            cells[3] = new Cell(0, 5);
            cells[4] = new Cell(0, 6);
        }
    }

    class O extends Tetromino {
        public O() {
            super(TYPE_O);
            cells = new Cell[4];
            cells[0] = new Cell(-1, 4);
            cells[1] = new Cell(-1, 5);
            cells[2] = new Cell(0, 4);
            cells[3] = new Cell(0, 5);
        }
    }

    class I extends Tetromino {
        public I() {
            super(TYPE_I);
            cells = new Cell[4];
            cells[0] = new Cell(-3, 5);
            cells[1] = new Cell(-2, 5);
            cells[2] = new Cell(-1, 5);
            cells[3] = new Cell(0, 5);
        }
    }

    class T extends Tetromino {
        public T() {
            super(TYPE_T);
            cells = new Cell[4];
            cells[0] = new Cell(-2, 5);
            cells[1] = new Cell(-1, 5);
            cells[2] = new Cell(-1, 4);
            cells[3] = new Cell(-1, 6);
        }
    }

    public Tetromino ranShape() {
        Random random = new Random();
        int index = random.nextInt(7);
        switch (index) {
            case 0:
                return new Z();
            case 1:
                return new S();
            case 2:
                return new J();
            case 3:
                return new L();
            case 4:
                return new O();
            case 5:
                return new I();
            case 6:
                return new T();
        }
        return null;
    }

}
