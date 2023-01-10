package com.codinginflow.firebasewatching.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.codinginflow.firebasewatching.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        mAuth = FirebaseAuth.getInstance()

        binding.logIn.setOnClickListener {
            if (checkEntries()) {
                login(
                    binding.username.text.toString(), binding.password.text.toString()
                )
            } else {
                Toast.makeText(
                    baseContext, "Fill the requirements.", Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.signUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkEntries(): Boolean {
        return (binding.username.text.isNotEmpty() &&
                binding.password.text.isNotEmpty())
    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(
                    baseContext, "Authentication success.", Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this, UserActivity::class.java)
                intent.putExtra("uid", user?.uid.toString())
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(
                    baseContext, "Authentication failed.", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}