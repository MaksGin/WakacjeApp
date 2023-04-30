package com.wakacjeapp.client_interface

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wakacjeapp.Messages.NewMessageActivity
import com.wakacjeapp.R
import com.wakacjeapp.adapter.MainMenuAdapter
import com.wakacjeapp.databinding.ActivityMainClientMenuBinding
import com.wakacjeapp.model.*
import com.wakacjeapp.trip.model.Trip
import com.wakacjeapp.trip.model.TripDay
import java.time.LocalDate
import java.util.Calendar


class MainClientMenu : AppCompatActivity() {

    private lateinit var main_binding: ActivityMainClientMenuBinding
    private lateinit var mList: ArrayList<DataItem>
    private lateinit var adapter: MainMenuAdapter

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main_binding = ActivityMainClientMenuBinding.inflate(layoutInflater)


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar()?.hide();

//        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        setContentView(main_binding.root)

        main_binding.mainMenuRecyclerView.setHasFixedSize(true)
        main_binding.mainMenuRecyclerView.layoutManager = LinearLayoutManager(this)
        val user_mail = intent.getStringExtra("USER_EMAIL")
        mList = ArrayList()
        getTripsList()
        adapter = MainMenuAdapter(mList, this)

        Toast.makeText(this@MainClientMenu, "Konto użytkownika: $user_mail", Toast.LENGTH_SHORT)
            .show()


        main_binding.menuAllButton.setOnClickListener {
            val intent = Intent(this, NewMessageActivity::class.java)
            startActivity(intent)
        }

        main_binding.ShowAccountButton.setOnClickListener {
            Toast.makeText(this@MainClientMenu, "Konto użytkownika", Toast.LENGTH_SHORT).show()
        }


    }


    private fun getTripsList() {
        val tripsRef = FirebaseDatabase.getInstance().reference.child("trips") //odszukanie wycieczek

        // ---------- M E N U -------
        mList.add(DataItem(DataItemType.SEARCH, Search("Szukaj")))
        mList.add(DataItem(DataItemType.MENU, Menu(
            R.drawable.chat_bubble_img,"Chat",
            R.drawable.baseline_map_24,"Wycieczki",
            R.drawable.chat_bubble_img,"Chat",
            R.drawable.chat_bubble_img,"Chat")))
        // -------------------------

        // --- Nagłówek ---
        mList.add(DataItem(DataItemType.TEXT, TitleText("Moje wycieczki",20)))

        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tripsList: MutableList<Trip> = ArrayList()
                val tripDays: ArrayList<TripDay> = ArrayList()
                tripDays.add(TripDay(1,"Szybkie jedzenie i skok na plaże"))
                tripDays.add(TripDay(2,"Szybkie jedzenie i skok na plaże"))
                tripDays.add(TripDay(3,"Szybkie jedzenie i skok na plaże"))
                val currentDate = Calendar.getInstance().time
                for (ds in dataSnapshot.children) {
                    val offer = ds.child("offer").getValue(String::class.java)
                    val image = ds.child("image").getValue(Int::class.java)!!
                    val trip = Trip(currentDate,20.0,currentDate,"Pustynia","Iran",2.3,"Szybki opis",tripDays)
                    tripsList.add(trip)

                }

                tripsList.shuffle()
                mList.add(DataItem(DataItemType.HOLIDAY, tripsList))
                mList.add(DataItem(DataItemType.TEXT, TitleText("Oferty",20)))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.turcja,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_png)))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.iran,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_img2)))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.turcja,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.turcja,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_img3)))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.iran,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))

                main_binding.mainMenuRecyclerView.adapter = adapter
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Error", "onCancelled", databaseError.toException())
            }
        }
        tripsRef.addValueEventListener(valueEventListener)
    }


//    private fun getTripsList() {
//        val tripsRef = FirebaseDatabase.getInstance().reference.child("trips") //odszukanie wycieczek
//
//        // ---------- M E N U -------
//        mList.add(DataItem(DataItemType.SEARCH, Search("Szukaj")))
//        mList.add(DataItem(DataItemType.MENU, Menu(
//            R.drawable.chat_bubble_img,"Chat",
//            R.drawable.baseline_map_24,"Wycieczki",
//            R.drawable.chat_bubble_img,"Chat",
//            R.drawable.chat_bubble_img,"Chat")))
//        // -------------------------
//
//        // --- Nagłówek ---
//        mList.add(DataItem(DataItemType.TEXT, TitleText("Moje wycieczki",20)))
//
//        val valueEventListener: ValueEventListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val tripsList: MutableList<Trip> = ArrayList()
//                val tripDays: ArrayList<TripDay> = ArrayList()
//                tripDays.plus(TripDay(1,"Szybkie jedzenie i skok na plaże"))
//                tripDays.plus(TripDay(2,"Szybkie jedzenie i skok na plaże"))
//                tripDays.plus(TripDay(3,"Szybkie jedzenie i skok na plaże"))
//                val currentDate = Calendar.getInstance().time
//                for (ds in dataSnapshot.children) {
//                    val offer = ds.child("offer").getValue(String::class.java)
//                    val image = ds.child("image").getValue(Int::class.java)!!
//                    val trip = Trip(currentDate,20.0,currentDate,"Pustynia","Iran",2.3,"Szybki opis",tripDays)
//                    tripsList.add(trip)
//
//                }
//
//                tripsList.shuffle()
//                mList.add(DataItem(DataItemType.HOLIDAY, tripsList))
//                mList.add(DataItem(DataItemType.TEXT, TitleText("Oferty",20)))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.turcja,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_png)))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.iran,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_img2)))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.turcja,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.turcja,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_img3)))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.iran,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
//
//                main_binding.mainMenuRecyclerView.adapter = adapter
//            }
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.e("Error", "onCancelled", databaseError.toException())
//            }
//        }
//        tripsRef.addValueEventListener(valueEventListener)
//    }
}
