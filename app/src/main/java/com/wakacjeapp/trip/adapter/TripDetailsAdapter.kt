package com.wakacjeapp.trip.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wakacjeapp.databinding.ActivityTripDetailsCardBinding
import com.wakacjeapp.trip.TripDetailsActivity
import com.wakacjeapp.trip.TripDetailsDayActivity
import com.wakacjeapp.trip.model.TripDay

class TripDetailsAdapter(private val daysList: ArrayList<TripDay>,val context: Context) :
    RecyclerView.Adapter<TripDetailsAdapter.TripDetailsViewHolder>() {

    inner class TripDetailsViewHolder(private val itemBinding: ActivityTripDetailsCardBinding):RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(tripDay: TripDay) {
            itemBinding.tvDayNumber.text = tripDay.numer.toString()
            itemBinding.tvDescription.text = tripDay.opis

            itemBinding.root.setOnClickListener{
                Log.e("Error", "TripDay ${tripDay.opis}")
                // Tutaj włącza się aktywność ale trzeba zmienić chyba strukture bazy żeby była godzina i taka lista co jaka godzina itd, to da się w mainclientmenu zmienić pola
                // w MainClientMenu, wpierw usunąć wszystkie rekordy z bazy, odkomentować "dodaj wycieczki" tam gdzie jest listener dla ikonki konta i tam trzeba zmienić strukture bazy Trip
//                val intent = Intent(context, TripDetailsDayActivity::class.java)
//                intent.putExtra("trip_day", tripDay)
//                context.startActivity(intent)
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