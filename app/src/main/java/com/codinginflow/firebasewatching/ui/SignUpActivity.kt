package com.codinginflow.firebasewatching.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.codinginflow.firebasewatching.data.User
import com.codinginflow.firebasewatching.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.signUpButton.setOnClickListener {
            if (binding.tvPassword.text.isNullOrEmpty() ||
                binding.tvEmail.text.isNullOrEmpty() ||
                binding.tvName.text.isNullOrEmpty()
            ) {
                Toast.makeText(
                    baseContext, "Fill the requirements", Toast.LENGTH_SHORT
                ).show()
            } else {
                signup(
                    password = binding.tvPassword.text.toString(),
                    email = binding.tvEmail.text.toString(),
                    name = binding.tvName.text.toString()
                )
            }
        }
    }

    private fun backToLogIn() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signup(password: String, email: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                addUser(
                    name = name, email = email, uid = auth.currentUser!!.uid
                )
                Toast.makeText(
                    baseContext, "Sign up successful", Toast.LENGTH_SHORT
                ).show()

                backToLogIn()

            } else {
                Toast.makeText(
                    baseContext, task.exception?.cause.toString(), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun addUser(name: String, email: String, uid: String) {

        mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("user").child(uid).setValue(User(uid, name, email))

    }
}