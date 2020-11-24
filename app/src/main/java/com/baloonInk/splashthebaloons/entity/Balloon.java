package com.baloonInk.splashthebaloons.entity;

import android.graphics.Bitmap;

import java.util.List;
import java.util.Random;

import lombok.Data;

@Data
public class Balloon implements Cloneable {
    private int x;
    private int y;
    private int maxX;
    private int balloon_speed;
    private List<Bitmap> balloonMap;
    private boolean isDrawing = false;
    private static final Random random = new Random();

    public Balloon(int x, int y, int maxX, int balloon_speed, List<Bitmap> balloonMap) {
        this.x = x;
        this.y = y;
        this.maxX = maxX;
        this.balloon_speed = balloon_speed;
        this.balloonMap = balloonMap;
    }

    public static Balloon copy(Balloon balloon) {
        return new Balloon(random.nextInt(100) - 100 - balloon.getX(),random.nextInt(400) + 1 , balloon.maxX,
                balloon.balloon_speed, balloon.balloonMap);
    }
}
