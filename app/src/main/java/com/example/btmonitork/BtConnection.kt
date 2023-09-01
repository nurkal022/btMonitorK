package com.example.btmonitork

import android.bluetooth.BluetoothAdapter

class BtConnection(private val adapter: BluetoothAdapter,private val activity: ControlActivity) {
    lateinit var cThread: ConnectThread
    fun connect(mac: String) {
        if (adapter.isEnabled && mac.isNotEmpty()) {
            val device = adapter.getRemoteDevice(mac)
            device.let {
                cThread = ConnectThread(it, activity)
                cThread.start()
            }
        }
    }
}



