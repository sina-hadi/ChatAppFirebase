package com.codinginflow.firebasewatching

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.codinginflow.firebasewatching.databinding.ActivityMain2Binding
import com.codinginflow.firebasewatching.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.FirebaseDatabaseKtxRegistrar
import com.google.firebase.ktx.Firebase

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var auth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.button3.setOnClickListener {
            val password = binding.text2.text.toString()
            val email = binding.text3.text.toString()
            val name = binding.text4.text.toString()
            signup(password, email, name)
        }
    }

    private fun signup(password: String, email: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                addUser(name, email, auth.currentUser!!.uid)
                Toast.makeText(
                    baseContext, "Sign up successful", Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    baseContext, task.exception?.cause.toString(), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun addUser(name: String, email: String, uid: String) {

        mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("user").child(uid).setValue(User(name, email, uid))

    }
}