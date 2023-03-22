package com.wakacjeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wakacjeapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private var binding: ActivityRegisterBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.loginTextViewBtn?.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        binding?.createAccBtn?.setOnClickListener {
            dataCheck()
        }

    }

    private var name = ""
    private var email = ""
    private var password = ""

    private fun dataCheck(){

        name = binding?.nameEditText?.text.toString().trim()
        email = binding?.emailEditText?.text.toString().trim()
        password = binding?.passwordEditText?.text.toString().trim()
        val cPassword = binding?.confirmEditText?.text.toString().trim()
        if(name.isEmpty()){
            Toast.makeText(this, "Wprowadz imie..", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Wprowadz email...", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Wprowadz adres email..", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Wprowadz hasło..", Toast.LENGTH_SHORT).show();
        } else if (cPassword.isEmpty()) {
            Toast.makeText(this, "Potwierdz hasło..", Toast.LENGTH_SHORT).show();
        } else if (password != cPassword) {
            Toast.makeText(this, "Hasła sie nie zgadzają..", Toast.LENGTH_SHORT).show();
        } else {
            createUserAccount();
        }

    }

    private fun createUserAccount() {


        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {



                email = binding?.emailEditText?.text.toString().trim()
                saveToFirebase(User(email,name))
                Toast.makeText(this, "Sukces", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                // e-> progressDialog.dismiss()
                //Toast.makeText(this,"Operacja nie powiodła się! Bład: ${e.message}",Toast.LENGTH_SHORT).show();
            }
    }
    private fun saveToFirebase(user:User) {
        // Get a reference to the Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference()

        // Save the data to the "data" node in the database
        val user = User(email,name)
        ref.child("users").setValue(user)

    }
}