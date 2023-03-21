package com.wakacjeapp.model

//klasa potrzebna tworzenia obiektów wyświetlanych później jako jeden element Recycleview
data class DataItem(val viewType: Int) {

    var recyclerItemList: List<Trip>? = null
    var banner: Banner? = null

    constructor(viewType: Int, recyclerItemList: List<Trip>) : this(viewType) {
        this.recyclerItemList = recyclerItemList
    }

    constructor(viewType: Int, banner: Banner) : this(viewType) {
        this.banner = banner
    }

}

//klasy potrzebne do stworzenia obiektów typu DataItem

data class Trip(val image: Int, val offer: String?)
//data class RecyclerItem(val image: Int, val offer: String)
data class Banner(val image: Int)