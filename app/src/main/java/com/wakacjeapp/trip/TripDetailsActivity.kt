package com.wakacjeapp.trip

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wakacjeapp.Messages.ChatGroupActivity
import com.wakacjeapp.Messages.NewMessageActivity
import com.wakacjeapp.R
import com.wakacjeapp.User
import com.wakacjeapp.client_interface.MainClientMenu
import com.wakacjeapp.databinding.ActivityMainClientMenuBinding
import com.wakacjeapp.databinding.ActivityTripDetailsBinding
import com.wakacjeapp.model.DataItem
import com.wakacjeapp.model.DataItemType
import com.wakacjeapp.model.ImageMap
import com.wakacjeapp.model.TitleText
import com.wakacjeapp.trip.adapter.TripDetailsAdapter
import com.wakacjeapp.trip.model.Trip
import com.wakacjeapp.trip.model.TripDay

class TripDetailsActivity : AppCompatActivity() {

    private lateinit var activity_trip: ActivityTripDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_trip_details)
        val wycieczka = intent.getParcelableExtra<Trip>("trip")


        activity_trip = ActivityTripDetailsBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        setContentView(activity_trip.root)

        if (wycieczka != null) {
            ustaw_dane(wycieczka)
        }


        //Zmień widoczność guzika jeśli jest już uczestnikiem tej wycieczki - na chat grupowy:

        //przycisk cofania
        activity_trip.arrowBack.setOnClickListener {
            super.onBackPressed();
        }


        if(wycieczka?.uzytkownicy?.let { sprawdz_usera(it) } == true) {
            activity_trip.buttonSingUp.text = "Chat Grupowy"
            activity_trip.buttonSingUp.setOnClickListener {
                val intent = Intent(this, ChatGroupActivity::class.java)
                intent.putExtra("groupName","${wycieczka.kraj} - ${wycieczka.data_pocz}")
                startActivity(intent)
            }
        }
        else if (wycieczka != null && sprawdz_miejsce(wycieczka.ilosc_miejsc)) {
            activity_trip.buttonSingUp.setOnClickListener {
                dodaj_do_wycieczki(wycieczka)
            }
        }else{
            activity_trip.buttonSingUp.text = "Brak miejsc"
            activity_trip.buttonSingUp.textColors
            activity_trip.buttonSingUp.setOnClickListener {
                Toast.makeText(this@TripDetailsActivity, "Brak miejsc", Toast.LENGTH_SHORT).show()
            }
        }

        //        przycisk do zapisywania na wycieczkę

    }

    fun dodaj_do_wycieczki(wycieczka: Trip){
        dodajUzytkownikaDoWycieczki(wycieczka.id_wycieczki)
        Toast.makeText(this@TripDetailsActivity, "Zarezerwowałeś wycieczke do ${wycieczka.kraj}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainClientMenu::class.java)
        startActivity(intent)
    }

    fun sprawdz_miejsce(ilosc_miejsc: Int): Boolean {
        return ilosc_miejsc != 0
    }

    private fun sprawdz_usera(list: ArrayList<User>): Boolean {
        for (uczestnik in list) {
            if (uczestnik.uid == FirebaseAuth.getInstance().currentUser?.uid) {
                return true
            }
        }
        return false
    }

    fun ustaw_dane(wycieczka: Trip){
        val imageMap = ImageMap().imageMap
        val adapter = TripDetailsAdapter(wycieczka.plan, this)
        activity_trip.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        activity_trip.recyclerView.adapter = adapter

        activity_trip.tripDetailsTitle.text=wycieczka.kraj
        activity_trip.dataValueTv.text="${wycieczka?.data_pocz} - ${wycieczka?.data_powrotu}"
        activity_trip.priceValueTv.text="${wycieczka?.cena} zł"
        activity_trip.rateTv.text= wycieczka.ocena.toString()
        activity_trip.photo.setImageResource(imageMap[wycieczka.zdjecie] ?: R.drawable.dominikana)
}

    fun dodajUzytkownikaDoWycieczki(wycieczkaId: String) {
        var database = FirebaseDatabase.getInstance()
        var wycieczkiRef = database.getReference("wycieczki")
        var wycieczkaRef = wycieczkiRef.child(wycieczkaId)

        wycieczkaRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var wycieczka = dataSnapshot.getValue(Trip::class.java)!! // Pobiera aktualne wartości wycieczk
                wycieczka.uzytkownicy.add(User(name = FirebaseAuth.getInstance().currentUser?.displayName, email = FirebaseAuth.getInstance().currentUser?.email, uid = FirebaseAuth.getInstance().currentUser?.uid)) // Dodaje uzytkownika
                wycieczka.ilosc_miejsc = wycieczka.ilosc_miejsc-1
                wycieczkaRef.setValue(wycieczka) // Aktualizuje wartości
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

}