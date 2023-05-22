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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wakacjeapp.LoginActivity
import com.wakacjeapp.Messages.NewMessageActivity
import com.wakacjeapp.R
import com.wakacjeapp.User
import com.wakacjeapp.adapter.MainMenuAdapter
import com.wakacjeapp.databinding.ActivityMainClientMenuBinding
import com.wakacjeapp.model.*
import com.wakacjeapp.trip.model.Trip
import com.wakacjeapp.trip.model.TripDay
import com.wakacjeapp.trip.model.TripDayItem


class MainClientMenu : AppCompatActivity() {

    private lateinit var main_binding: ActivityMainClientMenuBinding
    private lateinit var mList: ArrayList<DataItem>
    private lateinit var adapter: MainMenuAdapter


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main_binding = ActivityMainClientMenuBinding.inflate(layoutInflater)

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();
        setContentView(main_binding.root)


        main_binding.mainMenuRecyclerView.setHasFixedSize(true)
        main_binding.mainMenuRecyclerView.layoutManager = LinearLayoutManager(this)

        mList = ArrayList()
        getTripsList()
        adapter = MainMenuAdapter(mList, this)

        var imiee =""
        znajdzUzytkownika(FirebaseAuth.getInstance().uid, object : UserListener {
            override fun onUserFound(imie: String) {
                main_binding.daneUz.text = imie
                imiee = imie
            }
        })


