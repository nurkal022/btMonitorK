package com.example.btmonitork

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.btmonitork.ml.Model
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import kotlin.random.Random


class HistoryActivity : AppCompatActivity(), HistoryAdapter.Listener {
    val db=DbHelper(this,null)
    lateinit var historyText:TextView
    lateinit var bHistory:Button
    lateinit var historyList:RecyclerView
    lateinit var lineChart: LineChart
    private lateinit var adapterRc:HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        lineChart = findViewById(R.id.chart)
        historyText=findViewById(R.id.historyText)
        bHistory  = findViewById(R.id.bHistory)
        historyList=findViewById(R.id.historyList)
        historyList.layoutManager=LinearLayoutManager(this)
        adapterRc= HistoryAdapter(this)
        historyList.adapter=adapterRc

        bHistory.setOnClickListener {
            getData()
        }
        getData()
        init_Chart("x: 0.0 y: 17.826921;x: 1.0 y: 57.707817;x: 2.0 y: 83.458984;x: 3.0 y: 75.611946;x: 4.0 y: 8.975304;x: 5.0 y: 49.478806;x: 6.0 y: 93.4307;x: 7.0 y: 64.01391;x: 8.0 y: 50.822693;x: 9.0 y: 80.918236;x: 10.0 y: 83.72641;x: 11.0 y: 7.5215044;x: 12.0 y: 56.340088;x: 13.0 y: 61.444344;x: 14.0 y: 17.791008;x: 15.0 y: 92.67718;x: 16.0 y: 74.99317;x: 17.0 y: 15.668774;x: 18.0 y: 44.16306;x: 19.0 y: 12.684226;x: 20.0 y: 77.74162;x: 21.0 y: 89.025986;x: 22.0 y: 12.215626;x: 23.0 y: 81.97086;x: 24.0 y: 47.612984;x: 25.0 y: 52.939995;x: 26.0 y: 78.95546;x: 27.0 y: 64.874146;x: 28.0 y: 50.15741;x: 29.0 y: 61.354065;x: 30.0 y: 82.20981;x: 31.0 y: 83.27294;x: 32.0 y: 86.18684;x: 33.0 y: 78.39497;x: 34.0 y: 36.475216;x: 35.0 y: 44.737553;x: 36.0 y: 48.175697;x: 37.0 y: 4.666358;x: 38.0 y: 71.1736;x: 39.0 y: 14.170986;x: 40.0 y: 51.49008;x: 41.0 y: 80.77852;x: 42.0 y: 20.905119;x: 43.0 y: 67.3883;x: 44.0 y: 46.22239;x: 45.0 y: 77.650375;x: 46.0 y: 13.914609;x: 47.0 y: 89.322365;x: 48.0 y: 27.906132;x: 49.0 y: 88.10157;x: 50.0 y: 10.372538;x: 51.0 y: 57.57672;x: 52.0 y: 12.898522;x: 53.0 y: 90.34016;x: 54.0 y: 64.74582;x: 55.0 y: 24.200832;x: 56.0 y: 72.93904;x: 57.0 y: 46.005917;x: 58.0 y: 26.598162;x: 59.0 y: 35.484486;x: 60.0 y: 93.573906;x: 61.0 y: 41.128807;x: 62.0 y: 28.2722;x: 63.0 y: 17.213457;x: 64.0 y: 32.095493;x: 65.0 y: 53.19272;x: 66.0 y: 14.290607;x: 67.0 y: 77.46727;x: 68.0 y: 65.76147;x: 69.0 y: 89.758644;x: 70.0 y: 50.754673;x: 71.0 y: 12.254029;x: 72.0 y: 92.77227;x: 73.0 y: 59.91152;x: 74.0 y: 69.86513;x: 75.0 y: 32.96169;x: 76.0 y: 43.300705;x: 77.0 y: 8.076745;x: 78.0 y: 87.85061;x: 79.0 y: 5.9658823;x: 80.0 y: 81.351295;x: 81.0 y: 17.244673;x: 82.0 y: 18.53742;x: 83.0 y: 11.292517;x: 84.0 y: 0.0077188015;x: 85.0 y: 14.894372;x: 86.0 y: 50.56484;x: 87.0 y: 91.81939;x: 88.0 y: 91.39602;x: 89.0 y: 37.285526;x: 90.0 y: 76.44098;x: 91.0 y: 0.26049018;x: 92.0 y: 81.2029;x: 93.0 y: 6.4645233;x: 94.0 y: 45.40547;x: 95.0 y: 26.566113;x: 96.0 y: 67.659195;x: 97.0 y: 87.29688;x: 98.0 y: 50.139248;x: 99.0 y: 42.161034;x: 100.0 y: 72.40659;x: 101.0 y: 39.892704;x: 102.0 y: 67.02393;x: 103.0 y: 63.71394;x: 104.0 y: 60.553593;x: 105.0 y: 94.54951;x: 106.0 y: 19.751513;x: 107.0 y: 72.278145;x: 108.0 y: 98.49672;x: 109.0 y: 13.221777;x: 110.0 y: 72.02882;x: 111.0 y: 23.374182;x: 112.0 y: 8.803457;x: 113.0 y: 82.77897;x: 114.0 y: 23.883945;x: 115.0 y: 19.917696;x: 116.0 y: 33.751343;x: 117.0 y: 12.982482;x: 118.0 y: 61.198254;x: 119.0 y: 58.164127;x: 120.0 y: 4.186076;x: 121.0 y: 96.06194;x: 122.0 y: 14.655054;x: 123.0 y: 82.688835;x: 124.0 y: 13.453096;x: 125.0 y: 67.67706;x: 126.0 y: 23.975737;x: 127.0 y: 30.805052;x: 128.0 y: 41.589157;x: 129.0 y: 91.4019;x: 130.0 y: 79.32742;x: 131.0 y: 11.0113735;x: 132.0 y: 51.111187;x: 133.0 y: 53.964172;x: 134.0 y: 53.919357;x: 135.0 y: 40.63818;x: 136.0 y: 60.051083;x: 137.0 y: 97.42698;x: 138.0 y: 31.40509;x: 139.0 y: 81.722176;x: 140.0 y: 81.860725;x: 141.0 y: 51.239014;x: 142.0 y: 26.684767;x: 143.0 y: 23.48779;x: 144.0 y: 36.017654;x: 145.0 y: 43.341328;x: 146.0 y: 58.52891;x: 147.0 y: 97.17249;x: 148.0 y: 72.22802;x: 149.0 y: 29.853512;x: 150.0 y: 43.15708;x: 151.0 y: 2.1328568;x: 152.0 y: 98.74016;x: 153.0 y: 59.634445;x: 154.0 y: 77.09952;x: 155.0 y: 19.353092;x: 156.0 y: 59.204178;x: 157.0 y: 88.39699;x: 158.0 y: 79.507416;x: 159.0 y: 83.10565;x: 160.0 y: 99.55612;x: 161.0 y: 40.041317;x: 162.0 y: 84.28055;x: 163.0 y: 37.353516;x: 164.0 y: 7.4486375;x: 165.0 y: 26.258743;x: 166.0 y: 53.234535;x: 167.0 y: 79.617645;x: 168.0 y: 81.735535;x: 169.0 y: 32.859344;x: 170.0 y: 16.88205;x: 171.0 y: 70.85615;x: 172.0 y: 2.447766;x: 173.0 y: 49.11949;x: 174.0 y: 9.662592;x: 175.0 y: 90.12019;x: 176.0 y: 65.64163;x: 177.0 y: 98.96658;x: 178.0 y: 23.597033;x: 179.0 y: 32.38549;x: 180.0 y: 29.582882;x: 181.0 y: 57.069702;x: 182.0 y: 56.90082;x: 183.0 y: 77.33953;x: 184.0 y: 85.14679;x: 185.0 y: 33.748806;x: 186.0 y: 32.161457;\n")
    }

    fun getData(){
        adapterRc.Clear()
        var res=db.getAllData()
        //Log.v("MyLog:r", res.toString())
        var k=1
        for(i in res){
            var buf=i.split("#$#")
            //Log.v("MyLog:rr", buf.toString())
            var hi=HistoryItem(k.toString(),buf[1],buf[0],buf[2])
            adapterRc.addHistory(hi)
            k++
        }

    }

    fun init_Chart(data:String){
        Log.v("MyLog:++++",data)

        val dataSet = LineDataSet(ArrayList<Entry>(), "Real-Time Data")
        val entries = ArrayList<Entry>()
        for (i in data.split(";").subList(0,187)){
            var x=i.split(" ")
            //Log.v("MyLog:XXX","///***///")
            entries.add(Entry(x[1].toFloat(),x[3].toFloat()))
        }
        //Log.v("MyLog:",entries.size.toString())
        //Log.v("MyLog:+++***",entries.toString())
        dataSet.values = entries

        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.invalidate()
        //ShowLastData()
    }


    override fun OnClick(item: HistoryItem) {
        Log.v("MyLog:++",item.toString())
        init_Chart(item.data)
    }


}