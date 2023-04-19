package com.wakacjeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wakacjeapp.databinding.ActivityTripDetailsBinding

class TripDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTripDetailsBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)
    }
}