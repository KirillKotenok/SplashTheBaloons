package com.baloonInk.splashthebaloons.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;

import com.baloonInk.splashthebaloons.R;
import com.baloonInk.splashthebaloons.view.GameView;

public class MainActivity extends AppCompatActivity {
    public static final String SCORE_KEY = "SCORE";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_start).setOnClickListener(v -> {
            Display display = getWindowManager().getDefaultDisplay();
            setContentView(new GameView(this, display.getWidth(), display.getHeight()));
        });
        findViewById(R.id.button_exit).setOnClickListener(v -> finish());
    }

    public void callEndActivity(int score) {
        intent = new Intent(this, EndActivity.class);
        intent.putExtra(SCORE_KEY, score);
        startActivity(intent);
        finish();
    }
/*    public void getAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(String.format("You have %d scores.", GameView.score));
        builder.setItems(new String[]{"Main menu", "Exit"}, ((dialog, which) -> {
            switch (which){
                case 0 :
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    GameView.score = 0;
                    finish();
                    break;
                case 1:
                    finish();
                    break;
            }

        }));
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xE2FF511A));
    }*/
}