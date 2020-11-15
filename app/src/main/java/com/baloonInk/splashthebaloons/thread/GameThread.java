package com.baloonInk.splashthebaloons.thread;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.baloonInk.splashthebaloons.view.GameView;

import lombok.Data;

@Data
public class GameThread extends Thread {
    private GameView gameView;
    private boolean running;

    public GameThread(GameView gameView) {
        this.gameView = gameView;
        running = false;
    }

    @Override
    public void run() {
        while(running) {
            SurfaceHolder holder = gameView.getHolder();
            if (holder.getSurface().isValid()) {
                Canvas canvas = holder.lockCanvas();
                gameView.drawObjects(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
