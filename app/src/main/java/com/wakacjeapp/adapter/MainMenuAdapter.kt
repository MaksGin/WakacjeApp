package com.wakacjeapp.adapter

import android.content.Intent
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.wakacjeapp.Messages.ChatLogActivity
import com.wakacjeapp.Messages.NewMessageActivity
import com.wakacjeapp.R
import com.wakacjeapp.client_interface.MainClientMenu
import com.wakacjeapp.databinding.EachItemBinding
import com.wakacjeapp.databinding.RowMenuActionBinding
import com.wakacjeapp.databinding.RowTitleBinding
import com.wakacjeapp.databinding.RowTripAdBinding
import com.wakacjeapp.databinding.RowTripBinding
import com.wakacjeapp.databinding.RowTripSearchBinding
import com.wakacjeapp.databinding.RowTripSoloBinding
import com.wakacjeapp.model.*
import android.content.Context
import com.wakacjeapp.Messages.LatestMessagesActivity
import com.wakacjeapp.client_interface.AllTrips
import com.wakacjeapp.client_interface.ClientTrips
import com.wakacjeapp.client_interface.InfoActivity
import com.wakacjeapp.trip.TripDetailsActivity
import com.wakacjeapp.trip.model.Trip

// Klasa pozwalająca na stworzenie obiektu który później zostanie zastąpiony elementami w recycle view
// recycle view na razie nie ma żadnych danych, uruchamiając konstruktor uzupełniamy jednocześnie zawartość recycle
// adaptery należy później przypisać

