package com.wakacjeapp.trip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wakacjeapp.R
import com.wakacjeapp.databinding.ActivityTripDetailsDayBinding
import com.wakacjeapp.trip.adapter.TripDetailsDayAdapter
import com.wakacjeapp.trip.model.TripDayItem

class TripDetailsDayActivity : AppCompatActivity() {

    var binding: ActivityTripDetailsDayBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_trip_details_day)

        binding = ActivityTripDetailsDayBinding.inflate(layoutInflater);
        setContentView(binding?.root)
        val activitiesList = listOf<TripDayItem>()
        val adapter = TripDetailsDayAdapter(activitiesList as ArrayList<TripDayItem>)
        binding?.recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView?.adapter = adapter
    }
}