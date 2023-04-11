package com.wakacjeapp.client_interface

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wakacjeapp.R
import com.wakacjeapp.adapter.MainMenuAdapter
import com.wakacjeapp.databinding.ActivityMainClientMenuBinding
import com.wakacjeapp.model.*


class MainClientMenu : AppCompatActivity() {

    private lateinit var main_binding: ActivityMainClientMenuBinding
    private lateinit var mList: ArrayList<DataItem>

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main_binding = ActivityMainClientMenuBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        setContentView(main_binding.root)

        //testowy kom ale z pushem do doskonalenia

        main_binding.mainMenuRecyclerView.setHasFixedSize(true)
        main_binding.mainMenuRecyclerView.layoutManager = LinearLayoutManager(this)

        mList = ArrayList()
        prepareData()

        main_binding.menuAllButton.setOnClickListener{
            Toast.makeText(this@MainClientMenu, "Wszystkie aplikacje", Toast.LENGTH_SHORT).show()
        }

        main_binding.ShowAccountButton.setOnClickListener{
            Toast.makeText(this@MainClientMenu, "Konto użytkownika", Toast.LENGTH_SHORT).show()
        }

    }

    fun getYourTrips(){
        val tripsRef = FirebaseDatabase.getInstance().reference.child("trips") //odszukanie wycieczek

        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tripsList: MutableList<Trip> = ArrayList()
                for (ds in dataSnapshot.children) {
                    val offer = ds.child("offer").getValue(String::class.java)
                    val trip = Trip(R.drawable.dominikana,offer)

                    tripsList.add(trip)
                }
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, tripsList))
                Log.e("holiday", "---------------------------------")
                Log.e("holiday", "mlista $mList")

                val adapter = MainMenuAdapter(mList)
                main_binding.mainMenuRecyclerView.adapter = adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Error", "onCancelled", databaseError.toException())
            }
        }
        tripsRef.addValueEventListener(valueEventListener)
    }

    fun getTripsList() {
        val tripsRef = FirebaseDatabase.getInstance().reference.child("trips") //odszukanie wycieczek

        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tripsList: MutableList<Trip> = ArrayList()
                for (ds in dataSnapshot.children) {
                    val offer = ds.child("offer").getValue(String::class.java)
                    val image = ds.child("image").getValue(Int::class.java)!!
                    val trip = Trip(R.drawable.dominikana,offer)
                    mList.add(DataItem(DataItemType.YOUR_HOLIDAY, tripsList))
                    tripsList.add(trip)
                }

                Log.e("holiday", "---------------------------------")
                Log.e("holiday", "mlista $mList")

                val adapter = MainMenuAdapter(mList)
                main_binding.mainMenuRecyclerView.adapter = adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Error", "onCancelled", databaseError.toException())
            }
        }
        tripsRef.addValueEventListener(valueEventListener)
    }



            private fun prepareData() {


        val clothingList = ArrayList<Trip>()
        clothingList.add(Trip(R.drawable.dominikana, "Up to 25% off"))
        clothingList.add(Trip(R.drawable.dominikana, "Up to 25% off"))
        clothingList.add(Trip(R.drawable.dominikana, "Up to 25% off"))

          mList.add(DataItem(DataItemType.TEXT, TitleText("Test",20)))
                mList.add(DataItem(DataItemType.SEARCH, Search("Szukaj")))
                mList.add(DataItem(DataItemType.MENU, Menu(
                    R.drawable.chat_bubble_img,"Chat",
                    R.drawable.chat_bubble_img,"Wycieczki",
                    R.drawable.chat_bubble_img,"Chat",
                    R.drawable.chat_bubble_img,"Chat")))
                mList.add(DataItem(DataItemType.TEXT, TitleText("Moje wycieczki",20)))
                getTripsList()


//          mList.add(DataItem(DataItemType.HOLIDAY, clothingList))
//          mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_png)))
//          mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_png)))
//          mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
//          mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
//          mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
//          mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
//          mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))

    }
}
