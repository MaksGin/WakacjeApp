package com.wakacjeapp.client_interface

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.wakacjeapp.databinding.ActivityMainClientMenuBinding

class MainClientMenu : AppCompatActivity() {

    private lateinit var binding: ActivityMainClientMenuBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainClientMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}