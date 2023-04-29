package com.wakacjeapp.model

//klasa potrzebna tworzenia obiektów wyświetlanych później jako jeden element Recycleview
data class DataItem(val viewType: Int) {

    var recyclerItemList: List<com.wakacjeapp.trip.model.Trip>? = null
    var banner: Banner? = null
    var holiday: Holidaysolo? = null
    var titleText: TitleText? = null
    var menu: Menu? = null
    var search: Search? = null

    constructor(viewType: Int, recyclerItemList: List<com.wakacjeapp.trip.model.Trip>) : this(viewType) {
        this.recyclerItemList = recyclerItemList
    }

    constructor(viewType: Int, banner: Banner) : this(viewType) {
        this.banner = banner
    }

    constructor(viewType: Int, holiday: Holidaysolo) : this(viewType){
        this.holiday = holiday
    }

    constructor(viewType: Int, titleText: TitleText) : this(viewType){
        this.titleText=titleText
    }

    constructor(viewType: Int, menu: Menu) : this(viewType){
        this.menu=menu
    }

    constructor(viewType: Int, search: Search) : this(viewType){
        this.search=search
    }

}

//klasy potrzebne do stworzenia obiektów typu DataItem

data class Trip(val image: Int, val offer: String?)
data class Banner(val image: Int)
data class Holidaysolo(val image: Int, val title: String?, val description: String?)

data class TitleText(val description: String?, val size: Int)

data class Menu(val first_button_img: Int, val first_text: String,
                val second_button_img: Int, val second_text: String,
                val third_button_img: Int, val third_text: String,
                val fourth_button_img: Int, val fourth_text: String
)

data class Search(val search_text: String)