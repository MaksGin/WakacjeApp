package com.wakacjeapp.client_interface

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.wakacjeapp.R
import com.wakacjeapp.adapter.MainMenuAdapter
import com.wakacjeapp.databinding.ActivityMainClientMenuBinding
import com.wakacjeapp.model.Banner
import com.wakacjeapp.model.DataItem
import com.wakacjeapp.model.DataItemType
import com.wakacjeapp.model.RecyclerItem

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
        prepareData()

        val adapter = MainMenuAdapter(mList)
        binding.mainMenuRecyclerView.adapter = adapter



    }


    private fun prepareData() {

        // best seller
        val bestSellerList = ArrayList<RecyclerItem>()
        bestSellerList.add(RecyclerItem(R.drawable.dominikana , "Up to 20% off"))
        bestSellerList.add(RecyclerItem(R.drawable.dominikana , "Up to 20% off"))
        bestSellerList.add(RecyclerItem(R.drawable.dominikana , "Up to 20% off"))
        bestSellerList.add(RecyclerItem(R.drawable.dominikana , "Up to 20% off"))
        bestSellerList.add(RecyclerItem(R.drawable.dominikana , "Up to 20% off"))
        bestSellerList.add(RecyclerItem(R.drawable.dominikana , "Up to 20% off"))


        //clothing
        val clothingList = ArrayList<RecyclerItem>()
        clothingList.add(RecyclerItem(R.drawable.dominikana, "Up to 25% off"))
        clothingList.add(RecyclerItem(R.drawable.dominikana, "Up to 25% off"))
        clothingList.add(RecyclerItem(R.drawable.dominikana, "Up to 25% off"))
        clothingList.add(RecyclerItem(R.drawable.dominikana, "Up to 25% off"))
        clothingList.add(RecyclerItem(R.drawable.dominikana, "Up to 25% off"))
        clothingList.add(RecyclerItem(R.drawable.dominikana, "Up to 25% off"))
        clothingList.add(RecyclerItem(R.drawable.dominikana, "Up to 25% off"))


        mList.add(DataItem(DataItemType.BEST_SELLER, bestSellerList))
        mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_png)))
        mList.add(DataItem(DataItemType.HOLIDAY, clothingList))
        mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_png)))
        mList.add(DataItem(DataItemType.BEST_SELLER, bestSellerList.asReversed()))
        mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_png)))
    }
}
