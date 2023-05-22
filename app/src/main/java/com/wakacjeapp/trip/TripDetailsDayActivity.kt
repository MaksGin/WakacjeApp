package com.wakacjeapp.trip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.wakacjeapp.R
import com.wakacjeapp.databinding.ActivityTripDetailsDayBinding
import com.wakacjeapp.model.ImageMap
import com.wakacjeapp.trip.adapter.TripDetailsDayAdapter
import com.wakacjeapp.trip.model.TripDay
import com.wakacjeapp.trip.model.TripDayItem

class TripDetailsDayActivity : AppCompatActivity() {

    private lateinit var activity_trip_day: ActivityTripDetailsDayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_trip_details_day)
        val szczegoly_wycieczki = intent.getParcelableExtra<TripDay>("trip_day")

        activity_trip_day = ActivityTripDetailsDayBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        setContentView(activity_trip_day.root)

        activity_trip_day.textViewDetails.text = "Szczegółowy plan dnia:"
        activity_trip_day.tvDayNumberTitle.text = "Dzień " + szczegoly_wycieczki?.numer
        activity_trip_day.arrowBack2.setOnClickListener {
            super.onBackPressed();
        }

        if(szczegoly_wycieczki != null) {
            przypiszDane(szczegoly_wycieczki)
        }

//        binding = ActivityTripDetailsDayBinding.inflate(layoutInflater);
//        setContentView(binding?.root)
//        val activitiesList = listOf<TripDayItem>()
//        val adapter = TripDetailsDayAdapter(activitiesList as ArrayList<TripDayItem>)
//        binding?.recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        binding?.recyclerView?.adapter = adapter
    }

    fun przypiszDane(szczegoly_wycieczki: TripDay) {
        //val imageMap = ImageMap().imageMap
        val adapter = TripDetailsDayAdapter(szczegoly_wycieczki.plan_godzinowy, this)
        activity_trip_day.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        activity_trip_day.recyclerView.adapter = adapter

    }
}