package com.sven.tetris.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.sven.tetris.R;
import com.sven.tetris.ViewInterface;
import com.sven.tetris.biz.TeterisPresenter;
import com.sven.tetris.model.Cell;
import com.sven.tetris.model.Tetromino;
import com.sven.tetris.util.Constant;
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
    private int score = 0;
    private int cleanLine = 0;
    private int level = 1;
    private TextView scoreView;
    private SharedPreferences preferences;
    private boolean isSuspend = false;
    private int maxScore = 0;
    private TextView scoreTitle;
    private TextView cleanLineView;
    private TextView levelView;

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
        scoreView = findViewById(R.id.score);
        scoreTitle = findViewById(R.id.score_title);
        cleanLineView = findViewById(R.id.clear_line);
        levelView = findViewById(R.id.level);
    }

    private void initData() {
        presenter = new TeterisPresenter(this);
        preferences = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        maxScore = preferences.getInt(Constant.SP_KEY_MAX_SCORE, 0);
        isSuspend = preferences.getBoolean(Constant.SP_KEY_SUSPEND, false);
        if (isSuspend) {
            score = preferences.getInt(Constant.SP_KEY_SCORE, 0);
            cleanLine = preferences.getInt(Constant.SP_KEY_CLEAN_LINE, 0);
            level = preferences.getInt(Constant.SP_KEY_LEVEL, 1);
            presenter.initStopCells(preferences.getString(Constant.SP_KEY_CELL, null));
            presenter.setGameState(TeterisPresenter.GAME_SUSPEND);

            scoreTitle.setText(R.string.Score);
            scoreView.setText(score + "");
            stop.setPressed(isSuspend);
        } else {
            scoreTitle.setText(R.string.max_score);
            scoreView.setText(maxScore + "");
        }
        cleanLineView.setText(cleanLine + "");
        levelView.setText(level + "");
        initListener();
    }

    private void initListener() {
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter == null) {
                    return;
                }
                if (presenter.getGameState() == TeterisPresenter.GAME_SUSPEND
                        || presenter.getGameState() == TeterisPresenter.GAME_STOP) {
                    presenter.startGame();
                    return;
                }
                presenter.rotationTeteris();
            }
        });
        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (presenter == null) {
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (presenter.getGameState() == TeterisPresenter.GAME_SUSPEND) {
                            presenter.startGame();
                            return true;
                        }
                        if (presenter.getGameState() == TeterisPresenter.GAME_STOP) {
                            level = level - 1;
                            if (level < 1) {
                                level = 1;
                            }
                            levelView.setText(level + "");
                            return true;
                        }
                        presenter.moveLeft();
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
                if (presenter == null) {
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (presenter.getGameState() == TeterisPresenter.GAME_SUSPEND) {
                            presenter.startGame();
                            return true;
                        }
                        if (presenter.getGameState() == TeterisPresenter.GAME_STOP) {
                            level = level + 1;
                            if (level > 6) {
                                level = 6;
                            }
                            levelView.setText(level + "");
                            return true;
                        }
                        presenter.moveRight();
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
                if (presenter == null || presenter.getGameState() == TeterisPresenter.GAME_STOP) {
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (presenter.getGameState() == TeterisPresenter.GAME_SUSPEND) {
                            presenter.startGame();
                            return true;
                        }
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
                scoreTitle.setText(R.string.Score);
                isSuspend = false;
                cleanLine = 0;
                score = 0;
                scoreView.setText(score+"");
                cleanLineView.setText(cleanLine+"");
                stop.setPressed(false);
            }
        });
        stop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isSuspend = !isSuspend;
                    if (isSuspend) {
                        if (presenter != null) {
                            presenter.stopGame();
                            presenter.setGameState(TeterisPresenter.GAME_SUSPEND);
                        }
                    } else {
                        if (presenter != null) {
                            presenter.startGame();
                        }
                    }
                    stop.setPressed(isSuspend);
                    return true;
                }
                return false;
            }
        });
        shotDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter == null || presenter.getGameState() == TeterisPresenter.GAME_STOP) {
                    return;
                }
                if (presenter.getGameState() == TeterisPresenter.GAME_SUSPEND) {
                    presenter.startGame();
                    return;
                }
                ObjectAnimator animator = ObjectAnimator.ofFloat(surfaceView, "TranslationY", 0
                        , getResources().getDimension(R.dimen.translationY));
                animator.setDuration(20);
                animator.setRepeatCount(1);
                animator.setInterpolator(new DecelerateInterpolator());
                animator.setRepeatMode(ValueAnimator.REVERSE);
                animator.start();
                presenter.shotDown();
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

    @Override
    public void updateScore(final boolean isLineScore) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isLineScore) {
                    score = score + Constant.SCORE_LINE;
                    cleanLine = cleanLine + 1;
                    cleanLineView.setText(cleanLine + "");
                } else {
                    score = score + Constant.SCORE_CELL;
                }
                if (scoreView != null) {
                    scoreView.setText(score + "");
                }
                if (score < 12000 && (score / 2000) == level) {
                    level = level + 1;
                    levelView.setText(level + "");
                    presenter.reActionMainTimer();
                }
            }
        });
    }

    @Override
    public int getLevel() {
        return level;
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
    protected void onStop() {
        SharedPreferences.Editor editor = preferences.edit();
        if (score > maxScore) {
            editor.putInt(Constant.SP_KEY_MAX_SCORE, score);
        }
        if (isSuspend) {
            editor.putBoolean(Constant.SP_KEY_SUSPEND, true);
            editor.putInt(Constant.SP_KEY_SCORE, score);
            editor.putInt(Constant.SP_KEY_CLEAN_LINE, cleanLine);
            editor.putInt(Constant.SP_KEY_LEVEL, level);
            editor.putString(Constant.SP_KEY_CELL, presenter.getCellJsonStr());
        } else {
            editor.putBoolean(Constant.SP_KEY_SUSPEND, false);
        }
        editor.apply();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
