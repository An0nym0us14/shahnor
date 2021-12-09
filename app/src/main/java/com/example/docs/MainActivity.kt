package com.example.docs

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.example.docs.databinding.ActivityMainBinding
import com.google.firebase.storage.FirebaseStorage
import java.net.URI
import java.sql.Types.NULL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var ImageUri : Uri
    lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val button = findViewById<Button>(R.id.button)
        val uplaod =  findViewById<Button>(R.id.button2)
        button.setOnClickListener {
            selectimage()
        }
        uplaod.setOnClickListener {
            uploadimage()
        }

    }

    private fun uploadimage() {
        val progressDialog =  ProgressDialog(this)
        progressDialog.setMessage(" Uploading image")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss" , Locale.getDefault())
        val now = Date()
        val filename =  formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("image/$filename")

        storageReference.putFile(ImageUri).
                addOnSuccessListener {
                    binding.image.setImageURI(null)
                    Toast.makeText(this@MainActivity , "Success",Toast.LENGTH_SHORT).show()
                    if (progressDialog.isShowing)progressDialog.dismiss()
                }.addOnFailureListener {
            if (progressDialog.isShowing)progressDialog.dismiss()
            Toast.makeText(this@MainActivity , "Reupload",Toast.LENGTH_SHORT).show()
        }
    }

    private fun selectimage() {
        val intent = Intent()
        intent.type = "images/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent,100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            ImageUri = data?.data!!
            binding.image.setImageURI(ImageUri)


        }
    }
}