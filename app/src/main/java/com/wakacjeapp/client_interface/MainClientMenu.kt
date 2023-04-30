package com.wakacjeapp.client_interface

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wakacjeapp.Messages.NewMessageActivity
import com.wakacjeapp.R
import com.wakacjeapp.User
import com.wakacjeapp.adapter.MainMenuAdapter
import com.wakacjeapp.databinding.ActivityMainClientMenuBinding
import com.wakacjeapp.model.*
import com.wakacjeapp.trip.model.Trip
import com.wakacjeapp.trip.model.TripDay
import java.util.Calendar


class MainClientMenu : AppCompatActivity() {

    private lateinit var main_binding: ActivityMainClientMenuBinding
    private lateinit var mList: ArrayList<DataItem>
    private lateinit var adapter: MainMenuAdapter

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main_binding = ActivityMainClientMenuBinding.inflate(layoutInflater)


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar()?.hide();

//        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        setContentView(main_binding.root)

        main_binding.mainMenuRecyclerView.setHasFixedSize(true)
        main_binding.mainMenuRecyclerView.layoutManager = LinearLayoutManager(this)
        val user_mail = intent.getStringExtra("USER_EMAIL")
        mList = ArrayList()
        getTripsList()
        adapter = MainMenuAdapter(mList, this)

        Toast.makeText(this@MainClientMenu, "Konto użytkownika: $user_mail", Toast.LENGTH_SHORT)
            .show()


        main_binding.menuAllButton.setOnClickListener {
            val intent = Intent(this, NewMessageActivity::class.java)

            dodajUzytkownikaDoWycieczki("-NUGxERHNwRHJLWM7p86", User(name = "Marcin", email = "marcin@wp.pl", uid = "f8OGF4FoJkMkOY69ykZ4l4K0geK2"))
            //startActivity(intent)
        }

