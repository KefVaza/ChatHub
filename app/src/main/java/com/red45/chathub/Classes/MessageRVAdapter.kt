package com.red45.chathub.Classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.red45.chathub.R

class MessageRVAdapter (private val msgList : ArrayList<MessageRVModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class UserModelViewHolder(itemView : View) :  RecyclerView.ViewHolder(itemView){
        val userMsgTv : TextView = itemView.findViewById(R.id.tvUser)
    }
    class BotModelViewHolder(itemView : View) :  RecyclerView.ViewHolder(itemView){
        val botMsgTv : TextView = itemView.findViewById(R.id.tvBot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View
        return if(viewType==0){
            view = LayoutInflater.from(parent.context).inflate(R.layout.user_message_rv,parent,false)
            UserModelViewHolder(view)
        }else{
            view = LayoutInflater.from(parent.context).inflate(R.layout.bot_message_rv,parent,false)
            BotModelViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sender = msgList.get(position).sender
        when(sender){
            "user" -> (holder as UserModelViewHolder).userMsgTv.setText(msgList.get(position).message)
            "bot" -> (holder as BotModelViewHolder).botMsgTv.setText(msgList.get(position).message)
        }
    }
    override fun getItemCount(): Int {
       return msgList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(msgList.get(position).sender){
            "user" -> 0
            "bot" -> 1
            else -> 1
        }
    }


}