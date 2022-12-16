package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var mail:EditText
    private lateinit var password:EditText
    private lateinit var loginButton:Button
    private lateinit var registerButton:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mail = findViewById(R.id.loginmail)
        password = findViewById(R.id.loginpass)
        loginButton = findViewById(R.id.login)
        registerButton = findViewById(R.id.register)


        registerButton.setOnClickListener {
            startActivity(Intent(this,Register::class.java))
        }

        loginButton.setOnClickListener {
            if(password.text.isEmpty()||mail.text.isEmpty()){
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }else {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(mail.text.toString(), password.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            mail.setText("")
                            password.setText("")
                            var intent = Intent(this, Profile::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
            }
        }

    }
}