package com.wakacjeapp.Messages

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.wakacjeapp.User
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.wakacjeapp.databinding.UserRowMessageBinding

class NewMessageAdapter(val context: Context, private var items: ArrayList<User>): RecyclerView.Adapter<NewMessageAdapter.ViewHolder>() {


//    interface OnItemClickListener {
//        fun onItemClick(user: User)
//    }
//    private var onItemClickListener: OnItemClickListener? = null
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//        onItemClickListener = listener
//    }

    class ViewHolder(binding: UserRowMessageBinding): RecyclerView.ViewHolder(binding.root){
        val tv1 = binding.userName

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(UserRowMessageBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = items[position]
        holder.tv1.text = user.name

        holder.tv1.setOnClickListener {

            val intent = Intent(context,ChatLogActivity::class.java)
            intent.putExtra("name",user.name)
            intent.putExtra("receiverUid", user.uid)
            user.uid?.let { it1 -> Log.i("USER ID", it1) }
            context.startActivity(intent)

        }
        Log.e("Error", "Pozycja bindsolo ${user}")



    }


}