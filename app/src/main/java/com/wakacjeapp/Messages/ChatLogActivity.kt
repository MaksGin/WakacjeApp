package com.wakacjeapp.Messages

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.wakacjeapp.databinding.ActivityChatLogBinding

class ChatLogActivity : AppCompatActivity() {

    private var binding: ActivityChatLogBinding? = null
    private lateinit var messageAdapter: ChatMessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var messageBox: EditText
    private lateinit var myref: DatabaseReference

    var receiverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatLogBinding.inflate(layoutInflater)
        setContentView(binding?.root)






        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("receiverUid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        myref = FirebaseDatabase.getInstance().reference

        //senderid i receiverid maja to samo id

        //senderRoom = receiverUid + senderUid
        //receiverRoom = senderUid + receiverUid
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name

        messageList = ArrayList()
        messageAdapter = ChatMessageAdapter(this,messageList)

        binding?.rvChatLog?.layoutManager = LinearLayoutManager(this@ChatLogActivity)
        binding?.rvChatLog?.adapter = messageAdapter
        messageBox = binding?.enterMsg!!

        binding?.btnSend?.setOnClickListener{

            val message = messageBox.text.toString()
            val messageObject = Message(message,senderUid)

            myref.child("chats").child(senderRoom!!).child("messages").push() //update UI wysyłającego i odbiorcy
                .setValue(messageObject).addOnSuccessListener {

                    myref.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                    Log.i("receiverRoom",receiverRoom!!)
                    Log.i("receiverRoom",senderRoom!!)
                    Log.i("receiverUid",receiverUid!!)
                    Log.i("senderid",senderUid!!)
                }
            messageBox.setText("")

        }

        //logika dodawania wiadomosci do recycleView
        myref.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)

                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })




    }
}



