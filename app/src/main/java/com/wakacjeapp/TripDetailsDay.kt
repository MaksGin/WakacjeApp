package com.wakacjeapp

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wakacjeapp.databinding.ActivityTripDetailsDayBinding

class TripDetailsDay : AppCompatActivity() {

    private lateinit var binding: ActivityTripDetailsDayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details_day)
    }
}