        main_binding.ShowAccountButton.setOnClickListener {
            dodajNoweWycieczki()
            Toast.makeText(this@MainClientMenu, "Konto użytkownika", Toast.LENGTH_SHORT).show()
        }


    }

    fun dodajUzytkownikaDoWycieczki(wycieczkaId: String, nowyUzytkownik: User) {
        val database = FirebaseDatabase.getInstance()
        val wycieczkiRef = database.getReference("wycieczki")

        val wycieczkaRef = wycieczkiRef.child(wycieczkaId)

        wycieczkaRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val wycieczka = dataSnapshot.getValue(Trip::class.java) // Pobiera aktualne wartości wycieczki
                wycieczka?.uzytkownicy?.add(nowyUzytkownik) // Dodaje uzytkownika
                wycieczkaRef.setValue(wycieczka) // Aktualizuje wartości
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun dodajNowaWycieczka(nowaWycieczka: Trip) {
        val database = FirebaseDatabase.getInstance()
        val wycieczkiRef = database.getReference("wycieczki")

        val nowaWycieczkaRef = wycieczkiRef.push()
        val nowaWycieczkaKey = nowaWycieczkaRef.key

        val nowaWycieczkaMap = hashMapOf(
            "cena_za_osobe" to nowaWycieczka.cena,
            "data_wyjazdu" to nowaWycieczka.data_pocz,
            "data_powrotu" to nowaWycieczka.data_powrotu,
            "miejsce" to nowaWycieczka.miejsce,
            "kraj" to nowaWycieczka.kraj,
            "ocena" to nowaWycieczka.ocena,
            "opis" to nowaWycieczka.opis,
            "zdjecie" to nowaWycieczka.zdjecie,
            "ilosc_miejsc" to nowaWycieczka.ilosc_miejsc,
            "id_wycieczki" to nowaWycieczkaKey,
            "plan_wycieczki" to nowaWycieczka.plan,
            "uzytkownicy" to nowaWycieczka.uzytkownicy
        )

        nowaWycieczkaRef.setValue(nowaWycieczkaMap)
    }

    fun dodajNoweWycieczki(){

        val daysList: ArrayList<TripDay> = ArrayList()
        daysList.add(TripDay(1, "Przylot do Rzymu o godzinie 14:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta."))
        daysList.add(TripDay(2, "Zwiedzanie Rzymu: Koloseum, Forum Romanum, Pałac Cezarów, Fontanna di Trevi, Panteon." ))
        daysList.add(TripDay(3,  "Wyjazd na wycieczkę poza miasto: Watykan, Muzea Watykańskie, Bazylika św. Piotra."))
        daysList.add(TripDay(4,  "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 19:00."))

        val uzytkownicy: ArrayList<User> = ArrayList()
        uzytkownicy.add( User(name = "Marcin", email = "marcin@wp.pl", uid = "f8OGF4FoJkMkOY69ykZ4l4K0geK2"))

        val trip = Trip(
            cena = 1000.0,
            data_pocz = "2023-06-03",
            data_powrotu = "2023-06-01",
            miejsce = "Rzym",
            kraj = "Włochy",
            ocena = 4.8,
            opis = "Zwiedzanie Rzymu i okolic",
            zdjecie = "wlochy.png",
            ilosc_miejsc = 16,
            plan = daysList,
            uzytkownicy = uzytkownicy,

        )

        dodajNowaWycieczka(trip)

//        val trip1 = Trip(
//            cena = 800.0,
//            data_pocz = "2023-07-10",
//            data_powrotu = "2023-07-03",
//            miejsce = "Barcelona",
//            kraj = "Hiszpania",
//            ocena = 4.5,
//            opis = "Zwiedzanie Barcelony i Costa Brava",
//            zdjecie = "hiszpania.png",
//            ilosc_miejsc = 10,
//            plan = listOf(
//                TripDay(
//                    numer = 1,
//                    opis = "Przylot do Barcelony o godzinie 12:00. Zakwaterowanie w apartamencie i czas wolny na zwiedzanie miasta."
//                ),
//                TripDay(
//                    numer = 2,
//                    opis = "Zwiedzanie Barcelony: La Rambla, Park Güell, Sagrada Familia, Katedra św. Eulalii."
//                ),
//                TripDay(
//                    numer = 3,
//                    opis = "Wyjazd na wycieczkę na Costa Brava: Tossa de Mar, Lloret de Mar, Blanes."
//                ),
//                TripDay(
//                    numer = 4,
//                    opis = "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 18:00."
//                )
//            ) as ArrayList<TripDay>,
//            uzytkownicy = listOf(
//                User(
//                    name = "Marcin",
//                    email = "marcin@wp.pl",
//                    uid = "f8OGF4FoJkMkOY69ykZ4l4K0geK2"
//                ),
//                User(
//                    name = "Maksymilian",
//                    email = "maksymilian@wp.pl",
//                    uid = "r7OxkN0jERgqw4OBQ5qQwvytjuf2"
//                ),
//            )  as ArrayList<User>
//        )
//
//        val trip2 = Trip(
//            cena = 1200.0,
//            data_pocz = "2023-09-05",
//            data_powrotu = "2023-06-01",
//            miejsce = "Ateny",
//            kraj = "Grecja",
//            ocena = 4.9,
//            opis = "Zwiedzanie Aten i wyspy Santorini",
//            zdjecie = "grecja.png",
//            ilosc_miejsc = 16,
//            plan = listOf(
//                TripDay(
//                    numer = 1,
//                    opis = "Przylot do Aten o godzinie 11:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta."
//                ),
//                TripDay(
//                    numer = 2,
//                    opis ="Zwiedzanie Aten: Akropol, Agora Rzymska, Muzeum Akropolu, Plaka."
//                ),
//                TripDay(
//                    numer = 3,
//                    opis = "Wyjazd na wycieczkę na wyspę Santorini: Oia, Fira, Kamari, Czerwone Plaże."
//                ),
//                TripDay(
//                    numer = 4,
//                    opis = "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 20:00."
//                )
//            ) as ArrayList<TripDay>,
//            uzytkownicy = listOf(
//                User(
//                    name = "Marcin",
//                    email = "marcin@wp.pl",
//                    uid = "f8OGF4FoJkMkOY69ykZ4l4K0geK2"
//                ),
//                User(
//                    name = "Agata P",
//                    email = "agata@wp.pl",
//                    uid = "6mb3E3G0XDZMBFoRjcLc9Jdyq4v2"
//                ),
//            ) as ArrayList<User>
//        )
//
//        val trip3 = Trip(
//            cena = 2000.0,
//            data_pocz = "2023-12-10",
//            data_powrotu = "2023-12-01",
//            miejsce = "Chartum",
//            kraj = "Sudan",
//            ocena = 4.2,
//            opis = "Safari w Parku Narodowym Dżabal Kurdufan",
//            zdjecie = "Sudan.png",
//            ilosc_miejsc = 10,
//            plan = listOf(
//                TripDay(
//                    numer = 1,
//                    opis = "Przylot do Chartumu o godzinie 8:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta."
//                ),
//                TripDay(
//                    numer = 2,
//                    opis = "Wyjazd na safari w Parku Narodowym Dżabal Kurdufan: zwiedzanie pięknych krajobrazów, spotkanie z dzikimi zwierzętami."
//                ),
//                TripDay(
//                    numer = 3,
//                    opis = "Zwiedzanie historycznych miejsc Chartumu: Muzeum Narodowe, Naga, Gebel Barkal."
//                ),
//                TripDay(
//                    numer = 4,
//                    opis = "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 19:00."
//                )
//            ) as ArrayList<TripDay>,
//            uzytkownicy = listOf(
//                User(
//                    name = "Marcin",
//                    email = "marcin@wp.pl",
//                    uid = "f8OGF4FoJkMkOY69ykZ4l4K0geK2"
//                ),
//                User(
//                    name = "Aleksandra",
//                    email = "aleksandra@wp.pl",
//                    uid = "0nnht96CZhSABjTYlWzT6Beh7lw1"
//                ),
//            )  as ArrayList<User>
//        )
//
//        val trip4 = Trip(
//            cena = 3000.0,
//            data_pocz = "2023-10-30",
//            data_powrotu = "2023-10-10",
//            miejsce = "Isfahan",
//            kraj = "Iran",
//            ocena = 4.8,
//            opis = "Zwiedzanie zabytków i kulinarnych przysmaków Iranu",
//            zdjecie = "hiszpania.png",
//            ilosc_miejsc = 8,
//            plan = listOf(
//                TripDay(
//                    numer = 1,
//                    opis = "Przylot do Isfahan o godzinie 11:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta."
//                ),
//                TripDay(
//                    numer = 2,
//                    opis = "Zwiedzanie zabytków Isfahanu: Most Si-o-seh Pol, Pałac Haszty Beheszt, Meczet Szejka Lotfollaha."
//                ),
//                TripDay(
//                    numer = 3,
//                    opis = "Wycieczka do Perspolis: zwiedzanie ruiny starożytnego miasta i Muzeum Archeologicznego w Sziraz."
//                ),
//                TripDay(
//                    numer = 4,
//                    opis = "Degustacja kulinarnych przysmaków Iranu i czas wolny na zakupy. Wyjazd powrotny do kraju o godzinie 20"
//                )
//            ) as ArrayList<TripDay>,
//            uzytkownicy = listOf(
//                User(
//                    name = "Marcin",
//                    email = "marcin@wp.pl",
//                    uid = "f8OGF4FoJkMkOY69ykZ4l4K0geK2"
//                ),
//                User(
//                    name = "Maksymilian",
//                    email = "maksymilian@wp.pl",
//                    uid = "r7OxkN0jERgqw4OBQ5qQwvytjuf2"
//                ),
//                User(
//                    name = "Aleksandra",
//                    email = "aleksandra@wp.pl",
//                    uid = "0nnht96CZhSABjTYlWzT6Beh7lw1"
//                ),
//            )  as ArrayList<User>
//        )



    }


    private fun getTripsList() {
        val tripsRef = FirebaseDatabase.getInstance().reference.child("trips") //odszukanie wycieczek

        // ---------- M E N U -------
        mList.add(DataItem(DataItemType.SEARCH, Search("Szukaj")))
        mList.add(DataItem(DataItemType.MENU, Menu(
            R.drawable.chat_bubble_img,"Chat",
            R.drawable.baseline_map_24,"Wycieczki",
            R.drawable.chat_bubble_img,"Chat",
            R.drawable.chat_bubble_img,"Chat")))
        // -------------------------

        // --- Nagłówek ---
        mList.add(DataItem(DataItemType.TEXT, TitleText("Moje wycieczki",20)))

        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tripsList: MutableList<Trip> = ArrayList()
                val tripDays: ArrayList<TripDay> = ArrayList()
                tripDays.add(TripDay(1,"Szybkie jedzenie i skok na plaże"))
                tripDays.add(TripDay(2,"Szybkie jedzenie i skok na plaże"))
                tripDays.add(TripDay(3,"Szybkie jedzenie i skok na plaże"))
                val currentDate = Calendar.getInstance().time
                for (ds in dataSnapshot.children) {
                    val offer = ds.child("offer").getValue(String::class.java)
                    val image = ds.child("image").getValue(Int::class.java)!!

                }

                tripsList.shuffle()
                mList.add(DataItem(DataItemType.HOLIDAY, tripsList))
                mList.add(DataItem(DataItemType.TEXT, TitleText("Oferty",20)))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.turcja,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_png)))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.iran,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_img2)))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.turcja,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.turcja,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_img3)))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.iran,"Syria","Testowy opis")))
                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))

                main_binding.mainMenuRecyclerView.adapter = adapter
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Error", "onCancelled", databaseError.toException())
            }
        }
        tripsRef.addValueEventListener(valueEventListener)
    }














