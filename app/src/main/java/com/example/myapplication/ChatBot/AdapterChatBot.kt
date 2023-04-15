package com.example.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ChatBot.ChatModel
import com.example.myapplication.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.listitem_chat.view.*

class AdapterChatBot : RecyclerView.Adapter<AdapterChatBot.MyViewHolder>() {
    private val list = ArrayList<ChatModel>()
    private lateinit var database: DatabaseReference

    inner class MyViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.listitem_chat, parent, false)
    ) {
        fun bind(chat: ChatModel) = with(itemView) {
            if(!chat.isBot) {
                txtChat_bot.setVisibility(View.GONE)
                txtChat_user.setVisibility(View.VISIBLE)
                txtChat_user.text = chat.chat
            }else{
                txtChat_user.setVisibility(View.GONE)
                txtChat_bot.setVisibility(View.VISIBLE)
                txtChat_bot.text = chat.chat
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(parent)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun addChatToList(chat: ChatModel) {
        list.add(chat)
        notifyDataSetChanged()
    }

}