package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Register : AppCompatActivity() {
    private lateinit var regmail: EditText
    private lateinit var regpass: EditText
    private lateinit var phone: EditText
    private lateinit var username: EditText
    private lateinit var signupbutton: Button
    private lateinit var database: DatabaseReference
    private lateinit var link: ImageView
    private var REQUEST_CODE = 1
    private lateinit var storageReference: StorageReference
    private lateinit var imageuri:Uri






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        regmail = findViewById(R.id.registermail)
        regpass = findViewById(R.id.registerpassword)
        phone = findViewById(R.id.phone)
        username = findViewById(R.id.username)
        signupbutton = findViewById(R.id.signupbutton)
        link = findViewById(R.id.url)

        link.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }
        imageuri = Uri.parse("android.resource://$packageName/${R.drawable.ic_launcher_foreground}")

        signupbutton.setOnClickListener {
            val mail = regmail.text.toString()
            val pass = regpass.text.toString()
            val name = username.text.toString()
            val number = phone.text.toString()


            if (name.isEmpty() || pass.isEmpty() || number.isEmpty() || mail.isEmpty()) {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    regmail.text.toString(),
                    regpass.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            database = FirebaseDatabase.getInstance().getReference("Users")
                            val User = User(name, mail, number, pass)
                            database.child(Firebase.auth.currentUser?.uid.toString()).setValue(User)
                                .addOnSuccessListener {
                                    uploadprofilepic()
                                    FirebaseAuth.getInstance().signOut()
                                }
                        }
                    }
            }

        }


    }

    private fun uploadprofilepic() {

        storageReference = FirebaseStorage.getInstance().getReference("Users/"+Firebase.auth.currentUser?.uid)
        storageReference.putFile(imageuri).addOnSuccessListener {
            Toast.makeText(this, "image uploaded", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))

        }.addOnFailureListener {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data


            link.setImageURI(selectedImageUri)
            if (selectedImageUri != null) {
                imageuri = selectedImageUri

            }
        }
    }
}