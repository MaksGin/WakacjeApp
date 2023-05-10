package com.wakacjeapp.client_interface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.wakacjeapp.R
import com.wakacjeapp.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {

    private lateinit var activity_info: ActivityInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        activity_info = ActivityInfoBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        setContentView(activity_info.root)

        activity_info.arrowBack.setOnClickListener {
            super.onBackPressed()
        }

        activity_info.textViewLegend.text = "Legenda:"
        activity_info.textViewCzat.text = "W tym miejscu znajdziesz czat z innymi użytkownikami"
        activity_info.textViewWycieczki.text = "Tutaj znajdują się wycieczki, na które jesteś zapisany/a"
        activity_info.textViewOferty.text = "Tu znajdziesz ciekawe ofery wycieczek, na które możesz się zapisać"
        activity_info.textViewInfo.text = "Aby zapisać się na wycieczkę, kliknij w okienko z jej nazwą, a następnie wybierz przycisk ZAPISZ SIĘ NA TĘ WYCIECZKĘ"
    }
}