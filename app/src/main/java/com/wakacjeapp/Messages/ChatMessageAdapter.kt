package com.wakacjeapp.Messages

import android.content.Context
import android.graphics.Color
import android.provider.Telephony.Mms.Sent
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.wakacjeapp.R
import com.wakacjeapp.User
import com.wakacjeapp.databinding.ChatRowBinding
import com.wakacjeapp.databinding.UserRowMessageBinding


class ChatMessageAdapter(val context: Context,val messageList: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType == 1){
            val view: View = LayoutInflater.from(context).inflate(R.layout.chat_row,parent,false)
            ReceiveViewHolder(view)
        }else{
            val view: View = LayoutInflater.from(context).inflate(R.layout.chat_row_to,parent,false)
            SentViewHolder(view)
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMsg = messageList[position]

        if(holder.javaClass == SentViewHolder::class.java){

            //stuff for sent view holder
            val viewHolder = holder as SentViewHolder
            viewHolder.sentMessage.text = currentMsg.message
            viewHolder.senderName.text = currentMsg.senderName
        }else{
            val viewHolder = holder as ReceiveViewHolder
            viewHolder.receiveMessage.text =  currentMsg.message
            viewHolder.receiverName.text = currentMsg.senderName

        }



    }



    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        return if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            ITEM_SENT
        }else{
            ITEM_RECEIVE
        }
    }


    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sentMessage: TextView = itemView.findViewById<TextView>(R.id.tv_sent_msg)
        val senderName: TextView = itemView.findViewById<TextView>(R.id.tv_sender_username)
    }
    class ReceiveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val receiveMessage: TextView = itemView.findViewById<TextView>(R.id.tv_received_msg)
        val receiverName: TextView = itemView.findViewById<TextView>(R.id.tv_received_username)
    }

}