//    private fun getTripsList() {
//        val tripsRef = FirebaseDatabase.getInstance().reference.child("trips") //odszukanie wycieczek
//
//        // ---------- M E N U -------
//        mList.add(DataItem(DataItemType.SEARCH, Search("Szukaj")))
//        mList.add(DataItem(DataItemType.MENU, Menu(
//            R.drawable.chat_bubble_img,"Chat",
//            R.drawable.baseline_map_24,"Wycieczki",
//            R.drawable.chat_bubble_img,"Chat",
//            R.drawable.chat_bubble_img,"Chat")))
//        // -------------------------
//
//        // --- Nagłówek ---
//        mList.add(DataItem(DataItemType.TEXT, TitleText("Moje wycieczki",20)))
//
//        val valueEventListener: ValueEventListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val tripsList: MutableList<Trip> = ArrayList()
//                val tripDays: ArrayList<TripDay> = ArrayList()
//                tripDays.plus(TripDay(1,"Szybkie jedzenie i skok na plaże"))
//                tripDays.plus(TripDay(2,"Szybkie jedzenie i skok na plaże"))
//                tripDays.plus(TripDay(3,"Szybkie jedzenie i skok na plaże"))
//                val currentDate = Calendar.getInstance().time
//                for (ds in dataSnapshot.children) {
//                    val offer = ds.child("offer").getValue(String::class.java)
//                    val image = ds.child("image").getValue(Int::class.java)!!
//                    val trip = Trip(currentDate,20.0,currentDate,"Pustynia","Iran",2.3,"Szybki opis",tripDays)
//                    tripsList.add(trip)
//
//                }
//
//                tripsList.shuffle()
//                mList.add(DataItem(DataItemType.HOLIDAY, tripsList))
//                mList.add(DataItem(DataItemType.TEXT, TitleText("Oferty",20)))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.turcja,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_png)))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.iran,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_img2)))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.turcja,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.turcja,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_img3)))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.iran,"Syria","Testowy opis")))
//                mList.add(DataItem(DataItemType.YOUR_HOLIDAY, Holidaysolo(R.drawable.dominikana,"Syria","Testowy opis")))
//
//                main_binding.mainMenuRecyclerView.adapter = adapter
//            }
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.e("Error", "onCancelled", databaseError.toException())
//            }
//        }
//        tripsRef.addValueEventListener(valueEventListener)
//    }
}
