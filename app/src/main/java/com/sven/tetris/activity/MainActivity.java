package com.sven.tetris.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sven.tetris.R;
import com.sven.tetris.ViewInterface;
import com.sven.tetris.biz.TeterisPresenter;
import com.sven.tetris.model.Cell;
import com.sven.tetris.model.Tetromino;
import com.sven.tetris.util.Utils;
import com.sven.tetris.view.MainSurfaceView;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements ViewInterface{

    private MainSurfaceView surfaceView;
    private TeterisPresenter presenter;
    private TextView textView;
    private TextView change;
    private TextView stop;
    private ImageView left;
    private ImageView down;
    private ImageView right;
    private ImageView shotDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarColor(this, R.color.button_bg);
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
        textView = findViewById(R.id.restart);

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
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.moveLeft();
                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.moveRight();
                }
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.moveDown();
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.startGame();
                }
            }
        });
    }

    @Override
    public void refreshStoppedCells(Map<String, Cell> cellMap) {
        if (cellMap == null || surfaceView == null) {
            return;
        }
        surfaceView.setStoppedCells(cellMap);
    }

    @Override
    public void refreshTetromino(Tetromino tetromino) {
        if (tetromino == null || surfaceView == null) {
            return;
        }
        surfaceView.setNowTetromino(tetromino);
    }
}
