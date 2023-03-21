package com.wakacjeapp.client_interface

import android.os.Build
import android.os.Bundle
import android.util.Log
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

    private lateinit var binding: ActivityMainClientMenuBinding
    private lateinit var mList: ArrayList<DataItem>

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainClientMenuBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        setContentView(binding.root)




        binding.mainMenuRecyclerView.setHasFixedSize(true)
        binding.mainMenuRecyclerView.layoutManager = LinearLayoutManager(this)



        mList = ArrayList()

        getTripsList()
        prepareData()


        Log.e("holiday", "============holiday===================")
        Log.e("holiday", "$mList")




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
                    Log.e("holiday", "Offer: $offer   image: $image ")

                    tripsList.add(trip)
                    Log.e("holiday", "---------------------------------")
                    Log.e("holiday", "lista $tripsList")


                }
                mList.add(DataItem(DataItemType.HOLIDAY, tripsList))
                Log.e("holiday", "---------------------------------")
                Log.e("holiday", "mlista $mList")

                val adapter = MainMenuAdapter(mList)
                binding.mainMenuRecyclerView.adapter = adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Error", "onCancelled", databaseError.toException())
            }
        }
        tripsRef.addValueEventListener(valueEventListener)
    }



            private fun prepareData() {

        // best seller
        val bestSellerList = ArrayList<Trip>()
        bestSellerList.add(Trip(R.drawable.dominikana , "Up to 20% off"))
        bestSellerList.add(Trip(R.drawable.dominikana , "Up to 20% off"))
        bestSellerList.add(Trip(R.drawable.dominikana , "Up to 20% off"))
        bestSellerList.add(Trip(R.drawable.dominikana , "Up to 20% off"))
        bestSellerList.add(Trip(R.drawable.dominikana , "Up to 20% off"))
        bestSellerList.add(Trip(R.drawable.dominikana , "Up to 20% off"))
//
//
//        //clothing
//        val clothingList = ArrayList<RecyclerItem>()
//        clothingList.add(RecyclerItem(R.drawable.dominikana, "Up to 25% off"))
//        clothingList.add(RecyclerItem(R.drawable.dominikana, "Up to 25% off"))
//        clothingList.add(RecyclerItem(R.drawable.dominikana, "Up to 25% off"))
//        clothingList.add(RecyclerItem(R.drawable.dominikana, "Up to 25% off"))
//        clothingList.add(RecyclerItem(R.drawable.dominikana, "Up to 25% off"))
//        clothingList.add(RecyclerItem(R.drawable.dominikana, "Up to 25% off"))
//        clothingList.add(RecyclerItem(R.drawable.dominikana, "Up to 25% off"))
//
//
        mList.add(DataItem(DataItemType.BEST_SELLER, bestSellerList))
//        mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_png)))
//        mList.add(DataItem(DataItemType.HOLIDAY, clothingList))
//        mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_png)))
//        mList.add(DataItem(DataItemType.BEST_SELLER, bestSellerList.asReversed()))
        mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_png)))
    }
}
