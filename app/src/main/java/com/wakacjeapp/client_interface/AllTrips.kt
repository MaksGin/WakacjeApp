package com.wakacjeapp.client_interface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wakacjeapp.R
import com.wakacjeapp.User
import com.wakacjeapp.adapter.MainMenuAdapter
import com.wakacjeapp.databinding.ActivityAllTripsBinding
import com.wakacjeapp.model.DataItem
import com.wakacjeapp.model.DataItemType
import com.wakacjeapp.model.Menu
import com.wakacjeapp.model.Search
import com.wakacjeapp.model.TitleText
import com.wakacjeapp.trip.model.Trip

class AllTrips : AppCompatActivity() {

    private lateinit var alltrips: ActivityAllTripsBinding
    private lateinit var mList: ArrayList<DataItem>
    private lateinit var adapter: MainMenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alltrips = ActivityAllTripsBinding.inflate(layoutInflater)
        setContentView(alltrips.root)

      alltrips.alltripsRecycleview.setHasFixedSize(true)
        alltrips.alltripsRecycleview.layoutManager = LinearLayoutManager(this)

        mList = ArrayList()
        getTripsList()
        adapter = MainMenuAdapter(mList, this)

        alltrips.bckBtn.setOnClickListener { super.onBackPressed() }

    }

    private fun getTripsList() {
        val tripsRef = FirebaseDatabase.getInstance().reference.child("wycieczki") //odszukanie wycieczek

        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val wszystkie_wycieczki: MutableList<Trip> = ArrayList()

                for (ds in dataSnapshot.children) {
                    val wycieczka = ds.getValue(Trip::class.java)
                    if (wycieczka != null) {
                        wszystkie_wycieczki.add(wycieczka)
                    }
                }
                wszystkie_wycieczki.shuffle()

                wszystkie_wycieczki.forEach { wycieczka ->
                    mList.add(DataItem(DataItemType.YOUR_HOLIDAY, wycieczka))
                }
                alltrips.alltripsRecycleview.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        tripsRef.addValueEventListener(valueEventListener)
    }

}