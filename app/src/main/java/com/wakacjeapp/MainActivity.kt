package com.wakacjeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wakacjeapp.client_interface.MainClientMenu
import com.wakacjeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMainmenuIntent.setOnClickListener{
            startActivity(Intent(this,MainClientMenu::class.java));
        }
    }
}