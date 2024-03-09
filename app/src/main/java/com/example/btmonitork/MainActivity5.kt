package com.example.btmonitork

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button

class MainActivity5 : AppCompatActivity() {
    lateinit var bUniver:Button
    lateinit var bStudent:Button
    lateinit var bHealth:Button
    lateinit var bDrag:Button
    lateinit var bQ:Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)
        bStudent = findViewById(R.id.bStudent)
        bUniver = findViewById(R.id.bUniver)
        bHealth = findViewById(R.id.bHealth)
        bDrag = findViewById(R.id.bDrag)
        bQ = findViewById(R.id.bQ)

        bUniver.setOnClickListener {
            val intent = Intent(it.context,UniverActivity::class.java)
            //intent.putExtra("key", "value")
            startActivity(intent)
        }


        bQ.setOnClickListener {
            val intent = Intent(it.context,QuestionActivity::class.java)
            //intent.putExtra("key", "value")
            startActivity(intent)
        }
        bStudent.setOnClickListener {

            val intent = Intent(it.context,DocActivity::class.java)
            //intent.putExtra("key", "value")
            startActivity(intent)
        }

        bDrag.setOnClickListener {
            val intent = Intent(it.context,DragActivity::class.java)
            //intent.putExtra("key", "value")
            startActivity(intent)
        }

        bHealth.setOnClickListener {
            val intent = Intent(it.context,ControlActivity::class.java)
            //intent.putExtra("key", "value")
            startActivity(intent)
        }
    }
}