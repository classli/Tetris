package com.sven.tetris.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sven.tetris.model.Cell;
import com.sven.tetris.model.Tetromino;
import com.sven.tetris.util.Constant;
import com.sven.tetris.R;

import java.util.Map;

/**
 * Created by weixiao on 18/3/11.
 */

public class MainSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable{
    private final static String TAG = "MainSurfaceView";

    private SurfaceHolder surfaceHolder;
    private Paint paint;
    private Thread thread;
    private Canvas canvas;
    private int viewWidth;
    private int viewHeight;
    private boolean flag;

    private float unitWidth;

    private int backGroundColor;
    private float backGroundStrokeWidth;
    private int unitDefaultColor;
    private int unitLineColor;
    private int unitDownColor;
    private int unitFallColor;
    private int unitFallLine;
    private int unitDownLine;
    private Tetromino nowTetromino;
    private Cell[][] stoppedCells;
    private int interval = 0;

    public MainSurfaceView(Context context) {
        super(context);
        init();
    }

    public MainSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MainSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        this.setKeepScreenOn(true);
        backGroundColor = getResources().getColor(R.color.button_bg);
        backGroundStrokeWidth = getResources().getDimension(R.dimen.one_dp);
        unitDefaultColor = getResources().getColor(R.color.unit_default);
        unitLineColor = getResources().getColor(R.color.unit_line_color);
        unitDownColor = getResources().getColor(R.color.unit_down);
        unitFallColor = getResources().getColor(R.color.unit_fall);
        unitFallLine = getResources().getColor(R.color.unit_fall_line);
        unitDownLine = getResources().getColor(R.color.unit_down_line);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        viewWidth = this.getWidth();
        viewHeight = this.getHeight();
        unitWidth = viewWidth / (Constant.col + 2);
        flag = true;
        thread = new Thread(this);
        thread.start();
    }

    private void myDraw() {
        try {
            canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {
                if (interval == 10) {
                    interval = 0;
                }
                interval ++;
                canvas.drawColor(backGroundColor);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(unitDefaultColor);
                canvas.drawRect(unitWidth, unitWidth, viewWidth-unitWidth, viewHeight -unitWidth, paint);
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(backGroundStrokeWidth);
                canvas.drawRect(unitWidth, unitWidth, viewWidth-unitWidth, viewHeight -unitWidth, paint);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(unitLineColor);
                for (int i=0; i<Constant.row-1; i++) {
                    canvas.drawLine(unitWidth, unitWidth*i+2*unitWidth, viewWidth-unitWidth, unitWidth*i+2*unitWidth, paint);
                }
                for (int i=0; i<Constant.col-1; i++) {
                    canvas.drawLine(unitWidth*i+2*unitWidth, unitWidth, unitWidth*i+2*unitWidth, viewHeight-unitWidth, paint);
                }

                if (stoppedCells != null) {
                    for (int r=0; r<stoppedCells.length; r++) {
                        for (int c=0; c<stoppedCells[0].length;c++) {
                            Cell cell = stoppedCells[r][c];
                            if (cell == null) {
                                continue;
                            }
                            if (Cell.CELL_CENTER == cell.getState()) {
                                continue;
                            }
                            int row = cell.getRow();
                            if (row < 0 || row >= Constant.row) {
                                continue;
                            }
                            int col = cell.getCol();
                            if (Cell.CELL_WILL_DEAD == cell.getState() && interval > 5) {
                                paint.setStyle(Paint.Style.FILL);
                                paint.setColor(unitDefaultColor);
                                canvas.drawRect(col*unitWidth+unitWidth, row*unitWidth+unitWidth, col*unitWidth+2*unitWidth,
                                        row*unitWidth+2*unitWidth, paint);
                                paint.setColor(unitLineColor);
                                paint.setStyle(Paint.Style.STROKE);
                                paint.setStrokeWidth(backGroundStrokeWidth);
                                canvas.drawRect(col*unitWidth+unitWidth, row*unitWidth+unitWidth, col*unitWidth+2*unitWidth,
                                        row*unitWidth+2*unitWidth, paint);
                            } else {
                                paint.setStyle(Paint.Style.FILL);
                                paint.setColor(unitDownColor);
                                canvas.drawRect(col*unitWidth+unitWidth, row*unitWidth+unitWidth, col*unitWidth+2*unitWidth,
                                        row*unitWidth+2*unitWidth, paint);
                                paint.setColor(unitDownLine);
                                paint.setStyle(Paint.Style.STROKE);
                                paint.setStrokeWidth(backGroundStrokeWidth);
                                canvas.drawRect(col*unitWidth+unitWidth, row*unitWidth+unitWidth, col*unitWidth+2*unitWidth,
                                        row*unitWidth+2*unitWidth, paint);
                            }
                        }
                    }
                }
                if (nowTetromino != null) {
                    Cell [] cells = nowTetromino.cells;
                    for (Cell cell : cells) {
                        if (Cell.CELL_CENTER == cell.getState()) {
                            continue;
                        }
                        int row = cell.getRow();
                        if (row < 0 || row >= Constant.row) {
                            continue;
                        }
                        int col = cell.getCol();
                        paint.setStyle(Paint.Style.FILL);
                        paint.setColor(unitFallColor);
                        canvas.drawRect(col*unitWidth+unitWidth, row*unitWidth+unitWidth, col*unitWidth+2*unitWidth,
                                row*unitWidth+2*unitWidth, paint);
                        paint.setColor(unitFallLine);
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(backGroundStrokeWidth);
                        canvas.drawRect(col*unitWidth+unitWidth, row*unitWidth+unitWidth, col*unitWidth+2*unitWidth,
                                row*unitWidth+2*unitWidth, paint);
                    }
                }
            }
        } catch (Throwable t) {
            Log.e(TAG, t.toString());
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }

    @Override
    public void run() {
        while (flag) {
            long start = System.currentTimeMillis();
            myDraw();
            long end = System.currentTimeMillis();
            try {
                if (end - start < 30) {
                    Thread.sleep(30 - (end - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setStoppedCells(Cell[][] cells) {
        this.stoppedCells = cells;
    }

    public void setNowTetromino(Tetromino nowTetromino) {
        this.nowTetromino = nowTetromino;
    }
}