        main_binding.menuAllButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            Toast.makeText(this@MainClientMenu, "Pomyślnie wylogowano", Toast.LENGTH_SHORT).show()
            startActivity(intent)

        }

        main_binding.ShowAccountButton.setOnClickListener {
            Toast.makeText(this@MainClientMenu, "Cześć $imiee", Toast.LENGTH_SHORT).show()
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

    private fun sprawdz_usera(list: ArrayList<User>, user: User): Boolean {
        for (uczestnik in list) {
            if (uczestnik.uid == user.uid) {
                return true
            }
        }
        return false
    }
    private fun uzupelnij_liste(losowe_itemy: ArrayList<DataItem>): ArrayList<DataItem> {
        losowe_itemy.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_png)))
        losowe_itemy.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_img2)))
        losowe_itemy.add(DataItem(DataItemType.BANNER, Banner(R.drawable.ad_img3)))
        return losowe_itemy
    }



    private fun getTripsList() {
        val tripsRef = FirebaseDatabase.getInstance().reference.child("wycieczki") //odszukanie wycieczek

        // ---------- M E N U -------
        mList.add(DataItem(DataItemType.SEARCH, Search("Szukaj")))
        mList.add(DataItem(DataItemType.MENU, Menu(
            R.drawable.chat_bubble_img,"Chat",
            R.drawable.baseline_map_24,"Wycieczki",
            R.drawable.offer_icon,"Oferty",
            R.drawable.baseline_info_24,"Info")))
        // -------------------------

        // --- Nagłówek ---

        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val twoje_wycieczki: MutableList<Trip> = ArrayList()
                val wszystkie_wycieczki: MutableList<Trip> = ArrayList()
                val losowe_itemy: MutableList<DataItem> = ArrayList()

                for (ds in dataSnapshot.children) {
                    val wycieczka = ds.getValue(Trip::class.java)
                    if (wycieczka != null) {
                        wszystkie_wycieczki.add(wycieczka)
//                        Log.e("zdjecie", wycieczka.zdjecie.toString())
                    }
                }
                wszystkie_wycieczki.shuffle()
                var czy_jest_chociaz_jedna = 0

                val szukanyUzytkownik = User(name = FirebaseAuth.getInstance().currentUser?.displayName, email = FirebaseAuth.getInstance().currentUser?.email, uid = FirebaseAuth.getInstance().currentUser?.uid)
                    for (wycieczka in wszystkie_wycieczki) {
                        if(sprawdz_usera(wycieczka.uzytkownicy, szukanyUzytkownik)){
                            twoje_wycieczki.add(wycieczka)
                            czy_jest_chociaz_jedna++
                        }
                    }

                if(czy_jest_chociaz_jedna == 0){
                    mList.add(DataItem(DataItemType.TEXT, TitleText("Oferty",20,"oferty")))
                    wszystkie_wycieczki.forEach { wycieczka ->
                        losowe_itemy.add(DataItem(DataItemType.YOUR_HOLIDAY, wycieczka))
                    }

                    uzupelnij_liste(losowe_itemy as ArrayList<DataItem>)
                }else if(czy_jest_chociaz_jedna == 1){
                    mList.add(DataItem(DataItemType.TEXT, TitleText("Moje wycieczki",20,"moje_wycieczki")))
                    mList.add(DataItem(DataItemType.YOUR_HOLIDAY, twoje_wycieczki[0]))
                    mList.add(DataItem(DataItemType.TEXT, TitleText("Oferty",20,"oferty")))

                    wszystkie_wycieczki.forEach { wycieczka ->
                        losowe_itemy.add(DataItem(DataItemType.YOUR_HOLIDAY, wycieczka))
                    }

                    uzupelnij_liste(losowe_itemy as ArrayList<DataItem>)
                }else{
                    mList.add(DataItem(DataItemType.TEXT, TitleText("Moje wycieczki",20,"moje_wycieczki")))
                    mList.add(DataItem(DataItemType.HOLIDAY, twoje_wycieczki))
                    mList.add(DataItem(DataItemType.TEXT, TitleText("Oferty",20,"oferty")))

                    wszystkie_wycieczki.forEach { wycieczka ->
                        losowe_itemy.add(DataItem(DataItemType.YOUR_HOLIDAY, wycieczka))
                    }
                    uzupelnij_liste(losowe_itemy as ArrayList<DataItem>)
                }

                losowe_itemy.shuffle()

                mList.addAll(losowe_itemy)
                main_binding.mainMenuRecyclerView.adapter = adapter
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Error", "onCancelled", databaseError.toException())
            }
        }
        tripsRef.addValueEventListener(valueEventListener)
    }

    fun dodajNowaWycieczka(nowaWycieczka: Trip) {
        val database = FirebaseDatabase.getInstance()
        val wycieczkiRef = database.getReference("wycieczki")

        val nowaWycieczkaRef = wycieczkiRef.push()
        val nowaWycieczkaKey = nowaWycieczkaRef.key

        val nowaWycieczkaMap = hashMapOf(
            "cena" to nowaWycieczka.cena,
            "data_pocz" to nowaWycieczka.data_pocz,
            "data_powrotu" to nowaWycieczka.data_powrotu,
            "miejsce" to nowaWycieczka.miejsce,
            "kraj" to nowaWycieczka.kraj,
            "ocena" to nowaWycieczka.ocena,
            "opis" to nowaWycieczka.opis,
            "zdjecie" to nowaWycieczka.zdjecie,
            "ilosc_miejsc" to nowaWycieczka.ilosc_miejsc,
            "id_wycieczki" to nowaWycieczkaKey,
            "plan" to nowaWycieczka.plan,
            "uzytkownicy" to nowaWycieczka.uzytkownicy
        )

        nowaWycieczkaRef.setValue(nowaWycieczkaMap)
    }
    fun dodajNoweWycieczki(){

        //Wycieczka do Rzymu
        //Plan godzinowy
        val daysDetailsList00: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList00.add(TripDayItem("14.00","Zakwaterowanie w hotelu"))
        daysDetailsList00.add(TripDayItem("15.00","Czas wolny na zwiedzanie miasta"))
        daysDetailsList00.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList01: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList01.add(TripDayItem("8.00","Zwiedzanie Koloseum i Forum Romanum"))
        daysDetailsList01.add(TripDayItem("13.00","Obiad na miescie"))
        daysDetailsList01.add(TripDayItem("15.00","Zwiedzanie Pałacu Cezarów i Fontanny di Trevi"))

        val daysDetailsList02: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList02.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList02.add(TripDayItem("9.00","Wyjazd na wycieczkę poza Rzym autokarem"))
        daysDetailsList02.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList03: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList03.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList03.add(TripDayItem("9.00","Czas wolny na zakupy"))
        daysDetailsList03.add(TripDayItem("18.00","Wykwaterowanie z hotelu"))

        val daysList0: ArrayList<TripDay> = ArrayList()
        daysList0.add(TripDay(1, "Przylot do Rzymu o godzinie 14:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta.",daysDetailsList00))
        daysList0.add(TripDay(2, "Zwiedzanie Rzymu: Koloseum, Forum Romanum, Pałac Cezarów, Fontanna di Trevi, Panteon." ,daysDetailsList01))
        daysList0.add(TripDay(3,  "Wyjazd na wycieczkę poza miasto: Watykan, Muzea Watykańskie, Bazylika św. Piotra.",daysDetailsList02))
        daysList0.add(TripDay(4,  "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 19:00.",daysDetailsList03))

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
            plan = daysList0,
            uzytkownicy = uzytkownicy,
            )

       //Wycieczka do Barcelony

        //Plan godzinowy
        val daysDetailsList10: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList10.add(TripDayItem("13.00","Zakwaterowanie w hotelu"))
        daysDetailsList10.add(TripDayItem("14.00","Czas wolny na zwiedzanie miasta"))
        daysDetailsList10.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList11: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList11.add(TripDayItem("8.00","Przejście La Ramblą i wycieczka do Parku Güell"))
        daysDetailsList11.add(TripDayItem("14.00","Obiad na miescie"))
        daysDetailsList11.add(TripDayItem("15.00","Przejście do Sagrady Familii"))

        val daysDetailsList12: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList12.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList12.add(TripDayItem("9.00","Wyjazd na Costa Brava autokarem"))
        daysDetailsList12.add(TripDayItem("19.00","Powrót i kolacja w hotelu"))

        val daysDetailsList13: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList13.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList13.add(TripDayItem("9.00","Czas wolny na zakupy"))
        daysDetailsList13.add(TripDayItem("17.00","Wykwaterowanie z hotelu"))

        val daysList1: ArrayList<TripDay> = ArrayList()
        daysList1.add(TripDay(1, "Przylot do Barcelony o godzinie 12:00. Zakwaterowanie w apartamencie i czas wolny na zwiedzanie miasta.",daysDetailsList10))
        daysList1.add(TripDay(2,  "Zwiedzanie Barcelony: La Rambla, Park Güell, Sagrada Familia, Katedra św. Eulalii.",daysDetailsList11))
        daysList1.add(TripDay(3,   "Wyjazd na wycieczkę na Costa Brava: Tossa de Mar, Lloret de Mar, Blanes.",daysDetailsList12))
        daysList1.add(TripDay(4,   "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 18:00.",daysDetailsList13))

        val trip1 = Trip(
            cena = 800.0,
            data_pocz = "2023-07-10",
            data_powrotu = "2023-07-03",
            miejsce = "Barcelona",
            kraj = "Hiszpania",
            ocena = 4.5,
            opis = "Zwiedzanie Barcelony i Costa Brava",
            zdjecie = "hiszpania.png",
            ilosc_miejsc = 10,
            plan = daysList1,
            uzytkownicy = uzytkownicy
        )

        //Wycieczka do Grecji

        //Plan godzinowy
        val daysDetailsList20: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList20.add(TripDayItem("12.00","Zakwaterowanie w hotelu"))
        daysDetailsList20.add(TripDayItem("13.00","Czas wolny na zwiedzanie miasta"))
        daysDetailsList20.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList21: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList21.add(TripDayItem("8.00","Wyjście na zwiedzanie Aten"))
        daysDetailsList21.add(TripDayItem("14.00","Obiad na miescie"))
        daysDetailsList21.add(TripDayItem("15.00","Wycieczka na wzgórze Akropol"))

        val daysDetailsList22: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList22.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList22.add(TripDayItem("9.00","Rejs na wyspę Santorini"))
        daysDetailsList22.add(TripDayItem("19.00","Powrót i kolacja w hotelu"))

        val daysDetailsList23: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList23.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList23.add(TripDayItem("9.00","Czas wolny na zakupy"))
        daysDetailsList23.add(TripDayItem("18.00","Wykwaterowanie z hotelu"))

        val daysList2: ArrayList<TripDay> = ArrayList()
        daysList2.add(TripDay(1, "Przylot do Aten o godzinie 11:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta.",daysDetailsList20))
        daysList2.add(TripDay(2,  "Zwiedzanie Aten: Akropol, Agora Rzymska, Muzeum Akropolu, Plaka.",daysDetailsList21))
        daysList2.add(TripDay(3,   "Wyjazd na wycieczkę na wyspę Santorini: Oia, Fira, Kamari, Czerwone Plaże.",daysDetailsList22))
        daysList2.add(TripDay(4,   "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 20:00.",daysDetailsList23))

        val trip2 = Trip(
            cena = 1200.0,
            data_pocz = "2023-09-05",
            data_powrotu = "2023-06-01",
            miejsce = "Ateny",
            kraj = "Grecja",
            ocena = 4.9,
            opis = "Zwiedzanie Aten i wyspy Santorini",
            zdjecie = "grecja.png",
            ilosc_miejsc = 16,
            plan = daysList2,
            uzytkownicy = uzytkownicy
        )

        //Wycieczka do Sudanu

        //Plan godzinowy
        val daysDetailsList30: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList30.add(TripDayItem("9.00","Zakwaterowanie w hotelu"))
        daysDetailsList30.add(TripDayItem("10.00","Wyjście na zwiedzanie miasta"))
        daysDetailsList30.add(TripDayItem("15.00","Obiad na mieście"))
        daysDetailsList30.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList31: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList31.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList31.add(TripDayItem("10.00","Wyjazd na safari w Parku Narodowym Dżabal Kurdufan"))
        daysDetailsList31.add(TripDayItem("15.00","Zwiedzanie Pałacu Cezarów i Fontanny di Trevi"))

        val daysDetailsList32: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList32.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList32.add(TripDayItem("9.00","Zwiedzanie Chartumu"))
        daysDetailsList32.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList33: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList33.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList33.add(TripDayItem("9.00","Czas wolny na zakupy"))
        daysDetailsList33.add(TripDayItem("18.00","Wykwaterowanie z hotelu"))

        val daysList3: ArrayList<TripDay> = ArrayList()
        daysList3.add(TripDay(1, "Przylot do Chartumu o godzinie 8:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta.",daysDetailsList30))
        daysList3.add(TripDay(2,  "Wyjazd na safari w Parku Narodowym Dżabal Kurdufan: zwiedzanie pięknych krajobrazów, spotkanie z dzikimi zwierzętami.",daysDetailsList31))
        daysList3.add(TripDay(3,   "Zwiedzanie historycznych miejsc Chartumu: Muzeum Narodowe, Naga, Gebel Barkal.",daysDetailsList32))
        daysList3.add(TripDay(4,   "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 19:00.",daysDetailsList33))

        val trip3 = Trip(
            cena = 2000.0,
            data_pocz = "2023-12-10",
            data_powrotu = "2023-12-01",
            miejsce = "Chartum",
            kraj = "Sudan",
            ocena = 4.2,
            opis = "Safari w Parku Narodowym Dżabal Kurdufan",
            zdjecie = "sudan.png",
            ilosc_miejsc = 10,
            plan = daysList3,
            uzytkownicy = uzytkownicy
        )

        //Wycieczka do Izraela

        //Plan godzinowy
        val daysDetailsList40: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList40.add(TripDayItem("14.00","Zakwaterowanie w hotelu"))
        daysDetailsList40.add(TripDayItem("15.00","Czas wolny na zwiedzanie miasta"))
        daysDetailsList40.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList41: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList41.add(TripDayItem("8.00","Wyjście na zwiedzanie Jerozolimy"))
        daysDetailsList41.add(TripDayItem("13.00","Obiad na miescie"))
        daysDetailsList41.add(TripDayItem("15.00","Zwiedzanie Bazyliki Zmartwychwstania"))

        val daysDetailsList42: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList42.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList42.add(TripDayItem("9.00","Zwiedzanie Starego Miasta w Jerozolimie"))
        daysDetailsList42.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList43: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList43.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList43.add(TripDayItem("9.00","Wyjazd nad Morze Martwe"))
        daysDetailsList43.add(TripDayItem("18.00","Powrót do Tel Awiwu i wykwaterowanie z hotelu"))

        val daysList4: ArrayList<TripDay> = ArrayList()
        daysList4.add(TripDay(1, "Przylot do Tel Awiwu. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta.",daysDetailsList40))
        daysList4.add(TripDay(2, "Wycieczka do Jerozolimy: Wzgórze Świątynne, Zamek Dawida, Kartańska i Wieża Dawida.",daysDetailsList41))
        daysList4.add(TripDay(3, "Zwiedzanie Starego Miasta Jerozolimy: Mur Zachodni, Bazylika Grobu Pańskiego, Żydowski Wschód i Wzgórze Skopus.",daysDetailsList42))
        daysList4.add(TripDay(4, "Wyjazd do morza Martwego i kąpiel w wodach o wysokiej zawartości soli. Powrót do Tel Awiwu.",daysDetailsList43))
        val trip4 = Trip(
            cena = 2500.0,
            data_pocz = "2023-07-15",
            data_powrotu = "2023-07-22",
            miejsce = "Tel Awiw",
            kraj = "Izrael",
            ocena = 4.7,
            opis = "Zwiedzanie Izraela",
            zdjecie = "izrael.png",
            ilosc_miejsc = 8,
            plan = daysList4,
            uzytkownicy = uzytkownicy
        )

        //Wycieczka do Indii

        //Plan godzinowy
        val daysDetailsList50: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList50.add(TripDayItem("9.00","Zakwaterowanie w hotelu"))
        daysDetailsList50.add(TripDayItem("11.00","Czas wolny na zwiedzanie miasta"))
        daysDetailsList50.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList51: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList51.add(TripDayItem("8.00","Wyjście na zwiedzanie Qutub Minar"))
        daysDetailsList51.add(TripDayItem("13.00","Obiad na miescie"))
        daysDetailsList51.add(TripDayItem("15.00","Zwiedzanie Delhi"))

        val daysDetailsList52: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList52.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList52.add(TripDayItem("9.00","Wyjazd na wycieczkę do taj Mahal autokarem"))
        daysDetailsList52.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList53: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList53.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList53.add(TripDayItem("9.00","Wyjazd do Jaipur"))
        daysDetailsList53.add(TripDayItem("18.00","Powrót do hotelu i kolacja"))

        val daysList5: ArrayList<TripDay> = ArrayList()
        daysList5.add(TripDay(1, "Przylot do Delhi o godzinie 9:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta.",daysDetailsList50))
        daysList5.add(TripDay(2, "Zwiedzanie Qutub Minar, Czerwonej Fortecy i Meczetu Jama Masjid.",daysDetailsList51))
        daysList5.add(TripDay(3, "Wyjazd do Agru i zwiedzanie słynnego Mauzoleum Taj Mahal.",daysDetailsList52))
        daysList5.add(TripDay(4, "Wyjazd do Jaipur i zwiedzanie Amber Fort i Pałacu Hawa Mahal.",daysDetailsList53))
        val trip5 = Trip(
            cena = 3000.0,
            data_pocz = "2023-11-05",
            data_powrotu = "2023-11-12",
            miejsce = "Delhi",
            kraj = "Indie",
            ocena = 4.5,
            opis = "Kulturowe zwiedzanie Indii",
            zdjecie = "indie.png",
            ilosc_miejsc = 12,
            plan = daysList5,
            uzytkownicy = uzytkownicy
        )

        // Wycieczka do Ugandy

        //Plan godzinowy
        val daysDetailsList60: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList60.add(TripDayItem("19.00","Zakwaterowanie w hotelu"))
        daysDetailsList60.add(TripDayItem("20.00","Kolacja w hotelu"))

        val daysDetailsList61: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList61.add(TripDayItem("8.00","Zwiedzanie kampali"))
        daysDetailsList61.add(TripDayItem("13.00","Obiad na miescie"))
        daysDetailsList61.add(TripDayItem("15.00","Zwiedzanie Muzeum Narodowego Ugandy"))

        val daysDetailsList62: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList62.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList62.add(TripDayItem("9.00","Wyjazd na safari do Parku Narodowego Murchison Falls autokarem"))
        daysDetailsList62.add(TripDayItem("19.00","Powrót i kolacja w hotelu"))

        val daysDetailsList63: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList63.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList63.add(TripDayItem("9.00","Czas wolny na zakupy"))
        daysDetailsList63.add(TripDayItem("18.00","Wyjazd na nocne safari"))

        val daysList6: ArrayList<TripDay> = ArrayList()
        daysList6.add(TripDay(1, "Przylot do Entebbe w godzinach wieczornych. Transfer do hotelu w Kampali.",daysDetailsList60))
        daysList6.add(TripDay(2, "Zwiedzanie Kampali, spacer po Parku Owadów, Muzeum Sztuki i Rzemiosła, Muzeum Narodowe Ugandy.",daysDetailsList61))
        daysList6.add(TripDay(3, "Wyjazd na safari do Parku Narodowego Murchison Falls, podziwianie wodospadów, spotkanie z dzikimi zwierzętami.",daysDetailsList62))
        daysList6.add(TripDay(4, "Czas wolny na odpoczynek. Wieczorem wyjazd na nocne safari i obserwacja dzikich zwierząt.",daysDetailsList63))

        val trip6 = Trip(
            cena = 3500.0,
            data_pocz = "2023-09-15",
            data_powrotu = "2023-09-22",
            miejsce = "Kampala",
            kraj = "Uganda",
            ocena = 4.5,
            opis = "Safari w Parku Narodowym Murchison Falls",
            zdjecie = "Uganda.png",
            ilosc_miejsc = 8,
            plan = daysList6,
            uzytkownicy = uzytkownicy
        )

