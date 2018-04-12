package com.sven.tetris.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.sven.tetris.R;
import com.sven.tetris.ViewInterface;
import com.sven.tetris.biz.TeterisPresenter;
import com.sven.tetris.model.Cell;
import com.sven.tetris.model.Tetromino;
import com.sven.tetris.util.Constant;
import com.sven.tetris.util.Utils;
import com.sven.tetris.view.MainSurfaceView;

public class MainActivity extends AppCompatActivity implements ViewInterface {
    private static final String TAG = "MainActivity";
    private MainSurfaceView surfaceView;
    private TeterisPresenter presenter;
    private TextView restart;
    private TextView change;
    private TextView stop;
    private ImageView left;
    private ImageView down;
    private ImageView right;
    private ImageView shotDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        surfaceView = findViewById(R.id.main_view);
        left = findViewById(R.id.btn_left);
        right = findViewById(R.id.btn_right);
        down = findViewById(R.id.btn_down);
        shotDown = findViewById(R.id.btn_ddown);
        stop = findViewById(R.id.pause);
        restart = findViewById(R.id.restart);
        change = findViewById(R.id.change);
    }

    private void initData() {
        presenter = new TeterisPresenter(this);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.rotationTeteris();
                }
            }
        });
        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (presenter != null) {
                            presenter.moveLeft();
                        }
                        handler.sendEmptyMessageDelayed(Constant.MSG_LEFT, Constant.LONG_PRESS_TIME);
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacksAndMessages(null);
                        break;
                }
                return true;
            }
        });
        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (presenter != null) {
                            presenter.moveRight();
                        }
                        handler.sendEmptyMessageDelayed(Constant.MSG_RIGHT, Constant.LONG_PRESS_TIME);
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacksAndMessages(null);
                        break;
                }
                return true;
            }
        });
        down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (presenter != null) {
                            presenter.moveDown();
                        }
                        handler.sendEmptyMessageDelayed(Constant.MSG_DOWN, Constant.LONG_PRESS_TIME);
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacksAndMessages(null);
                        break;
                }
                return true;
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.startGame();
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.stopGame();
                }
            }
        });
        shotDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(surfaceView, "TranslationY", 0
                            , getResources().getDimension(R.dimen.translationY));
                    animator.setDuration(30);
                    animator.setRepeatCount(1);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.setRepeatMode(ValueAnimator.REVERSE);
                    animator.start();
                    presenter.shotDown();
                }
            }
        });
    }

    @Override
    public void refreshStoppedCells(Cell[][] cells) {
        if (cells == null || surfaceView == null) {
            return;
        }
        surfaceView.setStoppedCells(cells);
    }

    @Override
    public void refreshTetromino(Tetromino tetromino) {
        if (tetromino == null || surfaceView == null) {
            return;
        }
        surfaceView.setNowTetromino(tetromino);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.MSG_DOWN:
                    handler.sendEmptyMessageDelayed(Constant.MSG_DOWN, Constant.LONG_PRESS_DOWN);
                    if (presenter != null) {
                        presenter.moveDown();
                    }
                    break;
                case Constant.MSG_LEFT:
                    handler.sendEmptyMessageDelayed(Constant.MSG_LEFT, Constant.LONG_PRESS_LEFT);
                    if (presenter != null) {
                        presenter.moveLeft();
                    }
                    break;
                case Constant.MSG_RIGHT:
                    handler.sendEmptyMessageDelayed(Constant.MSG_RIGHT, Constant.LONG_PRESS_LEFT);
                    if (presenter != null) {
                        presenter.moveRight();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
