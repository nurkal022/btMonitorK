package com.example.btmonitork

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.btmonitork.databinding.ActivityMainBinding

class BtListActivity : AppCompatActivity(),RcAdapter.Listener {
    private var btAdapter: BluetoothAdapter? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RcAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = btManager.adapter
        adapter= RcAdapter(this)
        binding.rcView.layoutManager=LinearLayoutManager(this)
        binding.rcView.adapter=adapter
        getPairedDevises()
    }

    private fun  getPairedDevises(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 2)
                return
            }
        }

        val pairedDevices:Set<BluetoothDevice>?=btAdapter?.bondedDevices
        val tempList=ArrayList<ListItem>()
        pairedDevices?.forEach{
            tempList.add(ListItem(it.name,it.address))
          // Log.d("MyLog","Name:${it.name}")
        }
        adapter.submitList(tempList)
    }

    companion object{
        const val DEVICE_KEY="device_key"
    }

    override fun OnClick(item: ListItem) {
        val i=Intent().apply {
            putExtra(DEVICE_KEY,item)
        }
        setResult(RESULT_OK,i)
        finish()
    }

}