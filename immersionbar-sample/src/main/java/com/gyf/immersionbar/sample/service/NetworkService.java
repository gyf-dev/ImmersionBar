package com.gyf.immersionbar.sample.service;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.gyf.immersionbar.sample.event.NetworkEvent;
import com.gyf.immersionbar.sample.receiver.NetworkBroadCastReceiver;

import org.greenrobot.eventbus.EventBus;

/**
 * @author geyifeng
 * @date 2019-04-22 13:34
 */
public class NetworkService extends JobIntentService {

    private final static int JOB_ID = 1;
    private NetworkEvent mNetworkEvent;
    private NetworkBroadCastReceiver mReceiver;

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetwork(this);
    }

    public static void enqueueWork(Context context) {
        enqueueWork(context, NetworkService.class, JOB_ID, new Intent());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        registerNetwork(this);
    }

    private void registerNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                        getNetworkEvent().setAvailable(true);
                        EventBus.getDefault().post(mNetworkEvent);
                    }

                    @Override
                    public void onLost(Network network) {
                        super.onLost(network);
                        getNetworkEvent().setAvailable(false);
                        EventBus.getDefault().post(mNetworkEvent);
                    }
                });
            } else {
                IntentFilter filter = new IntentFilter();
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                mReceiver = new NetworkBroadCastReceiver();
                LocalBroadcastManager.getInstance(context).registerReceiver(mReceiver, filter);
            }
        } else {
            getNetworkEvent().setAvailable(false);
            EventBus.getDefault().post(mNetworkEvent);
        }
    }

    private void unregisterNetwork(Context context) {
        if (mNetworkEvent != null) {
            mNetworkEvent = null;
        }
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mReceiver);
        }
    }

    private NetworkEvent getNetworkEvent() {
        if (mNetworkEvent == null) {
            mNetworkEvent = new NetworkEvent();
        }
        return mNetworkEvent;
    }
}
