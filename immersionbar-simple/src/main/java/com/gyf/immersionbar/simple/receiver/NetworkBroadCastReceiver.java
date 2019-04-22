package com.gyf.immersionbar.simple.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.gyf.immersionbar.simple.event.NetworkEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * @author geyifeng
 * @date 2019-04-22 13:50
 */
public class NetworkBroadCastReceiver extends BroadcastReceiver {

    private NetworkEvent mNetworkEvent;

    public void setNetworkEvent(NetworkEvent networkEvent) {
        this.mNetworkEvent = networkEvent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            if (mNetworkEvent == null) {
                mNetworkEvent = new NetworkEvent();
            }
            mNetworkEvent.setAvailable(true);
            EventBus.getDefault().post(mNetworkEvent);
        }
    }
}
