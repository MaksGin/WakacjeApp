package com.wakacjeapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wakacjeapp.databinding.RowTripSoloBinding
import com.wakacjeapp.model.DataItemType
import com.wakacjeapp.trip.model.Trip


class MainMenuChildAdapter(private val viewType: Int, private val recyclerItemList: List<Trip>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class HolidayViewHolder(private val binding: RowTripSoloBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindHolidayView(recyclerItem: Trip) {
            //binding.holidayImage.setImageResource(recyclerItem.cena.toInt());
            binding.holidayText.text = recyclerItem.kraj;

            binding.root.setOnClickListener{
                Log.e("kraj", "Trip ${recyclerItem.kraj}")
                Log.e("data", "data ${recyclerItem.data_pocz}")
                Log.e("plan", " ${recyclerItem.plan[1]}")
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
