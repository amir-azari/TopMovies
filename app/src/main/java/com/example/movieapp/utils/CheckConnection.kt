package com.example.movieapp.utils

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import javax.inject.Inject

class CheckConnection @Inject constructor(private val cm: ConnectivityManager) : LiveData<Boolean>() {

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }
    }

    private val networkRequest = NetworkRequest.Builder().build()

    override fun onActive() {
        super.onActive()
        cm.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        cm.unregisterNetworkCallback(networkCallback)
    }
}
