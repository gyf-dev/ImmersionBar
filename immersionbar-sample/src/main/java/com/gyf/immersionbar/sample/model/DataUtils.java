package com.gyf.immersionbar.sample.model;

import android.content.Context;
import androidx.core.content.ContextCompat;

import com.gyf.immersionbar.sample.R;
import com.gyf.immersionbar.sample.bean.FunBean;

import java.util.ArrayList;

/**
 * @author geyifeng
 * @date 2019/4/19 3:19 PM
 */
public class DataUtils {
    public static ArrayList<FunBean> getMainData(Context context) {
        ArrayList<FunBean> funBeans = new ArrayList<>();
        funBeans.add(new FunBean(context.getString(R.string.text_params), ContextCompat.getDrawable(context, R.mipmap.icon_1)));
        funBeans.add(new FunBean(context.getString(R.string.text_params_kotlin), ContextCompat.getDrawable(context, R.mipmap.icon_2)));
        funBeans.add(new FunBean(context.getString(R.string.text_pic_color), ContextCompat.getDrawable(context, R.mipmap.icon_3)));
        funBeans.add(new FunBean(context.getString(R.string.text_pic), ContextCompat.getDrawable(context, R.mipmap.icon_4)));
        funBeans.add(new FunBean(context.getString(R.string.text_color), ContextCompat.getDrawable(context, R.mipmap.icon_5)));
        funBeans.add(new FunBean(context.getString(R.string.text_shape), ContextCompat.getDrawable(context, R.mipmap.icon_6)));
        funBeans.add(new FunBean(context.getString(R.string.text_swipe_back), ContextCompat.getDrawable(context, R.mipmap.icon_7)));
        funBeans.add(new FunBean(context.getString(R.string.text_fragment), ContextCompat.getDrawable(context, R.mipmap.icon_8)));
        funBeans.add(new FunBean(context.getString(R.string.text_dialog), ContextCompat.getDrawable(context, R.mipmap.icon_9)));
        funBeans.add(new FunBean(context.getString(R.string.text_popup), ContextCompat.getDrawable(context, R.mipmap.icon_10)));
        funBeans.add(new FunBean(context.getString(R.string.text_drawer), ContextCompat.getDrawable(context, R.mipmap.icon_11)));
        funBeans.add(new FunBean(context.getString(R.string.text_coordinator), ContextCompat.getDrawable(context, R.mipmap.icon_12)));
        funBeans.add(new FunBean(context.getString(R.string.text_tab), ContextCompat.getDrawable(context, R.mipmap.icon_13)));
        funBeans.add(new FunBean(context.getString(R.string.text_tab_two), ContextCompat.getDrawable(context, R.mipmap.icon_14)));
        funBeans.add(new FunBean(context.getString(R.string.text_web), ContextCompat.getDrawable(context, R.mipmap.icon_15)));
        funBeans.add(new FunBean(context.getString(R.string.text_action_bar), ContextCompat.getDrawable(context, R.mipmap.icon_16)));
        funBeans.add(new FunBean(context.getString(R.string.text_flyme), ContextCompat.getDrawable(context, R.mipmap.icon_17)));
        funBeans.add(new FunBean(context.getString(R.string.text_over), ContextCompat.getDrawable(context, R.mipmap.icon_18)));
        funBeans.add(new FunBean(context.getString(R.string.text_key_board), ContextCompat.getDrawable(context, R.mipmap.icon_19)));
        funBeans.add(new FunBean(context.getString(R.string.text_all_edit), ContextCompat.getDrawable(context, R.mipmap.icon_20)));
        funBeans.add(new FunBean(context.getString(R.string.text_login), ContextCompat.getDrawable(context, R.mipmap.icon_21)));
        funBeans.add(new FunBean(context.getString(R.string.text_white_bar), ContextCompat.getDrawable(context, R.mipmap.icon_22)));
        funBeans.add(new FunBean(context.getString(R.string.text_auto_status_font), ContextCompat.getDrawable(context, R.mipmap.icon_23)));
        funBeans.add(new FunBean(context.getString(R.string.text_status_hide), ContextCompat.getDrawable(context, R.mipmap.icon_24)));
        funBeans.add(new FunBean(context.getString(R.string.text_navigation_hide), ContextCompat.getDrawable(context, R.mipmap.icon_25)));
        funBeans.add(new FunBean(context.getString(R.string.text_bar_hide), ContextCompat.getDrawable(context, R.mipmap.icon_26)));
        funBeans.add(new FunBean(context.getString(R.string.text_bar_show), ContextCompat.getDrawable(context, R.mipmap.icon_27)));
        funBeans.add(new FunBean(context.getString(R.string.text_full), ContextCompat.getDrawable(context, R.mipmap.icon_28)));
        funBeans.add(new FunBean(context.getString(R.string.text_bar_font_dark), ContextCompat.getDrawable(context, R.mipmap.icon_29)));
        funBeans.add(new FunBean(context.getString(R.string.text_bar_font_light), ContextCompat.getDrawable(context, R.mipmap.icon_30)));
        return funBeans;
    }
}
