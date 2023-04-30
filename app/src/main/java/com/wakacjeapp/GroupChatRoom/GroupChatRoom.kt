package com.wakacjeapp.GroupChatRoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference
import com.wakacjeapp.R
import com.wakacjeapp.databinding.ActivityGroupChatRoomBinding
import com.wakacjeapp.databinding.ActivityRegisterBinding

class GroupChatRoom : AppCompatActivity() {


    private var binding: ActivityGroupChatRoomBinding? = null
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupChatRoomBinding.inflate(layoutInflater)
        setContentView(binding?.root)



        binding?.createRoom?.setOnClickListener {

        }
    }

    private fun createRoom(roomname: String){

    }
}