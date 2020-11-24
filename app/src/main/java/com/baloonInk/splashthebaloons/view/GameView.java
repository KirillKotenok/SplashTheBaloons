package com.baloonInk.splashthebaloons.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.baloonInk.splashthebaloons.R;
import com.baloonInk.splashthebaloons.activity.EndActivity;
import com.baloonInk.splashthebaloons.activity.MainActivity;
import com.baloonInk.splashthebaloons.entity.Balloon;
import com.baloonInk.splashthebaloons.entity.Dart;
import com.baloonInk.splashthebaloons.thread.GameThread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public static boolean IS_RUNNING = false;

    //Screen size
    private int screenX;
    private int screenY;

    //Score
    private Paint scorePaint = new Paint();

    //Balloons
    private List<Balloon> yellowBalloonList;
    private List<Balloon> redBalloonList;
    private Balloon yellowBalloon;
    private Balloon redBalloon;
    private Iterator<Balloon> redBalloonIterator;
    private Iterator<Balloon> yellowBalloonIterator;

    //Life
    private Bitmap lifes[] = new Bitmap[2];
    private int life_count = 3;

    //Dart
    private Dart dart;

    //Background
    private Bitmap background;

    //Canvas
    private int canvasWidth;
    private int canvasHeight;

    //Program
    public static int score;
    private MainActivity mainActivity;
    private Random random;
    private Timer timer;
    private GameThread gameThread;
    private boolean isTouch = false;
    private Handler handler;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        getHolder().addCallback(this);
        timer = new Timer();
        this.screenX = screenX;
        this.screenY = screenY;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
        IS_RUNNING = true;
        yellowBalloon = new Balloon(-10, balloonYRandomize(),
                screenX,
                4, getYellowBalloonsList());
        redBalloon = new Balloon(-10, balloonYRandomize(),
                screenX,
                7, getRedBalloonsList());
        yellowBalloonList = initBalloonList(yellowBalloon, yellowBalloonList);
        redBalloonList = initBalloonList(redBalloon, redBalloonList);
        dart = new Dart(screenX / 2, screenY - getDart().getHeight() - 10, screenY - getDart().getHeight() - 10,
                0 - getDart().getHeight(), 15, getDart());
        mainActivity = (MainActivity) context;
        redBalloonIterator = redBalloonList.iterator();
        yellowBalloonIterator = yellowBalloonList.iterator();
        initLifes();
        initScore();
        handler = new Handler();
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameThread = new GameThread(GameView.this);
                gameThread.setRunning(true);
                gameThread.start();
            }
        }, 100);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<Balloon> initBalloonList(Balloon balloon, List<Balloon> balloonList) {
        balloonList = new ArrayList<>();
        balloonList.add(Balloon.copy(balloon));
        balloonList.add(Balloon.copy(balloon));
        balloonList.add(Balloon.copy(balloon));
        balloonList.add(Balloon.copy(balloon));
        return balloonList;
    }

    private int balloonYRandomize() {
        random = new Random();
        int randomY = random.nextInt(screenY - screenY/3) + 1;
        return randomY;
    }

    private List<Bitmap> getYellowBalloonsList() {
        List<Bitmap> balloonList = new ArrayList<>();
        List<Integer> idList = new ArrayList<>();
        idList.add(R.drawable.yellow_balloon);
        idList.add(R.drawable.splash_balloon);
        for (Integer id : idList) {
            balloonList.add(getScaledBitmap(id));
        }
        return balloonList;
    }

    private List<Bitmap> getRedBalloonsList() {
        List<Bitmap> balloonList = new ArrayList<>();
        List<Integer> idList = new ArrayList<>();
        idList.add(R.drawable.red_balloon);
        idList.add(R.drawable.splash_balloon);
        for (Integer id : idList) {
            balloonList.add(getScaledBitmap(id));
        }
        return balloonList;
    }

    private Bitmap getDart() {
        Bitmap dart = getScaledBitmap(R.drawable.dart_2);
        return dart;
    }

    private Bitmap getScaledBitmap(Integer id) {
        Bitmap buff = BitmapFactory.decodeResource(getResources(), id);
        if (id == R.drawable.dart_2) {
            Bitmap finalBitmap = Bitmap.createScaledBitmap(buff, 100, 100, false);
            return finalBitmap;
        }
        Bitmap finalBitmap = Bitmap.createScaledBitmap(buff, 70, 70, false);
        return finalBitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            isTouch = true;
        }
        return true;
    }

    public void drawObjects(Canvas canvas) {
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        canvas.drawBitmap(background, 0, 0, null);

        lifeDrawing(canvas);

        redBalloonsDrawing(canvas);

        yellowBalloonsDrawing(canvas);

        drawScore(canvas);

        dartRun(canvas);
    }

    //Life
    private void initLifes() {
        lifes[0] = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        lifes[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_g);
    }

    private void lifeDrawing(Canvas canvas) {
        //Life
        for (int i = 0; i < 3; i++) {
            int x = (int) (canvasWidth - 350 + lifes[0].getWidth() * 1.5 * i);
            int y = 30;
            if (i < life_count) {
                canvas.drawBitmap(lifes[0], x, y, null);
            } else {
                canvas.drawBitmap(lifes[1], x, y, null);
            }
        }
    }

    //RedBalloon
    private void redBalloonsDrawing(Canvas canvas) {
        int countX = -20;
        if (!redBalloonIterator.hasNext()) {
            redBalloonIterator = redBalloonList.iterator();
        } else {
            redBalloonIterator.next().setDrawing(true);
        }
        for (Balloon r : redBalloonList) {
            if (r.getX() >= r.getMaxX()) {
                if (countX < -100) {
                    countX = -20;
                }
                r.setX(countX);
                r.setY(balloonYRandomize());
                r.setDrawing(false);
                countX -= 20;
            } else {
                r.setX(r.getX() + r.getBalloon_speed());
            }
        }
        for (Balloon r : redBalloonList) {
            //Проверка на коллизию
            if (dart.getX() > r.getX() - r.getBalloonMap().get(0).getWidth()
                    && dart.getX() < r.getX() + r.getBalloonMap().get(0).getWidth()
                    && dart.getY() - dart.getDart_img().getHeight() > r.getY()
                    && dart.getY() - dart.getDart_img().getHeight() < r.getY() + r.getBalloonMap().get(0).getHeight()) {
                canvas.drawBitmap(r.getBalloonMap().get(1), r.getX(), r.getY(), null);
                life_count--;
                if (life_count <= 0) {
                    gameThread.setRunning(false);
                    IS_RUNNING = false;
                    mainActivity.runOnUiThread(() -> mainActivity.callEndActivity(score));
                }
                r.setX(-20 - r.getBalloonMap().get(0).getWidth());
                r.setY(balloonYRandomize());
            }
            canvas.drawBitmap(r.getBalloonMap().get(0), r.getX(), r.getY(), null);
        }
    }

    //YellowBalloon
    private void yellowBalloonsDrawing(Canvas canvas) {
        int countX = -20;
        if (!yellowBalloonIterator.hasNext()) {
            yellowBalloonIterator = yellowBalloonList.iterator();
        } else {
            yellowBalloonIterator.next().setDrawing(true);
        }
        for (Balloon r : yellowBalloonList) {
            if (r.getX() >= r.getMaxX()) {
                if (countX < -100) {
                    countX = -20;
                }
                r.setX(countX);
                r.setY(balloonYRandomize());
                r.setDrawing(false);
                countX -= 20;
            } else {
                r.setX(r.getX() + r.getBalloon_speed());
            }
        }
        for (Balloon r : yellowBalloonList) {
            //Проверка на коллизию
            if (dart.getX() > r.getX() - r.getBalloonMap().get(0).getWidth()
                    && dart.getX() < r.getX() + r.getBalloonMap().get(0).getWidth()
                    && dart.getY() - dart.getDart_img().getHeight() > r.getY()
                    && dart.getY() - dart.getDart_img().getHeight() < r.getY() + r.getBalloonMap().get(0).getHeight()) {
                score += 10;
                canvas.drawBitmap(r.getBalloonMap().get(1), r.getX(), r.getY(), null);
                r.setX(-20 - r.getBalloonMap().get(0).getWidth());
                r.setY(balloonYRandomize());
            } else {
                canvas.drawBitmap(r.getBalloonMap().get(0), r.getX(), r.getY(), null);
            }
        }

    }

    //Dart
    private void dartRun(Canvas canvas) {
        canvas.drawBitmap(dart.getDart_img(), dart.getX(), dart.getY(), null);

        if (isTouch) {
            dart.setY(dart.getY() - dart.getDart_speed());
            canvas.drawBitmap(dart.getDart_img(), dart.getX(), dart.getY(), null);
        }
        if (dart.getY() <= dart.getMaxY()) {
            dart.setY(dart.getMinY());
            isTouch = false;
            canvas.drawBitmap(dart.getDart_img(), dart.getX(), dart.getY(), null);
        }
    }

    //Score
    private void drawScore(Canvas canvas) {
        canvas.drawText("Score : " + score, 20, 60, scorePaint);
    }

    private void initScore() {
        scorePaint.setColor(Color.BLACK);
        scorePaint.setTextSize(32);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);
    }
}
