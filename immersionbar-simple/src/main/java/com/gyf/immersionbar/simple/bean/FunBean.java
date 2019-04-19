package com.gyf.immersionbar.simple.bean;

import android.graphics.drawable.Drawable;

import com.gyf.immersionbar.simple.utils.Utils;

import java.io.Serializable;

/**
 * @author geyifeng
 * @date 2019/4/19 3:01 PM
 */
public class FunBean implements Serializable {
    private String name;
    private Drawable pic;
    private int flower;

    public FunBean(String name, Drawable pic) {
        this.name = name;
        this.pic = pic;
        this.flower = Utils.getFlower();
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
}
