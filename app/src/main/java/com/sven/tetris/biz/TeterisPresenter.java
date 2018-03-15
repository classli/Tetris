package com.sven.tetris.biz;

import android.util.Log;

import com.sven.tetris.ViewInterface;
import com.sven.tetris.model.Cell;
import com.sven.tetris.model.TetrisFactory;
import com.sven.tetris.model.Tetromino;
import com.sven.tetris.util.Constant;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by weixiao on 18/3/13.
 */

public class TeterisPresenter {
    private static final String TAG = "TeterisPresenter";
    public static final int GAME_START = 1;
    public static final int GAME_STOP = 0;

    private ViewInterface mvpView;
    private Tetromino nowTetromino;
    private Tetromino nextTetromino;

    private Map<String, Cell> stoppedCellMap = new HashMap<String, Cell>();
    private Timer timer;
    private int startTime = 500;
    private int intervalTime = 1000;

    private int gameState;

    public TeterisPresenter(ViewInterface view) {
        this.mvpView = view;
        mvpView.refreshStoppedCells(stoppedCellMap);
    }

    public void startGame() {
        stoppedCellMap.clear();
        nowTetromino = null;
        nextTetromino = null;

        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        gameState = GAME_START;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (nowTetromino == null) {
                    initTetris();
                }
                if (!handleDownCollision()) {
                    nowTetromino.moveDown();
                }
            }
        }, startTime, intervalTime);
    }

    public void stopGame() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        gameState = GAME_STOP;
    }

    public void initTetris() {
        randomShape();
        mvpView.refreshTetromino(nowTetromino);
    }

    private void randomShape() {
        if (nowTetromino == null) {
            nowTetromino = TetrisFactory.getInstance().ranShape();
            nextTetromino = TetrisFactory.getInstance().ranShape();
        } else {
            nowTetromino = nextTetromino;
            nextTetromino = TetrisFactory.getInstance().ranShape();
        }
    }

    private boolean handleDownCollision() {
        if (nowTetromino != null) {
            boolean isCollision = false;
            for (Cell cell : nowTetromino.cells) {
                int row = cell.getRow();
                int col = cell.getCol();
                String key = (row + 1) + "-" + col;
                if ((stoppedCellMap.containsKey(key) && Cell.CELL_CENTER != stoppedCellMap.get(key).getState())
                        || cell.getRow() >= Constant.row - 1) {
                    isCollision = true;

                    boolean isTop = false;
                    for (Cell ce : nowTetromino.cells) {
                        if (ce.getRow() <= 0) {
                            isTop = true;
                        }
                    }
                    if (row == 0 || isTop) {
                        gameState = GAME_STOP;
                    }
                }
            }
            if (isCollision) {
                for (Cell cell : nowTetromino.cells) {
                    int row = cell.getRow();
                    int col = cell.getCol();
                    String key = row + "-" + col;
                    stoppedCellMap.put(key, cell);
                }
                initTetris();
            }
            if (gameState == GAME_STOP) {
                if (timer != null) {
                    timer.cancel();
                }
            }
            return isCollision;
        }
        return false;
    }

    private boolean isLeftCollision() {
        if (nowTetromino != null) {
            for (Cell cell : nowTetromino.cells) {
                int row = cell.getRow();
                int col = cell.getCol();
                String key = row + "-" + (col - 1);
                if ((stoppedCellMap.containsKey(key) && Cell.CELL_CENTER != stoppedCellMap.get(key).getState())
                        || col <= 0) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private boolean isRightCollision() {
        if (nowTetromino != null) {
            for (Cell cell : nowTetromino.cells) {
                int row = cell.getRow();
                int col = cell.getCol();
                String key = row + "-" + (col + 1);
                if ((stoppedCellMap.containsKey(key) && Cell.CELL_CENTER != stoppedCellMap.get(key).getState())
                        || col >= Constant.col - 1) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private void eliminateCell() {

    }

    public void rotationTeteris() {
        if (nowTetromino != null) {
            Cell[] cells = nowTetromino.cells;
            if (nowTetromino.type.equals(Tetromino.TYPE_I)) {
                if (cells[0].getCol() == cells[3].getCol()) {
                    if (!isAntRotationCollision(nowTetromino.cells[1], nowTetromino.cells)) {
                        nowTetromino.antRotation(nowTetromino.cells[1]);
                    } else {
                        if (cells[0].getCol() == 0) {
                            nowTetromino.moveRight();
                            if (!isAntRotationCollision(nowTetromino.cells[1], nowTetromino.cells)) {
                                nowTetromino.antRotation(nowTetromino.cells[1]);
                            } else {
                                nowTetromino.moveLeft();
                            }
                        } else if (cells[0].getCol() == Constant.col-1) {
                            nowTetromino.moveLeft();
                            nowTetromino.moveLeft();
                            if (!isAntRotationCollision(nowTetromino.cells[1], nowTetromino.cells)) {
                                nowTetromino.antRotation(nowTetromino.cells[1]);
                            } else {
                                nowTetromino.moveRight();
                                nowTetromino.moveRight();
                            }
                        } else if (cells[0].getCol() == Constant.col-2) {
                            nowTetromino.moveLeft();
                            if (!isAntRotationCollision(nowTetromino.cells[1], nowTetromino.cells)) {
                                nowTetromino.antRotation(nowTetromino.cells[1]);
                            } else {
                                nowTetromino.moveRight();
                            }
                        }
                    }
                } else {
                    if (!isRotationCollision(nowTetromino.cells[1], nowTetromino.cells)) {
                        nowTetromino.rotation(nowTetromino.cells[1]);
                    }
                }
            } else if (nowTetromino.type.equals(Tetromino.TYPE_Z)) {
                if (nowTetromino.cells[1].getRow()<nowTetromino.cells[2].getRow()
                        && !isRotationCollision(nowTetromino.cells[1], nowTetromino.cells)) {
                    nowTetromino.rotation(nowTetromino.cells[1]);
                } else if (!isAntRotationCollision(nowTetromino.cells[1], nowTetromino.cells)){
                    nowTetromino.antRotation(nowTetromino.cells[1]);
                }
            } else if (nowTetromino.type.equals(Tetromino.TYPE_S)) {
                if (nowTetromino.cells[1].getRow() < nowTetromino.cells[3].getRow()
                        && !isAntRotationCollision(nowTetromino.cells[1], nowTetromino.cells)) {
                    nowTetromino.antRotation(nowTetromino.cells[1]);
                } else if(!isRotationCollision(nowTetromino.cells[1], nowTetromino.cells)) {
                    nowTetromino.rotation(nowTetromino.cells[1]);
                }
            }else {
                if (!isRotationCollision(cells[1], cells)) {
                    nowTetromino.rotation(cells[1]);
                }
            }
        }
    }

    private boolean isRotationCollision(Cell center, Cell[] cells) {
        if (cells == null || center == null) {
            return false;
        }
        int row;
        int col;
        for (Cell cell : cells) {
            row = center.getRow() - center.getCol() + cell.getCol();
            col = center.getRow() + center.getCol() - cell.getRow();
            String key = row + "-" + col;
            if ((stoppedCellMap.containsKey(key) && Cell.CELL_CENTER != stoppedCellMap.get(key).getState())
                    || row > Constant.row - 1 || col < 0 || col > Constant.col - 1) {
                return true;
            }
        }
        return false;
    }

    private boolean isAntRotationCollision(Cell center, Cell[] cells) {
        if (cells == null || center == null) {
            return false;
        }
        int row;
        int col;
        for (Cell cell : cells) {
            row = center.getRow() + center.getCol() - cell.getCol();
            col = center.getCol() - center.getRow() + cell.getRow();
            String key = row + "-" + col;
            if ((stoppedCellMap.containsKey(key) && Cell.CELL_CENTER != stoppedCellMap.get(key).getState())
                    || row > Constant.row - 1 || col < 0 || col > Constant.col - 1) {
                return true;
            }
        }
        return false;
    }

    public void moveLeft() {
        if (nowTetromino != null && !isLeftCollision()) {
            nowTetromino.moveLeft();
        }
    }

    public void moveRight() {
        if (nowTetromino != null && !isRightCollision()) {
            nowTetromino.moveRight();
        }
    }

    public void moveDown() {
        if (nowTetromino != null && !handleDownCollision()) {
            nowTetromino.moveDown();
        }
    }
}
