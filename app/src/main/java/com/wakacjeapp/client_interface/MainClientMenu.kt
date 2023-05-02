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
//            dodajNoweWycieczki()
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

        val daysList0: ArrayList<TripDay> = ArrayList()
        daysList0.add(TripDay(1, "Przylot do Rzymu o godzinie 14:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta."))
        daysList0.add(TripDay(2, "Zwiedzanie Rzymu: Koloseum, Forum Romanum, Pałac Cezarów, Fontanna di Trevi, Panteon." ))
        daysList0.add(TripDay(3,  "Wyjazd na wycieczkę poza miasto: Watykan, Muzea Watykańskie, Bazylika św. Piotra."))
        daysList0.add(TripDay(4,  "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 19:00."))

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

        val daysList1: ArrayList<TripDay> = ArrayList()
        daysList1.add(TripDay(1, "Przylot do Barcelony o godzinie 12:00. Zakwaterowanie w apartamencie i czas wolny na zwiedzanie miasta." ))
        daysList1.add(TripDay(2,  "Zwiedzanie Barcelony: La Rambla, Park Güell, Sagrada Familia, Katedra św. Eulalii."))
        daysList1.add(TripDay(3,   "Wyjazd na wycieczkę na Costa Brava: Tossa de Mar, Lloret de Mar, Blanes."))
        daysList1.add(TripDay(4,   "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 18:00."))

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

        val daysList2: ArrayList<TripDay> = ArrayList()
        daysList2.add(TripDay(1, "Przylot do Aten o godzinie 11:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta." ))
        daysList2.add(TripDay(2,  "Zwiedzanie Aten: Akropol, Agora Rzymska, Muzeum Akropolu, Plaka."))
        daysList2.add(TripDay(3,   "Wyjazd na wycieczkę na wyspę Santorini: Oia, Fira, Kamari, Czerwone Plaże."))
        daysList2.add(TripDay(4,   "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 20:00."))

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

        val daysList3: ArrayList<TripDay> = ArrayList()
        daysList3.add(TripDay(1, "Przylot do Chartumu o godzinie 8:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta."))
        daysList3.add(TripDay(2,  "Wyjazd na safari w Parku Narodowym Dżabal Kurdufan: zwiedzanie pięknych krajobrazów, spotkanie z dzikimi zwierzętami."))
        daysList3.add(TripDay(3,   "Zwiedzanie historycznych miejsc Chartumu: Muzeum Narodowe, Naga, Gebel Barkal."))
        daysList3.add(TripDay(4,   "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 19:00."))

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

        val daysList4: ArrayList<TripDay> = ArrayList()
        daysList4.add(TripDay(1, "Przylot do Tel Awiwu. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta."))
        daysList4.add(TripDay(2, "Wycieczka do Jerozolimy: Wzgórze Świątynne, Zamek Dawida, Kartańska i Wieża Dawida."))
        daysList4.add(TripDay(3, "Zwiedzanie Starego Miasta Jerozolimy: Mur Zachodni, Bazylika Grobu Pańskiego, Żydowski Wschód i Wzgórze Skopus."))
        daysList4.add(TripDay(4, "Wyjazd do morza Martwego i kąpiel w wodach o wysokiej zawartości soli. Powrót do Tel Awiwu."))
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


        val daysList5: ArrayList<TripDay> = ArrayList()
        daysList5.add(TripDay(1, "Przylot do Delhi o godzinie 9:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta."))
        daysList5.add(TripDay(2, "Zwiedzanie Qutub Minar, Czerwonej Fortecy i Meczetu Jama Masjid."))
        daysList5.add(TripDay(3, "Wyjazd do Agru i zwiedzanie słynnego Mauzoleum Taj Mahal."))
        daysList5.add(TripDay(4, "Wyjazd do Jaipur i zwiedzanie Amber Fort i Pałacu Hawa Mahal."))
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
        val daysList6: ArrayList<TripDay> = ArrayList()
        daysList6.add(TripDay(1, "Przylot do Entebbe w godzinach wieczornych. Transfer do hotelu w Kampali."))
        daysList6.add(TripDay(2, "Zwiedzanie Kampali, spacer po Parku Owadów, Muzeum Sztuki i Rzemiosła, Muzeum Narodowe Ugandy."))
        daysList6.add(TripDay(3, "Wyjazd na safari do Parku Narodowego Murchison Falls, podziwianie wodospadów, spotkanie z dzikimi zwierzętami."))
        daysList6.add(TripDay(4, "Czas wolny na odpoczynek. Wieczorem wyjazd na nocne safari i obserwacja dzikich zwierząt."))

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
        val daysList7: ArrayList<TripDay> = ArrayList()
        daysList7.add(TripDay(1, "Przylot do Tokio o godzinie 10:00. Transfer do hotelu i zakwaterowanie."))
        daysList7.add(TripDay(2, "Zwiedzanie Tokio: Świątynia Asakusa, Park Ueno, Pałac Cesarski, Brama Shibuya."))
        daysList7.add(TripDay(3, "Wycieczka do Kioto: Świątynia Kinkakuji, Świątynia Kiyomizu, Dzielnica Gejsz, Pałac Cesarski w Kioto."))
        daysList7.add(TripDay(4, "Czas wolny na zakupy i odpoczynek. Wieczorem wizyta w jednym z barów karaoke."))

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


        val daysList8: ArrayList<TripDay> = ArrayList()
        daysList8.add(TripDay(1, "Przylot do Seulu o godzinie 12:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta."))
        daysList8.add(TripDay(2,  "Zwiedzanie pałaców Gyeongbokgung i Changdeokgung. Wieczorem wizyta w dzielnicy Gangnam."))
        daysList8.add(TripDay(3,   "Spacer po Parku Olimpijskim, wizyta w muzeum sztuki nowoczesnej. Wieczorem kolacja w tradycyjnej koreańskiej restauracji."))
        daysList8.add(TripDay(4,   "Zwiedzanie demilitarzonej strefy nad granicą z Koreą Północną. Powrót do hotelu na odpoczynek."))
        daysList8.add(TripDay(5,   "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 20:00."))

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


        val daysList9: ArrayList<TripDay> = ArrayList()
        daysList9.add(TripDay(1, "Przylot do Nowego Jorku o godzinie 10:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta."))
        daysList9.add(TripDay(2,  "Zwiedzanie centrum Nowego Jorku: Times Square, Rockefeller Center, Broadway. Wieczorem wizyta w muzeum sztuki współczesnej."))
        daysList9.add(TripDay(3,   "Wycieczka do Central Parku, spacer po jego atrakcjach. Wieczorem rejs po rzece Hudson."))
        daysList9.add(TripDay(4,   "Zwiedzanie Statuy Wolności oraz Historycznej Wyspy Ellis. Wieczorem kolacja w jednej z nowojorskich restauracji."))
        daysList9.add(TripDay(5,   "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 21:00."))

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

        val daysList10: ArrayList<TripDay> = ArrayList()
        daysList10.add(TripDay(1, "Przylot na Islandię o godzinie 10:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta."))
        daysList10.add(TripDay(2, "Zwiedzanie słynnych gejzerów Geysir, wodospadów Gullfoss i wulkanicznych formacji Borgarfjörður Eystri."))
        daysList10.add(TripDay(3, "Wyjazd na safari na lodowcu Vatnajökull: zwiedzanie pięknych krajobrazów, wędrówka po lodowcu, podziwianie jaskiń lodowych."))
        daysList10.add(TripDay(4, "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 19:00."))

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

        val daysList11: ArrayList<TripDay> = ArrayList()
        daysList11.add(TripDay(1, "Przylot do Edynburga o godzinie 10:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta."))
        daysList11.add(TripDay(2, "Zwiedzanie historycznych zamków i pałaców Edynburga: Zamek Edynburski, Pałac Holyrood, Zamek Craigmillar."))
        daysList11.add(TripDay(3, "Wycieczka do Parku Narodowego Loch Lomond i The Trossachs: zwiedzanie pięknych krajobrazów, wędrówka po górach."))
        daysList11.add(TripDay(4, "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 19:00."))

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



        val daysList12: ArrayList<TripDay> = ArrayList()
        daysList12.add(TripDay(1, "Przylot do Oslo o godzinie 12:00. Zakwaterowanie w hotelu i czas wolny na zwiedzanie miasta."))
        daysList12.add(TripDay(2, "Wyjazd na górską wędrówkę do Narodowego Parku Jotunheimen, jeden z najpiękniejszych szlaków turystycznych w Norwegii."))
        daysList12.add(TripDay(3, "Zwiedzanie historycznych miejsc Oslo: Muzeum Wikingów, Munch Museum, Opera w Oslo."))
        daysList12.add(TripDay(4, "Rejs po fiordach Oslofjord i Aurlandsfjord z przystankiem w malowniczych miejscowościach Flåm i Gudvangen."))
        daysList12.add(TripDay(5, "Czas wolny na zakupy i odpoczynek. Wyjazd powrotny do kraju o godzinie 19:00."))

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