class MainMenuAdapter(private val dataItemList : List<DataItem>, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class SoloHolidayItemViewHolder(private val binding: RowTripBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindSoloHolidayView(holiday: Trip){
            val imageMap = ImageMap().imageMap
            binding.tripImg.setImageResource(imageMap[holiday.zdjecie] ?: R.drawable.dominikana);
            binding.tripTitle.text = holiday.kraj
            binding.tripDescription.text = holiday.opis

            binding.root.setOnClickListener{
                val intent = Intent(context, TripDetailsActivity::class.java)
                intent.putExtra("trip", holiday)
                context.startActivity(intent)
            }

        }
    }

    inner class BannerItemViewHolder(private val binding: RowTripAdBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindBannerView(banner: Banner){
            binding.tripAdImg.setImageResource(banner.image)
        }
    }

    inner class TextItemViewHolder(private val binding: RowTitleBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindTitleView(titleText: TitleText){
            binding.mainTitle.text = titleText.description
            binding.mainTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleText.size.toFloat())


            val intenMap = IntentMap().intentMap
            binding.showMoreBtn.setOnClickListener{
                val intent = Intent(context,intenMap[titleText.intent] ?: AllTrips::class.java)
                context.startActivity(intent)
            }
        }
    }

    inner class MenuItemViewHolder(private val binding: RowMenuActionBinding) : RecyclerView.ViewHolder(binding.root){

        private val btn1 = binding.FirstButtonMenu
        private val btn2 = binding.SecondButtonMenu
        private val btn3 = binding.ThirdButtonMenu
        private val btn4 = binding.FourthButtonMenu

        fun bindMenuView(menu: Menu){
            binding.FirstButtonMenu.setBackgroundResource(menu.first_button_img)
            binding.FirstTextMenu.text = menu.first_text
            binding.SecondButtonMenu.setBackgroundResource(menu.second_button_img)
            binding.SecondTextMenu.text = menu.second_text
            binding.ThirdButtonMenu.setBackgroundResource(menu.third_button_img)
            binding.ThirdTextMenu.text = menu.third_text
            binding.FourthButtonMenu.setBackgroundResource(menu.fourth_button_img)
            binding.FourthTextMenu.text = menu.fourth_text

            btn1.setOnClickListener {
                val intent = Intent(context,NewMessageActivity::class.java)
                context.startActivity(intent)
            }

            btn2.setOnClickListener {
                val intent = Intent(context,ClientTrips::class.java)
                context.startActivity(intent)
            }

            btn3.setOnClickListener {
                val intent = Intent(context,AllTrips::class.java)
                context.startActivity(intent)
            }

            btn4.setOnClickListener {
                val intent = Intent(context,InfoActivity::class.java)
                context.startActivity(intent)
            }

        }
    }

    inner class SearchItemViewHolder(private val binding: RowTripSearchBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindSearchView(search: Search){
            binding.SearchText.text = search.search_text

            binding.root.setOnClickListener {
                val intent = Intent(context, AllTrips::class.java)
                context.startActivity(intent)
            }
        }
    }

    inner class RecyclerItemViewHolder(private val binding: EachItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            //odwołujemy się do layoutu z each_item.xml do nowego recycle view
            binding.childRecyclerView.setHasFixedSize(true)
            binding.childRecyclerView.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
        }

        fun bindHolidayRecyclerView(recyclerItemList: List<com.wakacjeapp.trip.model.Trip>) {
            val adapter = MainMenuChildAdapter(DataItemType.HOLIDAY, recyclerItemList, context)
            binding.childRecyclerView.adapter = adapter
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(dataItemList[position].viewType){
            DataItemType.BANNER ->
                R.layout.row_trip_ad
            DataItemType.YOUR_HOLIDAY ->
                R.layout.row_trip
            DataItemType.TEXT ->
                R.layout.row_title
            DataItemType.SEARCH ->
                R.layout.row_trip_search
            DataItemType.MENU ->
                R.layout.row_menu_action
            else ->
                R.layout.each_item

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            R.layout.row_trip_ad ->{
                val binding = RowTripAdBinding.inflate(LayoutInflater.from(parent.context), parent,false)
                return BannerItemViewHolder(binding)
            }
            R.layout.row_trip ->{
                val binding = RowTripBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return SoloHolidayItemViewHolder(binding)
            }
            R.layout.row_title ->{
                val binding =  RowTitleBinding.inflate(LayoutInflater.from(parent.context),parent, false)
                return TextItemViewHolder(binding)
            }
            R.layout.row_trip_search ->{
                val binding = RowTripSearchBinding.inflate(LayoutInflater.from(parent.context),parent, false)
                return  SearchItemViewHolder(binding)
            }
            R.layout.row_menu_action ->{
                val binding = RowMenuActionBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return MenuItemViewHolder(binding)
            }
            else ->{
                val binding = EachItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
                return RecyclerItemViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
       return dataItemList.size
    }

    // Ostateczna funkcja pozwalająca na uruchomienie innych funkcji dostosowanych na podstawie
    // wyswietlania. Jesli dataitemlist ma byc pozioma np 0 to uruchom funkcje dla poziomego itemu

override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


    when (holder) {
        is BannerItemViewHolder -> {
            dataItemList[position].banner?.let { holder.bindBannerView(it) }
        }
        is SoloHolidayItemViewHolder -> {
            dataItemList[position].holiday?.let { holder.bindSoloHolidayView(it) }
        }
        is TextItemViewHolder -> {
            dataItemList[position].titleText?.let { holder.bindTitleView(it) }
        }
        is SearchItemViewHolder ->{
            dataItemList[position].search?.let { holder.bindSearchView(it) }
        }
        is MenuItemViewHolder ->{
            dataItemList[position].menu?.let { holder.bindMenuView(it) }
            }

        else -> {
            when (dataItemList[position].viewType) {
                DataItemType.HOLIDAY -> {
                    dataItemList[position].recyclerItemList?.let {
                        (holder as RecyclerItemViewHolder).bindHolidayRecyclerView(
                            it
                        )
                    }
                }
                else -> {
                    dataItemList[position].recyclerItemList?.let {
                        (holder as RecyclerItemViewHolder).bindHolidayRecyclerView(
                            it
                        )
                    }
                }
            }
        }
    }
}

}