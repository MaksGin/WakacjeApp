package com.wakacjeapp.Messages

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wakacjeapp.R
import com.wakacjeapp.databinding.ActivityChatGroupBinding
import com.wakacjeapp.databinding.ActivityChatLogBinding

class ChatGroupActivity : AppCompatActivity() {

    private var binding: ActivityChatGroupBinding? = null
    private lateinit var messageAdapter: ChatMessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var messageBox: EditText
    private lateinit var myref: DatabaseReference
    private var senderNameMap = HashMap<String, String>()
    var groupRoom: String? = null
    var senderUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatGroupBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val groupName = intent.getStringExtra("groupName")
        senderUid = FirebaseAuth.getInstance().currentUser?.uid
        myref = FirebaseDatabase.getInstance().reference

        groupRoom = "groups/$groupName"

        supportActionBar?.title = groupName

        messageList = ArrayList()
        messageAdapter = ChatMessageAdapter(this,messageList)

        binding?.rvGroupChatLog?.layoutManager = LinearLayoutManager(this@ChatGroupActivity)
        binding?.rvGroupChatLog?.adapter = messageAdapter
        messageBox = binding?.enterGroupMsg!!

        binding?.btnSendGroup?.setOnClickListener{

            val message = messageBox.text.toString()
            val userRef = FirebaseDatabase.getInstance().reference.child("users").child(senderUid!!)
            userRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val senderName = snapshot.child("name").getValue(String::class.java)
                    val messageObject = Message(message, senderUid, senderName)

                    myref.child(groupRoom!!).child("messages").push() //update UI wysyłającego i odbiorcy
                        .setValue(messageObject).addOnSuccessListener {
                            Log.i("groupRoom", groupRoom!!)
                            Log.i("senderUid", senderUid!!)
                        }
                    messageBox.setText("")
                }

                override fun onCancelled(error: DatabaseError) {
                    // obsługa błędów
                }
            })




        }

        //logika dodawania wiadomosci do recycleView
        myref.child(groupRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        val senderId = message?.senderId
                        if (senderId != null && !senderNameMap.containsKey(senderId)) {
                            myref.child("users").child(senderId).child("name")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val senderName = snapshot.getValue(String::class.java)
                                        if (senderName != null) {
                                            senderNameMap[senderId] = senderName
                                            message.senderName = senderName
                                            messageList.add(message)
                                            messageAdapter.notifyDataSetChanged()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {}
                                })
                        } else {
                            message?.senderName = senderNameMap[senderId]
                            messageList.add(message!!)
                        }
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }
}
