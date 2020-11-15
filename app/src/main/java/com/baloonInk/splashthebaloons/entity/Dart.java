package com.baloonInk.splashthebaloons.entity;

import android.graphics.Bitmap;

import lombok.Data;

@Data
public class Dart {
    private int x;
    private int y;
    private int minY;
    private int maxY;
    private int dart_speed;
    private Bitmap dart_img;

    public Dart(int x, int y, int minY, int maxY, int dart_speed, Bitmap dart_img) {
        this.x = x;
        this.y = y;
        this.minY = minY;
        this.maxY = maxY;
        this.dart_speed = dart_speed;
        this.dart_img = dart_img;
    }
}
