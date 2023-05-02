package com.wakacjeapp.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wakacjeapp.Messages.LatestMessagesActivity
import com.wakacjeapp.databinding.RowTripSoloBinding
import com.wakacjeapp.model.DataItemType
import com.wakacjeapp.trip.model.Trip
import android.content.Context
import android.os.Bundle
import com.wakacjeapp.R
import com.wakacjeapp.model.ImageMap
import com.wakacjeapp.trip.TripDetailsActivity


class MainMenuChildAdapter(private val viewType: Int, private val recyclerItemList: List<Trip>, val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class HolidayViewHolder(private val binding: RowTripSoloBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindHolidayView(recyclerItem: Trip) {
            val imageMap = ImageMap().imageMap
            binding.holidayImage.setImageResource(imageMap[recyclerItem.zdjecie] ?: R.drawable.dominikana);
            binding.holidayText.text = recyclerItem.kraj;

            binding.root.setOnClickListener{
                val intent = Intent(context,TripDetailsActivity::class.java)
                intent.putExtra("trip", recyclerItem)
                context.startActivity(intent)
            }
        }


    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            DataItemType.HOLIDAY -> {
                val binding = RowTripSoloBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return HolidayViewHolder(binding)
            }
            else -> {
                val binding = RowTripSoloBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return HolidayViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return recyclerItemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is HolidayViewHolder -> {
                holder.bindHolidayView(recyclerItemList[position])
            }
        }
    }
}
