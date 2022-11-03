package com.codinginflow.firebasewatching.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.codinginflow.firebasewatching.adapter.PeopleAdapter
import com.codinginflow.firebasewatching.data.User
import com.codinginflow.firebasewatching.databinding.ActivityUserBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userList = ArrayList<User>()

        val adapter = PeopleAdapter(this, userList)
        binding.recyclerMain.layoutManager = LinearLayoutManager(this)
        binding.recyclerMain.adapter = adapter

        mDbRef = Firebase.database.reference

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
                Toast.makeText(this@UserActivity, "Connection Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }
}