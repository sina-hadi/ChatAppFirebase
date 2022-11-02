package com.codinginflow.firebasewatching

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.codinginflow.firebasewatching.databinding.ActivityMain3Binding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity3 : AppCompatActivity() {

    private lateinit var binding: ActivityMain3Binding
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
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
                Toast.makeText(this@MainActivity3, "Connection Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }
}