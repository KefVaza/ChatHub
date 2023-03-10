package com.red45.chathub.Classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.red45.chathub.R

class MessageRvAdapter(private val messageList: ArrayList<MessageRvModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class UserMsgViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userMsgTv: TextView = itemView.findViewById(R.id.tvUser)

    }

    class BotMsgViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView){
        val botMsgTv: TextView = itemView.findViewById(R.id.tvBot)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return if(viewType == 0){
            view = LayoutInflater.from(parent.context).inflate(R.layout.user_msg_rv_item, parent, false)
            UserMsgViewHolder(view)
        }
        else{
            view = LayoutInflater.from(parent.context).inflate(R.layout.bot_msg_rv_item, parent, false)
            BotMsgViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val sender = messageList.get(position).sender
        when (sender){
            "user" -> (holder as UserMsgViewHolder).userMsgTv.setText(messageList.get(position).message)
            "bot" -> (holder as BotMsgViewHolder).botMsgTv.setText(messageList.get(position).message)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(messageList.get(position).sender){
            "user" -> 0
            "bot" -> 1
            else -> 1
        }
    }

}