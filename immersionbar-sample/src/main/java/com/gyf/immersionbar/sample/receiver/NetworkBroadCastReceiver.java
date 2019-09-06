package com.gyf.immersionbar.sample.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.gyf.immersionbar.sample.event.NetworkEvent;
import com.gyf.immersionbar.sample.utils.Utils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author geyifeng
 * @date 2019-04-22 13:50
 */
public class NetworkBroadCastReceiver extends BroadcastReceiver {

    private NetworkEvent mNetworkEvent;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            getNetworkEvent().setAvailable(Utils.isNetworkConnected(context));
            EventBus.getDefault().post(mNetworkEvent);
        }
    }

    private NetworkEvent getNetworkEvent() {
        if (mNetworkEvent == null) {
            mNetworkEvent = new NetworkEvent();
        }
        return mNetworkEvent;
    }
}
