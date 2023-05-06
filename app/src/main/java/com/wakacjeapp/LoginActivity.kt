package com.wakacjeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.wakacjeapp.Messages.LatestMessagesActivity
import com.wakacjeapp.client_interface.MainClientMenu
import com.wakacjeapp.databinding.ActivityLoginBinding

const val USER_EMAIL: String = "user_email"

class LoginActivity : AppCompatActivity() {

    private var binding : ActivityLoginBinding? = null
    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        auth = FirebaseAuth.getInstance()

        var email = binding?.emailEditText?.text.toString()
        var password = binding?.passwordEditText?.text.toString()

        binding?.loginBtn?.setOnClickListener{
            logIn()
        }

        binding?.createAccountButton?.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    companion object {
        var email= ""
        var password= ""
        val name = email
    }

    private fun logIn() {


        email = binding?.emailEditText?.text.toString()
        password = binding?.passwordEditText?.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Wpisz email i hasło", Toast.LENGTH_SHORT).show()
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {
                        val intent = Intent(this, MainClientMenu::class.java)
                        intent.putExtra(USER_EMAIL, email)
                        startActivity(intent)
                        //overridePendingTransition(R.anim.slide_enter_right,R.anim.slide_exit_right)
                        /*val bundle = Bundle()
                    bundle.putString("email", email)
                    val myFrag = AccountFragment()
                    myFrag.arguments = bundle
                    */

                        Toast.makeText(this, "Udane logowanie", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Nie udalo sie", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}