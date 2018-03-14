package com.sven.tetris;

import com.sven.tetris.model.Cell;
import com.sven.tetris.model.Tetromino;

import java.util.Map;

/**
 * Created by weixiao on 18/3/13.
 */

public interface ViewInterface {
    void refreshStoppedCells(Map<String, Cell> cellMap);
    void refreshTetromino(Tetromino tetromino);
}
