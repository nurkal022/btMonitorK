package com.example.btmonitork

import ReceiveThread
import android.bluetooth.BluetoothAdapter
import android.util.Log
import android.widget.Toast
import java.lang.Exception

class BtConnection(private val adapter: BluetoothAdapter,private val listener: ReceiveThread.Listener,private val activity: ControlActivity) {
    lateinit var cThread: ConnectThread
    fun connect(mac: String) {
        if (adapter.isEnabled && mac.isNotEmpty()) {

            val device = adapter.getRemoteDevice(mac)
            device.let {
                cThread = ConnectThread(it,listener, activity)
                cThread.start()

            }
        }
    }

    fun sendMessage(message: String){
        try {
            cThread.rThread.sendMessage(message.toByteArray())
        }catch (e:Exception){
            Toast.makeText(activity,"Device not selected", Toast.LENGTH_LONG)

        }
    }




}



