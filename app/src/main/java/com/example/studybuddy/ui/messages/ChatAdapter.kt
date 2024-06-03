package com.example.studybuddy.ui.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.studybuddy.R
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.data.model.MessageDto

class ChatAdapter(private var currentUserId: Int, private var messagesList: List<MessageDto>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_RECEIVE = 1;
    private val ITEM_SENT = 2;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 1) {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.receive, parent, false)
            return ReceiveViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messagesList[position]

        if(holder.javaClass == SentViewHolder::class.java) {
            val viewHolder = holder as SentViewHolder
            viewHolder.sentMessage.text = currentMessage.text
        } else {
            val viewHolder = holder as ReceiveViewHolder
            viewHolder.receiveMessage.text = currentMessage.text
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messagesList[position]

        return if(currentUserId == currentMessage.senderId) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.sent_message)
    }
    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage = itemView.findViewById<TextView>(R.id.receive_message)
    }

    fun addMessage(newMessage: MessageDto) {
        messagesList.toMutableList().add(newMessage)
        notifyDataSetChanged()
    }

    fun updateMessages(newMessages: List<MessageDto>, userId: Int) {
        currentUserId = userId
        messagesList = newMessages
        notifyDataSetChanged()
    }
}
