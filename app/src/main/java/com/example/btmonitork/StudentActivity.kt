package com.example.btmonitork

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class StudentActivity : AppCompatActivity(), DocumentsAdapter.OnItemLongClickListener {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var documentsRecyclerView: RecyclerView
    lateinit var addButton: Button
    lateinit var idText: EditText
    lateinit var nameText: EditText
    lateinit var dataText: EditText
    lateinit var adapter: DocumentsAdapter
    lateinit var data: ArrayList<Document>
    lateinit var bClear:Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

        documentsRecyclerView = findViewById(R.id.documentsRecyclerView)
        addButton = findViewById(R.id.addbutton)
        idText = findViewById(R.id.idText)
        nameText = findViewById(R.id.nameText)
        dataText = findViewById(R.id.dataText)
        bClear = findViewById(R.id.clearbutton)
       // saveUsername("docLen","0")

        initRecyclerView()
        loadData()

        bClear.setOnClickListener {
            clearHistory()
        }

        addButton.setOnClickListener {
            val id = idText.text.toString()
            val name = nameText.text.toString()
            val dataa = dataText.text.toString()
            saveDocument(id, name, dataa)
            val newData = ArrayList<Document>()
            for (i in 1..3) {
                newData.add(Document("новое название $i", "новый Uri", "новый тип"))
            }
        }


    }


    private fun clearHistory() {
        sharedPreferences.edit().clear().apply()
        data.clear()
        val adapter=DocumentsAdapter(data,this)
        documentsRecyclerView.adapter=adapter
    }


    private fun loadData() {
        val docLen = sharedPreferences.getString("docLen", "0")?.toInt() ?: 0
        data = ArrayList()
        for (i in 1..docLen) {
            val id = sharedPreferences.getString("id_$i", "") ?: ""
            val name = sharedPreferences.getString("name_$i", "") ?: ""
            val dataa = sharedPreferences.getString("data_$i", "") ?: ""
            data.add(Document(id, name, dataa))
        }
        val adapter=DocumentsAdapter(data,this)
        documentsRecyclerView.adapter=adapter
    }


    private fun saveDocument(id: String, name: String, dataa: String) {
        val docLen = sharedPreferences.getString("docLen", "0")?.toInt() ?: 0
        val newDocLen = docLen + 1
        sharedPreferences.edit().apply {
            putString("id_$newDocLen", id)
            putString("name_$newDocLen", name)
            putString("data_$newDocLen", dataa)
            putString("docLen", newDocLen.toString())
            apply()
        }
        // Очищаем список и загружаем данные заново
        data.clear()
        loadData()
        // Уведомляем адаптер об изменениях
        val adapter=DocumentsAdapter(data,this)
        documentsRecyclerView.adapter=adapter    }



    private fun initRecyclerView() {
        documentsRecyclerView.layoutManager = LinearLayoutManager(this)
        data = ArrayList()
        adapter = DocumentsAdapter(data,this)
        documentsRecyclerView.adapter = adapter
    }

    private fun updateDataInRecyclerView(newData: List<Document>) {
        // Предполагается, что у вас есть доступ к списку данных и адаптеру
        data.clear()
        data.addAll(newData)

    }

    override fun onItemLongClicked(position: Int) {
        // Удаление элемента из данных
        data.removeAt(position)
        // Уведомление адаптера об изменении данных
        adapter.notifyItemRemoved(position)
    }


}