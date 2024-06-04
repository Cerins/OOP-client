package com.example.studybuddy.ui.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.studybuddy.R
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.data.model.MessageDto

class ChatAdapter(
    private var currentUserId: Int,
    var messagesList: List<MessageDto>
): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage: TextView? = itemView.findViewById(R.id.sent_message)
        val receivedMessage: TextView? = itemView.findViewById(R.id.receive_message)
        val replyContainer: LinearLayout = itemView.findViewById(R.id.reply_container)
        val repliedMessage: TextView = itemView.findViewById(R.id.replied_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messagesList[position]

        if (message.senderId == currentUserId) {
            holder.sentMessage?.text = message.text
            holder.sentMessage?.visibility = View.VISIBLE
            holder.receivedMessage?.visibility = View.GONE
        } else {
            holder.receivedMessage?.text = message.text
            holder.receivedMessage?.visibility = View.VISIBLE
            holder.sentMessage?.visibility = View.GONE
        }

        if (message.respondsToId != null) {
            val repliedMessage = messagesList.find { it.id == message.respondsToId }
            holder.replyContainer.visibility = View.VISIBLE
            holder.repliedMessage.text = repliedMessage?.text
        } else {
            holder.replyContainer.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            // Handle click if necessary
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messagesList[position]
        return if (currentMessage.senderId == currentUserId) {
            R.layout.sent
        } else {
            R.layout.receive
        }
    }

    override fun getItemCount(): Int = messagesList.size

    fun updateMessages(newMessages: List<MessageDto>, userId: Int) {
        messagesList = newMessages
        notifyDataSetChanged()
    }

    fun addMessage(message: MessageDto) {
        messagesList = messagesList + message
        notifyItemInserted(messagesList.size - 1)
    }
}
