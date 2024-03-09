package com.example.btmonitork

import ReceiveThread
import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import java.io.IOException
import java.lang.Exception
import java.util.*


class ConnectThread(private val device: BluetoothDevice,private val listener: ReceiveThread.Listener, private val activity: ControlActivity) : Thread() {
    val uuid = "00001101-0000-1000-8000-00805F9B34FB"
    var mSocket: BluetoothSocket? = null
    lateinit var rThread: ReceiveThread

    init {
        try {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                {
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 2)
                }
            }
            mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
            Log.d("MyLog:","mSocket"+mSocket.toString())
        }catch (i: IOException){
            Log.d("MyLog","device error")
        }
    }


    override fun run() {
        listener.onRecevie("test")

        try {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                {
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 2)
                    return
                }
            }

            listener.onRecevie("Connecting...")

            mSocket?.connect()

            listener.onRecevie("Connected ${device.name}")

            rThread=ReceiveThread(mSocket!!, listener)
            rThread.start()

        }catch (i: IOException){
            listener.onRecevie("Can not connect to device")
            closeConnection()
        }
    }

    fun closeConnection(){
        try {
            mSocket?.close()
        }catch (i: IOException){
            Log.d("MyLog","Can not Close")

        }
    }
}