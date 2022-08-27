package com.codinginflow.firebasewatching

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.codinginflow.firebasewatching.databinding.ActivityMain3Binding
import com.codinginflow.firebasewatching.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityMain3Binding
    private lateinit var mDbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

//        val userUid = intent.putExtra("uid",ss)

        var userList = ArrayList<User>()
        val adapter = PeopleAdapter(this, userList)
        binding.recyclerMain.layoutManager = LinearLayoutManager(this)
        binding.recyclerMain.adapter = adapter

        mDbRef = FirebaseDatabase.getInstance().reference

        mDbRef.child("user").addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for (postSnapshots in snapshot.children) {
                    val currentUser = postSnapshots.getValue(User::class.java)
                    userList.add(currentUser!!)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })




    }
}