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

import com.sven.tetris.R;
import com.sven.tetris.model.Cell;
import com.sven.tetris.model.Tetromino;

/**
 * Created by weixiao on 18/4/13.
 */

public class NextTeterisView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private final static String TAG = "NextTeterisView";
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    private Thread thread;
    private Canvas canvas;
    private int viewWidth;
    private int viewHeight;
    private boolean flag;
    private float unitWidth;
    private Tetromino tetromino;
    private int cellColor;
    private int radius = 3;
    private int unitLineColor;
    private float backGroundStrokeWidth;

    public NextTeterisView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public NextTeterisView(Context context) {
        super(context);
        init(context);
    }

    public NextTeterisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        cellColor = ContextCompat.getColor(context, R.color.next_cell);
        unitLineColor = ContextCompat.getColor(context, R.color.unit_line_color);
        backGroundStrokeWidth = getResources().getDimension(R.dimen.one_dp);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        viewWidth = this.getWidth();
        viewHeight = this.getHeight();
        unitWidth = (viewWidth -2) / 4;
        flag = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }

    private void myDraw() {
        try {
            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.WHITE);

            if (tetromino != null) {
                if (Tetromino.TYPE_I.equals(tetromino.type)) {
                    for (int col = 0; col < 4; col++) {
                        draw(1, col, paint, canvas);
                    }
                } else if (Tetromino.TYPE_T.equals(tetromino.type)) {
                    for (Cell cell : tetromino.cells) {
                        int row = cell.getRow() + 2;
                        int col = cell.getCol() - 4;
                        draw(row, col, paint, canvas);
                    }
                } else {
                    for (Cell cell : tetromino.cells) {
                        if (cell.getState() == Cell.CELL_CENTER) {
                            continue;
                        }
                        int row = cell.getRow() + 1;
                        int col = cell.getCol() - 4;
                        draw(row, col, paint, canvas);
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

    private void draw(int row, int col, Paint paint, Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(cellColor);
        canvas.drawRect(col*unitWidth+backGroundStrokeWidth, row*unitWidth+backGroundStrokeWidth,
                col*unitWidth+unitWidth+backGroundStrokeWidth,
                row*unitWidth+unitWidth+backGroundStrokeWidth, paint);
        paint.setColor(unitLineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(backGroundStrokeWidth);
        canvas.drawRect(col*unitWidth+backGroundStrokeWidth, row*unitWidth+backGroundStrokeWidth,
                col*unitWidth+unitWidth+backGroundStrokeWidth,
                row*unitWidth+unitWidth+backGroundStrokeWidth, paint);
    }

    public void refreshNextTeteris(Tetromino tetromino) {
        this.tetromino = tetromino;
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
}
