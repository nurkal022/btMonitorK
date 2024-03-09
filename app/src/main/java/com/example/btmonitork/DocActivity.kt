package com.example.btmonitork

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.FileInputStream

class DocActivity : AppCompatActivity() {

    private lateinit var ivProfile: ImageView
    private lateinit var etProfileName: EditText
    private lateinit var fI: EditText
    private lateinit var fH: EditText
    private lateinit var fW: EditText
    private lateinit var fB: EditText

    private val imageRequestCode = 1
    private val fileName = "profileImage.png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc)

        ivProfile = findViewById(R.id.ivProfile)
        etProfileName = findViewById(R.id.fN)
        fI = findViewById(R.id.fI)
        fH = findViewById(R.id.fH)
        fW = findViewById(R.id.fW)
        fB = findViewById(R.id.fB)

        ivProfile.setOnClickListener {selectImage() }


        loadFormData()
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, imageRequestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == imageRequestCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imageRequestCode) {
            val imageUri = data?.data
            val imageStream = imageUri?.let { contentResolver.openInputStream(it) }
            val selectedImage = BitmapFactory.decodeStream(imageStream)

            val desiredWidth = 500
            val desiredHeight = 500

            val scaledImage = Bitmap.createScaledBitmap(selectedImage, desiredWidth, desiredHeight, true)

            ivProfile.setImageBitmap(scaledImage)
            saveImage(scaledImage)
        }
    }

    private fun saveImage(bitmap: Bitmap) {
        openFileOutput(fileName, MODE_PRIVATE).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }
    }

    private fun loadImage() {
        try {
            val fis: FileInputStream = openFileInput(fileName)
            val bitmap = BitmapFactory.decodeStream(fis)
            ivProfile.setImageBitmap(bitmap)
            fis.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveFormData() {
        val sharedPref = getSharedPreferences("ProfileData", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("etProfileName", etProfileName.text.toString())
            putString("fI", fI.text.toString())
            putString("fH", fH.text.toString())
            putString("fW", fW.text.toString())
            putString("fB", fB.text.toString())
            apply()
        }
    }

    private fun loadFormData() {
        val sharedPref = getSharedPreferences("ProfileData", MODE_PRIVATE)
        etProfileName.setText(sharedPref.getString("etProfileName", ""))
        fI.setText(sharedPref.getString("fI", ""))
        fH.setText(sharedPref.getString("fH", ""))
        fW.setText(sharedPref.getString("fW", ""))
        fB.setText(sharedPref.getString("fB", ""))
        loadImage()
    }

    override fun onPause() {
        super.onPause()
        saveFormData()
    }
}
