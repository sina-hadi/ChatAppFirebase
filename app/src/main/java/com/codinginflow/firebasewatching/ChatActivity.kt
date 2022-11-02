package com.codinginflow.firebasewatching

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messageRv: RecyclerView
    private lateinit var buttonSend: Button
    private lateinit var textSend: EditText
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val receiverUid = intent.getStringExtra("ABC")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid.toString()

        val senderRoom = receiverUid + senderUid
        val receiverRoom = senderUid + receiverUid

        mDbRef = FirebaseDatabase.getInstance().reference

        messageRv = findViewById(R.id.messageRv)
        buttonSend = findViewById(R.id.buttonSend)
        textSend = findViewById(R.id.textSend)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        messageRv.layoutManager = LinearLayoutManager(this)
        messageRv.adapter = messageAdapter


        mDbRef.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for (postSnapshots in snapshot.children) {
                        val message = postSnapshots.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity, "Connection Failed", Toast.LENGTH_SHORT)
                        .show()
                }

            })

        buttonSend.setOnClickListener {
            val myText = textSend.text.toString()
            val messageObject = Message(myText, senderUid)

            mDbRef.child("chats").child(senderRoom).child("message").push().setValue(messageObject)
                .addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom).child("message").push()
                        .setValue(messageObject)
                }
            textSend.text.clear()
        }


    }
}