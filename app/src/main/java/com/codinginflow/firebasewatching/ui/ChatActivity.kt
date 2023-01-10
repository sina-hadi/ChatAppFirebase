package com.codinginflow.firebasewatching.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.codinginflow.firebasewatching.data.Message
import com.codinginflow.firebasewatching.adapter.MessageAdapter
import com.codinginflow.firebasewatching.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receiverName = intent.getStringExtra("USER_NAME")
        val receiverUid = intent.getStringExtra("USER_UID")
        title = receiverName

        setProperties(receiverUid!!)

    }

    private fun setProperties(receiverUid: String) {
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val senderRoom = receiverUid + senderUid
        val receiverRoom = senderUid + receiverUid

        mDbRef = FirebaseDatabase.getInstance().reference

        val messageList: ArrayList<Message> = ArrayList()

        setRecyclerView(messageList = messageList)

        collectChat(senderRoom = senderRoom, messageList = messageList)

        binding.buttonSend.setOnClickListener {
            if (checkEntry()) {
                sendMessage(
                    senderUid = senderUid,
                    senderRoom = senderRoom,
                    receiverRoom = receiverRoom
                )
            }
        }
    }

    private fun checkEntry(): Boolean{
        return binding.textSend.text.toString().isNotEmpty()
    }

    private fun setRecyclerView(messageList: ArrayList<Message>) {
        messageAdapter = MessageAdapter(this, messageList)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = false
        linearLayoutManager.stackFromEnd = true

        binding.messageRv.layoutManager = linearLayoutManager
        binding.messageRv.adapter = messageAdapter

    }

    private fun collectChat(senderRoom: String, messageList: ArrayList<Message>) {
        mDbRef.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshots in snapshot.children) {
                        val message = postSnapshots.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
//                    messageAdapter.notifyDataSetChanged()
                    setRecyclerView(messageList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity, "Connection Failed", Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    private fun sendMessage(senderUid: String, senderRoom: String, receiverRoom: String) {
        val myText = binding.textSend.text.toString()
        val messageObject = Message(myText, senderUid)

        mDbRef.child("chats").child(senderRoom).child("message").push().setValue(messageObject)
            .addOnSuccessListener {
                mDbRef.child("chats").child(receiverRoom).child("message").push()
                    .setValue(messageObject)
            }
        binding.textSend.text.clear()
    }

}