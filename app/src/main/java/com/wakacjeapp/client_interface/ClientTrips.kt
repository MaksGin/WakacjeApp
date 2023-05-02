package com.wakacjeapp.client_interface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.wakacjeapp.databinding.ActivityClientTripsBinding
import com.wakacjeapp.model.DataItem
import com.wakacjeapp.model.DataItemType
import com.wakacjeapp.trip.model.Trip

class ClientTrips : AppCompatActivity() {
    private lateinit var clienttrip: ActivityClientTripsBinding
    private lateinit var mList: ArrayList<DataItem>
    private lateinit var adapter: MainMenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clienttrip = ActivityClientTripsBinding.inflate(layoutInflater)
        setContentView(clienttrip.root)

        clienttrip.yourtripsRecycleview.setHasFixedSize(true)
        clienttrip.yourtripsRecycleview.layoutManager = LinearLayoutManager(this)

        mList = ArrayList()
        getTripsList()
        adapter = MainMenuAdapter(mList, this)

        clienttrip.bckBtn.setOnClickListener { super.onBackPressed() }


    }
    private fun sprawdz_usera(list: ArrayList<User>, user: User): Boolean {
        for (uczestnik in list) {
            if (uczestnik.uid == user.uid) {
                return true
            }
        }
        return false
    }

    private fun getTripsList() {
        val tripsRef = FirebaseDatabase.getInstance().reference.child("wycieczki") //odszukanie wycieczek

        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val wszystkie_wycieczki: MutableList<Trip> = ArrayList()
                val szukanyUzytkownik = User(name = FirebaseAuth.getInstance().currentUser?.displayName, email = FirebaseAuth.getInstance().currentUser?.email, uid = FirebaseAuth.getInstance().currentUser?.uid)
                for (ds in dataSnapshot.children) {
                    val wycieczka = ds.getValue(Trip::class.java)
                        if(wycieczka != null && sprawdz_usera(wycieczka.uzytkownicy, szukanyUzytkownik)){
                            wszystkie_wycieczki.add(wycieczka)
                        }

                }
                wszystkie_wycieczki.forEach { wycieczka ->
                    mList.add(DataItem(DataItemType.YOUR_HOLIDAY, wycieczka))
                }
                clienttrip.yourtripsRecycleview.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        tripsRef.addValueEventListener(valueEventListener)
    }
}