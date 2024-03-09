package com.example.btmonitork

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.Calendar

class DbHelper(val context:Context, val factory:SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context,"ecg",factory,1) {


    override fun onCreate(db: SQLiteDatabase?) {
        val query="CREATE TABLE ecg_data (id INT PRIMARY KEY, data TEXT,date TEXT,prediction TEXT)"

            db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS ecg_data")
        db!!.execSQL("DROP TABLE IF EXISTS documents")
        onCreate(db)
    }

    fun addData(data:String,date: String,prediction: String){
        val values=ContentValues()
        values.put("data",data)
        values.put("date",date)
        if (prediction.isEmpty()) {
            values.put("prediction", "0")
        } else {
            values.put("prediction", prediction)
        }
        val db=this.writableDatabase
        db.insert("ecg_data",null,values)
       // Log.v("MyLog:data_add",date+data+prediction)

        db.close()
    }
//
//    fun getData(): Cursor? {
//        val db = this.readableDatabase
//        val res = db.rawQuery("SELECT * FROM ecg_data", null)
//        db.close()
//        return res
//    }

    fun clearTable() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM ecg_data")
    }

    fun getLastData(): String? {
        val db = this.readableDatabase
        var answer: String? = null
        val query = "SELECT data, date FROM ecg_data ORDER BY id DESC LIMIT 1"
        val cursor = db.rawQuery(query, null)
        Log.v("MyLog:data_get",cursor.toString())
        if (cursor.moveToFirst()) {
            val data = cursor.getString(cursor.getColumnIndex("data"))
            val date = cursor.getString(cursor.getColumnIndex("date"))
            val prediction = cursor.getString(cursor.getColumnIndex("prediction"))
            answer = "$data#$#$date#$#$prediction"
        }

        cursor.close()
        db.close()

        return answer
    }




    fun getAllData(): List<String> {
        val db = this.readableDatabase
        val data = mutableListOf<String>()

        val query = "SELECT * FROM ecg_data"
        val cursor = db.rawQuery(query, null)

        // Check if the cursor has any rows
        if (cursor.moveToFirst()) {
            do {
                val rowData =
                    "${cursor.getString(cursor.getColumnIndex("data"))}#$#" +
                            "${cursor.getString(cursor.getColumnIndex("date"))}#$#" +
                            "${cursor.getString(cursor.getColumnIndex("prediction"))}"
                data.add(rowData)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        Log.v("MyLog:Q", data.toString())
        return data
    }
    fun addDocData(name:String,data: String){

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Months are zero-based
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val currentTime = "$year-$month-$day"

        val values=ContentValues()
        values.put("data",data)
        values.put("name",name)
        values.put("date",currentTime)

        val db=this.writableDatabase
        db.insert("documents",null,values)
        Log.v("MyLog:data_add","ggggg")

        db.close()
    }


    fun getAllDocData(): List<String> {
        val db = this.readableDatabase
        val data = mutableListOf<String>()

        val query = "SELECT * FROM documents"
        val cursor = db.rawQuery(query, null)

        // Check if the cursor has any rows
        if (cursor.moveToFirst()) {
            do {
                val rowData =
                    "${cursor.getString(cursor.getColumnIndex("data"))}#$#" +
                            "${cursor.getString(cursor.getColumnIndex("name"))}#$#"+
                            "${cursor.getString(cursor.getColumnIndex("date"))}#$#"
                data.add(rowData)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        Log.v("MyLog:Q", data.toString())
        return data
    }
    }

