package com.gyf.barlibrary;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by geyifeng on 2017/5/9.
 */
public class BarParams {
    @ColorInt
    public int statusBarColor = Color.TRANSPARENT;
    @ColorInt
    public int navigationBarColor = Color.BLACK;
    @FloatRange(from = 0f, to = 1f)
    public float statusBarAlpha = 0.0f;
    @FloatRange(from = 0f, to = 1f)
    public float navigationBarAlpha = 0.0f;
    public boolean fullScreen = false;
    public boolean fullScreenTemp = fullScreen;
    public BarHide barHide = BarHide.FLAG_SHOW_BAR;
    public boolean darkFont = false;
    @ColorInt
    public int statusBarColorTransform = Color.BLACK;
    @ColorInt
    public int navigationBarColorTransform = Color.BLACK;
    public View view;
    public Map<View, Map<Integer, Integer>> viewMap = new HashMap<>();
    @ColorInt
    public int viewColorBeforeTransform = statusBarColor;
    @ColorInt
    public int viewColorAfterTransform = statusBarColorTransform;
    public boolean fits = false;
    public int navigationBarColorTem = navigationBarColor;
    public View statusBarView;
    public View navigationBarView;
    public View statusBarViewByHeight;
}
