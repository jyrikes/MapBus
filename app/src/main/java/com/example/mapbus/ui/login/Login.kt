package com.example.mapbus.ui.login

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mapbus.MainActivity
import com.example.mapbus.R
import com.example.mapbus.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var  binding: ActivityLoginBinding
    private  val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener { view ->
            val email = binding.editTextUsername.text.toString()
            val senha = binding.editTextPassword.text.toString()
            if (email.isEmpty() || senha.isEmpty()){
                val snackbar = Snackbar.make(view,"Prencha todos os campos",Snackbar.LENGTH_LONG)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            }else{
                auth.signInWithEmailAndPassword(email,senha).addOnCompleteListener{autenticacao ->
                    if (autenticacao.isSuccessful){
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        val snackbar = Snackbar.make(view,"Usuário ou senha incorreto",Snackbar.LENGTH_LONG)
                        snackbar.setBackgroundTint(Color.RED)
                        snackbar.show()
                    }


                }
            }
        }
    }
}