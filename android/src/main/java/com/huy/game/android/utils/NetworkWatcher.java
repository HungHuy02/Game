package com.huy.game.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;


public final class NetworkWatcher extends LiveData<Boolean> {
    private final ConnectivityManager connectivityManager;
    private final ConnectivityManager.NetworkCallback networkCallback;
    private final NetworkRequest networkRequest;

    public NetworkWatcher(@NonNull Context context){
        this.connectivityManager = context.getSystemService(ConnectivityManager.class);

        this.networkCallback = new ConnectivityManager.NetworkCallback(){
            @Override
            public void onLost(@NonNull Network network) {
                setNewValue(false);
            }

            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                setNewValue(
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                );
            }
        };
        this.networkRequest = new NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build();

        if (!isNetworkAvailable()) {
            setNewValue(false);
        }
    }

    private void setNewValue(Boolean isConnected) {
        if (getValue() != isConnected) {
            NetworkWatcher.super.postValue(isConnected);
        }
    }

    @Override
    protected void onActive() {
        this.connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    @Override
    protected void onInactive() {
        this.connectivityManager.unregisterNetworkCallback(this.networkCallback);
    }

    private boolean isNetworkAvailable() {
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities capabilities =
                    connectivityManager.getNetworkCapabilities(network);
                if (capabilities != null) {
                    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
                }
            }
        }
        return false;
    }
}





