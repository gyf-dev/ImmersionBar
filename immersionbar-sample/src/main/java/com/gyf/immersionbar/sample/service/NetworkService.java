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

    private static final int JOB_ID = 1;
    private static final Object NETWORK_LOCK = new Object();

    private static boolean sNetworkCallbackRegistered;
    private static NetworkBroadCastReceiver sReceiver;

    private static final ConnectivityManager.NetworkCallback NETWORK_CALLBACK =
            new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    postNetworkEvent(true);
                }

                @Override
                public void onLost(Network network) {
                    super.onLost(network);
                    postNetworkEvent(false);
                }
            };

    public static void enqueueWork(Context context) {
        enqueueWork(context, NetworkService.class, JOB_ID, new Intent());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        registerNetwork(getApplicationContext());
    }

    private static void registerNetwork(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            postNetworkEvent(false);
            return;
        }
        synchronized (NETWORK_LOCK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!sNetworkCallbackRegistered) {
                    connectivityManager.registerDefaultNetworkCallback(NETWORK_CALLBACK);
                    sNetworkCallbackRegistered = true;
                }
            } else if (sReceiver == null) {
                IntentFilter filter = new IntentFilter();
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                sReceiver = new NetworkBroadCastReceiver();
                LocalBroadcastManager.getInstance(context).registerReceiver(sReceiver, filter);
            }
        }
    }

    private static void postNetworkEvent(boolean available) {
        NetworkEvent networkEvent = new NetworkEvent();
        networkEvent.setAvailable(available);
        EventBus.getDefault().post(networkEvent);
    }
}