// Wycieczka do Japonii

        //Plan godzinowy
        val daysDetailsList70: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList70.add(TripDayItem("11.00","Zakwaterowanie w hotelu w Tokio"))
        daysDetailsList70.add(TripDayItem("12.00","Czas wolny na zwiedzanie miasta"))
        daysDetailsList70.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList71: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList71.add(TripDayItem("8.00","Zwiedzanie świątyń w Tokio"))
        daysDetailsList71.add(TripDayItem("13.00","Obiad na miescie"))
        daysDetailsList71.add(TripDayItem("15.00","Zwiedzanie Pałacu Cesarskiego"))

        val daysDetailsList72: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList72.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList72.add(TripDayItem("9.00","Wyjazd na wycieczkę do Kioto pociągiem Shinkansen"))
        daysDetailsList72.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList73: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList73.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList73.add(TripDayItem("9.00","Czas wolny na zakupy"))
        daysDetailsList73.add(TripDayItem("18.00","Wizyta w barze karaoke"))

        val daysList7: ArrayList<TripDay> = ArrayList()
        daysList7.add(TripDay(1, "Przylot do Tokio o godzinie 10:00. Transfer do hotelu i zakwaterowanie.",daysDetailsList70))
        daysList7.add(TripDay(2, "Zwiedzanie Tokio: Świątynia Asakusa, Park Ueno, Pałac Cesarski, Brama Shibuya.",daysDetailsList71))
        daysList7.add(TripDay(3, "Wycieczka do Kioto: Świątynia Kinkakuji, Świątynia Kiyomizu, Dzielnica Gejsz, Pałac Cesarski w Kioto.",daysDetailsList72))
        daysList7.add(TripDay(4, "Czas wolny na zakupy i odpoczynek. Wieczorem wizyta w jednym z barów karaoke.",daysDetailsList73))

        val trip7 = Trip(
            cena = 5000.0,
            data_pocz = "2024-03-15",
            data_powrotu = "2024-03-22",
            miejsce = "Tokio",
            kraj = "Japonia",
            ocena = 4.8,
            opis = "Zwiedzanie Tokio i Kioto",
            zdjecie = "Japonia.png",
            ilosc_miejsc = 6,
            plan = daysList7,
            uzytkownicy = uzytkownicy
        )

        //Wycieczka do Korei

        //Plan godzinowy
        val daysDetailsList80: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList80.add(TripDayItem("13.00","Zakwaterowanie w hotelu w Seulu"))
        daysDetailsList80.add(TripDayItem("14.00","Czas wolny na zwiedzanie miasta"))
        daysDetailsList80.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList81: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList81.add(TripDayItem("8.00","Wyjście na zwiedzanie miasta"))
        daysDetailsList81.add(TripDayItem("13.00","Obiad na miescie"))
        daysDetailsList81.add(TripDayItem("15.00","Przejazd do dzielnicy Gangnam"))

        val daysDetailsList82: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList82.add(TripDayItem("8.00","Wyjście na spacer po Parku Olimpijskim"))
        daysDetailsList82.add(TripDayItem("12.00","Zwiedzanie Muzeum Sztuki Nowoczesnej"))
        daysDetailsList82.add(TripDayItem("19.00","Kolacja w tradycyjnej koreańskiej restauracji"))

        val daysDetailsList83: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList83.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList83.add(TripDayItem("9.00","Wyjazd do strefy DMZ"))
        daysDetailsList83.add(TripDayItem("15.00","Powrót do hotelu i odpoczynek"))

        val daysDetailsList84: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList84.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList84.add(TripDayItem("9.00","Czas wolny na zakupy"))
        daysDetailsList84.add(TripDayItem("18.00","Wykwaterowanie z hotelu"))

        val daysList8: ArrayList<TripDay> = ArrayList()
        daysList8.add(TripDay(1, "Przylot do Seulu o godzinie 12:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta.",daysDetailsList80))
        daysList8.add(TripDay(2,  "Zwiedzanie pałaców Gyeongbokgung i Changdeokgung. Wieczorem wizyta w dzielnicy Gangnam.",daysDetailsList81))
        daysList8.add(TripDay(3,   "Spacer po Parku Olimpijskim, wizyta w muzeum sztuki nowoczesnej. Wieczorem kolacja w tradycyjnej koreańskiej restauracji.",daysDetailsList82))
        daysList8.add(TripDay(4,   "Zwiedzanie demilitarzonej strefy nad granicą z Koreą Północną. Powrót do hotelu na odpoczynek.",daysDetailsList83))
        daysList8.add(TripDay(5,   "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 20:00.",daysDetailsList84))

        val trip8 = Trip(
            cena = 4000.0,
            data_pocz = "2023-10-01",
            data_powrotu = "2023-10-08",
            miejsce = "Seul",
            kraj = "Korea Południowa",
            ocena = 4.5,
            opis = "Odkryj Koreę Południową - Seul i DMZ",
            zdjecie = "Korea.png",
            ilosc_miejsc = 8,
            plan = daysList8,
            uzytkownicy = uzytkownicy
        )

        //Wycieczka do Stanów

        //Plan godzinowy
        val daysDetailsList90: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList90.add(TripDayItem("11.00","Zakwaterowanie w hotelu"))
        daysDetailsList90.add(TripDayItem("12.00","Czas wolny na zwiedzanie miasta"))
        daysDetailsList90.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList91: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList91.add(TripDayItem("8.00","Zwiedzanie centrum Nowego Jorku"))
        daysDetailsList91.add(TripDayItem("13.00","Obiad na miescie"))
        daysDetailsList91.add(TripDayItem("15.00","Wizyta w muzeum sztuki współczesnej"))

        val daysDetailsList92: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList92.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList92.add(TripDayItem("9.00","Wyjazd na wycieczkę do Central Parku"))
        daysDetailsList92.add(TripDayItem("19.00","Rejs po rzece Hudson"))

        val daysDetailsList93: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList93.add(TripDayItem("8","Śniadanie w hotelu"))
        daysDetailsList93.add(TripDayItem("9","Wycieczka do Statuy Wolności"))
        daysDetailsList93.add(TripDayItem("18.00","Kolacja"))

        val daysDetailsList94: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList94.add(TripDayItem("8","Śniadanie w hotelu"))
        daysDetailsList94.add(TripDayItem("9","Czas wolny na zakupy"))
        daysDetailsList94.add(TripDayItem("20.00","Wykwaterowanie z hotelu"))

        val daysList9: ArrayList<TripDay> = ArrayList()
        daysList9.add(TripDay(1, "Przylot do Nowego Jorku o godzinie 10:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta.",daysDetailsList90))
        daysList9.add(TripDay(2,  "Zwiedzanie centrum Nowego Jorku: Times Square, Rockefeller Center, Broadway. Wieczorem wizyta w muzeum sztuki współczesnej.",daysDetailsList91))
        daysList9.add(TripDay(3,   "Wycieczka do Central Parku, spacer po jego atrakcjach. Wieczorem rejs po rzece Hudson.",daysDetailsList92))
        daysList9.add(TripDay(4,   "Zwiedzanie Statuy Wolności oraz Historycznej Wyspy Ellis. Wieczorem kolacja w jednej z nowojorskich restauracji.",daysDetailsList93))
        daysList9.add(TripDay(5,   "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 21:00.",daysDetailsList94))

        val trip9 = Trip(
            cena = 5000.0,
            data_pocz = "2023-11-05",
            data_powrotu = "2023-11-12",
            miejsce = "Nowy Jork",
            kraj = "Stany Zjednoczone",
            ocena = 4.8,
            opis = "Odkryj Nowy Jork - miasto które cię zaskoczy",
            zdjecie = "Usa.png",
            ilosc_miejsc = 12,
            plan = daysList9,
            uzytkownicy = uzytkownicy
        )

        //Wycieczka na Islandię

        //Plan godzinowy
        val daysDetailsList100: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList100.add(TripDayItem("11.00","Zakwaterowanie w hotelu"))
        daysDetailsList100.add(TripDayItem("12.00","Czas wolny na zwiedzanie miasta"))
        daysDetailsList100.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList101: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList101.add(TripDayItem("8.00","Zwiedzanie gejzerów i wodospadów"))
        daysDetailsList101.add(TripDayItem("13.00","Obiad w plenerze"))

        val daysDetailsList102: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList102.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList102.add(TripDayItem("9.00","Wyjazd na wycieczkę na lodowiec Vatnajökull autokarem"))
        daysDetailsList102.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList103: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList103.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList103.add(TripDayItem("9.00","Czas wolny na zakupy"))
        daysDetailsList103.add(TripDayItem("18.00","Wykwaterowanie z hotelu"))

        val daysList10: ArrayList<TripDay> = ArrayList()
        daysList10.add(TripDay(1, "Przylot na Islandię o godzinie 10:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta.",daysDetailsList100))
        daysList10.add(TripDay(2, "Zwiedzanie słynnych gejzerów Geysir, wodospadów Gullfoss i wulkanicznych formacji Borgarfjörður Eystri.",daysDetailsList101))
        daysList10.add(TripDay(3, "Wyjazd na safari na lodowcu Vatnajökull: zwiedzanie pięknych krajobrazów, wędrówka po lodowcu, podziwianie jaskiń lodowych.",daysDetailsList102))
        daysList10.add(TripDay(4, "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 19:00.",daysDetailsList103))

        val trip10 = Trip(
            cena = 3500.0,
            data_pocz = "2024-05-15",
            data_powrotu = "2024-05-22",
            miejsce = "Reykjavik",
            kraj = "Islandia",
            ocena = 4.5,
            opis = "Safari na lodowcu Vatnajökull",
            zdjecie = "Islandia.png",
            ilosc_miejsc = 15,
            plan = daysList10,
            uzytkownicy = uzytkownicy
        )

        //Wycieczka do Szkocji

        //Plan godzinowy
        val daysDetailsList110: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList110.add(TripDayItem("11.00","Zakwaterowanie w hotelu"))
        daysDetailsList110.add(TripDayItem("12.00","Czas wolny na zwiedzanie miasta"))
        daysDetailsList110.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList111: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList111.add(TripDayItem("8.00","Wyjście na zwiedzanie Edynburga"))
        daysDetailsList111.add(TripDayItem("13.00","Obiad na miescie"))
        daysDetailsList111.add(TripDayItem("15.00","Przejście na zwiedzanie Zamku Craigmillar"))

        val daysDetailsList112: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList112.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList112.add(TripDayItem("9.00","Wycieczka do Parku Narodowego Loch Lomond i The Trossachs"))
        daysDetailsList112.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList113: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList113.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList113.add(TripDayItem("9.00","Czas wolny na zakupy"))
        daysDetailsList113.add(TripDayItem("18.00","Wykwaterowanie z hotelu"))

        val daysList11: ArrayList<TripDay> = ArrayList()
        daysList11.add(TripDay(1, "Przylot do Edynburga o godzinie 10:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta.",daysDetailsList110))
        daysList11.add(TripDay(2, "Zwiedzanie historycznych zamków i pałaców Edynburga: Zamek Edynburski, Pałac Holyrood, Zamek Craigmillar.",daysDetailsList111))
        daysList11.add(TripDay(3, "Wycieczka do Parku Narodowego Loch Lomond i The Trossachs: zwiedzanie pięknych krajobrazów, wędrówka po górach.",daysDetailsList112))
        daysList11.add(TripDay(4, "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 19:00.",daysDetailsList113))

        val trip11 = Trip(
            cena = 2500.0,
            data_pocz = "2024-07-01",
            data_powrotu = "2024-07-08",
            miejsce = "Edynburg",
            kraj = "Szkocja",
            ocena = 4.1,
            opis = "Zwiedzanie zamków i pałaców Edynburga",
            zdjecie = "Szkocja.png",
            ilosc_miejsc = 20,
            plan = daysList11,
            uzytkownicy = uzytkownicy
        )

        //Wycieczka do Norwegii

        //Plan godzinowy
        val daysDetailsList120: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList120.add(TripDayItem("13.00","Zakwaterowanie w hotelu"))
        daysDetailsList120.add(TripDayItem("14.00","Czas wolny na zwiedzanie miasta"))
        daysDetailsList120.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList121: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList121.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList121.add(TripDayItem("13.00","Wyjazd na górską wędrówkę do Narodowego Parku Jotunheimen"))

        val daysDetailsList122: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList122.add(TripDayItem("8.00","Śniadanie w hotelu"))
        daysDetailsList122.add(TripDayItem("9.00","Zwiedzanie Oslo"))
        daysDetailsList122.add(TripDayItem("19.00","Kolacja w hotelu"))

        val daysDetailsList123: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList123.add(TripDayItem("8","Śniadanie w hotelu"))
        daysDetailsList123.add(TripDayItem("9","Rejs po fiordach Oslofjord i Aurlandsfjord z przystankiem w malowniczych miejscowościach Flåm i Gudvangen"))

        val daysDetailsList124: ArrayList<TripDayItem> = ArrayList()
        daysDetailsList124.add(TripDayItem("8","Śniadanie w hotelu"))
        daysDetailsList124.add(TripDayItem("9","Czas wolny na zakupy"))
        daysDetailsList124.add(TripDayItem("18.00","Wykwaterowanie z hotelu"))

        val daysList12: ArrayList<TripDay> = ArrayList()
        daysList12.add(TripDay(1, "Przylot do Oslo o godzinie 12:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta.",daysDetailsList120))
        daysList12.add(TripDay(2, "Wyjazd na górską wędrówkę do Narodowego Parku Jotunheimen, jeden z najpiękniejszych szlaków turystycznych w Norwegii.",daysDetailsList121))
        daysList12.add(TripDay(3, "Zwiedzanie historycznych miejsc Oslo: Muzeum Wikingów, Munch Museum, Opera w Oslo.",daysDetailsList122))
        daysList12.add(TripDay(4, "Rejs po fiordach Oslofjord i Aurlandsfjord z przystankiem w malowniczych miejscowościach Flåm i Gudvangen.",daysDetailsList123))
        daysList12.add(TripDay(5, "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 19:00.",daysDetailsList124))

        val trip12 = Trip(
            cena = 3000.0,
            data_pocz = "2023-09-10",
            data_powrotu = "2023-09-16",
            miejsce = "Oslo",
            kraj = "Norwegia",
            ocena = 4.8,
            opis = "Wycieczka do Norwegii",
            zdjecie = "Norwegia.png",
            ilosc_miejsc = 8,
            plan = daysList12,
            uzytkownicy = uzytkownicy
        )

        dodajNowaWycieczka(trip);
        dodajNowaWycieczka(trip1);
        dodajNowaWycieczka(trip2);
        dodajNowaWycieczka(trip3);
        dodajNowaWycieczka(trip4);
        dodajNowaWycieczka(trip5);
        dodajNowaWycieczka(trip6);
        dodajNowaWycieczka(trip7);
        dodajNowaWycieczka(trip8);
        dodajNowaWycieczka(trip9);
        dodajNowaWycieczka(trip10);
        dodajNowaWycieczka(trip11);
        dodajNowaWycieczka(trip12);

    }
}
