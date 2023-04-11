package com.wakacjeapp.Messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wakacjeapp.R
import com.wakacjeapp.RegisterActivity
import com.wakacjeapp.User
import com.wakacjeapp.databinding.ActivityNewMessageBinding

class NewMessageActivity : AppCompatActivity() {

    private var binding: ActivityNewMessageBinding? = null
    private lateinit var userList: ArrayList<User>
    private lateinit var myref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: NewMessageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMessageBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = "Select User"
        userList = ArrayList()
        adapter = NewMessageAdapter(this,userList)

        myref = FirebaseDatabase.getInstance().getReference()
        auth = FirebaseAuth.getInstance()
        binding?.rView?.layoutManager = LinearLayoutManager(this@NewMessageActivity)
        binding?.rView?.adapter = adapter


        myref.child("users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for(postsnapshot in snapshot.children){

                    val currentUser = postsnapshot.getValue(User::class.java)

                    if(auth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                    }

                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun refreshUserList() {
        myref.child("users").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val updatedUserList = ArrayList<User>()
                for (postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if (auth.currentUser?.uid != currentUser?.uid) {
                        updatedUserList.add(currentUser!!)
                    }
                }
                userList.clear()
                userList.addAll(updatedUserList)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO()
            }
        })
    }
}

