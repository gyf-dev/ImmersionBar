package com.gyf.immersionbar.sample.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;

/**
 * Demo 中统一的 Toast 管理类。
 * 弹出新 Toast 前会立即取消上一个 Toast，避免消息排队显示。
 */
public final class ToastUtils {

    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    private static Toast sToast;

    private ToastUtils() {
    }

    public static void show(@NonNull Context context, @NonNull CharSequence text) {
        Context applicationContext = context.getApplicationContext();
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showInternal(applicationContext, text);
        } else {
            MAIN_HANDLER.post(() -> showInternal(applicationContext, text));
        }
    }

    private static void showInternal(Context context, CharSequence text) {
        if (sToast != null) {
            sToast.cancel();
        }
        sToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        sToast.show();
    }
}
