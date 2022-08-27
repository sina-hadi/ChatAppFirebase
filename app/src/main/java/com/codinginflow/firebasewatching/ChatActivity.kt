package com.codinginflow.firebasewatching

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messageRv: RecyclerView
    private lateinit var buttonSend : Button
    private lateinit var textSend : EditText
    private lateinit var messageAdapter : MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference

    val recieverRoom : String? = null
    val senderRomm: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val recieverUid = intent.getStringExtra("ABC")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid.toString()

        val senderRoom = recieverUid + senderUid
        val recieverRoom = senderUid + recieverUid

//        Log.e("TAG1",name)
        if (recieverUid != null) {
            Log.e("TAG1",recieverUid)
        }
        Log.e("TAG2",senderUid)
        Log.e("TAG3",senderRoom)

        mDbRef = FirebaseDatabase.getInstance().reference

//        supportActionBar?.title = name

        messageRv = findViewById(R.id.messageRv)
        buttonSend = findViewById(R.id.buttonSend)
        textSend = findViewById(R.id.textSend)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)
        messageRv.layoutManager = LinearLayoutManager(this)
        messageRv.adapter = messageAdapter



        mDbRef.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object: ValueEventListener{
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
                }

            })

        buttonSend.setOnClickListener {
            val myText = textSend.text.toString()
            val messageObject = Message(myText,senderUid)

            Log.e("TAG",myText)
            Log.e("TAG",senderUid.toString())

            mDbRef.child("chats").child(senderRoom).child("message").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(recieverRoom).child("message").push()
                        .setValue(messageObject)
                }
            textSend.text.clear()
        }


    }
}