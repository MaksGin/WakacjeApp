package com.wakacjeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.wakacjeapp.databinding.ActivityLoginBinding
import com.wakacjeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var userEmail: String? = null
    private var binding : ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        userEmail = intent.getStringExtra(USER_EMAIL)

        binding?.tvuserID?.setText(userEmail).toString()


        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")
    }
}