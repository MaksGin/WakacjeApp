package com.wakacjeapp.trip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wakacjeapp.R
import com.wakacjeapp.trip.model.TripDay

class TripDetailsAdapter(private val daysList: ArrayList<TripDay>) :
    RecyclerView.Adapter<TripDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TripDetailsAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.activity_trip_details_card, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripDetailsAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return daysList.size
    }

    class ViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        val numer : TextView
        val opis: TextView

        init {
            numer = view.findViewById(R.id.tvDayNumber)
            opis = view.findViewById(R.id.tvDescription)
        }

    }
}