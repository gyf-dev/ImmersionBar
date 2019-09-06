package com.gyf.immersionbar.sample.bean;

import android.graphics.drawable.Drawable;

import com.gyf.immersionbar.sample.MyApp;
import com.gyf.immersionbar.sample.utils.DensityUtil;
import com.gyf.immersionbar.sample.utils.Utils;

import java.io.Serializable;

/**
 * @author geyifeng
 * @date 2019/4/19 3:01 PM
 */
public class FunBean implements Serializable {
    private String name;
    private Drawable pic;
    private int flower;
    private int marginStart;
    private int marginEnd;

    public FunBean(String name, Drawable pic) {
        this.name = name;
        this.pic = pic;
        this.flower = Utils.getFlowerIcon();
        this.marginStart = DensityUtil.dip2px(MyApp.getContext(), 8);
        this.marginEnd = DensityUtil.dip2px(MyApp.getContext(), 8);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getPic() {
        return pic;
    }

    public void setPic(Drawable pic) {
        this.pic = pic;
    }

    public int getFlower() {
        return flower;
    }

    public int getMarginStart() {
        return marginStart;
    }

    public void setMarginStart(int marginStart) {
        this.marginStart = marginStart;
    }

    public int getMarginEnd() {
        return marginEnd;
    }

    public void setMarginEnd(int marginEnd) {
        this.marginEnd = marginEnd;
    }
}
