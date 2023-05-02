package com.wakacjeapp.model

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wakacjeapp.User

interface UserListener {
    fun onUserFound(imie: String)
}


fun znajdzUzytkownika(id: String?, listener: UserListener) {
    val database = FirebaseDatabase.getInstance()
    val uzytkownicyRef = database.getReference("users")
    id?.let { uzytkownicyRef.child(it) }
        ?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usr = dataSnapshot.getValue(User::class.java)
                if (usr != null) {
                    val imie = usr.name.toString()
                    listener.onUserFound(imie)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
}