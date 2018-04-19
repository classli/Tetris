package com.sven.tetris;

import com.sven.tetris.model.Cell;
import com.sven.tetris.model.Tetromino;

/**
 * Created by weixiao on 18/3/13.
 */

public interface ViewInterface {
    void refreshStoppedCells(Cell[][] cells);
    void refreshTetromino(Tetromino nowTetromino, Tetromino nextTetromino);
    void updateScore(boolean isLineScore);
    int getLevel();
    void showMaxScore();
}
