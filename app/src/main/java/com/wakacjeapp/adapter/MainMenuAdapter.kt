package com.wakacjeapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.wakacjeapp.R
import com.wakacjeapp.databinding.EachItemBinding
import com.wakacjeapp.databinding.RowTripAdBinding
import com.wakacjeapp.model.Banner
import com.wakacjeapp.model.DataItem
import com.wakacjeapp.model.DataItemType
import com.wakacjeapp.model.RecyclerItem

// Klasa pozwalająca na stworzenie obiektu który później zostanie zastąpiony elementami w recycle view
// recycle view na razie nie ma żadnych danych, uruchamiając konstruktor uzupełniamy jednocześnie zawartość recycle
// adaptery należy później przypisać

class MainMenuAdapter(private val dataItemList : List<DataItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class BannerItemViewHolder(private val binding: RowTripAdBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindBannerView(banner: Banner){
            binding.tripAdImg.setImageResource(banner.image)
        }
    }

    inner class RecyclerItemViewHolder(private val binding: EachItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            //odwołujemy się do layoutu z each_item.xnk do nowego recycle view
            binding.childRecyclerView.setHasFixedSize(true)
            binding.childRecyclerView.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
        }

        fun bindClothingRecyclerView(recyclerItemList: List<RecyclerItem>) {
            val adapter = MainMenuChildAdapter(DataItemType.HOLIDAY, recyclerItemList)
            binding.childRecyclerView.adapter = adapter
        }

        fun bindBestSellerRecyclerView(recyclerItemList: List<RecyclerItem>) {

            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding.childRecyclerView)
            val adapter = MainMenuChildAdapter(DataItemType.BEST_SELLER, recyclerItemList)
            binding.childRecyclerView.adapter = adapter
        }
    }

    override fun getItemViewType(position: Int): Int {
        when(dataItemList[position].viewType){
            DataItemType.BANNER ->
                return R.layout.row_trip_ad
            else ->
                return R.layout.each_item

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            R.layout.row_trip_ad ->{
                val binding = RowTripAdBinding.inflate(LayoutInflater.from(parent.context), parent,false)
                return BannerItemViewHolder(binding)
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
        else -> {
            when (dataItemList[position].viewType) {
                DataItemType.BEST_SELLER -> {
                    dataItemList[position].recyclerItemList?.let {
                        (holder as RecyclerItemViewHolder).bindBestSellerRecyclerView(
                            it
                        )
                    }
                }
                else -> {
                    dataItemList[position].recyclerItemList?.let {
                        (holder as RecyclerItemViewHolder).bindClothingRecyclerView(
                            it
                        )
                    }
                }
            }
        }
    }
}

}