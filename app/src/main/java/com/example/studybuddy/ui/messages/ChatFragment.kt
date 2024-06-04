package com.example.studybuddy.ui.messages

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.R
import com.example.studybuddy.data.model.MessageDto
import com.example.studybuddy.data.model.MessageRequest
import com.example.studybuddy.databinding.FragmentChatBinding
import com.example.studybuddy.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private lateinit var replyContextLayout: LinearLayout
    private lateinit var replyContextText: TextView
    private lateinit var closeReplyContext: ImageView
    private val viewModel: MessagesViewModel by viewModels()
    private lateinit var binding: FragmentChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private var userId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replyContextLayout = view.findViewById(R.id.reply_context_layout)
        replyContextText = view.findViewById(R.id.reply_context_text)
        closeReplyContext = view.findViewById(R.id.close_reply_context)

        // This can be called here, because, apart from setupSendButton(), it requires no params
        closeReplyContext.setOnClickListener {
            clearReplyContext()
        }

        userId = arguments?.getInt("userId")

        userId?.let { id ->
            viewModel.getUser(id)
            viewModel.loadMessages(id)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val currentUserId = viewModel.getUserId()
            currentUserId?.let { id ->
                setupRecyclerView(id)
                viewModel.messagesResult.collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            val messages = response.data
                            messages?.let {
                                chatAdapter.updateMessages(it, id)
                                binding.messagesList.scrollToPosition(chatAdapter.itemCount - 1)
                            }
                            setupSendButton(currentUserId, userId!!)
                        }

                        is Resource.Loading -> {
                            // Show loading state if necessary
                        }

                        is Resource.Error -> {
                            // Handle error state if necessary
                        }

                        else -> {}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userResult.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        binding.recieverName.text = response.data?.firstName
                    }

                    is Resource.Loading -> {
                        // Show loading state if necessary
                    }

                    is Resource.Error -> {
                        // Handle error state if necessary
                    }

                    else -> {}
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messageResult.collect { response ->
                when (response) {
                    is Resource.Success -> {
                        val newMessage = response.data
                        newMessage?.let {
                            chatAdapter.addMessage(it)
                            binding.messagesList.scrollToPosition(chatAdapter.itemCount - 1)
                        }
                    }

                    is Resource.Loading -> {
                        // Show loading state if necessary
                    }

                    is Resource.Error -> {
                        // Handle error state if necessary
                    }

                    else -> {}
                }
            }
        }
    }

    // Sets everything up for the RecyclerView in the fragment_chat.xml
    // It uses the ChatAdapter for that
    private fun setupRecyclerView(id: Int) {
        chatAdapter = ChatAdapter(id, emptyList())
        val layoutManager = LinearLayoutManager(context).apply {
            stackFromEnd = true
        }
        binding.messagesList.layoutManager = layoutManager
        binding.messagesList.adapter = chatAdapter

        // An ItemTouchHelper that lets you swipe the message to reply to it.
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition // Use adapterPosition instead of bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val message = chatAdapter.messagesList[position]
                    showReplyContext(message)
                    chatAdapter.notifyItemChanged(position)
                }
            }

            // Used by itemTouchHelperCallback to limit the swipe of onSwiped()
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val newDx = dX / 4 // Sets the swipe length limit
                super.onChildDraw(c, recyclerView, viewHolder, newDx, dY, actionState, isCurrentlyActive)
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.messagesList)
    }

    // Shows reply box above EditText input field
    private fun showReplyContext(message: MessageDto) {
        replyContextText.text = message.text
        replyContextLayout.visibility = View.VISIBLE
        viewModel.setReplyMessageId(message.id)
    }

    // Cancels the reply to the message.
    private fun clearReplyContext() {
        replyContextLayout.visibility = View.GONE
        replyContextText.text = ""
        viewModel.setReplyMessageId(null)
    }

    // Sets up the onClickListener for the send button
    private fun setupSendButton(senderId: Int, receiverId: Int) {
        binding.sendButton.setOnClickListener {
            val message = binding.messageBox.text.toString()

            if (message.isNotEmpty()) {
                val replyToId = viewModel.messagesReplyId.value
                val messageObject = MessageRequest(
                    text = message,
                    fileName = null,
                    file = null,
                    respondsToId = replyToId,
                    senderId = senderId,
                    receiverId = receiverId
                )
                viewModel.createMessage(messageObject)
                binding.messageBox.setText("")
                clearReplyContext()
            }
        }
    }
}