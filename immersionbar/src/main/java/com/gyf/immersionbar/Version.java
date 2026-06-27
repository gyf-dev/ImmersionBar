package com.gyf.immersionbar;

import android.os.Build;

/**
 * 统一管理项目中用到的Android版本号常量。
 * 除{@link #BAKLAVA}（API 36，编译SDK为35时平台常量尚不存在）外，均委托{@link Build.VERSION_CODES}。
 *
 * @author geyifeng
 */
class Version {

    // Android 4.0  API 14
    static final int ICE_CREAM_SANDWICH = Build.VERSION_CODES.ICE_CREAM_SANDWICH;

    // Android 4.1  API 16
    static final int JELLY_BEAN = Build.VERSION_CODES.JELLY_BEAN;

    // Android 4.2  API 17
    static final int JELLY_BEAN_MR1 = Build.VERSION_CODES.JELLY_BEAN_MR1;

    // Android 4.4  API 19
    static final int KITKAT = Build.VERSION_CODES.KITKAT;

    // Android 5.0  API 21
    static final int LOLLIPOP = Build.VERSION_CODES.LOLLIPOP;

    // Android 5.1  API 22
    static final int LOLLIPOP_MR1 = Build.VERSION_CODES.LOLLIPOP_MR1;

    // Android 6.0  API 23
    static final int M = Build.VERSION_CODES.M;

    // Android 8.0  API 26
    static final int O = Build.VERSION_CODES.O;

    // Android 9    API 28
    static final int P = Build.VERSION_CODES.P;

    // Android 10   API 29
    static final int Q = Build.VERSION_CODES.Q;

    // Android 11   API 30
    static final int R = Build.VERSION_CODES.R;

    // Android 15   API 35
    static final int VANILLA_ICE_CREAM = Build.VERSION_CODES.VANILLA_ICE_CREAM;

    // Android 16   API 36
    static final int BAKLAVA = Build.VERSION_CODES.BAKLAVA;

}
