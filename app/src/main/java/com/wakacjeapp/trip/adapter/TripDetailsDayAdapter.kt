package com.wakacjeapp.trip.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wakacjeapp.databinding.TripDetailsElementBinding
import com.wakacjeapp.trip.TripDetailsDayActivity
import com.wakacjeapp.trip.model.TripDayItem

class TripDetailsDayAdapter(
    private val activitiesList: ArrayList<TripDayItem>,
    tripDetailsDayActivity: TripDetailsDayActivity
) :
    RecyclerView.Adapter<TripDetailsDayAdapter.TripDetailsDayViewHolder>() {

    inner class TripDetailsDayViewHolder(private val itemBinding: TripDetailsElementBinding):RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(tripDayItem: TripDayItem) {
            itemBinding.tvTimeValue.text = tripDayItem.godzina.toString()
            itemBinding.textView3.text = tripDayItem.opis
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripDetailsDayViewHolder {
        val view = TripDetailsElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TripDetailsDayViewHolder(view)
    }

    override fun getItemCount(): Int {
        return activitiesList.size
    }

    override fun onBindViewHolder(holder: TripDetailsDayViewHolder, position: Int) {
        val hour = activitiesList[position]
        holder.bindItem(hour)
    }

}