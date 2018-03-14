package com.sven.tetris.biz;

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
                if (!handleCollision()) {
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

    private boolean handleCollision() {
        if (nowTetromino != null) {
            boolean isCollision = false;
            for (Cell cell : nowTetromino.cells) {
                int row = cell.getRow();
                int col = cell.getCol();
                String key = (row + 1) + "-" + col;
                if ((stoppedCellMap.containsKey(key) && Cell.CELL_CENTER != stoppedCellMap.get(key).getState())
                        || cell.getRow() >= Constant.row-1) {
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
            if (gameState == GAME_STOP){
                if (timer != null) {
                    timer.cancel();
                }
            }
            return isCollision;
        }
        return false;
    }

    private boolean handleLeftCollision() {
        if (nowTetromino != null) {
            for (Cell cell : nowTetromino.cells) {
                int row = cell.getRow();
                int col = cell.getCol();
                String key = (row + 1) + "-" + (col-1);
                if ((stoppedCellMap.containsKey(key) && Cell.CELL_CENTER != stoppedCellMap.get(key).getState())
                        || col <=0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean handleRightCollision() {
        if (nowTetromino != null) {
            for (Cell cell : nowTetromino.cells) {
                int row = cell.getRow();
                int col = cell.getCol();
                String key = (row + 1) + "-" + (col+1);
                if ((stoppedCellMap.containsKey(key) && Cell.CELL_CENTER != stoppedCellMap.get(key).getState())
                        || col >= Constant.col -1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private void eliminateCell() {

    }

    public void rotationTeteris() {
        if (nowTetromino != null) {
            Cell[] cells = nowTetromino.cells;
            if (nowTetromino.type.equals(Tetromino.TYPE_I)) {

            } else {
                nowTetromino.rotation(cells[1]);
            }
        }
    }

    public void moveLeft() {
        if (nowTetromino != null && handleLeftCollision()) {
            nowTetromino.moveLeft();
        }
    }

    public void moveRight() {
        if (nowTetromino != null && handleRightCollision()) {
            nowTetromino.moveRight();
        }
    }

    public void moveDown() {
        if (nowTetromino != null) {
            handleCollision();
            nowTetromino.moveDown();
        }
    }
}
