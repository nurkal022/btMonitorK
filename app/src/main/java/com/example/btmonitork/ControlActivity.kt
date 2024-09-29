package com.example.btmonitork

import ReceiveThread
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.btmonitork.databinding.ActivityControlBinding
import com.example.btmonitork.ml.AnnModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.util.Calendar
import java.util.Date
import kotlin.random.Random


class ControlActivity : AppCompatActivity(),ReceiveThread.Listener {
    private lateinit var binding: ActivityControlBinding
    private lateinit var actListLauncher: ActivityResultLauncher<Intent>
    lateinit var btConnection: BtConnection
    private var listItem:ListItem?=null
    val db=DbHelper(this,null)

    val dataSet = LineDataSet(ArrayList<Entry>(), "Real-Time Data")
    lateinit var lineChart: LineChart

    private var messageBuffer = ""
    private var isCollectingNumber = false
    private var currentNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBtListResult()
        init()
        binding.apply {

//            tvMessage.setOnClickListener {
//                val intent =Intent(it.context,MainActivity5::class.java)
//                //intent.putExtra("key", "value")
//                startActivity(intent)
//            }

            bPredict.setOnClickListener{
                //btConnection.sendMessage("B")
                predict()
            }

            bScan.setOnClickListener{
                //addNewDataToChart((Math.random() * 100).toFloat())
                btConnection.sendMessage("A")
                Log.d("MyLog:","Button B passed")
                //chart.clear()
            }

            bSave.setOnClickListener { addDataToDataBase() }

            bClear.setOnClickListener { db.clearTable() }

            bHistory.setOnClickListener {
                val intent =Intent(it.context,HistoryActivity::class.java)
                //intent.putExtra("key", "value")
                startActivity(intent)
            }
        }
        lineChart = findViewById(R.id.chart)
        init_Chart()
    }


    private fun init(){
        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = btManager.adapter
        btConnection= BtConnection(btAdapter,this,this)
    }

    fun init_Chart(){
        val entries = ArrayList<Entry>()
        for (i in 0..186){
            entries.add(Entry(i.toFloat(), Random.nextFloat()*100))
        }
        Log.v("MyLog:",entries.size.toString())
        Log.v("MyLog:",entries.toString())
        dataSet.values = entries

        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.invalidate()
        //ShowLastData()
    }



    private fun addNewDataToChart(y:Float) {
        val maxDataPoints = 187 // Change this to your desired limit
        val x = dataSet.entryCount.toFloat()
        //val y = (Math.random() * 100).toFloat() // Generate a random number between 0 and 100
        val newEntry = Entry(x, y)

        // Add the new data point
        dataSet.addEntry(newEntry)

        if (dataSet.entryCount > maxDataPoints) {
            dataSet.removeFirst()
            for (entry in dataSet.values) {
                entry.x = entry.x - 1f
            }
        }

        // Notify the chart that the data has changed and redraw the chart
        lineChart.data.notifyDataChanged()
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.control_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.id_list){
            actListLauncher.launch(Intent(this, BtListActivity::class.java))
        } else if(item.itemId == R.id.id_connect){
            listItem?.let {
                try {
                    Log.d("MyLog","Name:"+(it.mac).toString())
                    btConnection.connect(it.mac!!)
                    // Connection was successful
                } catch (e: Exception) {
                   Toast.makeText(this,"Device not selected",Toast.LENGTH_LONG)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onBtListResult(){
        actListLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK){
               // Log.d("MyLog","Name: ${(it.data?.getStringExtra(BtListActivity.DEVICE_KEY) as ListItem).name}")
                listItem=it.data?.getSerializableExtra(BtListActivity.DEVICE_KEY) as ListItem
                //Log.d("MyLog","Name:"+(listItem!!.name).toString())
            }
        }
    }

    override fun onRecevie(message: String) {
        runOnUiThread {
            Log.d("MyLog:", "Received data: $message")

            // Проверка на конкретные системные сообщения
            if (message.contains("Connected HC_WORK") || message.contains("Connecting...")) {
                processSystemMessage(message)
                return@runOnUiThread
            }

            messageBuffer += message

            while (messageBuffer.isNotEmpty()) {
                when {
                    messageBuffer.startsWith("***") -> {
                        if (isCollectingNumber) {
                            processNumber(currentNumber)
                            isCollectingNumber = false
                            currentNumber = ""
                        }
                        messageBuffer = messageBuffer.substring(3)
                        isCollectingNumber = true
                    }
                    isCollectingNumber -> {
                        val numberEnd = messageBuffer.indexOfFirst { !it.isDigit() && it != '.' }
                        if (numberEnd == -1) {
                            currentNumber += messageBuffer
                            messageBuffer = ""
                        } else {
                            currentNumber += messageBuffer.substring(0, numberEnd)
                            processNumber(currentNumber)
                            isCollectingNumber = false
                            currentNumber = ""
                            messageBuffer = messageBuffer.substring(numberEnd)
                        }
                    }
                    else -> {
                        val nextStart = messageBuffer.indexOf("***")
                        if (nextStart == -1) {
                            // Проверяем, не содержит ли оставшийся буфер важные системные сообщения
                            if (messageBuffer.contains("Connected HC_WORK") || messageBuffer.contains("Connecting...")) {
                                processSystemMessage(messageBuffer)
                            }
                            messageBuffer = ""
                        } else {
                            messageBuffer = messageBuffer.substring(nextStart)
                        }
                    }
                }
            }
        }
    }

    private fun processNumber(number: String) {
        Log.v("MyLog:", "Processing number: $number")
        try {
            val value = number.toFloatOrNull()
            if (value != null) {
                addNewDataToChart(value)
                binding.tvMessage.text = value.toString()
            } else {
                Log.e("MyLog:", "Invalid number format: $number")
            }
        } catch (e: Exception) {
            Log.e("MyLog:", "Error processing number", e)
        }
    }

    private fun processSystemMessage(message: String) {
        Log.v("MyLog:", "Processing system message: $message")
        when {
            message.contains("Connected HC_WORK") -> binding.tvMessage.text = "Connected HC_WORK"
            message.contains("Connecting...") -> binding.tvMessage.text = "Connecting..."
            else -> Log.d("MyLog:", "Unhandled system message: $message")
        }
    }

    private fun addDataToDataBase(){
        var data=""
        for (i in 0..dataSet.entryCount-1){
            var h=dataSet.getEntryForIndex(i).toString()
            //Log.v("MyLog:",h)
            data+=h.subSequence(7,h.length).toString()+";"
        }

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Months are zero-based
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val currentTime = "$year-$month-$day"
        Random.nextInt(0,2)
        db.addData(data,currentTime,Random.nextInt(0,2).toString())
        //Log.v("MyLog_database:",""+db.getData())
    }

    private fun predict(){
        val model = AnnModel.newInstance(this)

// Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 187), DataType.FLOAT32)
        val arraySize = 187
        val minValue = 0
        val maxValue = 100

        val random = Random.Default
        val randomArray = IntArray(arraySize) { random.nextInt(minValue, maxValue + 1) }

        inputFeature0.loadArray(randomArray)

// Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        val outputValues = outputFeature0.floatArray
        val lll: List<String> = listOf("Бәрі дұрыс", "Барикардия","Тахикардия")

// You can then access the prediction values
        for (i in outputValues.indices) {
            var predictionValue = outputValues[i]
            // Do something with the predictionValue

            Log.v("MyLog:prediction", "Prediction $i: $predictionValue")
            if(predictionValue.toInt()==1){
                if(i==0){
                    binding.tvMessage.setTextColor(Color.parseColor("#00C322"))
                }
                else if(i==1){
                    binding.tvMessage.setTextColor(Color.parseColor("#FF1300"))
                }
                binding.tvMessage.text=lll.get(i)
            }

        }

        model.close()
    }
}