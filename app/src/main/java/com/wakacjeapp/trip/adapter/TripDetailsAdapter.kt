package com.wakacjeapp.trip.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wakacjeapp.databinding.ActivityTripDetailsCardBinding
import com.wakacjeapp.trip.model.TripDay

class TripDetailsAdapter(private val daysList: ArrayList<TripDay>) :
    RecyclerView.Adapter<TripDetailsAdapter.TripDetailsViewHolder>() {

    inner class TripDetailsViewHolder(private val itemBinding: ActivityTripDetailsCardBinding):RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(tripDay: TripDay) {
            itemBinding.tvDayNumber.text = tripDay.numer.toString()
            itemBinding.tvDescription.text = tripDay.opis

            itemBinding.root.setOnClickListener{
                Log.e("Error", "TripDay ${tripDay.opis}")
            }

        }


    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TripDetailsViewHolder {
        val view = ActivityTripDetailsCardBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return TripDetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripDetailsViewHolder, position: Int) {
        val day = daysList[position]
        holder.bindItem(day)
    }

    override fun getItemCount(): Int {
        return daysList.size
    }

//    class ViewHolder (view : View) : RecyclerView.ViewHolder(view) {
//        val numer : TextView
//        val opis: TextView
//
//        init {
//            numer = view.findViewById(R.id.tvDayNumber)
//            opis = view.findViewById(R.id.tvDescription)
//        }
//
//    }
}