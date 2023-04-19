package com.wakacjeapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wakacjeapp.databinding.RowTripSaleBinding
import com.wakacjeapp.databinding.RowTripSoloBinding
import com.wakacjeapp.model.DataItemType
import com.wakacjeapp.model.Trip


class MainMenuChildAdapter(private val viewType: Int, private val recyclerItemList: List<Trip>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class BestSellerViewHolder(private val binding: RowTripSaleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindBestSellerView(recyclerItem: Trip) {
            binding.bestSellerImage.setImageResource(recyclerItem.image)
            binding.bestSellerText.text = recyclerItem.offer
        }
    }

    inner class HolidayViewHolder(private val binding: RowTripSoloBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindHolidayView(recyclerItem: Trip) {
            binding.holidayImage.setImageResource(recyclerItem.image)
            binding.holidayText.text = recyclerItem.offer

            binding.root.setOnClickListener{
                Log.e("Error", "Trip ${recyclerItem.offer}")
            }
        }


    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            DataItemType.BEST_SELLER -> {
                val binding = RowTripSaleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return BestSellerViewHolder(binding)
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
