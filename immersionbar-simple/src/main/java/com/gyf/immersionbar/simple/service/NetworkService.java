package com.gyf.immersionbar.simple.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.IBinder;

import com.apkfuns.logutils.LogUtils;
import com.gyf.immersionbar.simple.event.NetworkEvent;
import com.gyf.immersionbar.simple.receiver.NetworkBroadCastReceiver;

import org.greenrobot.eventbus.EventBus;

/**
 * @author geyifeng
 * @date 2019-04-22 13:34
 */
public class NetworkService extends Service {

    private NetworkEvent mNetworkEvent = new NetworkEvent();
    private NetworkBroadCastReceiver mReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerNetwork(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterNetwork(this);
    }

    private void registerNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    mNetworkEvent.setAvailable(true);
                    EventBus.getDefault().post(mNetworkEvent);
                }

                @Override
                public void onLost(Network network) {
                    super.onLost(network);
                    mNetworkEvent.setAvailable(false);
                    EventBus.getDefault().post(mNetworkEvent);
                }
            });
        } else {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mReceiver = new NetworkBroadCastReceiver();
            context.registerReceiver(mReceiver, filter);
        }
    }

    private void unRegisterNetwork(Context context) {
        if (mNetworkEvent != null) {
            mNetworkEvent = null;
        }
        if (mReceiver != null) {
            context.unregisterReceiver(mReceiver);
        }
    }
}
