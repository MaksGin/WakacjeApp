package com.wakacjeapp.trip

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.wakacjeapp.R
import com.wakacjeapp.databinding.ActivityTripDetailsBinding
import com.wakacjeapp.trip.adapter.TripDetailsAdapter
import com.wakacjeapp.trip.model.TripDay

class TripDetailsActivity : AppCompatActivity() {

    var binding: ActivityTripDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_trip_details)

        val daysList: ArrayList<TripDay> = ArrayList()
        daysList.add(TripDay(1, "Jakieś krótkie zwiedzanie"))
        daysList.add(TripDay(2, "Coś tam innego") )
        daysList.add(TripDay(3, "Jeszcze jakieś tam rzeczy"))


        binding = ActivityTripDetailsBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        setContentView(binding?.root)
        //tu pobrana lista z bazy
        val adapter = TripDetailsAdapter(daysList)
        binding?.recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView?.adapter = adapter

        //przycisk do zapisywania na wycieczkę
//        binding?.buttonSingUp?.setOnClickListener {
//            //val intent = Intent()
//            //startActivity(intent)
//        }


    }
}