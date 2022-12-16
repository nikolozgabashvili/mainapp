package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class Profile : AppCompatActivity() {
    private lateinit var info:TextView
    private lateinit var image: ImageView
    private lateinit var gmailinfo:TextView
    private lateinit var welcome:TextView
    private lateinit var database:DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)
        image = findViewById(R.id.imageView)
        info = findViewById(R.id.textView)
        gmailinfo = findViewById(R.id.gmailinfo)
        welcome = findViewById(R.id.welcome)
        button  = findViewById(R.id.button)

        val user = Firebase.auth.currentUser
        val name = user?.uid.toString()

        storageReference = FirebaseStorage.getInstance().getReference("Users")
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(name).get().addOnSuccessListener {
            if (it.exists()){
                val number = it.child("number").value.toString()
                info.text = number

            }else{
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }
        }
        database.child(name).get().addOnSuccessListener {
            if (it.exists()){
                val number = it.child("gmail").value.toString()
                gmailinfo.text = number

            }else{
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }
        }
        database.child(name).get().addOnSuccessListener {
            if (it.exists()){
                val number = it.child("username").value.toString()
                welcome.text = "welcome $number"

            }else{
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }
        }

        storageReference.child(name).downloadUrl.addOnSuccessListener { uri->
            val imageUri = uri
            Glide.with(this)
                .load(imageUri)
                .into(image)

        }.addOnFailureListener {
            image.setImageResource(R.drawable.ic_launcher_foreground)
        }

        button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,MainActivity::class.java))
        }

    }
}