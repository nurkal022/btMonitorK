package com.example.btmonitork

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import java.io.IOException
import java.util.*


class ConnectThread(private val device: BluetoothDevice, private val activity: ControlActivity) : Thread() {
    val uuid = "00001101-0000-1000-8000-00805F9B34FB"
    var mSocket: BluetoothSocket? = null
    private val TAG = "BluetoothConnection"

    init {
        try {
            /////

//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
                }
            }


        }catch (i: IOException){

        }
    }

    private fun checkSelfPermission(bluetoothConnect: String): Int {
        return 1
    }


    override fun run() {
        try {
            Log.d("MyLog","Connecting...")
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            )
            mSocket?.connect()
            Log.d("MyLog","Connected")
        }catch (i: IOException){
            Log.d("MyLog","Can not connect to device")
            closeConnection()
        }
    }

    fun closeConnection(){
        try {
            mSocket?.close()
        }catch (i: IOException){

        }
    }
}