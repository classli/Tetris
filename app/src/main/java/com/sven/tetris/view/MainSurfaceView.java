package com.sven.tetris.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sven.tetris.model.Cell;
import com.sven.tetris.model.Tetromino;
import com.sven.tetris.util.Constant;
import com.sven.tetris.R;


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
    private int radius = 3;

    public MainSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public MainSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MainSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        this.setKeepScreenOn(true);
        backGroundStrokeWidth = getResources().getDimension(R.dimen.one_dp);
        unitDefaultColor = ContextCompat.getColor(context, R.color.unit_default);
        unitLineColor = ContextCompat.getColor(context, R.color.unit_line_color);
        unitDownColor = ContextCompat.getColor(context, R.color.unit_down);
        unitFallColor = ContextCompat.getColor(context, R.color.unit_fall);
        unitFallLine = ContextCompat.getColor(context, R.color.unit_fall_line);
        unitDownLine = ContextCompat.getColor(context, R.color.unit_down_line);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        viewWidth = this.getWidth();
        viewHeight = this.getHeight();
        unitWidth = (viewWidth-2) / Constant.col;
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
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(unitDefaultColor);
                canvas.drawRect(backGroundStrokeWidth, backGroundStrokeWidth, viewWidth-backGroundStrokeWidth,
                        viewHeight-backGroundStrokeWidth, paint);
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(backGroundStrokeWidth);
                canvas.drawRect(0, 0, viewWidth, viewHeight, paint);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(unitLineColor);
                for (int i=0; i<Constant.row-1; i++) {
                    canvas.drawLine(backGroundStrokeWidth, unitWidth*i+unitWidth+backGroundStrokeWidth,
                            viewWidth-backGroundStrokeWidth, unitWidth*i+unitWidth+backGroundStrokeWidth, paint);
                }
                for (int i=0; i<Constant.col-1; i++) {
                    canvas.drawLine(unitWidth*i+unitWidth+backGroundStrokeWidth, backGroundStrokeWidth,
                            unitWidth*i+unitWidth+backGroundStrokeWidth, viewHeight-backGroundStrokeWidth, paint);
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
                            if (Cell.CELL_WILL_DEAD == cell.getState() && interval > 6) {
                                paint.setStyle(Paint.Style.FILL);
                                paint.setColor(unitDefaultColor);
                                canvas.drawRect(col*unitWidth+backGroundStrokeWidth, row*unitWidth+backGroundStrokeWidth,
                                        col*unitWidth+unitWidth+backGroundStrokeWidth,
                                        row*unitWidth+unitWidth+backGroundStrokeWidth, paint);
                                paint.setColor(unitLineColor);
                                paint.setStyle(Paint.Style.STROKE);
                                paint.setStrokeWidth(backGroundStrokeWidth);
                                canvas.drawRoundRect(col*unitWidth+backGroundStrokeWidth, row*unitWidth+backGroundStrokeWidth,
                                        col*unitWidth+unitWidth+backGroundStrokeWidth,
                                        row*unitWidth+unitWidth+backGroundStrokeWidth, radius, radius, paint);
                            } else {
                                paint.setStyle(Paint.Style.FILL);
                                paint.setColor(unitDownColor);
                                canvas.drawRect(col*unitWidth+backGroundStrokeWidth, row*unitWidth+backGroundStrokeWidth,
                                        col*unitWidth+unitWidth+backGroundStrokeWidth,
                                        row*unitWidth+unitWidth+backGroundStrokeWidth, paint);
                                paint.setColor(unitDownLine);
                                paint.setStyle(Paint.Style.STROKE);
                                paint.setStrokeWidth(backGroundStrokeWidth);
                                canvas.drawRoundRect(col*unitWidth+backGroundStrokeWidth, row*unitWidth+backGroundStrokeWidth,
                                        col*unitWidth+unitWidth+backGroundStrokeWidth,
                                        row*unitWidth+unitWidth+backGroundStrokeWidth, radius, radius, paint);
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
                        canvas.drawRect(col*unitWidth+backGroundStrokeWidth, row*unitWidth+backGroundStrokeWidth,
                                col*unitWidth+unitWidth+backGroundStrokeWidth,
                                row*unitWidth+unitWidth+backGroundStrokeWidth, paint);
                        paint.setColor(unitFallLine);
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(backGroundStrokeWidth);
                        canvas.drawRoundRect(col*unitWidth+backGroundStrokeWidth, row*unitWidth+backGroundStrokeWidth,
                                col*unitWidth+unitWidth+backGroundStrokeWidth,
                                    row*unitWidth+unitWidth+backGroundStrokeWidth, radius, radius, paint);

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
                if (end - start < 16) {
                    Thread.sleep(16 - (end - start));
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
