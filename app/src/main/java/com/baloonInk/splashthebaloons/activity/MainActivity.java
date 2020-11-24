package com.baloonInk.splashthebaloons.activity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.baloonInk.splashthebaloons.R;
import com.baloonInk.splashthebaloons.view.GameView;

public class MainActivity extends AppCompatActivity {
    private GameView gameView;
    private Point point;
    private Handler handler;
    public static final String SCORE_KEY = "SCORE";
    private final static long INTERVAL = 30;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start_btn).setOnClickListener(v -> {
            setContentView(new GameView(this, point.x, point.y));
        });
        findViewById(R.id.exit_btn).setOnClickListener(v -> finish());
    }

    public void callEndActivity(int score) {
        intent = new Intent(this, EndActivity.class);
        intent.putExtra(SCORE_KEY, score);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (GameView.IS_RUNNING) {
            finish();
            System.exit(0);
        } else {
            super.onBackPressed();
        }
    }
}